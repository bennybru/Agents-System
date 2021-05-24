package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private AtomicInteger currentTick;
	private int duration;

	public TimeService(int duration) {
		super("Timer");
		currentTick = new AtomicInteger(0);
		this.duration = duration;
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		while (currentTick.get() <= duration) {//PROBLEM
			MessageBrokerImpl.getInstance().sendBroadcast(new TickBroadcast(currentTick.get()));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			currentTick.incrementAndGet();
		}

		MessageBrokerImpl.getInstance().sendBroadcast(new TickBroadcast(-1));

	}
}