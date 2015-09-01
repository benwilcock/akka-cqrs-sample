package com.soagrowers.cqrs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.soagrowers.cqrs.actors.ExamplePersistentActor;
import com.soagrowers.cqrs.commands.Cmd;

/**
 * Created by ben on 01/09/15.
 */
public class PersistenceActorExample {

    public static void main(String... args) throws Exception {
        final ActorSystem system = ActorSystem.create("example");
        final ActorRef persistentActor =
                system.actorOf(Props.create(ExamplePersistentActor.class), "persistentActor-4-java");

        persistentActor.tell(new Cmd("foo"), null);
        persistentActor.tell(new Cmd("baz"), null);
        persistentActor.tell(new Cmd("bar"), null);
        persistentActor.tell("snap", null);
        persistentActor.tell(new Cmd("buzz"), null);
        persistentActor.tell("print", null);

        Thread.sleep(5000);
        system.shutdown();
    }
}
