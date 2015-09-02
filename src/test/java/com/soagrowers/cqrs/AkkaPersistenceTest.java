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

    private static ActorSystem actorSystem;
    private ActorRef actorRef;

    @BeforeClass
    public static void init(){
         actorSystem = ActorSystem.create("example");
    }

    @Before
    public void setup(){
        actorRef = actorSystem.actorOf(Props.create(ExamplePersistentActor.class), "actorRef-4-java");
    }

    @After
    public void teardown() throws InterruptedException {
        actorRef.tell("print", null);
        TimeUnit.SECONDS.sleep(5l);
    }

    @AfterClass
    public static void close(){
        actorSystem.shutdown();
    }

    @Test
    public void test(){
        actorRef.tell(new Cmd("foo"), null);
        actorRef.tell(new Cmd("baz"), null);
        actorRef.tell(new Cmd("bar"), null);
        actorRef.tell("snap", null);
        actorRef.tell(new Cmd("buzz"), null);
    }
}
