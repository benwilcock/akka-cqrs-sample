package com.soagrowers.cqrs.actors;

import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;
import com.soagrowers.cqrs.commands.Cmd;
import com.soagrowers.cqrs.commands.CreateToDoItemCommand;
import com.soagrowers.cqrs.commands.PrintStateCommand;
import com.soagrowers.cqrs.events.Evt;
import com.soagrowers.cqrs.events.ToDoItemCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;


public class ToDoItem extends UntypedPersistentActor {

    private final String persistenceId = UUID.randomUUID().toString();
    private Optional<String> description = Optional.empty();
    private ToDoItemState state = new ToDoItemState();
    private static final Logger logger = LoggerFactory.getLogger(ToDoItem.class);

    @Override
    public String persistenceId() {
        return persistenceId;
    }

    @Override
    public void onReceiveRecover(Object msg) {
        logger.trace("EVENT: " + msg.getClass().getSimpleName());
        if (msg instanceof Evt) {
            on((Evt) msg);
        } else if (msg instanceof SnapshotOffer) {
            state = (ToDoItemState) ((SnapshotOffer) msg).snapshot();
        } else {
            unhandled(msg);
        }
    }

    private void on(Evt event) {
        logger.trace("EVENT: " + event.getClass().getSimpleName());
        if (event.getClass().isAssignableFrom(ToDoItemCreatedEvent.class)) {
            on((ToDoItemCreatedEvent) event);
        }
    }

    private void on(ToDoItemCreatedEvent event) {
        logger.trace("EVENT: " + event.getClass().getSimpleName());
        logger.debug("ADDED: " + event.getEventId() + " '" + event.getItemDescription() + "'");
        description = Optional.of(event.getItemDescription());
        state.add(event);
    }

    @Override
    public void onReceiveCommand(Object msg) {
        logger.debug("COMMAND: " + msg.getClass().getSimpleName());

        if (msg instanceof Cmd) {
            on((Cmd) msg);
        } else if (msg.equals("snap")) {
            // IMPORTANT: create a copy of snapshot
            // because ExampleState is mutable !!!
            saveSnapshot(state.copy());
        } else {
            unhandled(msg);
        }
    }

    private void on(Cmd command) {
        logger.trace("COMMAND: " + command.getClass().getSimpleName());
        if (command.getClass().isAssignableFrom(CreateToDoItemCommand.class)) {
            on((CreateToDoItemCommand) command);
        } else if (command.getClass().isAssignableFrom(PrintStateCommand.class)) {
            on((PrintStateCommand) command);
        }
    }


    private void on(CreateToDoItemCommand command) {
        logger.trace("COMMAND: " + command.getClass().getSimpleName());
        if (!description.isPresent()) {
            final Evt toDoItemCreatedEvent = new ToDoItemCreatedEvent(command.getCommandId(), command.getToDoDescription());
            logger.debug("RAISED: " + toDoItemCreatedEvent.getClass().getSimpleName());
            persist(toDoItemCreatedEvent, getApplyEventProcedure());
        } else {
            String message = "Actor's can't be created twice!";
            logger.error(message);
            throw new IllegalStateException(message);
        }
    }

    private void on(PrintStateCommand command) {
        logger.trace("COMMAND: " + command.getClass().getSimpleName());
        logger.info("PRINT persistenceId: " + persistenceId + " description: " + description);
        logger.info("PRINT state: " + state.toString());
    }

    private Procedure<Evt> getApplyEventProcedure() {
        return
                new Procedure<Evt>() {
                    public void apply(Evt evt) throws Exception {
                        on(evt);
                        getContext().system().eventStream().publish(evt);
                        logger.debug("APPLIED: " + evt.getClass().getSimpleName() + " (" + evt.getEventId() + ")");
                    }
                };
    }
}
