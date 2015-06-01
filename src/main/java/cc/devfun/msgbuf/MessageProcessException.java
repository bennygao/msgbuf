
package cc.devfun.msgbuf;

public class MessageProcessException extends Exception {
	private static final long serialVersionUID = 2181230634451620112L;
	private StringBuffer errmsg;
	private Throwable cause;
	
	public MessageProcessException(String errmsg, Throwable cause) {
		this.cause = cause;
		this.errmsg = new StringBuffer(errmsg);
	}
	
	public MessageProcessException(String errmsg) {
		this(errmsg, null);
	}
	
	public MessageProcessException(MessageBean mb, String msg, Throwable cause) {
		this.cause = cause;
		errmsg = new StringBuffer();
		errmsg.append("#错误:").append(msg);
	}
	
	public MessageProcessException(MessageBean mb, String msg) {
		this(mb, msg, null);
	}
	
	@Override
	public String getMessage() {
		return this.errmsg.toString();
	}
	
	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}
	
	public Throwable getCause() {
		return this.cause;
	}

}
