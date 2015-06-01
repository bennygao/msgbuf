package cc.devfun.msgbuf.compiler;

import java.io.UnsupportedEncodingException;

import cc.devfun.msgbuf.compiler.codegeneration.J2meCodeGeneratorFactory;
import cc.devfun.msgbuf.compiler.codegeneration.J2seCodeGeneratorFactory;
import cc.devfun.msgbuf.compiler.codegeneration.AndroidCodeGeneratorFactory;
import cc.devfun.msgbuf.compiler.codegeneration.CPlusPlusCodeGeneratorFactory;
import cc.devfun.msgbuf.compiler.codegeneration.CSharpCodeGeneratorFactory;
import cc.devfun.msgbuf.compiler.codegeneration.CodeGenerator;
import cc.devfun.msgbuf.compiler.codegeneration.CodeGeneratorFactory;
import cc.devfun.msgbuf.compiler.codegeneration.HtmlDocumentGeneratorFactory;
import cc.devfun.msgbuf.compiler.codegeneration.ObjectiveCCodeGeneratorFactory;
import cc.devfun.msgbuf.compiler.parser.ParserException;
import cc.devfun.msgbuf.compiler.parser.mdl.Parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class MessageCompiler {

	public MessageCompiler() {
	}

	public void compile(String filename, ModelBuilder builder) throws Exception {
		Parser.parseMdl(filename, builder);
	}

	private static void usage(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("MessageCompiler <options> <message-define-file>",
				options);
		System.out.println();

		System.out.println("生成HTML文档");
		System.out
				.println("MessageCompiler -doc -override -mdlencoding <Encoding> -encoding <Encoding> -outdir <Dir> <message-define-file>");
		System.out.println(" -override 强制没有修改过的message也重新生成代码。");
		System.out
				.println(" -mdlencoding <Encoding> 指定消息定义文件的字符编码，例如UTF8、gb2312。");
		System.out
				.println(" -encoding <Encoding> 指定生成HTML的字符编码，例如UTF8、gb2312。");
		System.out.println(" -srcdir <Dir> 指定存放生成HTML的路径。");
		System.out.println(" <message-define-file> 消息定义文件。");
		System.out.println();

		System.out.println("生成Java2 SE代码");
		System.out
				.println("MessageCompiler -j2se -override -mdlencoding <Encoding> -encoding <Encoding> -srcdir <Dir> -package <Package> <message-define-file>");
		System.out.println(" -override 强制没有修改过的message也重新生成代码。");
		System.out
				.println(" -mdlencoding <Encoding> 指定消息定义文件的字符编码，例如UTF8、gb2312。");
		System.out.println(" -encoding <Encoding> 指定生成源代码的字符编码，例如UTF8、gb2312。");
		System.out.println(" -srcdir <Dir> 指定存放生成源代码的路径。");
		System.out.println(" -package <Package> 指定生成源代码class的package。");
		System.out.println(" <message-define-file> 消息定义文件。");
		System.out.println();

		System.out.println("生成C#代码");
		System.out
				.println("MessageCompiler -csharp -override -mdlencoding <Encoding> -encoding <Encoding> -srcdir <Dir> -package <Namespace> <message-define-file>");
		System.out.println(" -override 强制没有修改过的message也重新生成代码。");
		System.out
				.println(" -mdlencoding <Encoding> 指定消息定义文件的字符编码，例如UTF8、gb2312。");
		System.out.println(" -encoding <Encoding> 指定生成源代码的字符编码，例如UTF8、gb2312。");
		System.out.println(" -srcdir <Dir> 指定存放生成源代码的路径。");
		System.out.println(" -package <Namespace> 指定生成源代码class的namespace。");
		System.out.println(" <message-define-file> 消息定义文件。");
		System.out.println();

		System.out.println("生成C++代码");
		System.out
				.println("MessageCompiler -cpp -override -mdlencoding <Encoding> -encoding <Encoding> -srcdir <Dir> -package <Namespace> <message-define-file>");
		System.out.println(" -override 强制没有修改过的message也重新生成代码。");
		System.out
				.println(" -mdlencoding <Encoding> 指定消息定义文件的字符编码，例如UTF8、gb2312。");
		System.out.println(" -encoding <Encoding> 指定生成源代码的字符编码，例如UTF8、gb2312。");
		System.out.println(" -srcdir <Dir> 指定存放生成源代码的路径。");
		System.out.println(" -package <Namespace> 指定生成源代码class的namespace。");
		System.out.println(" <message-define-file> 消息定义文件。");
		System.out.println();

		// System.out.println("生成J2ME代码");
		// System.out.println("MessageCompiler -j2me -encoding <Encoding> -srcdir <Dir> -package <Package> <message-define-file>");
		// System.out.println(" -encoding <Encoding> 指定生成源代码的字符编码，例如UTF8、gb2312。");
		// System.out.println(" -srcdir <Dir> 指定存放生成源代码的路径。");
		// System.out.println(" -package <Package> 指定生成源代码class的package。");
		// System.out.println(" <message-define-file> 消息定义文件。");
		// System.out.println();
		//
		// System.out.println("生成Objective-C代码");
		// System.out.println("MessageCompiler -objc -encoding <Encoding> -srcdir <Dir> <message-define-file>");
		// System.out.println(" -encoding <Encoding> 指定生成源代码的字符编码，例如UTF8、gb2312。");
		// System.out.println(" -srcdir <Dir> 指定存放生成源代码的路径。");
		// System.out.println(" <message-define-file> 消息定义文件。");
		// System.out.println();

	}

	public static void execute() throws Exception {
		Context context = Context.getInstance();
		ModelBuilder builder = new ModelBuilder();
		MessageCompiler compiler = new MessageCompiler();
		compiler.compile(context.getFilename(), builder);

		try {
			MessageDefinition msgdef = builder.getMessageDefinition();
			msgdef.integralityCheck();
			CodeGeneratorFactory factory = null;
			switch (context.getTarget()) {
			case Context.J2ME:
				factory = new J2meCodeGeneratorFactory();
				break;

			case Context.J2SE:
				factory = new J2seCodeGeneratorFactory();
				break;

			case Context.CPLUSPLUS:
				factory = new CPlusPlusCodeGeneratorFactory();
				break;

			case Context.OBJECTIVEC:
				factory = new ObjectiveCCodeGeneratorFactory();
				break;

			case Context.ANDROID:
				factory = new AndroidCodeGeneratorFactory();
				break;

			case Context.DOCUMENT:
				factory = new HtmlDocumentGeneratorFactory();
				break;

			case Context.CSHARP:
				factory = new CSharpCodeGeneratorFactory();
				break;

			default:
				throw new RuntimeException("为定义的代码生成语言类型id "
						+ context.getTarget());
			}

			CodeGenerator generator = factory.createCodeGenerator();
			generator.generate(context.getSrcdir(), context.getPkgname(),
					context.getOutputEncoding(), msgdef);
		} catch (UnsupportedEncodingException uee) {
			System.err.println("错误：不支持的字符编码 " + uee.getMessage());
			System.exit(-1);
		} catch (ParserException pe) {
			System.err.println(pe.getMessage());
			System.exit(-1);
		}
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Context context = Context.getInstance();
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options options = new Options();
		options.addOption(OptionBuilder.withDescription("生成html格式文档。").create(
				"doc"));
		// options.addOption(OptionBuilder.withDescription("生成J2ME客户端代码。").create(
		// "j2me"));
		options.addOption(OptionBuilder.withDescription("生成J2SE服务器端代码。")
				.create("j2se"));
		options.addOption(OptionBuilder.withDescription("生成C++代码。").create(
				"cpp"));
		// options.addOption(OptionBuilder.withDescription("生成Objective-C端代码。")
		// .create("objc"));
		// options.addOption(OptionBuilder.withDescription("生成Android端代码。")
		// .create("android"));
		options.addOption(OptionBuilder.withDescription("生成C#代码。").create(
				"csharp"));
		options.addOption(OptionBuilder.withDescription("覆盖已经存在的结果源代码。")
				.create("override"));
		options.addOption(OptionBuilder.withArgName("OutputDirectory").hasArg()
				.withDescription("存放html格式文档的目录。").create("outdir"));
		options.addOption(OptionBuilder.withArgName("Encoding").hasArg()
				.withDescription("生成源文件字符编码。").create("encoding"));
		options.addOption(OptionBuilder.withArgName("Encoding").hasArg()
				.withDescription("MDL文件字符编码。").create("mdlencoding"));
		options.addOption(OptionBuilder.withArgName("Dir").hasArg()
				.withDescription("源代码目录。").create("srcdir"));
		options.addOption(OptionBuilder.withArgName("Package").hasArg()
				.withDescription("生成代码的package。").create("package"));
		options.addOption(OptionBuilder.withArgName("Prefix").hasArg()
				.withDescription("生成消息实体类名的前缀。").create("prefix"));
		// options.addOption(OptionBuilder.withArgName("ClassName").hasArg()
		// .withDescription("指令编号与消息实体的对应关系类名。").create("mapclass"));
		options.addOption("h", "help", false, "打印帮助信息。");

		boolean isJ2me = false;
		boolean isJ2se = false;
		boolean isCPlusPlus = false;
		boolean isObjectiveC = false;
		boolean isAndroid = false;
		boolean isCSharp = false;
		boolean isDoc = false;
		int actionNum = 0;

		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				usage(options);
				System.exit(0);
			}

			if (line.hasOption("j2me")) {
				isJ2me = true;
				++actionNum;
			}

			if (line.hasOption("j2se")) {
				isJ2se = true;
				++actionNum;
			}

			if (line.hasOption("cpp")) {
				isCPlusPlus = true;
				++actionNum;
			}

			if (line.hasOption("objc")) {
				isObjectiveC = true;
				++actionNum;
			}

			if (line.hasOption("android")) {
				isAndroid = true;
				++actionNum;
			}

			if (line.hasOption("csharp")) {
				isCSharp = true;
				++actionNum;
			}

			if (line.hasOption("doc")) {
				isDoc = true;
				++actionNum;
			}

			if (line.hasOption("override")) {
				Context.getInstance().setOverride(true);
			}

			if (actionNum > 1) {
				System.err
						.println("错误：-j2me -j2se -cpp -objc -csharp -android -doc 选项中只能指定一个。");
				usage(options);
				System.exit(1);
			}

			if (line.hasOption("encoding")) {
				context.setOutputEncoding(line.getOptionValue("encoding"));
			}

			if (line.hasOption("mdlencoding")) {
				context.setMdlEncoding(line.getOptionValue("mdlencoding"));
			}

			if (line.hasOption("prefix")) {
				context.setPrefix(line.getOptionValue("prefix"));
			}

			if (!isDoc) {
				if (!line.hasOption("srcdir")) {
					System.err.println("错误：没有指定生成源代码存放的目录。-srcdir");
					usage(options);
					System.exit(1);
				} else {
					context.setSrcdir(line.getOptionValue("srcdir"));
				}

				if (line.hasOption("package")) {
					context.setPkgname(line.getOptionValue("package"));
				}

				if (isJ2me || isJ2se || isAndroid) {
					if (context.getPkgname() == null) {
						System.err.println("错误：没有指定生成代码的package。-package");
						usage(options);
						System.exit(1);
					}
				}

				if (isCSharp) {
					if (context.getPkgname() == null) {
						System.err.println("错误：没有指定C#的namespace，请用-package指定。");
						usage(options);
						System.exit(1);
					}
				}
			} else {
				if (!line.hasOption("outdir")) {
					System.err.println("错误：没有指定输出html文档的目录。");
					usage(options);
					System.exit(1);
				} else {
					context.setSrcdir(line.getOptionValue("outdir"));
				}
			}

			String[] otherArgs = line.getArgs();
			if (otherArgs.length == 0) {
				System.err.println("错误：没有指定消息定义文件路径名。");
				usage(options);
				System.exit(1);
			} else {
				context.setFilename(otherArgs[0]);
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			usage(options);
			System.exit(1);
		}

		try {
			if (isJ2me) {
				context.setTarget(Context.J2ME);
			} else if (isJ2se) {
				context.setTarget(Context.J2SE);
			} else if (isCPlusPlus) {
				context.setTarget(Context.CPLUSPLUS);
			} else if (isObjectiveC) {
				context.setTarget(Context.OBJECTIVEC);
			} else if (isAndroid) {
				context.setTarget(Context.ANDROID);
			} else if (isDoc) {
				context.setTarget(Context.DOCUMENT);
			} else if (isCSharp) {
				context.setTarget(Context.CSHARP);
			}

			execute();
		} catch (ParserException pe) {
			System.err.println(pe.getMessage());
			System.exit(2);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(3);
		}

		if (!isDoc) {
			System.out.println("源代码已经成功生成到 " + context.getSrcdir()
					+ " 中相应的目录下。");
		} else {
			System.out
					.println("HTML文件已经成功生成到 " + context.getSrcdir() + " 目录下。");
		}
	}
}
