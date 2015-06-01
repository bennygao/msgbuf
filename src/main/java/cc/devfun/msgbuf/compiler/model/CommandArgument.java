package cc.devfun.msgbuf.compiler.model;

import cc.devfun.msgbuf.compiler.parser.Location;

public class CommandArgument {
	private MessageBean messageBean;
	private boolean compressed;

	public CommandArgument(String beanName, boolean compressed, Location location) {
		this.messageBean = MessageBean.getMessageBean(beanName, location);
		this.compressed = compressed;
	}

	public CommandArgument(String beanName, Location location) {
		this(beanName, false, location);
	}
	
	public CommandArgument() {
		this.messageBean = null;
		this.compressed = false;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (messageBean == null) {
			sb.append("void");
		} else if (messageBean.isNullMessage()) {
			sb.append("null");
		} else {
			sb.append(messageBean.getName());
		}
		
		if (compressed) {
			sb.append("(compressed)");
		}
		
		return sb.toString();
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public boolean isCompressed() {
		return compressed;
	}
}
