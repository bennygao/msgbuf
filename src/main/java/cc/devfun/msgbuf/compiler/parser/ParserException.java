package cc.devfun.msgbuf.compiler.parser;

public class ParserException extends RuntimeException {
	private static final long serialVersionUID = 1848717190710994495L;

	public ParserException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ParserException(String msg) {
		super(msg, null);
	}
	
	public ParserException(Throwable cause) {
		super(null, cause);
	}
}
