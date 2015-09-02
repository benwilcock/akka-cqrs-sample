package com.soagrowers.cqrs.commands;

/**
 * Created by Ben on 07/08/2015.
 */
public class MarkCompletedCommand {

    private final String todoId;

    public MarkCompletedCommand(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoId() {
        return todoId;
    }
}
