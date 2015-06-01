
package cc.devfun.msgbuf.compiler.parser;

import java.io.InputStream;

import cc.devfun.msgbuf.compiler.ModelBuilder;


public interface Parser {
	public boolean parse(InputStream in, ModelBuilder builder);
}
