package cc.devfun.msgbuf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CommandArgument {
	private Class<? extends MessageBean> clazz;
	private int commandId;
	private int ioMode;
	private boolean compressed;
	private MessageBean messageBean;
	private CommandFactory commandFactory;

	public CommandArgument(int commandId, int mode,
			Class<? extends MessageBean> clazz, boolean compressed, CommandFactory factory) {
		this.commandId = commandId;
		this.ioMode = mode;
		this.clazz = clazz;
		this.compressed = compressed;
		this.messageBean = null;
		this.commandFactory = factory;
	}

	public CommandArgument(int commandId, int mode, CommandFactory factory) {
		this(commandId, mode, null, false, factory);
	}

	public void serialize(OutputStream ostream) throws Exception {
		DataOutputStream dos = new DataOutputStream(ostream);
		BinaryMessageBeanFormat format = new BinaryMessageBeanFormat(commandFactory, dos);
		// 向流中写入1个byte的压缩与否标志
		format.write(null, compressed, boolean.class);

		if (compressed) { // 指令消息是要压缩的
			// GZIP方法压缩写入
			GZIPOutputStream gzip = new GZIPOutputStream(ostream);
			format.setDataOutput(new DataOutputStream(gzip));
			format.write(null, messageBean, clazz);
			gzip.finish();
		} else {
			format.write(null, messageBean, clazz);
		}
	}

	public MessageBean deserialize(InputStream istream) throws Exception {
		DataInputStream dis = new DataInputStream(istream);
		BinaryMessageBeanFormat format = new BinaryMessageBeanFormat(commandFactory, dis);

		// 读1个byte的消息是否压缩标志
		boolean isZipped = format.read(null, boolean.class);
		if (isZipped) {
			// GZIP方法解压缩读入
			DataInputStream gzip = new DataInputStream(new GZIPInputStream(dis));
			format.setDataInput(gzip);
		}

		messageBean = format.read(null, clazz);
		return messageBean;
	}

	@Override
	public Object clone() {
		CommandArgument newone = new CommandArgument(commandId, ioMode, clazz,
				compressed, commandFactory);
		return newone;
	}

	public void clear() {
		if (messageBean != null) {
			commandFactory.returnMessageBean(messageBean);
			messageBean = null;
		}
	}

	public Class<? extends MessageBean> getClazz() {
		return clazz;
	}

	public boolean isCompressed() {
		return compressed;
	}

	public MessageBean getMessageBean() {
		if (clazz != null && messageBean == null) {
			messageBean = commandFactory
					.borrowMessageBean(clazz);
		}

		return messageBean;
	}

	public void setMessageBean(MessageBean mbean) {
		if (clazz == null) { // 这个Command的argument为void
			throw new MessageBufferException(String.format(
					"%s argument of command %d is void, cannot assign value.",
					(ioMode == CommandFactory.ARG_INPUT ? "INPUT" : "OUTPUT"),
					commandId));
		} else if (this.messageBean != null) {
			commandFactory.returnMessageBean(this.messageBean);
		}

		this.messageBean = mbean;
	}

	@Override
	public String toString() {
		if (clazz == null) {
			return "void";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(clazz.getSimpleName());
			if (compressed) {
				sb.append("(commpressed)");
			}
			return sb.toString();
		}
	}
}
