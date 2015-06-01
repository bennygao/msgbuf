package cc.devfun.msgbuf.compiler.codegeneration;

import cc.devfun.msgbuf.compiler.codegeneration.vm.AndroidCodeGenerator;

public class AndroidCodeGeneratorFactory implements CodeGeneratorFactory {

	@Override
	public CodeGenerator createCodeGenerator() throws Exception {
		// 
		return new AndroidCodeGenerator();
	}

}
