package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.Q;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;



public class FutureTest {

    private Future<Integer> element;

    @BeforeEach
    public void setUp(){
        element = new Future<>();
    }

    @Test
    public void getTest(){
        assertNull(element.get());
        element.resolve(1);
        assertEquals(1,element.get());
    }

    @Test
    public void resolveTest() {
        element.resolve(1);
        assertEquals(1,element.get());
        assertTrue(element.isDone());
    }

    @Test
    public void isDoneTest() {
        element.resolve(1);
        assertTrue(element.isDone());
    }

    @Test
    public void getSecondTest() {
        Integer result = element.get(1000, TimeUnit.MILLISECONDS);
        assertNull(result);

        element.resolve(1);
        result = element.get(1000,TimeUnit.MILLISECONDS);
        assertEquals(1,result);
    }

}