package cc.devfun.msgbuf.compiler.codegeneration.vm;

import java.io.File;
import java.io.Writer;
import java.util.Date;

import cc.devfun.msgbuf.compiler.codegeneration.Utils;
import cc.devfun.msgbuf.compiler.MessageDefinition;
import cc.devfun.msgbuf.compiler.codegeneration.CodeGenerator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class HtmlDocumentGenerator extends VelocityCodeGenerator implements
		CodeGenerator {
	public HtmlDocumentGenerator() throws Exception {
		init();
	}

	@Override
	public void generate(String outDir, String srcPackage, 
			String outputEncoding, MessageDefinition msgdef) throws Exception {      
		VelocityContext vc = new VelocityContext();
		if (outputEncoding == null) {
			vc.put("charset", "GB2312");
		} else {
			vc.put("charset", outputEncoding);
		}
		vc.put("package", srcPackage);
		vc.put("commands", msgdef.getAllCommands());
		vc.put("createTime", new Date());
		vc.put("utils", Utils.getInstance());
		vc.put("allMessageBeans", msgdef.getAllSortedMessageBeans());

		File outDirFile = new File(outDir);
		outDirFile.mkdirs();
		Template template;

		template = Velocity.getTemplate("html-doc/Message.html.vm");
		Writer writer = getSourceWriter(outDirFile.getAbsolutePath() + File.separatorChar + "Message.html", outputEncoding);
		template.merge(vc, writer);
		writer.close();
		
		template = Velocity.getTemplate("html-doc/Navigation.html.vm");
		writer = getSourceWriter(outDirFile.getAbsolutePath() + File.separatorChar + "Navigation.html", outputEncoding);
		template.merge(vc, writer);
		writer.close();
		
		template = Velocity.getTemplate("html-doc/index.html.vm");
		writer = getSourceWriter(outDirFile.getAbsolutePath() + File.separatorChar + "index.html", outputEncoding);
		template.merge(vc, writer);
		writer.close();

	}

}
