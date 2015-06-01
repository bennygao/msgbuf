package cc.devfun.msgbuf.compiler;

public class Context {
	static class SingletonHolder {
		static Context singleton = new Context();
	}

	public static Context getInstance() {
		return SingletonHolder.singleton;
	}

	public final static int J2ME = 1;
	public final static int J2SE = 2;
	public final static int CPLUSPLUS = 3;
	public final static int OBJECTIVEC = 4;
	public final static int ANDROID = 5;
	public final static int CSHARP = 6;
	public final static int DOCUMENT = 10;

	private int target = 0;
	private String srcdir = null;
	private String pkgname = null;
	private String filename = null;
	private String outputEncoding = null;
	private String mdlEncoding = null;
	private String prefix = null;
	private boolean override = false;
	private String platform = null;

	private Context() {
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getTarget() {
		return target;
	}

	public String getSrcdir() {
		return srcdir;
	}

	public void setSrcdir(String srcdir) {
		this.srcdir = srcdir;
	}

	public String getPkgname() {
		return pkgname;
	}

	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}

	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}
	
	public void setMdlEncoding(String mdlEncoding) {
		this.mdlEncoding = mdlEncoding;
	}
	
	public String getMdlEncoding() {
		return mdlEncoding;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setOverride(boolean b) {
		this.override = b;
	}
	
	public boolean isOverride() {
		return override;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
