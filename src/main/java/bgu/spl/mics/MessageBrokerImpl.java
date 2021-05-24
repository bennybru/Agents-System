package bgu.spl.mics;

import bgu.spl.mics.application.messages.TickBroadcast;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static class MessageBrokerHolder {
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> messagesSubsMap;
	private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> subsMessagesMap;
	private ConcurrentHashMap<Event,Future> eventFutureMap;

	private MessageBrokerImpl() {
		messagesSubsMap = new ConcurrentHashMap<>();
		subsMessagesMap = new ConcurrentHashMap<>();
		eventFutureMap = new ConcurrentHashMap<>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return MessageBrokerHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (type)
		{
			messagesSubsMap.putIfAbsent(type,new ConcurrentLinkedQueue<>());
			messagesSubsMap.get(type).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (type)
		{
			messagesSubsMap.putIfAbsent(type,new ConcurrentLinkedQueue<>());
			messagesSubsMap.get(type).add(m);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		eventFutureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		Queue<Subscriber> subs = messagesSubsMap.get(b.getClass());
		if (subs != null)
			for (Subscriber s : subs) {
				subsMessagesMap.get(s).add(b);
			}

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = null;
			synchronized (e.getClass()) {
				Queue<Subscriber> subs = messagesSubsMap.get(e.getClass());
				if (subs != null) {
					future = new Future<>();
					eventFutureMap.putIfAbsent(e,future);
					Subscriber sub = subs.remove();
					subsMessagesMap.get(sub).add(e);
					subs.add(sub);
				}
			}
		return future;
	}

	@Override
	public void register(Subscriber m) {
		BlockingQueue<Message> msgs = new LinkedBlockingQueue<>();
		subsMessagesMap.putIfAbsent(m,msgs);
	}

	@Override
	public void unregister(Subscriber m) {
		for(ConcurrentLinkedQueue<Subscriber> subQueue : messagesSubsMap.values()) {
			subQueue.remove(m);
		}
		for(Message msg : subsMessagesMap.get(m)){
			if (msg.getClass() != TickBroadcast.class) {
					eventFutureMap.get(msg).resolve(null);
			}
		}
		subsMessagesMap.remove(m);
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subsMessagesMap.get(m).take();
	}
}
