package com.soagrowers.cqrs.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.soagrowers.cqrs.commands.Cmd;
import com.soagrowers.cqrs.commands.CreateToDoItemCommand;
import com.soagrowers.cqrs.commands.PrintStateCommand;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

//import akka.testkit.JavaTestKit;
//import akka.testkit.TestActorRef;

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
        actorRef = actorSystem.actorOf(Props.create(ToDoItem.class), "TestCreateToDoItem");
        actorRef.tell(command, ActorRef.noSender());
    }

    @Test
    public void testCreateTwiceFails() {
        Cmd command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Buy some Bread!!");
        actorRef = actorSystem.actorOf(Props.create(ToDoItem.class), "TestCreateTwiceFailsToDoItem");
        actorRef.tell(command, ActorRef.noSender());
        actorRef.tell(command, ActorRef.noSender());
    }

    /*@Test
    public void testCreateCommandWithTestKit() {
        new JavaTestKit(actorSystem) {
            {
                final TestActorRef<ToDoItem> testItemRef = TestActorRef.create(actorSystem, Props.create(ToDoItem.class), "TestCreateCommandWithTestKit");
                final CreateToDoItemCommand command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Buy some Eggs!");


                assertFalse(testItemRef.underlyingActor().description.isPresent());


                testItemRef.tell(command, getTestActor());

                ToDoItem item = testItemRef.underlyingActor();
                assertTrue(item.description.isPresent());

            }
        };
    }

    @Test
    public void testGetGreeter() {
        new JavaTestKit(system) {{

            final ActorRef greeter = system.actorOf(Props.create(HelloAkkaJava.Greeter.class), "greeter2");

            greeter.tell(new HelloAkkaJava.WhoToGreet("testkit"), getTestActor());
            greeter.tell(new HelloAkkaJava.Greet(), getTestActor());

            final HelloAkkaJava.Greeting greeting = expectMsgClass(HelloAkkaJava.Greeting.class);

            new Within(duration("10 seconds")) {
                protected void run() {
                    Assert.assertEquals("hello, testkit", greeting.message);
                }
            };
        }};
    }*/
}
