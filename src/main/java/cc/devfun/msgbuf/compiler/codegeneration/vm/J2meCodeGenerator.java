
package cc.devfun.msgbuf.compiler.codegeneration.vm;

import java.io.File;
import java.io.Writer;
import java.util.Date;

import cc.devfun.msgbuf.compiler.MessageDefinition;
import cc.devfun.msgbuf.compiler.codegeneration.CodeGenerator;
import cc.devfun.msgbuf.compiler.codegeneration.Utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public class J2meCodeGenerator extends VelocityCodeGenerator implements
		CodeGenerator {

	public J2meCodeGenerator() throws Exception {
		init();
	}
	
	@Override
	public void generate(String srcDir, String srcPackage, 
			String outputEncoding, MessageDefinition msgdef) throws Exception {
		VelocityContext vc = new VelocityContext();
		vc.put("package", srcPackage);
		vc.put("commands", msgdef.getAllCommands());
		vc.put("createTime", new Date());
		vc.put("utils", Utils.getInstance());
		vc.put("allMessageBeans", msgdef.getAllMessageBeans());

		String path = getSourcePath(srcDir, srcPackage);
		new File(path).mkdirs();

		Template template;
		Writer writer;

		template = Velocity.getTemplate("j2me/MessageBuffer.java.vm");
		writer = getSourceWriter(path + File.separatorChar + "MessageBuffer.java", outputEncoding);
		template.merge(vc, writer);
		writer.close();

	}

}
