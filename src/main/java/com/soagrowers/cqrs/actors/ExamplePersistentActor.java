package com.soagrowers.cqrs.actors;

import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;
import com.soagrowers.cqrs.commands.Cmd;
import com.soagrowers.cqrs.events.Evt;

import static java.util.Arrays.asList;

/**
 * Created by ben on 28/08/15.
 */
public class ExamplePersistentActor extends UntypedPersistentActor{

    @Override
    public String persistenceId() { return "my-todo-item-stream"; }

    private ExampleState state = new ExampleState();

    public int getNumEvents() {
        return state.size();
    }

    @Override
    public void onReceiveRecover(Object msg) {
        if (msg instanceof Evt) {
            state.update((Evt) msg);
        } else if (msg instanceof SnapshotOffer) {
            state = (ExampleState)((SnapshotOffer)msg).snapshot();
        } else {
            unhandled(msg);
        }
    }

    @Override
    public void onReceiveCommand(Object msg) {
        if (msg instanceof Cmd) {
            final String data = ((Cmd)msg).getCommandId();
            final Evt evt1 = new Evt(data + "-" + getNumEvents());
            final Evt evt2 = new Evt(data + "-" + (getNumEvents() + 1));
            persist(asList(evt1, evt2), new Procedure<Evt>() {
                public void apply(Evt evt) throws Exception {
                    state.update(evt);
                    if (evt.equals(evt2)) {
                        getContext().system().eventStream().publish(evt);
                    }
                }
            });
        } else if (msg.equals("snap")) {
            // IMPORTANT: create a copy of snapshot
            // because ExampleState is mutable !!!
            saveSnapshot(state.copy());
        } else if (msg.equals("print")) {
            System.out.println(state);
        } else {
            unhandled(msg);
        }
    }
}
