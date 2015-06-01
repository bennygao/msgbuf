package cc.devfun.msgbuf.compiler.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import cc.devfun.msgbuf.compiler.Context;
import cc.devfun.msgbuf.compiler.ModelBuilder;
import cc.devfun.msgbuf.compiler.parser.Location;
import static java.lang.String.*;

public class MessageBean extends AbstractType {
	// ID必须从1开始（不能从0开始，C++实现中的要求）
	private static AtomicInteger ID = new AtomicInteger(1);
	private static MessageDigest shaDigest = null;
	public static Map<String, MessageBean> allMessageBeans = new HashMap<String, MessageBean>();

	public static MessageBean createMessageBean(String beanName,
			List<String> doc, String parent, Location location) {
		MessageBean bean = allMessageBeans.get(beanName);
		if (bean == null) {
			bean = new MessageBean(beanName);
			allMessageBeans.put(beanName, bean);
		}

		bean.define(doc, parent, location);
		// 引用父类
		getMessageBean(parent, location);
		return bean;
	}

	public static MessageBean getMessageBean(String beanName, Location location) {
		if (beanName == null) {
			beanName = ModelBuilder.NULL_MESSAGE_BEAN_NAME;
		}

		MessageBean bean = allMessageBeans.get(beanName);
		if (bean == null) { // 先引用，后定义message。
			bean = new MessageBean(beanName);
			allMessageBeans.put(beanName, bean);
		}

		if (location != null) {
			bean.addReference(location);
		}

		return bean;
	}

	private int typeId;
	private String name;
	private String parent = null;
	private MessageBean parentMessageBean = null;
	private List<String> doc;
	private Map<String, Field> fields;
	private List<String> fieldsName;
	private boolean resolved = false;
	private boolean zipped = false;
	private boolean defined = false;
	private Location definedLocation = null;
	private List<Location> references;
	private Set<MessageBean> inheritors;
	private Set<MessageBean> dependedBy;

	protected MessageBean(String name) {
		this.typeId = ID.getAndIncrement();
		this.name = name;
		this.defined = false;
		this.references = new ArrayList<Location>();
		inheritors = new HashSet<MessageBean>();
		dependedBy = new HashSet<MessageBean>();
	}

	public int getTypeId() {
		return typeId;
	}

	public int getDerivedLevel() {
		if (parentMessageBean == null) {
			return 1;
		} else {
			return parentMessageBean.getDerivedLevel() + 1;
		}
	}

	public void addReference(Location location) {
		references.add(location);
	}

	public Collection<Location> getReferences() {
		return references;
	}

	public void addInheritor(MessageBean h) {
		if (this != h) {
			inheritors.add(h);
		}
	}

	public Collection<MessageBean> getInheritors() {
		return inheritors;
	}

	public void addDependedBy(MessageBean d) {
		if (this != d) {
			dependedBy.add(d);
		}
	}

	public Collection<MessageBean> getDependedBy() {
		return dependedBy;
	}

	public int getReferencesCount() {
		return references.size();
	}

	public void define(List<String> doc, String parent, Location location) {
		if (defined) {
			throw new IllegalStateException(format("消息名字重复，%s 在 %s 已经定义过。",
					name, getDefinedLocation().toSimpleString()));
		}

		this.doc = doc;
		this.parent = parent;

		fields = new HashMap<String, Field>();
		fieldsName = new ArrayList<String>();

		this.defined = true;
		this.definedLocation = location;
	}

	public boolean isDefined() {
		return defined;
	}

	public boolean hasDoc() {
		return this.doc == null ? false : true;
	}

	public boolean containsBeanVector() {
		for (Field field : fields.values()) {
			if (field.getType().isVector()) {
				if (field.getType().getElementType().isBean()) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean containsMap() {
		for (Field field : fields.values()) {
			if (field.getType().isMap()) {
				return true;
			}
		}

		return false;
	}

	public boolean containsVector() {
		for (Field field : fields.values()) {
			if (field.getType().isVector()) {
				return true;
			}
		}

		return false;
	}

	public Collection<String> getDoc() {
		return this.doc;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public void addField(AbstractType type, String fieldName, List<String> doc) {
		if (fields.get(fieldName) != null) {
			throw new RuntimeException("message " + name + "的属性" + fieldName
					+ "定义重复。");
		} else {
			Field f = new Field(type, fieldName, doc);
			fields.put(fieldName, f);
			fieldsName.add(fieldName);
		}
	}

	public String getDigest() {
		try {
			if (shaDigest == null) {
				shaDigest = MessageDigest.getInstance("SHA");
			}

			String contents = toString();
			byte[] bytes = shaDigest.digest(contents.getBytes("UTF-8"));
			String digest = toHex(bytes);
			return digest;
		} catch (Exception e) {
			throw new RuntimeException("未MessageBean生成Digest出错。", e);
		}
	}

	private String getIdent(int ident) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ident; ++i) {
			sb.append('\t');
		}
		return sb.toString();
	}

	private void printJavaDoc(PrintWriter pw, List<String> doc, int ident) {
		if (doc == null) {
			return;
		} else if (doc.size() == 0) {
			return;
		} else {
			String identStr = getIdent(ident);
			pw.printf("%s/**\n", identStr);
			for (String l : doc) {
				pw.printf("%s * %s\n", identStr, l);
			}
			pw.printf("%s */\n", identStr);
		}
	}

	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		// MessageBean的JavaDoc
		printJavaDoc(pw, doc, 0);

		// MessageBean的内容
		pw.printf("message %s {\n", name);
		String ident = getIdent(1);
		for (String filedName : fieldsName) {
			Field field = fields.get(filedName);
			printJavaDoc(pw, field.getDoc(), 1);
			pw.printf("%s%s %s;\n\n", ident, field.getType().getName(),
					field.getName());
		}
		pw.printf("}\n");
		pw.close();
		return sw.toString();
	}

	public List<String> getFieldsName() {
		return fieldsName;
	}

	public Field getField(String key) {
		return fields.get(key);
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean r) {
		resolved = r;
	}

	@Override
	public boolean isBean() {
		return true;
	}

	@Override
	public boolean isVector() {
		return false;
	}

	public boolean isNull() {
		return false;
	}

	@Override
	public AbstractType getElementType() {
		return null;
	}

	public boolean isNullMessage() {
		return false;
	}

	@Override
	public String getDefaultValue() {
		return "null";
	}

	public String getClazzName() {
		String prefix = Context.getInstance().getPrefix();
		return prefix == null ? name : prefix + name;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getParent() {
		return parent;
	}

	public MessageBean getParentMessageBean() {
		return allMessageBeans.get(parent);
	}

	public String getParentClazzName() {
		if (parent == null) {
			return "MessageBean";
		} else {
			String prefix = Context.getInstance().getPrefix();
			return prefix == null ? parent : prefix + parent;
		}
	}

	private static String toHex(byte[] data) {
		if (data == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; ++i) {
			sb.append(getHex(data[i] >> 4)).append(getHex(data[i] & 0x0f));

		}

		return sb.toString();
	}

	static char[] hexChar = new char[] { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static char getHex(int b) {
		return hexChar[b & 0x0f];
	}

	public boolean isZipped() {
		return zipped;
	}

	public void setZipped(boolean zipped) {
		this.zipped = zipped;
	}

	@Override
	public boolean isBasic() {
		return false;
	}

	@Override
	public boolean isMap() {
		return false;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	public Location getDefinedLocation() {
		return definedLocation;
	}

	public void setDefinedLocation(Location definedLocation) {
		this.definedLocation = definedLocation;
	}

	@Override
	public String getPrototype() {
		return getClazzName();
	}

	public void setParentMessageBean(MessageBean parentMessageBean) {
		this.parentMessageBean = parentMessageBean;
		parentMessageBean.addInheritor(this);
	}
}
