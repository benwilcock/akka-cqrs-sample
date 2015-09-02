package com.soagrowers.cqrs.actors;

import com.soagrowers.cqrs.commands.Cmd;
import com.soagrowers.cqrs.commands.CreateToDoItemCommand;
import com.soagrowers.cqrs.commands.PrintStateCommand;
import com.soagrowers.cqrs.events.Evt;
import com.soagrowers.cqrs.events.ToDoItemCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class ToDoItem extends AggrRoot {

    private static final Logger LOG = LoggerFactory.getLogger(ToDoItem.class);
    protected Optional<String> description = Optional.empty();

    public ToDoItem() {
        super();
        LOG.debug("ID: " + super.persistenceId());
    }

    protected void on(Evt event) {
        if (event.getClass().isAssignableFrom(ToDoItemCreatedEvent.class)) {
            on((ToDoItemCreatedEvent) event);
        }
    }

    private void on(ToDoItemCreatedEvent event) {
        LOG.debug("ADDED     ["+persistenceId()+"]: " + event.toString());
        description = Optional.of(event.getItemDescription());
        state.add(event);
    }


    protected void on(Cmd command) {
        if (command.getClass().isAssignableFrom(CreateToDoItemCommand.class)) {
            on((CreateToDoItemCommand) command);
        } else if (command.getClass().isAssignableFrom(PrintStateCommand.class)) {
            on((PrintStateCommand) command);
        }
    }


    private void on(CreateToDoItemCommand command) {
        LOG.debug("PROCESSING["+persistenceId()+"]: " + command.toString());
        if (!description.isPresent()) {
            final Evt event = new ToDoItemCreatedEvent(command.getCommandId(), command.getToDoDescription());
            LOG.debug("RAISED    ["+persistenceId()+"]: " + event.toString());
            persist(event, getApplyEventProcedure());
        } else {
            String message = "ERROR     ["+persistenceId()+"] - This todo item has already been created and already has a name. The command is invalid!";
            LOG.error(message);
            throw new IllegalStateException(message);
        }
    }

    private void on(PrintStateCommand command) {
        LOG.info("PRINT persistenceId: " + super.persistenceId() + " description: " + description);
        LOG.info("PRINT state: " + state.toString());
    }


}
