package cc.devfun.msgbuf.compiler.codegeneration;

import cc.devfun.msgbuf.compiler.codegeneration.vm.CSharpCodeGenerator;

public class CSharpCodeGeneratorFactory implements CodeGeneratorFactory {
	@Override
	public CodeGenerator createCodeGenerator() throws Exception {
		return new CSharpCodeGenerator();
	}

}
