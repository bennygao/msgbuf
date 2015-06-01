package cc.devfun.msgbuf;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 指令编号与消息实体对应关系的接口。
 * 
 */
public abstract class CommandFactory {
	public final static int ARG_INPUT = 1;
	public final static int ARG_OUTPUT = -1;
	
	/** MessageBean对象池 */
	private MessageBeanPool pool;
	/** 指令映射表 */
	protected Map<Integer, Command> commands;

	public CommandFactory() {
		commands = new HashMap<Integer, Command>();
		pool = new MessageBeanPool(this);
	}
	
	public MessageBeanPool getMessageBeanPool() {
		return pool;
	}

	protected void addCommand(Command command) {
		commands.put(command.getId(), command);
	}

	public Command getCommand(int commandId) throws Exception {
		Command command = commands.get(Math.abs(commandId));
		if (command == null) {
			throw new MessageProcessException("Undefined command ID "
					+ commandId);
		} else {
			return (Command) command.clone();
		}
	}

	/**
	 * 获得所有MessageBean的Class
	 * 
	 * @return 所有MessageBean的Class
	 */
	public Set<Class<? extends MessageBean>> getAllClasses() {
		Set<Class<? extends MessageBean>> set = new LinkedHashSet<Class<? extends MessageBean>>();
		Class<? extends MessageBean> beanClazz;
		for (Command command : commands.values()) {
			beanClazz = command.getInput().getClazz();
			if (beanClazz != null) {
				set.add(beanClazz);
			}
			
			beanClazz = command.getOutput().getClazz();
			if (beanClazz != null) {
				set.add(beanClazz);
			}
		}

		return set;
	}
	
	public abstract MessageBean borrowMessageBean(int typeId);
	
	public <T extends MessageBean> T borrowMessageBean(Class<T> clazz) {
		return pool.borrowMessageBean(clazz);
	}
	
	public void returnMessageBean(MessageBean mbean) {
		pool.returnMessageBean(mbean);
	}
}
