package cc.devfun.msgbuf.compiler.util;

import static java.lang.System.*;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

class Keyword {
	String identifier;
	boolean reserved = false;

	public Keyword(String ident, boolean r) {
		this.identifier = ident;
		this.reserved = r;
	}

	public Keyword(String ident) {
		this(ident, true);
	}
}

public class KeywordGenerator {
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			err.println("Usage: KeywordGenerator <keywords text file> [file encoding]");
			exit(1);
		}

		Reader reader;
		if (args.length >= 2) {
			reader = new InputStreamReader(new FileInputStream(args[0]),
					args[1]);
		} else {
			reader = new InputStreamReader(new FileInputStream(args[0]));
		}

		KeywordGenerator generator = new KeywordGenerator(reader);
		generator.generate();
		exit(0);
	}

	private BufferedReader reader;

	public KeywordGenerator(Reader reader) {
		this.reader = new BufferedReader(reader);
	}

	public void generate() throws Exception {
		String line = null;
		String keywords[] = null;
		boolean reserved = false;
		Map<String, Keyword> keyMap = new LinkedHashMap<String, Keyword>();
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0) { // 空行
				continue;
			} else if (line.startsWith("#")) { // 注释
				continue;
			} else if (line.startsWith("%")) { // 开始保留
				reserved = true;
				continue;
			}

			keywords = line.split("[ |\\t|,|;]+");
			for (String kwd : keywords) {
				String key = kwd.toUpperCase();
				if (!keyMap.containsKey(key)) {
					keyMap.put(key, new Keyword(kwd, reserved));
				}
			}
		}

		out.println("========================================================");
		int cnt = 0;
		out.print("%token\t<sval>\t");
		for (String key : keyMap.keySet()) {
			Keyword kwd = keyMap.get(key);
			if (kwd.reserved) {
				continue;
			}

			if (cnt > 0) {
				out.print("\t\t\t\t");
			}

			out.println("KWD_" + key);
			++cnt;
		}
		out.println("\t\t\t\tKWD_RESERVED");

		out.println("========================================================");
		cnt = 0;
		for (String key : keyMap.keySet()) {
			Keyword kwd = keyMap.get(key);
			out.printf(
					"\t\tkeywords.put(\"%s\", new Integer(ParserTokens.KWD_%s));",
					kwd.identifier.toLowerCase(), kwd.reserved ? "RESERVED"
							: key);
			out.println();
		}

		out.println("========================================================");
		out.println("Total: " + cnt);

		reader.close();
	}

}
