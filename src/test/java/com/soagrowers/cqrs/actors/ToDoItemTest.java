package com.soagrowers.cqrs.actors;

import akka.actor.ActorInitializationException;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import com.soagrowers.cqrs.commands.Cmd;
import com.soagrowers.cqrs.commands.CreateToDoItemCommand;
import com.soagrowers.cqrs.commands.PrintStateCommand;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by ben on 02/09/15.
 */
public class ToDoItemTest {

    private static ActorSystem actorSystem;
    private ActorRef actorRef;
    private static final Logger LOG = LoggerFactory.getLogger(ToDoItemTest.class);

    @BeforeClass
    public static void init() {
        LOG.debug("init");
        actorSystem = ActorSystem.create("AkkaToDoItemTest");
    }


    @Before
    public void setup() {
        LOG.debug("setup");
        actorRef = actorSystem.actorOf(Props.create(ToDoItem.class), String.valueOf(System.currentTimeMillis()));
    }

    @After
    public void close()  {
        LOG.debug("close");
        actorRef.tell(new PrintStateCommand(UUID.randomUUID().toString()), ActorRef.noSender());
    }

    @AfterClass
    public static void teardown() throws InterruptedException {
        LOG.debug("teardown");
        //actorSystem.shutdown();
        //actorSystem.awaitTermination(Duration.create("10 seconds"));
        TimeUnit.SECONDS.sleep(5l);
    }

    @Test
    public void testCreateCommand() {
        Cmd command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Buy some Milk!");
        actorRef.tell(command, ActorRef.noSender());
    }

    @Test
    public void testCreateCommandWithoutActorSystemFails() {
        try {
            Cmd command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Buy some Milk!");
            ToDoItem item = new ToDoItem();
            fail("shouldn't be possible to instantiate an actor without the actor system!");
        } catch (ActorInitializationException e){
            assertTrue(true);
        }
    }

    @Test
    public void testCreateTwiceFails() {
        Cmd command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Buy some Bread!!");
        actorRef.tell(command, ActorRef.noSender());
        actorRef.tell(command, ActorRef.noSender());
    }

    /*
    @Test

    public void testCreateCommandWithTestKit() {
        new JavaTestKit(actorSystem) {{
            final Props props = Props.create(ToDoItem.class);

            // can also use JavaTestKit “from the outside”
            final JavaTestKit probe = new JavaTestKit(actorSystem);
            // “inject” the probe by passing it to the test subject
            // like a real resource would be passed in production
            actorRef.tell(probe.getRef(), getRef());
            // await the correct response
            expectMsgEquals(duration("1 second"), "done");

            // the run() method needs to finish within 3 seconds
            new Within(duration("6 seconds")) {
                protected void run() {

                    Cmd command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Buy some Eggs!");
                    actorRef.tell(command, getRef());

                    // This is a demo: would normally use expectMsgEquals().
                    // Wait time is bounded by 3-second deadline above.
//                    new AwaitCond() {
//                        protected boolean cond() {
//                            return probe.msgAvailable();
//                        }
//                    };

                    // response must have been enqueued to us before probe
                    // expectMsgEquals(Duration.Zero(), "world");

                    // check that the probe we injected earlier got the msg
                    probe.expectMsgEquals(duration("3 seconds"), "hello");
                    Assert.assertEquals(getRef(), probe.getLastSender());

                    // Will wait for the rest of the 3 seconds
                    expectNoMsg();
                }
            };
        }};
    }*/
}
