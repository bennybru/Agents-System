package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Q;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {

    public class PsudoEvent implements Event<Integer> {}
    public class PsudoBroadcast implements Broadcast {}

    MessageBroker element;
    Subscriber subscriber;
    Subscriber subscriber2;


    @BeforeEach
    public void setUp(){
        element = MessageBrokerImpl.getInstance();
    }

    @Test
    public void getInstanceTest() {
        assertEquals(element,MessageBrokerImpl.getInstance());
    }

    @Test
    public void subscribeEventTest() {
        subscriber = new Q();
        element.register(subscriber);
        element.subscribeEvent(PsudoEvent.class, subscriber);

        PsudoEvent event = new PsudoEvent();
        element.sendEvent(event);


        try {
            assertEquals(event, element.awaitMessage(subscriber));
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void subscribeBroadcastTest() {
        subscriber = new Q();
        subscriber2 = new M();
        element.register(subscriber);
        element.register(subscriber2);
        element.subscribeBroadcast(PsudoBroadcast.class, subscriber);
        element.subscribeBroadcast(PsudoBroadcast.class, subscriber2);

        PsudoBroadcast broadcast = new PsudoBroadcast();
        element.sendBroadcast(broadcast);

        try {
            assertEquals(broadcast,element.awaitMessage(subscriber));
            assertEquals(broadcast, element.awaitMessage(subscriber2));
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void completeTest() {
        PsudoEvent event = new PsudoEvent();
        Future<Integer> future = element.sendEvent(event);
        element.complete(event,1);

        assertTrue(future.isDone());
        assertEquals(1,future.get());
    }

    @Test
    public void sendBroadcastTest() {

        subscriber = new Q();
        subscriber2 = new M();
        element.register(subscriber);
        element.register(subscriber2);
        element.subscribeBroadcast(PsudoBroadcast.class, subscriber);
        element.subscribeBroadcast(PsudoBroadcast.class, subscriber2);

        PsudoBroadcast broadcast = new PsudoBroadcast();
        element.sendBroadcast(broadcast);

        try {
            assertEquals(broadcast,element.awaitMessage(subscriber));
            assertEquals(broadcast, element.awaitMessage(subscriber2));
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void sendEventTest() {
        subscriber = new Q();
        element.register(subscriber);
        element.subscribeEvent(PsudoEvent.class, subscriber);

        PsudoEvent event = new PsudoEvent();
        element.sendEvent(event);


        try {
            assertEquals(event, element.awaitMessage(subscriber));
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void registerTest() {
        subscriber = new Q();
        element.register(subscriber);
        element.subscribeEvent(PsudoEvent.class, subscriber);

        PsudoEvent event = new PsudoEvent();
        element.sendEvent(event);

        try {
            assertEquals(event,element.awaitMessage(subscriber));
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void unregisterTest() {
        subscriber = new Q();
        element.register(subscriber);
        element.subscribeEvent(PsudoEvent.class, subscriber);
        element.unregister(subscriber);

        PsudoEvent event = new PsudoEvent();
        element.sendEvent(event);

        try {
            assertNotEquals(event,element.awaitMessage(subscriber));
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void awaitMessageTest() {
        subscriber = new Q();
        element.register(subscriber);
        element.subscribeEvent(PsudoEvent.class, subscriber);

        PsudoEvent event = new PsudoEvent();
        element.sendEvent(event);


        try {
            assertEquals(event, element.awaitMessage(subscriber));
        }
        catch (Exception ex) {
        }
    }
}
