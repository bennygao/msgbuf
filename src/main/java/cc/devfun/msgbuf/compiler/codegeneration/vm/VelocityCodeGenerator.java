
package cc.devfun.msgbuf.compiler.codegeneration.vm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import cc.devfun.msgbuf.compiler.Context;
import cc.devfun.msgbuf.compiler.filter.LineEndFilterWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;


public class VelocityCodeGenerator {
	private final static String DIGEST_IDENTIFIER = "@DIGEST";

	public void init() throws Exception {
		Log log = LogFactory.getLog(this.getClass());
		Properties props = new Properties();
		// 模板文件是UTF-8编码
		props.setProperty("input.encoding", "UTF8");

		props.setProperty("resource.loader", "class");
		props.setProperty("class.resource.loader.description",
				"Velocity Classpath Resource Loader");
		props.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		if (log.isDebugEnabled()) {
			props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
					"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		} else {
			props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
					"org.apache.velocity.runtime.log.NullLogSystem");
		}
		Velocity.init(props);
	}

	protected String getSourcePath(String path, String pkg) {
		if (pkg == null) {
			return path;
		} else {
			return new StringBuffer(path).append(File.separatorChar)
					.append(pkg.replace('.', File.separatorChar)).toString();

		}
	}

	protected Writer getSourceWriter(String pathname, String encoding)
			throws Exception {
		Writer writer;
		if (encoding != null) {
			writer = new OutputStreamWriter(new FileOutputStream(pathname),
					encoding);
		} else {
			writer = new FileWriter(pathname);
		}
		
		return new LineEndFilterWriter(writer);
	}

	protected boolean needGenerate(String pathname, String encoding,
			String digest) {
		if (Context.getInstance().isOverride()) { // 命令行指定强制覆盖
			return true;
		}
		
		File f = new File(pathname);
		if (!f.exists()) { // 目标文件还不存在
			return true;
		}

		BufferedReader reader = null;
		try {
			InputStream is = new FileInputStream(f);
			reader = new BufferedReader(new InputStreamReader(is, encoding));
			String line;
			int idx;
			while ((line = reader.readLine()) != null) {
				idx = line.indexOf(DIGEST_IDENTIFIER);
				if (idx >= 0) {
					String oldDigest = line.substring(
							idx + DIGEST_IDENTIFIER.length()).trim();
					// System.out.println(pathname);
					// System.out.println("oldDigest:[" + oldDigest + "]");
					// System.out.println("newDigest:[" + digest + "]");
					return !oldDigest.equals(digest);
				}
			}

			return true;
		} catch (Exception e) {
			return true;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
