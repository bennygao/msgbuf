
package cc.devfun.msgbuf.compiler.model;

import java.util.List;

public class Field {
	private String name;
	private List<String> doc;
	private AbstractType type;

	public Field(AbstractType type, String name, List<String> doc) {
		this.type = type;
		this.name = name;
		this.doc = doc;
	}

	public String getName() {		
		return name;
	}

	public AbstractType getType() {
		return type;
	}

	public String getDefaultValue() {
		return type.getDefaultValue();
	}

	public boolean hasDoc() {
		return doc == null ? false : true;
	}

	public List<String> getDoc() {
		return doc;
	}
}
