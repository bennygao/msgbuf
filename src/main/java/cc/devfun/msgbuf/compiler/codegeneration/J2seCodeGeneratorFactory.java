
package cc.devfun.msgbuf.compiler.codegeneration;

import cc.devfun.msgbuf.compiler.codegeneration.vm.J2seCodeGenerator;

public class J2seCodeGeneratorFactory implements CodeGeneratorFactory {

	@Override
	public CodeGenerator createCodeGenerator() throws Exception {
		// TODO Auto-generated method stub
		return new J2seCodeGenerator();
	}

}
