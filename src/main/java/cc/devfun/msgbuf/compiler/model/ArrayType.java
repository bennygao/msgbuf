package cc.devfun.msgbuf.compiler.model;

public class ArrayType extends VectorType {
	public ArrayType(Object obj) {
		super(obj);
		AbstractType elType = (AbstractType) obj;
		if (!elType.isBasic()) {
			throw new IllegalArgumentException("不支持string和message的数组，请用vector代替。");
		}
	}
	
	public ArrayType(AbstractType elType) {
		super(elType);
		if (!elType.isBasic()) {
			throw new IllegalArgumentException("不支持string和message的数组，请用vector代替。");
		}
	}

	@Override
	public boolean isVector() {
		return false;
	}

	@Override
	public boolean isArray() {
		return true;
	}
	
	@Override
	public String getPrototype() {
		return String.format("%s[]", getElementType().getPrototype());
	}
}
