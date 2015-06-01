package cc.devfun.msgbuf.compiler.parser;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Location {
	private File file;
	private int line;
	private int column;

	public Location(File file, int line, int column) {
		super();
		this.file = file;
		this.line = line;
		this.column = column;
	}

	public File getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("文件: " + file.getAbsolutePath());
		pw.println("行号: " + line);
		pw.println("列号: " + column);
		return sw.toString();
	}
	
	public String toSimpleString() {
		return new StringBuilder().append(file.getName()).append(':')
				.append(line).toString();
	}
}
