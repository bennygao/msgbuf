
package cc.devfun.msgbuf.compiler.codegeneration;

import cc.devfun.msgbuf.compiler.codegeneration.vm.J2meCodeGenerator;

public class J2meCodeGeneratorFactory implements CodeGeneratorFactory {

	@Override
	public CodeGenerator createCodeGenerator() throws Exception {
		// TODO Auto-generated method stub
		return new J2meCodeGenerator();
	}

}
