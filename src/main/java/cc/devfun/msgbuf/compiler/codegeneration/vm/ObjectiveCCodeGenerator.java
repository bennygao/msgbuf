package cc.devfun.msgbuf.compiler.codegeneration.vm;

import java.io.File;
import java.io.Writer;
import java.util.Date;

import cc.devfun.msgbuf.compiler.codegeneration.CodeGenerator;
import cc.devfun.msgbuf.compiler.codegeneration.Utils;
import cc.devfun.msgbuf.compiler.MessageDefinition;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public class ObjectiveCCodeGenerator extends VelocityCodeGenerator implements
		CodeGenerator {

	public ObjectiveCCodeGenerator() throws Exception {
		init();
	}
	
	@Override
	public void generate(String srcDir, String srcPackage, 
			String outputEncoding, MessageDefinition msgdef) throws Exception {
		VelocityContext vc = new VelocityContext();
		vc.put("package", srcPackage);
		vc.put("commands", msgdef.getAllCommands());
		vc.put("zipCommands", msgdef.getAllZipCommands());
		vc.put("createTime", new Date());
		vc.put("utils", Utils.getInstance());
		vc.put("allMessageBeans", msgdef.getAllMessageBeans());

		String path = getSourcePath(srcDir, srcPackage);
		new File(path).mkdirs();

		Template template;
		Writer writer;

		// Generate C++ header file.
		template = Velocity.getTemplate("objc/msgbuf.h.vm");
		writer = getSourceWriter(path + File.separatorChar + "msgbuf.h", outputEncoding);
		template.merge(vc, writer);
		writer.close();
		
		// Generate C++ classes source code.
		template = Velocity.getTemplate("objc/msgbuf.m.vm");
		writer = getSourceWriter(path + File.separatorChar + "msgbuf.m", outputEncoding);
		template.merge(vc, writer);
		writer.close();

	}
}
