package com.soagrowers.cqrs.commands;

import java.io.Serializable;

/**
 * Created by ben on 28/08/15.
 */
public class Cmd implements Serializable{

    private final String commandId;

    public Cmd(String id) {
        this.commandId = id;
    }

    public String getCommandId() {
        return commandId;
    }

    @Override
    public String toString() {
        return "Cmd{" +
                "commandId='" + commandId + '\'' +
                '}';
    }
}
