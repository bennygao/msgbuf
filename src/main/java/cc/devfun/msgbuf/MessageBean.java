package cc.devfun.msgbuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MessageBean {
	private int typeId;
	private CommandFactory commandFactory = null;
	
	public MessageBean(int typeId) {
		this.typeId = typeId;
	}
	
	public int getTypeId() {
		return typeId;
	}
	
	public void set$CommandFactory(CommandFactory factory) {
		this.commandFactory = factory;
	}
	
	public void copy(MessageBean another) {
		if (another == null) {
			return;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		BinaryMessageBeanFormat format = new BinaryMessageBeanFormat(commandFactory, dos);
		another.serialize(format);
		
		byte[] bytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bais);
		format.setDataInput(dis);
		this.deserialize(format);
	}

	public void clear() {
	}

	public void release() {
		commandFactory.returnMessageBean(this);
	}

	// //////////////////////////////////////////////////////////////
	// 序列化/反序列化方法
	// //////////////////////////////////////////////////////////////
	public void serialize(MessageBeanFormat format) {
	}
	
	public void serialize(OutputStream output) {
	}

	public MessageBean deserialize(MessageBeanFormat format) {
		return this;
	}
	
	public MessageBean deserialize(InputStream input) {
		return this;
	}

	// ///////////////////////////////////////////////////
	// 格式化输出MessageBean
	// ///////////////////////////////////////////////////
	@Override
	public String toString() {
		try {
			TextMessageBeanFormat format = new TextMessageBeanFormat();
			format.write(null, this, getClass());
			return format.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
