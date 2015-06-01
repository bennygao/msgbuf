package cc.devfun.msgbuf;

public class MessageBufferException extends RuntimeException {
	private static final long serialVersionUID = 2227353523048553301L;

	public MessageBufferException(Throwable cause) {
		this(null, cause);
	}
	
	public MessageBufferException(String msg) {
		this(msg, null);
	}
	
	public MessageBufferException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
