package cc.devfun.msgbuf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 常用汉字简繁体转换。
 * 
 * @author gaobo
 * 
 */
public class TSChineseConvertor {
	static class SingletonHolder {
		static TSChineseConvertor singleton = new TSChineseConvertor();
	}

	public static TSChineseConvertor getInstance() {
		return SingletonHolder.singleton;
	}

	private Map<Character, Character> traditionalChineseIndex;
	private Map<Character, Character> simplifiedChineseIndex;

	private TSChineseConvertor() {
		traditionalChineseIndex = new HashMap<Character, Character>();
		simplifiedChineseIndex = new HashMap<Character, Character>();

		ClassLoader cl = getClass().getClassLoader();
		InputStream sin = cl
				.getResourceAsStream("com/snda/mobi/foundation/messagebuffer/mcsc.txt");
		if (sin == null) {
			throw new RuntimeException("未找到简体中文常用字文件");
		}

		InputStream tin = cl
				.getResourceAsStream("com/snda/mobi/foundation/messagebuffer/mctc.txt");
		if (tin == null) {
			throw new RuntimeException("未找到繁体中文常用字文件");
		}

		try {
			BufferedReader sReader = new BufferedReader(new InputStreamReader(sin, "utf8"));
			BufferedReader tReader = new BufferedReader(new InputStreamReader(tin, "utf8"));

			String ss = sReader.readLine();
			String ts = tReader.readLine();

			int len = ss.length();
			for (int i = 0; i < len; ++i) {
				Character sc = ss.charAt(i);
				Character tc = ts.charAt(i);
				simplifiedChineseIndex.put(sc, tc);
				traditionalChineseIndex.put(tc, sc);
			}
			
			sin.close();
			tin.close();
		} catch (Exception e) {
			throw new RuntimeException("读入简繁体常用字文件错误。", e);
		}
	}
	
	public String s2t(String str) {
		StringBuilder sb = new StringBuilder();
		int len = str.length();
		for (int i = 0; i < len; ++i) {
			Character sc = str.charAt(i);
			Character tc = simplifiedChineseIndex.get(sc);
			sb.append(tc != null ? tc : sc);
		}
		
		return sb.toString();
	}
	
	public String t2s(String str) {
		StringBuilder sb = new StringBuilder();
		int len = str.length();
		for (int i = 0; i < len; ++i) {
			Character tc = str.charAt(i);
			Character sc = traditionalChineseIndex.get(tc);
			sb.append(sc != null ? sc : tc);
		}
		
		return sb.toString();
	}


	public static void main(String[] args) {
		long l = System.currentTimeMillis();
		TSChineseConvertor cc = TSChineseConvertor.getInstance();
		System.out.println(System.currentTimeMillis() - l);
		System.out.println("[" + cc.s2t("a中1国2人adsf") + "]");
		System.out.println("[" + cc.t2s("鍾過") + "]");
	}

}
