package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private int id;

	public Moneypenny(int id) {
		super("MonneyPenny");
		this.id = id;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, broadcast ->
		{
			if (broadcast.getTick() == -1) {
				terminate();
			}
		});

		subscribeEvent(AgentsAvailableEvent.class,event ->
		{
			boolean available = Squad.getInstance().getAgents(event.getAgentsSerials());
			if (available) {
				Object[] result = new Object[] {Squad.getInstance().getAgentsNames(event.getAgentsSerials()),id,-1};
				complete(event, result);

				while ((int)result[2] == -1) {
					synchronized (result) {
						try {
							result.wait();
						}
						catch (InterruptedException e) {

						}
					}
				}
				if ((int)result[2] == 0) {
					Squad.getInstance().releaseAgents(event.getAgentsSerials());
				}
				else {
					Squad.getInstance().sendAgents(event.getAgentsSerials(), event.getDuration());
				}
			}
			else {
				complete(event, null);
			}
		});

	}

}
