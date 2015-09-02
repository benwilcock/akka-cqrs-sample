package com.soagrowers.cqrs.commands;

/**
 * Created by ben on 02/09/15.
 */
public class PrintStateCommand extends Cmd{

    public PrintStateCommand(String id) {
        super(id);
    }
}
