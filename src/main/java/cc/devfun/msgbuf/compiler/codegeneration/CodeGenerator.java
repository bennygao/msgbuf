
package cc.devfun.msgbuf.compiler.codegeneration;

import cc.devfun.msgbuf.compiler.MessageDefinition;

public interface CodeGenerator {
	public void generate(String srcDir, String srcPackage,
			String outputEncoding, MessageDefinition msgdef) throws Exception;
}
