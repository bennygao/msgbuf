package cc.devfun.msgbuf.compiler.codegeneration.vm;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cc.devfun.msgbuf.compiler.codegeneration.Utils;
import cc.devfun.msgbuf.compiler.MessageDefinition;
import cc.devfun.msgbuf.compiler.codegeneration.CodeGenerator;
import cc.devfun.msgbuf.compiler.model.MessageBean;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public class CPlusPlusCodeGenerator extends VelocityCodeGenerator implements
		CodeGenerator {

	public CPlusPlusCodeGenerator() throws Exception {
		init();
	}
	
	private Collection<MessageBean> getAllMessageBeans(MessageDefinition msgdef) {
		List<MessageBean> list = new ArrayList<MessageBean>();
		list.addAll(msgdef.getAllMessageBeans());
		Collections.sort(list, new Comparator<MessageBean>() {
			@Override
			public int compare(MessageBean o1, MessageBean o2) {
				int ld = o1.getDerivedLevel() - o2.getDerivedLevel();
				return ld != 0 ? ld : o1.getName().compareTo(o2.getName());
			}
		});
		return list;
	}
	
	@Override
	public void generate(String srcDir, String srcPackage, 
			String outputEncoding, MessageDefinition msgdef) throws Exception {
		VelocityContext vc = new VelocityContext();
		vc.put("package", srcPackage);
		vc.put("commands", msgdef.getAllCommands());
		vc.put("createTime", new Date());
		vc.put("utils", Utils.getInstance());
		vc.put("allMessageBeans", getAllMessageBeans(msgdef));
		vc.put("factoryName", "ObjectFactory");

		String path = getSourcePath(srcDir, null);
		new File(path).mkdirs();

		Template template;
		Writer writer;

		// Generate C++ header file.
		String fileName = srcPackage + ".h";
		vc.put("fileName", fileName);
		String header = path + File.separatorChar + fileName;
		System.out.println("生成头文件 " + header + " ... ");
		template = Velocity.getTemplate("cpp/header.h.vm");
		writer = getSourceWriter(header, outputEncoding);
		template.merge(vc, writer);
		writer.close();
		
		// Generate C++ classes source code.
		fileName = srcPackage + ".cpp";
		vc.put("fileName", fileName);
		String source = path + File.separatorChar + fileName;
		System.out.println("生成类实现文件" + source + " ... ");
		template = Velocity.getTemplate("cpp/mbean.cpp.vm");
		writer = getSourceWriter(source, outputEncoding);
		template.merge(vc, writer);
		writer.close();
	}
}
