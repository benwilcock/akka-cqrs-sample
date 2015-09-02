package com.soagrowers.cqrs.commands;

/**
 * Created by Ben on 07/08/2015.
 */
public class CreateToDoItemCommand extends Cmd{

    private final String toDoDescription;

    public CreateToDoItemCommand(String commandId, String itemDescriptiion) {
        super(commandId);
        this.toDoDescription = itemDescriptiion;
    }

    public String getToDoDescription() {
        return toDoDescription;
    }

    @Override
    public String toString() {
        return "CreateToDoItemCommand{" +
                "toDoDescription='" + toDoDescription + '\'' +
                "} " + super.toString();
    }
}
