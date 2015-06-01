
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


public class J2seCodeGenerator extends VelocityCodeGenerator implements
		CodeGenerator {
	public J2seCodeGenerator() throws Exception {
		init();
	}

	public void generate(String srcDir, String srcPackage,
			String outputEncoding, MessageDefinition msgdef) throws Exception {
		String factoryName = "ObjectFactory";
		VelocityContext vc = new VelocityContext();
		vc.put("factoryName", factoryName);
		vc.put("package", srcPackage);
		vc.put("msgbufPackage", "net.sf.calibur.msgbuf");
		vc.put("commands", msgdef.getAllCommands());
		vc.put("zipCommands", msgdef.getAllZipCommands());
		vc.put("createTime", new Date());
		vc.put("utils", Utils.getInstance());
		vc.put("allMessageBeans", msgdef.getAllMessageBeans());

		String path = getSourcePath(srcDir, srcPackage);
		StringBuffer pathname = new StringBuffer();
		new File(path).mkdirs();

		Template template;
		Writer writer;
		String fileName = factoryName + ".java";

		template = Velocity.getTemplate("j2se/ObjectFactory.java.vm");
		pathname.append(path).append(File.separatorChar).append(fileName);
		vc.put("fileName", fileName);
		System.out.print("生成 " + pathname.toString() + " ... ");
		writer = getSourceWriter(pathname.toString(), outputEncoding);
		template.merge(vc, writer);
		writer.close();
		System.out.println("完成。");

		template = Velocity.getTemplate("j2se/MessageBean.java.vm");
		for (MessageBean bean : msgdef.getAllMessageBeans()) {
			fileName = bean.getClazzName() + ".java";
			pathname.setLength(0);
			pathname.append(path).append(File.separatorChar).append(fileName);
			System.out.print("生成 " + bean.getName() + " 到 " + pathname.toString() + " ... ");
			if (needGenerate(pathname.toString(), outputEncoding, bean
					.getDigest())) {
				vc.put("messageBean", bean);
				vc.put("fileName", fileName);
				writer = getSourceWriter(pathname.toString(), outputEncoding);
				template.merge(vc, writer);
				writer.close();
				System.out.println("完成。");
			} else {
				System.out.println("签名相同，跳过。");
			}
		}

	}

}
