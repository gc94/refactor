package com.refactor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {

    private static final String PARAMS_SEPARATOR = ",";
    private static final String KV_SEPARATOR = ":";
    private static final String CMD_SEPARATOR = " ";
    private String command = "([^\\s]+[\\w])";
    private Pattern commandPattern = Pattern.compile(command);

    private String action;
    private String objectToModify;
    private Map<String, Object> params = new HashMap<>();

    public CommandProcessor() {

    }

    public void bind(String userCommand) {
        resetCommandParams();
        Matcher matcher = commandPattern.matcher(userCommand);
        while (matcher.find()) {
            bindParam(matcher.group(1));
        }
        System.out.println("Processed: " + this.toString());
    }

    private void bindParam(String paramValue) {
        if(action == null) {
            action = paramValue;
        } else if(objectToModify == null) {
            objectToModify = paramValue;
        } else {
            bindAdditionalParams(paramValue);
        }
    }

    private void bindAdditionalParams(String additionalParams) {
        Set<String> paramsToProcess = new HashSet<>();
        if(additionalParams != null) {
            if(additionalParams.contains(PARAMS_SEPARATOR)) {
                paramsToProcess.addAll(Arrays.asList(additionalParams.split(PARAMS_SEPARATOR)));
            } else {
                paramsToProcess.add(additionalParams);
            }
            paramsToProcess.forEach(e -> {
                addObjectParam(e);
            });
        }
    }

    private void addObjectParam(String param) {
        if(param != null && param.contains(KV_SEPARATOR)) {
            String[] kv = param.split(KV_SEPARATOR);
            params.put(kv[0], kv[1]);
        } else {
            throw new RuntimeException("Invalid command format!");
        }
    }

    private void resetCommandParams() {
        action = null;
        objectToModify = null;
        params = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder commandOutput = new StringBuilder("[Command]:");
        commandOutput.append(action)
                .append(CMD_SEPARATOR)
                .append(objectToModify)
                .append(CMD_SEPARATOR);
        if(params != null && params.size() > 0) {
            params.forEach((k,v) -> {
                commandOutput.append(k)
                        .append(KV_SEPARATOR)
                        .append(v)
                        .append(CMD_SEPARATOR);
            });
        }
        return commandOutput.toString();
    }

    public String getObjectToModify() {
        return objectToModify;
    }

    public String getAction() {
        return action;
    }

    public Map<String, Object> getAdditionalParams() {
        return params;
    }

}
