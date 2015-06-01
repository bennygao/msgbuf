package cc.devfun.msgbuf.compiler.codegeneration.vm;

import java.io.File;
import java.io.Writer;
import java.util.Date;

import cc.devfun.msgbuf.compiler.codegeneration.CodeGenerator;
import cc.devfun.msgbuf.compiler.codegeneration.Utils;
import cc.devfun.msgbuf.compiler.MessageDefinition;
import cc.devfun.msgbuf.compiler.model.MessageBean;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public class AndroidCodeGenerator extends VelocityCodeGenerator implements
		CodeGenerator {
	
	public AndroidCodeGenerator() throws Exception {
		init();
	}
	
	public void generate(String srcDir, String srcPackage,
			String outputEncoding, MessageDefinition msgdef) throws Exception {
		VelocityContext vc = new VelocityContext();
		vc.put("package", srcPackage);
		vc.put("commands", msgdef.getAllCommands());
		vc.put("zipCommands", msgdef.getAllZipCommands());
		vc.put("createTime", new Date());
		vc.put("utils", Utils.getInstance());

		String path = getSourcePath(srcDir, srcPackage);
		new File(path).mkdirs();

		Template template;
		Writer writer;

		template = Velocity.getTemplate("android/ObjectFactory.java.vm");
		writer = getSourceWriter(path + File.separatorChar
				+ "ObjectFactory.java", outputEncoding);
		template.merge(vc, writer);
		writer.close();

		template = Velocity.getTemplate("android/MessageBean.java.vm");
		StringBuffer pathname = new StringBuffer();
		for (MessageBean bean : msgdef.getAllMessageBeans()) {
			pathname.setLength(0);
			pathname.append(path).append(File.separatorChar).append(
					bean.getName()).append(".java");
			if (needGenerate(pathname.toString(), outputEncoding, bean
					.getDigest())) {
				vc.put("messageBean", bean);
				writer = getSourceWriter(pathname.toString(), outputEncoding);
				template.merge(vc, writer);
				writer.close();
			}
		}

	}
}
