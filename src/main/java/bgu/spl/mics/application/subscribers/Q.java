package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private int qTime;

	public Q() {
		super("Q");
		qTime = 0;
	}

	@Override
	protected void initialize() {

		subscribeEvent(GadgetAvailableEvent.class, event ->
		{
			Boolean available = Inventory.getInstance().getItem(event.getGadget());
			if (available) {
				MessageBrokerImpl.getInstance().complete(event, qTime);
			}
			else {
				MessageBrokerImpl.getInstance().complete(event, null);
			}
		});

		subscribeBroadcast(TickBroadcast.class, broadcast ->
		{
			if (broadcast.getTick() == -1) {
				terminate();
			}
			else {
				qTime = (int)broadcast.getTick();
			}
		});
	}
}
