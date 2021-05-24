package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private Inventory element;
    String[] gadgets = {"totahLazer","bezimSruhot","mitzPach","garbageCollector"};

    @BeforeEach
    public void setUp(){
        element = Inventory.getInstance();
    }

    @Test
    public void getInstanceTest(){
        Inventory check = element.getInstance();
        assertEquals(element,check);
    }

    @Test
    public void loadTest() {
        element.load(gadgets);

        for (int i=0; i<gadgets.length; i++)
            assertTrue(element.getItem(gadgets[i]));
    }

    @Test
    public void getItemTest() {
        for (int i=0; i<gadgets.length; i++)
            assertTrue(element.getItem(gadgets[i]));
    }
}
