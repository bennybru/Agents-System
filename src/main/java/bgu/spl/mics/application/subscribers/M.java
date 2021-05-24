package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int id;
	int tick;

	public M(int id) {
		super("M");
		this.id = id;
		tick = 0;
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
			}
		});

		subscribeEvent(MissionReceivedEvent.class, event ->
		{
			Diary.getInstance().incrementTotal();
			Object[] agentsAvailableRes;
			Future<Object[]> agentsTemp = getSimplePublisher().sendEvent(new AgentsAvailableEvent(event.getMissionInfo()));
			agentsAvailableRes = agentsTemp.get();

			Integer gadgetAvailableRes;
			if (agentsAvailableRes != null) {
				Future<Integer> gadgetTemp = getSimplePublisher().sendEvent(new GadgetAvailableEvent(event.getMissionInfo()));
				gadgetAvailableRes = gadgetTemp.get();
				if (gadgetAvailableRes != null && tick <= event.getTimeExpired()) {
					agentsAvailableRes[2] = 1;
					synchronized (agentsAvailableRes) {
						agentsAvailableRes.notifyAll();
					}

					Report report = new Report();
					report.setTimeCreated(tick);
					report.setMissionName(event.getMissionName());
					report.setMoneypenny((int) agentsAvailableRes[1]);
					report.setAgentsSerialNumbers(event.getSerialAgentsNumbers());
					report.setAgentsNames((List<String>) agentsAvailableRes[0]);
					report.setGadgetName(event.getGadget());
					report.setTimeIssued(event.getTimeIssued());
					report.setQTime(gadgetAvailableRes);
					Diary.getInstance().addReport(report);
				}
				else {
					agentsAvailableRes[2] = 0;

					synchronized (agentsAvailableRes) {
						agentsAvailableRes.notifyAll();
					}
				}
			}
		});
	}

}


