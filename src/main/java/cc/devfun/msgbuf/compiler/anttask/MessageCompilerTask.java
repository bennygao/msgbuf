
package cc.devfun.msgbuf.compiler.anttask;

import cc.devfun.msgbuf.compiler.MessageCompiler;
import cc.devfun.msgbuf.compiler.Context;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


public class MessageCompilerTask extends Task {
	@Override
	public void execute() throws BuildException {
		checkArgs();
		try {
			context.setSrcdir(destDir);
			context.setPkgname(pkgname);
			context.setFilename(filename);
			context.setOutputEncoding(outputEncoding);
			MessageCompiler.execute();
		} catch (Exception e) {
			throw new BuildException(e.getMessage(), getLocation());
		}

		log("源代码已经成功生成到源代码目录" + srcdir + "的" + pkgname + "包目录下。");
	}

	private String srcdir = null;
	private String pkgname = null;
	private String filename = null;
	private String outdir = null;
	private String outputEncoding = null;
	private String stub = null;
	private String destDir = null;

	private Context context = Context.getInstance();

	private void checkArgs() throws BuildException {
		if (stub == null) {
			throw new BuildException("未指定生成客户端还是服务器端代码，请增加stub参数。",
					getLocation());
		} else if (stub.toLowerCase().equals("j2me")) {
			context.setTarget(Context.J2ME);
		} else if (stub.toLowerCase().equals("j2se")) {
			context.setTarget(Context.J2SE);
		} else if (stub.toLowerCase().equals("cpp")) {
			context.setTarget(Context.CPLUSPLUS);
		} else if (stub.toLowerCase().equals("objc")) {
			context.setTarget(Context.OBJECTIVEC);
		} else if (stub.toLowerCase().equals("document")) {
			context.setTarget(Context.DOCUMENT);
		} else {
			throw new BuildException("错误的stub参数" + stub, getLocation());
		}

		
		if (context.getTarget() != Context.DOCUMENT) {
			if (srcdir == null) {
				throw new BuildException("未指定生成代码的目录，请增加srcdir参数。",
						getLocation());
			} else {
				destDir = srcdir;
			}

			if (pkgname == null) {
				throw new BuildException("未指定生成代码的package，请增加package参数。",
						getLocation());
			}
		} else {
			if (outdir == null) {
				throw new BuildException("未指定生成html文档的目录，请增加outdir参数。", getLocation());
			} else {
				destDir = outdir;
			}
		}

		if (filename == null) {
			throw new BuildException("为指定消息定义文件，请增加mdlfile参数。", getLocation());
		}
	}

	public void setSrcdir(String dir) {
		this.srcdir = dir;
	}

	public void setOutdir(String dir) {
		this.outdir = dir;
	}

	public void setPackage(String pkg) {
		this.pkgname = pkg;
	}

	public void setMdlfile(String file) {
		this.filename = file;
	}

	public void setEncoding(String encoding) {
		this.outputEncoding = encoding;
	}

	public void setStub(String stub) {
		this.stub = stub;
	}
}
