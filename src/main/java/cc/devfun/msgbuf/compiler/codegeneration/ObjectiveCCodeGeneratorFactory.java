package cc.devfun.msgbuf.compiler.codegeneration;

import cc.devfun.msgbuf.compiler.codegeneration.vm.ObjectiveCCodeGenerator;

public class ObjectiveCCodeGeneratorFactory  implements CodeGeneratorFactory {

	@Override
	public CodeGenerator createCodeGenerator() throws Exception {
		// TODO Auto-generated method stub
		return new ObjectiveCCodeGenerator();
	}
}
