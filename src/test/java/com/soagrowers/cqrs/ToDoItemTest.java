package com.soagrowers.cqrs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.soagrowers.cqrs.actors.ToDoItem;
import com.soagrowers.cqrs.commands.Cmd;
import com.soagrowers.cqrs.commands.CreateToDoItemCommand;
import com.soagrowers.cqrs.commands.PrintStateCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by ben on 02/09/15.
 */
public class ToDoItemTest {

    private static ActorSystem actorSystem;
    private ActorRef actorRef;
    private String name;
    private static final String PREFIX = "TODO-";
    private static final Logger logger = LoggerFactory.getLogger(ToDoItemTest.class);

    @BeforeClass
    public static void init(){
        actorSystem = ActorSystem.create("AkkaToDoItemTest");
    }


    @Before
    public void setup(){
        name = PREFIX + System.currentTimeMillis();
        actorRef = actorSystem.actorOf(Props.create(ToDoItem.class), name);
    }

    @After
    public void teardown() throws InterruptedException {
        actorRef.tell(new PrintStateCommand(UUID.randomUUID().toString()), ActorRef.noSender());
        TimeUnit.SECONDS.sleep(5l);
    }

    @Test
    public void testCreateCommand(){
        Cmd command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Test-one");
        actorRef.tell(command, ActorRef.noSender());
    }

    @Test
    public void testCreateTwiceFails(){
        Cmd command = new CreateToDoItemCommand(UUID.randomUUID().toString(), "Test-two");
        actorRef.tell(command, ActorRef.noSender());
        actorRef.tell(command, ActorRef.noSender());
    }
}
