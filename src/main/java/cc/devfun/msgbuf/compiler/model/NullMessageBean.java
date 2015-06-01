
package cc.devfun.msgbuf.compiler.model;

public class NullMessageBean extends MessageBean {
	public NullMessageBean() {
		super("NullMessageBean");
		define(null, null, null);
	}
	
	@Override
	public boolean isNullMessage() {
		return true;
	}
	
	@Override
	public String getClazzName() {
		return getName();
	}
	
	@Override
	public boolean isNull() {
		return true;
	}
}
