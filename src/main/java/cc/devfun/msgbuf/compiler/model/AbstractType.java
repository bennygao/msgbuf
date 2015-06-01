
package cc.devfun.msgbuf.compiler.model;

public abstract class AbstractType {
	public static String TYPE_BYTE = "byte";
	public static String TYPE_BOOLEAN = "boolean";
	public static String TYPE_SHORT = "short";
	public static String TYPE_INT = "int";
	public static String TYPE_LONG = "long";
	public static String TYPE_FLOAT = "float";
	public static String TYPE_DOUBLE = "double";
	public static String TYPE_STRING = "string";
	public static String TYPE_VECTOR = "vector";
	
	abstract public boolean isBean();
	abstract public boolean isVector();
	abstract public boolean isArray();
	abstract public boolean isMap();
	abstract public boolean isBasic();
	
	abstract public String getName();
	abstract public AbstractType getElementType();
	abstract public String getDefaultValue();
	abstract public String getPrototype();
}
