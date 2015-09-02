package com.soagrowers.cqrs.actors;

import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;
import com.soagrowers.cqrs.commands.Cmd;
import com.soagrowers.cqrs.events.Evt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by ben on 02/09/15.
 */
public abstract class AggrRoot extends UntypedPersistentActor {

    private static final Logger LOG = LoggerFactory.getLogger(AggrRoot.class);
    private final String persistenceId = UUID.randomUUID().toString();
    protected Events state = new Events();

    @Override
    public String persistenceId() {
        return persistenceId;
    }

    @Override
    public void onReceiveRecover(Object msg) {
        if (msg instanceof Evt) {
            on((Evt) msg);
        } else if (msg instanceof SnapshotOffer) {
            state = (Events) ((SnapshotOffer) msg).snapshot();
        } else {
            unhandled(msg);
        }
    }

    @Override
    public void onReceiveCommand(Object msg) {

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

    protected Procedure<Evt> getApplyEventProcedure() {
        return
                new Procedure<Evt>() {
                    public void apply(Evt evt) throws Exception {
                        on(evt);
                        getContext().system().eventStream().publish(evt);
                        LOG.debug("APPLIED   ["+persistenceId()+"]: " + evt.toString());
                    }
                };
    }

    protected abstract void on(Evt event);

    protected abstract void on(Cmd command);

}
