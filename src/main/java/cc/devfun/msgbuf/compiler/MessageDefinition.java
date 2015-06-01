package cc.devfun.msgbuf.compiler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import cc.devfun.msgbuf.compiler.model.AbstractType;
import cc.devfun.msgbuf.compiler.model.Command;
import cc.devfun.msgbuf.compiler.model.Field;
import cc.devfun.msgbuf.compiler.model.MapType;
import cc.devfun.msgbuf.compiler.model.MessageBean;
import cc.devfun.msgbuf.compiler.model.VectorType;
import cc.devfun.msgbuf.compiler.parser.Location;
import cc.devfun.msgbuf.compiler.parser.ParserException;

class CommandComparator implements Comparator<Integer> {
	public int compare(Integer v1, Integer v2) {
		return Math.abs(v1) - Math.abs(v2);
	}
}

/**
 * 消息定义，存储消息定义文件通过编译产生的所有消息结构化信息。包括:<br/>
 * <li/>所有消息及其域的定义。 <li/>
 * 指令编号与消息实体类的映射关系。
 * 
 * @author gaobo
 * 
 */
public class MessageDefinition {
	private Map<Integer, Command> commands;
	private MessageBean currentMessage;
	private Set<Integer> zipCommands;

	/**
	 * 构造一个MessageDefinition对象。
	 */
	public MessageDefinition() {
		commands = new TreeMap<Integer, Command>();
		zipCommands = new TreeSet<Integer>();
	}

	/**
	 * 增加一个命令定义
	 * 
	 * @param command
	 */
	public void addCommand(Command command) {
		if (commands.get(command.getId()) != null) {
			throw new RuntimeException("指令" + command.getId() + "与消息的对应关系定义重复");
		} else {
			commands.put(command.getId(), command);

			if (command.getInput().isCompressed()) {
				zipCommands.add(command.getId());
			}

			if (command.getOutput().isCompressed()) {
				zipCommands.add(-command.getId());
			}
		}
	}

	/**
	 * 添加一个在消息定义文件中，由message Name {...}块正式定义的消息。
	 * 
	 * @param name
	 *            消息类名
	 * @param doc
	 *            消息的Javadoc
	 */
	public void newMessage(String name, List<String> doc, String parent,
			Location location) {
		currentMessage = MessageBean.createMessageBean(name, doc, parent,
				location);
	}

	/**
	 * 在当前消息中添加一个域（字段、属性）。
	 * 
	 * @param obj
	 *            域的数据类型
	 * @param name
	 *            域的名称
	 * @param doc
	 *            域的Javadoc
	 */
	public void addField(Object obj, String name, List<String> doc) {
		AbstractType type = (AbstractType) obj;
		currentMessage.addField(type, name, doc);
	}

	/**
	 * 消息定义完整性检查。检查每个指令编号对应的消息是否被正确的定义。<br/>
	 * 错误的情况包括：<br/>
	 * <li/>消息只有引用没有定义。 <li/>消息循环引用。
	 * 
	 * @return 完整性检查通过返回true。
	 */
	public boolean integralityCheck() {
		Map<String, MessageBean> checkedMessages = new HashMap<String, MessageBean>();
		for (MessageBean mbean : MessageBean.allMessageBeans.values()) {
			validate(mbean, checkedMessages);
		}

		// 检查定义了，但是从未被引用的message
		for (MessageBean bean : MessageBean.allMessageBeans.values()) {
			if (bean.getReferencesCount() == 0
					&& !bean.getName().equals(
							ModelBuilder.NULL_MESSAGE_BEAN_NAME)) {
				System.err.println(String.format(
						"警告：[%s] 定义了消息 %s ，但没有任何地方使用它。", bean
								.getDefinedLocation().toSimpleString(), bean
								.getName()));
			}
		}

		// 设置各MessageBea的包含依赖关系
		MessageBean depended;
		for (MessageBean bean : MessageBean.allMessageBeans.values()) {
			for (Field f : bean.getFields().values()) {
				AbstractType type = f.getType();
				if (type.isBean()) {
					depended = (MessageBean) type;
					depended.addDependedBy(bean);
				} else if ((type.isArray() || type.isVector())
						&& type.getElementType().isBean()) {
					depended = (MessageBean) type.getElementType();
					depended.addDependedBy(bean);
				} else if (type.isMap()) {
					MapType mt = (MapType) type;
					if (mt.getKeyType().isBean()) {
						depended = (MessageBean) mt.getKeyType();
						depended.addDependedBy(depended);
					}

					if (mt.getValueType().isBean()) {
						depended = (MessageBean) mt.getValueType();
						depended.addDependedBy(depended);
					}
				}
			}
		}

		return true;
	}

	private String getUndefinedMessageInfo(MessageBean bean) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		for (Location location : bean.getReferences()) {
			ps.print(location.toString());
			ps.println("错误: " + "引用了未定义的消息类型 " + bean.getName() + "。");
			break;
		}

		return baos.toString();
	}

	/**
	 * 检查一个消息定义的完整性。
	 * 
	 * @param bean
	 *            待检查的消息对象。
	 * @param cms
	 *            已经检查过消息集合。
	 */
	private void validate(MessageBean bean, Map<String, MessageBean> cms) {
		if (!bean.isDefined()) {
			throw new ParserException(getUndefinedMessageInfo(bean));
		}

		Map<String, MessageBean> allDefinedMessages = MessageBean.allMessageBeans;
		MessageBean m = allDefinedMessages.get(bean.getName());
		if (m == null) {
			throw new ParserException(getUndefinedMessageInfo(bean));
		}

		if (m.hasParent()) {
			MessageBean parent = allDefinedMessages.get(m.getParent());
			if (parent == null) {
				throw new ParserException(m.getDefinedLocation().toString()
						+ "错误: 未定义" + bean.getName() + "继承之父类" + m.getParent()
						+ "。");
			} else {
				m.setParentMessageBean(parent);
			}
		}

		String beanName = m.getName();
		if (cms.get(beanName) != null) {
			if (!m.isResolved()) {
				throw new ParserException("错误: 循环引用的消息:" + m.getName());
			}
		} else {
			cms.put(beanName, m);
			for (Field f : m.getFields().values()) {
				AbstractType type = f.getType();
				if (type.isBean()) {// instanceof MessageBean) {
					MessageBean mb = (MessageBean) type;
					validate(mb, cms);
				} else if (type.isVector() || type.isArray()) {// instanceof
																// VectorType) {
					VectorType cct = (VectorType) type;
					AbstractType elType = cct.getElementType();
					if (elType.isBean()) {
						MessageBean mb = (MessageBean) elType;
						validate(mb, cms);
					}
				} else if (type.isMap()) {
					MapType mt = (MapType) type;
					AbstractType keyType = mt.getKeyType();
					if (keyType.isBean()) {
						MessageBean mb = (MessageBean) keyType;
						validate(mb, cms);
					}

					AbstractType valueType = mt.getValueType();
					if (valueType.isBean()) {
						MessageBean mb = (MessageBean) valueType;
						validate(mb, cms);
					}
				}
			}
			m.setResolved(true);
		}
	}

	/**
	 * 取得指令编号与消息实体类对应关系的map。
	 * 
	 * @return 指令编号与消息实体类对应关系的map。
	 */
	public Collection<Command> getAllCommands() {
		return commands.values();
	}

	/**
	 * 取得消息定义文件中，通过message Name {...}块正式定义的所有消息。
	 * 
	 * @return 消息定义文件中，通过message Name {...}块正式定义的所有消息。
	 */
	public Collection<MessageBean> getAllMessageBeans() {
		return MessageBean.allMessageBeans.values();
	}

	public Collection<Integer> getAllZipCommands() {
		return zipCommands;
	}

	public MessageBean[] getAllSortedMessageBeans() {
		Map<String, MessageBean> allDefinedMessages = MessageBean.allMessageBeans;
		Collection<MessageBean> beans = allDefinedMessages.values();
		MessageBean[] sortedBeans = new MessageBean[allDefinedMessages.size()];
		Arrays.sort(beans.toArray(sortedBeans), new Comparator<MessageBean>() {
			public int compare(MessageBean b1, MessageBean b2) {
				return b1.getName().compareTo(b2.getName());
			}
		});

		return sortedBeans;
	}

	@Override
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		ps.println("==== Command Message Map ====");
		for (Command command : commands.values()) {
			ps.println(command.toString());
		}

		ps.println("==== Message Define ====");
		Map<String, MessageBean> allDefinedMessages = MessageBean.allMessageBeans;
		for (MessageBean m : allDefinedMessages.values()) {
			ps.printf("#message#%s ", m.getName());
			ps.println();
			Map<String, Field> fields = m.getFields();
			for (Field f : fields.values()) {
				ps.printf("    %s %s", f.getType().getName(), f.getName());
				ps.println();
			}
		}

		return baos.toString();
	}
}
