package cc.devfun.msgbuf.compiler.codegeneration;

import cc.devfun.msgbuf.compiler.codegeneration.vm.HtmlDocumentGenerator;

public class HtmlDocumentGeneratorFactory implements CodeGeneratorFactory {

	@Override
	public CodeGenerator createCodeGenerator() throws Exception {
		// TODO Auto-generated method stub
		return new HtmlDocumentGenerator();
	}

}
