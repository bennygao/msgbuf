package cc.devfun.msgbuf.compiler.codegeneration.vm;

import java.io.File;
import java.io.Writer;
import java.util.Date;

import cc.devfun.msgbuf.compiler.Context;
import cc.devfun.msgbuf.compiler.codegeneration.CodeGenerator;
import cc.devfun.msgbuf.compiler.codegeneration.Utils;
import cc.devfun.msgbuf.compiler.model.MessageBean;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import cc.devfun.msgbuf.compiler.MessageDefinition;

public class CSharpCodeGenerator extends VelocityCodeGenerator implements
		CodeGenerator {
	private final static String DEFAULT_PLATFORM = "unity";

	public CSharpCodeGenerator() throws Exception {
		super.init();
	}

	private File getTargetDir(String srcDir, String srcPackage) {
		File baseDir = new File(srcDir);
		File targetDir = new File(baseDir.getAbsolutePath()
				+ File.separatorChar + srcPackage);
		return targetDir;
	}

	@Override
	public void generate(String srcDir, String srcPackage,
			String outputEncoding, MessageDefinition msgdef) throws Exception {
		String factoryName = "ObjectFactory";
		VelocityContext vc = new VelocityContext();
		vc.put("package", srcPackage);
		vc.put("commands", msgdef.getAllCommands());
		vc.put("zipCommands", msgdef.getAllZipCommands());
		vc.put("createTime", new Date());
		vc.put("utils", Utils.getInstance());
		vc.put("allMessageBeans", msgdef.getAllMessageBeans());
		vc.put("factoryName", factoryName);

		File outDir = getTargetDir(srcDir, srcPackage);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		StringBuffer pathname = new StringBuffer();
		String platform = Context.getInstance().getPlatform();
		if (platform == null) {
			platform = DEFAULT_PLATFORM;
		}

		Template template;
		Writer writer;

		// 生成MessageBeanFactory.cs
		template = Velocity.getTemplate("csharp/ObjectFactory.cs.vm");
		pathname.setLength(0);
		pathname.append(outDir.getAbsolutePath()).append(File.separatorChar)
				.append(factoryName).append(".cs");
		vc.put("fileName", factoryName + ".cs");
		System.out.print("生成 " + pathname.toString() + " ... ");
		writer = getSourceWriter(pathname.toString(), outputEncoding);
		template.merge(vc, writer);
		writer.close();
		System.out.println("完成。");

		// 生成所有MDL中定义的message bean, 每个message bean一个cs文件。
		template = Velocity.getTemplate("csharp/GivenBean.cs.vm");
		String baseName;
		for (MessageBean bean : msgdef.getAllMessageBeans()) {
			pathname.setLength(0);
			baseName = bean.getClazzName() + ".cs";
			pathname.append(outDir.getAbsolutePath())
					.append(File.separatorChar).append(baseName);
			vc.put("fileName", baseName);
			System.out.print("生成 " + bean.getName() + " 到 "
					+ pathname.toString() + " ... ");
			if (needGenerate(pathname.toString(), outputEncoding,
					bean.getDigest())) {
				vc.put("messageBean", bean);
				writer = getSourceWriter(pathname.toString(), outputEncoding);
				template.merge(vc, writer);
				writer.close();
				System.out.println("完成。");
			} else {
				System.out.println("签名相同，略过。");
			}
		}
	}
}
