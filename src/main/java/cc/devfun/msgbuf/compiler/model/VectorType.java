package cc.devfun.msgbuf.compiler.model;

public class VectorType extends AbstractType {
	private AbstractType elementType;

	public VectorType(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Element type cannot be null.");
		}
		this.elementType = (AbstractType) obj;
	}

	public VectorType(AbstractType elType) {
		if (elType == null) {
			throw new IllegalArgumentException("Element type cannot be null.");
		}
		this.elementType = elType;
	}

	public String toString() {
		return "vector<" + elementType.getName() + ">";
	}
	
	@Override
	public boolean isBasic() {
		return false;
	}

	@Override
	public boolean isBean() {
		return false;
	}

	@Override
	public AbstractType getElementType() {
		return elementType;
	}

	@Override
	public String getName() {
		return "vector";
	}

	@Override
	public String getDefaultValue() {
		return "null";
	}

	@Override
	public boolean isMap() {
		return false;
	}

	@Override
	public boolean isVector() {
		return true;
	}

	@Override
	public boolean isArray() {
		return false;
	}
	
	@Override
	public String getPrototype() {
		return String.format("vector<%s>", elementType.getPrototype());
	}
}
