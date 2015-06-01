package cc.devfun.msgbuf.compiler.model;

public class MapType extends AbstractType {
	private AbstractType keyType, valueType;

	public MapType(Object kt, Object vt) {
		this.keyType = (AbstractType) kt;
		this.valueType = (AbstractType) vt;
	}

	@Override
	public boolean isBean() {
		return false;
	}

	@Override
	public boolean isVector() {
		return false;
	}
	
	@Override
	public boolean isBasic() {
		return false;
	}

	@Override
	public String getName() {
		return "map";
	}

	@Override
	public AbstractType getElementType() {
		return null;
	}

	@Override
	public String getDefaultValue() {
		return "null";
	}

	@Override
	public boolean isMap() {
		return true;
	}

	public AbstractType getKeyType() {
		return keyType;
	}

	public AbstractType getValueType() {
		return valueType;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public String getPrototype() {
		return String.format("map<%s, %s>", keyType.getPrototype(),
				valueType.getPrototype());
	}
}
