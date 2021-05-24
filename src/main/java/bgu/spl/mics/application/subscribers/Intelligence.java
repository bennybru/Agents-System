package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private List<MissionInfo> missions;
	private int tick;

	public Intelligence(String name,List<MissionInfo> a) {
		super(name);
		missions=a;
		tick = 0;
	}

	public void setMissions(List<MissionInfo> missions) {
		this.missions = missions;
	}

	@Override
	protected void initialize() {

		subscribeBroadcast(TickBroadcast.class, broadcast ->
		{
			if (broadcast.getTick() == -1) {
				terminate();
			}
			else {
				tick = (int)broadcast.getTick();
				for (MissionInfo info : missions)
					if (tick >= info.getTimeIssued()) {
						getSimplePublisher().sendEvent(new MissionReceivedEvent(info));
					}
			}
		});
	}
}
