package com.refactor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccountManagement {

    private static List<Map<String, Object>> accounts = new ArrayList<>();

    private CommandProcessor commandProcessor;
    private CardManagement cardManagement;

    public AccountManagement(CommandProcessor commandProcessor, CardManagement cardManagement) {
        this.commandProcessor = commandProcessor;
        this.cardManagement = cardManagement;
    }

    public void performOperation() {
        switch (commandProcessor.getAction()) {
            case "CREATE":
                Map<String, Object> accountDetails = new HashMap<>();
                if(commandProcessor.getAdditionalParams().get("name") != null ) {
                    if(accounts.size() > 0) {
                        for(Map<String, Object> account : accounts) {
                            if(account.get("email").toString().equalsIgnoreCase(commandProcessor.getAdditionalParams().get("email").toString())) {
                                System.out.println("Unable to create account! This email is already used!");
                                return;
                            }
                        }
                    }
                    accountDetails.put("name", commandProcessor.getAdditionalParams().get("name"));
                }
                accountDetails.put("accountType", "NORMAL");
                if(commandProcessor.getAdditionalParams().get("balance") != null) {
                    accountDetails.put("balance", commandProcessor.getAdditionalParams().get("balance"));
                } else {
                    accountDetails.put("balance", 100); // default amount received for creating an account
                }
                if(commandProcessor.getAdditionalParams().get("currency") != null) {
                    accountDetails.put("currency", commandProcessor.getAdditionalParams().get("currency"));
                } else {
                    accountDetails.put("currency", "EUR"); // default currency if it's not specified
                }
                accountDetails.put("cards", null);

                if(commandProcessor.getAdditionalParams().get("email") != null) {
                    accountDetails.put("email", commandProcessor.getAdditionalParams().get("email"));
                }
                accountDetails.put("dateCreated", new Date());
                accounts.add(accountDetails);
                System.out.println("Account successfully created!");
            break;
            case "GET":
                if(accounts.size() != 0) {
                    Map<String, Object> accountSearched = null;
                    for(Map<String, Object> account : accounts) {
                        if(account.get("email").toString().equalsIgnoreCase(commandProcessor.getAdditionalParams().get("email").toString())) {
                            accountSearched = account;
                        }
                    }
                    if(accountSearched != null) {
                        System.out.println("---------------------");
                        System.out.println("Your account details:");
                        System.out.println("---------------------");
                        System.out.println(
                                String.format("Email: %s | type: %s | account balance: %s%s | created at: %s",
                                        accountSearched.get("email"),
                                        accountSearched.get("accountType"),
                                        accountSearched.get("balance"),
                                        accountSearched.get("currency"),
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(accountSearched.get("dateCreated"))));
                        if(accountSearched.get("cards") != null) {
                            System.out.println("-------------------------");
                            System.out.println("Attached cards to your account:");
                            System.out.println("-------------------------");
                            for(Card card : ((ArrayList<Card>)accountSearched.get("cards"))) {
                                System.out.println("[Card #" + card.getId() + "]");
                                System.out.println("IBAN:" + card.getIban());
                                System.out.println("Balance: " + card.getBalance());
                                System.out.println("Currency: " + card.getCurrency());
                                System.out.println("Type: " + card.getType() + " [" + card.getTypeDescription() + "]");
                                System.out.println("Date created:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(card.getDateCreated()));
                                System.out.println("Active: " + card.isActive());
                            }
                        }
                    } else {
                        System.out.println("We're sorry, but you don't have an account!");
                    }
                }
            break;
            case "GETALL":
                if(accounts.size() > 0) {
                    for(Map<String, Object> account : accounts) {
                        System.out.println("---------------------");
                        System.out.println("Account details:");
                        System.out.println("---------------------");
                        System.out.println(
                                String.format("Email: %s | type: %s | account balance: %s %s |  created at: %s",
                                        account.get("email"),
                                        account.get("accountType"),
                                        account.get("balance"),
                                        account.get("currency"),
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(account.get("dateCreated"))));
                        if(account.get("cards") != null) {
                            System.out.println("-------------------------");
                            System.out.println("Attached cards to your account:");
                            System.out.println("-------------------------");
                            for(Card card : ((ArrayList<Card>)account.get("cards"))) {
                                System.out.println("[Card #" + card.getId() + "]");
                                System.out.println("IBAN:" + card.getIban());
                                System.out.println("Balance: " + card.getBalance());
                                System.out.println("Currency: " + card.getCurrency());
                                System.out.println("Type: " + card.getType() + " [" + card.getTypeDescription() + "]");
                                System.out.println("Date created:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(card.getDateCreated()));
                                System.out.println("Active: " + card.isActive());
                            }
                        }
                    }
                } else {
                    System.out.println("There are no accounts in our database!");
                }
            break;
            case "DELETE":
                if(accounts.size() > 0) {
                    Map<String, Object> accountToBeRemoved = null;
                    for(Map<String, Object> account : accounts) {
                        if(account.get("email").toString().equalsIgnoreCase(commandProcessor.getAdditionalParams().get("email").toString())) {
                            accountToBeRemoved = account;
                            if(account.get("cards") != null) {
                                for(Card card : (ArrayList<Card>)(account.get("cards"))) {
                                    cardManagement.deleteCard(card.getId());
                                }
                            }
                        }
                    }
                    if(accountToBeRemoved != null) {
                        accounts.remove(accountToBeRemoved);
                        System.out.println("Your account has been successfully deleted!");
                    }
                }
            break;
            case "ATTACH_CARD":
                if(commandProcessor.getAdditionalParams().get("cardId") == null || commandProcessor.getAdditionalParams().get("email") == null) {
                    System.out.println("You should specify cardId and account's email");
                } else {
                    boolean attached = false;
                    for(Map<String, Object> account: accounts) {
                        if(account.get("email").toString().equalsIgnoreCase(commandProcessor.getAdditionalParams().get("email").toString())) {
                            Card availableCard = cardManagement.getCard(
                                    Long.valueOf(commandProcessor.getAdditionalParams().get("cardId").toString())
                            );
                            if(account.get("cards") == null) {
                                account.put("cards", new ArrayList<Card>());
                            }
                            ((ArrayList)account.get("cards")).add(availableCard);
                            attached = true;
                        }
                    }
                    if(attached) {
                        System.out.println("Card successfully attached!");
                    } else {
                        System.out.println("Unable to attach the card! Invalid card or account!");
                    }
                }
            break;
            case "GET_TOTAL_MONEY_STORED_EUR": // sum of accounts' balance + sum of attached cards' balance;
                if(accounts.size() > 0) {
                    BigDecimal totalMoney = new BigDecimal(0d);
                    for(Map<String, Object> account : accounts) {
                        if(account.get("balance") != null && account.get("currency") != null && account.get("currency").toString().equals("EUR")) {
                            totalMoney = totalMoney.add(BigDecimal.valueOf(Double.parseDouble(account.get("balance").toString())));
                        }
                        if(account.get("cards") != null) {
                            for(Card card : (ArrayList<Card>)account.get("cards")) {
                                if(card.isActive() && card.getCurrency().equals("EUR")) {
                                    totalMoney = totalMoney.add(card.getBalance());
                                }
                            }
                        }
                    }
                    System.out.println("Total EUR stored in this bank: " + totalMoney);
                } else {
                    System.out.println("There is no money stored in this bank!");
                }
            break;
        }
    }


}
