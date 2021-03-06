package bgu.spl.mics.application.passiveObjects;
import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	private static class SquadHolder {
		private static Squad instance = new Squad();
	}


	private Squad() {
		agents = new HashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent agent : agents) {
			this.agents.put(agent.getSerialNumber(),agent);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (String serial : serials) {
			Agent agent = agents.get(serial);
			synchronized (agent) {
				agent.release();
				agent.notifyAll();
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		try {
			Thread.sleep(100*time);
		}
		catch (InterruptedException e) {
		}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		boolean missingAgent = false;
		for (int i = 0; i < serials.size() && !missingAgent; i++)
			if (agents.get(serials.get(i)) == null)
				missingAgent = true;

		serials.sort(Comparator.naturalOrder());

		for (int i = 0; i < serials.size() && !missingAgent; i++) {
			Agent agent = agents.get(serials.get(i));
			synchronized (agent) {
				while (!agent.isAvailable())
					try {
						agent.wait();
					}
					catch (InterruptedException e) {
					}
				agent.acquire();
			}
		}
		return !missingAgent;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
    	List<String> names = new LinkedList<>();

    	for (String serial : serials) {
    		names.add(agents.get(serial).getName());
		}
	    return names;
    }

}
