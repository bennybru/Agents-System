package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {

    private Squad element;
    private Agent agent1;
    private Agent agent2;

    @BeforeEach
    public void setUp(){
        element = Squad.getInstance();
    }

    @Test
    public void getInstanceTest() {
        Squad check = element.getInstance();
        assertEquals(element,check);
    }

    @Test
    public void loadTest(){
        agent1 = new Agent();
        agent1.setName("a");
        agent1.setSerialNumber("001");

        agent2 = new Agent();
        agent2.setName("b");
        agent2.setSerialNumber("002");

        Agent[] agents = {agent1,agent2};
        element.load(agents);

        List<String> serials = new ArrayList<>();
        serials.add("001");
        serials.add("002");

        assertTrue(element.getAgents(serials));
        assertEquals("a",element.getAgentsNames(serials).get(0));
        assertEquals("b",element.getAgentsNames(serials).get(1));
        assertTrue(agent1.isAvailable() && agent2.isAvailable());
    }

    @Test
    public void releaseAgentsTest() {
        List<String> serials = new ArrayList<>();
        serials.add("001");
        serials.add("002");
        element.releaseAgents(serials);

        assertTrue(agent1.isAvailable() && agent2.isAvailable());
    }


    @Test
    public void sendAgentsTest() {
        List<String> serials = new ArrayList<>();
        serials.add("001");

        element.sendAgents(serials, 1000);
        assertTrue(!agent1.isAvailable());

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        catch (Exception ex) {
        }

        assertTrue(agent1.isAvailable());
    }

    @Test
    public void getAgentsTest() {
        List<String> serials = new ArrayList<>();
        serials.add("001");
        serials.add("002");

        assertTrue(element.getAgents(serials));
    }

    @Test
    public void getAgentsNamesTest() {
        List<String> serials = new ArrayList<>();
        serials.add("001");
        serials.add("002");

        assertEquals("a",element.getAgentsNames(serials).get(0));
        assertEquals("b",element.getAgentsNames(serials).get(1));
    }
}