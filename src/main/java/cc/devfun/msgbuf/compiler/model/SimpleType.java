
package cc.devfun.msgbuf.compiler.model;

import java.util.HashMap;
import java.util.Map;

class ByteType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_BYTE;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}
}

class BooleanType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_BOOLEAN;
	}
	
	@Override
	public String getDefaultValue() {
		return "false";
	}
}

class ShortType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_SHORT;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}
}

class IntType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_INT;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}
}

class LongType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_LONG;
	}

	@Override
	public String getDefaultValue() {
		return "0L";
	}
}

class FloatType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_FLOAT;
	}
	
	@Override
	public String getDefaultValue() {
		return "0";
	}
}

class DoubleType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_DOUBLE;
	}
	
	@Override
	public String getDefaultValue() {
		return "0";
	}
}

class StringType extends SimpleType {
	@Override
	public String getName() {
		return TYPE_STRING;
	}
	
	@Override
	public String getDefaultValue() {
		return "null";
	}
	
	@Override
	public boolean isBasic() {
		return false;
	}
}

public abstract class SimpleType extends AbstractType {
	private static Map<String, AbstractType> basicTypes = new HashMap<String, AbstractType>();
	static {
		basicTypes.put(TYPE_BYTE, new ByteType());
		basicTypes.put(TYPE_BOOLEAN, new BooleanType());
		basicTypes.put(TYPE_SHORT, new ShortType());
		basicTypes.put(TYPE_INT, new IntType());
		basicTypes.put(TYPE_LONG, new LongType());
		basicTypes.put(TYPE_DOUBLE, new DoubleType());
		basicTypes.put(TYPE_FLOAT, new FloatType());
		basicTypes.put(TYPE_STRING, new StringType());
	}

	public static AbstractType getInstance(String type) {
		return basicTypes.get(type);
	}
	
	@Override
	public boolean isBasic() {
		return true;
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
	public boolean isMap() {
		return false;
	}
	
	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public AbstractType getElementType() {
		return null;
	}
	
	@Override
	public String getPrototype() {
		return getName();
	}
}

