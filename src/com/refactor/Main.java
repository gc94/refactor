package com.refactor;

import java.util.Scanner;

public class Main {

    /**
     * Example of valid commands:
     * -> CREATE ACCOUNT email:test@test.com
     * -> CREATE ACCOUNT email:test2@test.com,balance:1000,currency:EUR
     * -> GETALL ACCOUNT
     * -> GET ACCOUNT email:test@test.com
     * -> CREATE CARD type:gold
     * -> ATTACH_CARD ACCOUNT cardId:1,email:test@test.com
     * -> DELETE ACCOUNT email:test@test.com
     * -> GET_TOTAL_MONEY_STORED_EUR ACCOUNT
     */

    static class AppContext {
        CommandProcessor commandProcessor;
        AccountManagement accountManagement;
        CardManagement cardManagement;
    }

    public static void main(String[] args) {
        try {
            AppContext appContext = initContext();
            String activeCommand = null;
            insertDummyData(appContext);
            do {
                try {
                    activeCommand = getUserInput();
                    executeCommand(appContext, activeCommand);
                } catch (Exception e) {
                    if("EXIT".equalsIgnoreCase(activeCommand)) {
                       throw new RuntimeException("Finished!");
                    } else {
                        System.out.println("[ERROR]: Unable to find a specific operation for this command!");
                    }
                }
            } while (true);

        } catch (Exception e) {
            System.out.println("Unable to execute command!");
            System.out.println("Usage:--------------------");
            System.out.println("execute [ACTION] [OBJECT] [ADDITIONAL:PARAMS]");
            System.out.println("Eg: execute [CREATE] [ACCOUNT] [email:john@example.com]");
            System.out.println("--------------------------");
        }
    }

    private static void insertDummyData(AppContext appContext) {
        executeCommand(appContext, "CREATE ACCOUNT email:john@test.com,balance:50000,currency:EUR");
        executeCommand(appContext, "CREATE ACCOUNT email:travis@test.com");
        executeCommand(appContext, "CREATE ACCOUNT email:mike@test.com");
        executeCommand(appContext, "CREATE CARD type:bronze");
        executeCommand(appContext, "CREATE CARD type:silver");
        executeCommand(appContext, "ATTACH_CARD ACCOUNT cardId:1,email:john@test.com");
    }

    private static void executeCommand(AppContext appContext, String userCommand) {
        appContext.commandProcessor.bind(userCommand);
        switch (appContext.commandProcessor.getObjectToModify()) {
            case "ACCOUNT":
                appContext.accountManagement.performOperation();
                break;
            case "CARD":
                appContext.cardManagement.performOperation();
                break;
            default:
                throw new RuntimeException("not implemented or unknown command");
        }
    }

    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your command:");
        return scanner.nextLine();
    }

    private static AppContext initContext() {
        AppContext context = new AppContext();
        context.commandProcessor = new CommandProcessor();
        context.cardManagement = new CardManagement(context.commandProcessor);
        context.accountManagement = new AccountManagement(context.commandProcessor, context.cardManagement);
        return context;
    }

}
