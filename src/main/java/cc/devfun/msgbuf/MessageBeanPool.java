package cc.devfun.msgbuf;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ObjectsQueue {
	private Lock lock;
	private Class<? extends MessageBean> clazz;
	private Set<MessageBean> idleQueue;
	private Set<MessageBean> activeQueue;
	private int maxIdle;

	public ObjectsQueue(Class<? extends MessageBean> clazz, int maxIdle) {
		this.maxIdle = maxIdle;
		this.lock = new ReentrantLock();
		this.clazz = clazz;

		idleQueue = new HashSet<MessageBean>();
		activeQueue = new HashSet<MessageBean>();
	}

	public Set<MessageBean> getIdleQueue() {
		return idleQueue;
	}

	public Set<MessageBean> getActiveQueue() {
		return activeQueue;
	}

	public int getIdlesNum() {
		return idleQueue.size();
	}

	public int getActivesNum() {
		return activeQueue.size();
	}

	public Class<? extends MessageBean> getClazz() {
		return clazz;
	}

	public MessageBean borrowMessageBean() {
		lock.lock();
		try {
			MessageBean mbean = null;
			if (idleQueue.isEmpty()) {
				try {
					mbean = clazz.newInstance();
				} catch (Throwable t) {
					throw new MessageBufferException(String.format(
							"Create MessageBean instance for class %s failed.",
							clazz.getSimpleName()), t);
				}
			} else {
				Iterator<MessageBean> itr = idleQueue.iterator();
				while (itr.hasNext()) {
					mbean = itr.next();
					break;
				}
			}

			idleQueue.remove(mbean);
			activeQueue.add(mbean);
			return mbean;
		} finally {
			lock.unlock();
		}
	}

	public boolean returnMessageBean(MessageBean mbean) {
		if (mbean == null) {
			return false;
		} else if (mbean.getClass() != clazz) {
			return false;
		}

		lock.lock();
		try {
			if (!activeQueue.remove(mbean)) {
				return false;
			}

			mbean.clear();
			if (maxIdle > 0 && idleQueue.size() < maxIdle) {
				idleQueue.add(mbean);
			}

			return true;
		} finally {
			lock.unlock();
		}
	}
}

public class MessageBeanPool {
	private final static int MAX_IDLE = 100;
	private Map<Class<? extends MessageBean>, ObjectsQueue> pool;
	private Lock lock;
	private ReleaseMessageBeanFormat recycler;
	private CommandFactory commandFactory;

	public MessageBeanPool(CommandFactory factory) {
		commandFactory = factory;
		lock = new ReentrantLock();
		pool = new HashMap<Class<? extends MessageBean>, ObjectsQueue>();
		recycler = new ReleaseMessageBeanFormat(this);
	}

	private ObjectsQueue getObjectsQueue(Class<? extends MessageBean> clazz) {
		lock.lock();
		try {
			ObjectsQueue oq = pool.get(clazz);
			if (oq == null) {
				oq = new ObjectsQueue(clazz, MAX_IDLE);
				pool.put(clazz, oq);
			}

			return oq;
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends MessageBean> T borrowMessageBean(Class<T> clazz) {
		ObjectsQueue queue = getObjectsQueue(clazz);
		T mbean = (T) queue.borrowMessageBean();
		mbean.set$CommandFactory(commandFactory);
		return mbean;
	}

	public boolean returnMessageBean(MessageBean mbean) {
		if (mbean == null) {
			return false;
		}

		mbean.serialize(recycler);
		ObjectsQueue queue = getObjectsQueue(mbean.getClass());
		return queue.returnMessageBean(mbean);
	}

	@Override
	public String toString() {
		List<ObjectsQueue> list = new ArrayList<ObjectsQueue>();
		list.addAll(pool.values());
		Collections.sort(list, new Comparator<ObjectsQueue>() {
			@Override
			public int compare(ObjectsQueue o1, ObjectsQueue o2) {
				int agap = o2.getActivesNum() - o1.getActivesNum();
				return agap != 0 ? agap : o2.getIdlesNum() - o1.getIdlesNum();
			}
		});

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("==== MessageBeanPool status ====");
		for (ObjectsQueue q : list) {
			pw.println(String.format("%15s actives:%-3d idles:%-3d", q.getClazz()
					.getSimpleName(), q.getActivesNum(), q.getIdlesNum()));
		}

		return sw.toString();
	}
}