package com.soagrowers.cqrs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.soagrowers.cqrs.actors.ExamplePersistentActor;
import com.soagrowers.cqrs.commands.Cmd;
import org.junit.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by ben on 28/08/15.
 */
public class AkkaPersistenceTest {

    private static ActorSystem system;
    private ActorRef persistentActor;

    @BeforeClass
    public static void init(){
         system = ActorSystem.create("example");
    }

    @Before
    public void setup(){
        persistentActor = system.actorOf(Props.create(ExamplePersistentActor.class), "persistentActor-4-java");
    }

    @After
    public void teardown() throws InterruptedException {
        persistentActor.tell("print", null);
        TimeUnit.SECONDS.sleep(5l);
    }

    @AfterClass
    public static void close(){
        system.shutdown();
    }


    @Test
    public void test(){
        persistentActor.tell(new Cmd("foo"), null);
        persistentActor.tell(new Cmd("baz"), null);
        persistentActor.tell(new Cmd("bar"), null);
        persistentActor.tell("snap", null);
        persistentActor.tell(new Cmd("buzz"), null);
    }
}
