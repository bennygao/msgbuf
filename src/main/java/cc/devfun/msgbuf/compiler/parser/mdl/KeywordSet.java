
package cc.devfun.msgbuf.compiler.parser.mdl;

import java.util.HashMap;
import java.util.Map;

public class KeywordSet {
	public static Map<String, Integer> keywords = new HashMap<String, Integer>();
	static {
		keywords.put("import", new Integer(ParserTokens.KWD_IMPORT));
		keywords.put("message", new Integer(ParserTokens.KWD_MESSAGE));
		keywords.put("vector", new Integer(ParserTokens.KWD_VECTOR));
		keywords.put("map", new Integer(ParserTokens.KWD_MAP));
		keywords.put("boolean", new Integer(ParserTokens.KWD_BOOLEAN));
		keywords.put("bool", new Integer(ParserTokens.KWD_BOOLEAN));
		keywords.put("byte", new Integer(ParserTokens.KWD_BYTE));
		keywords.put("short", new Integer(ParserTokens.KWD_SHORT));
		keywords.put("int16", new Integer(ParserTokens.KWD_SHORT));
		keywords.put("int", new Integer(ParserTokens.KWD_INT));
		keywords.put("int32", new Integer(ParserTokens.KWD_INT));
		keywords.put("long", new Integer(ParserTokens.KWD_LONG));
		keywords.put("int64", new Integer(ParserTokens.KWD_LONG));
		keywords.put("float", new Integer(ParserTokens.KWD_FLOAT));
		keywords.put("double", new Integer(ParserTokens.KWD_DOUBLE));
		keywords.put("string", new Integer(ParserTokens.KWD_STRING));
		keywords.put("null", new Integer(ParserTokens.KWD_NULL));
		keywords.put("void", new Integer(ParserTokens.KWD_VOID));
		keywords.put("compressed", new Integer(ParserTokens.KWD_COMPRESSED));
		keywords.put("extends", new Integer(ParserTokens.KWD_EXTENDS));
		keywords.put("command", new Integer(ParserTokens.KWD_COMMAND));
		
		keywords.put("abstract", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("as", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("base", new Integer(ParserTokens.KWD_RESERVED));
		
		keywords.put("break", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("case", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("catch", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("char", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("checked", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("class", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("const", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("continue", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("decimal", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("default", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("delegate", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("do", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("else", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("enum", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("event", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("explicit", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("extern", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("false", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("finally", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("fixed", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("for", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("foreach", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("goto", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("if", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("implicit", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("in", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("interface", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("internal", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("is", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("lock", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("namespace", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("new", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("object", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("operator", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("out", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("override", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("params", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("private", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("protected", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("public", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("readonly", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("ref", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("return", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("sbyte", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("sealed", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("sizeof", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("stackalloc", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("static", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("struct", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("switch", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("this", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("throw", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("true", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("try", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("typeof", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("uint", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("ulong", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("unchecked", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("unsafe", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("ushort", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("using", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("virtual", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("volatile", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("while", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("add", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("alias", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("ascending", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("async", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("await", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("descending", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("dynamic", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("from", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("get", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("global", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("group", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("into", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("join", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("let", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("orderby", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("partial", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("remove", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("select", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("set", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("value", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("var", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("where", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("yield", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("alignas", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("alignof", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("and", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("and_eq", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("asm", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("auto", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("bitand", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("bitor", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("char16_t", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("char32_t", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("compl", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("constexpr", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("const_cast", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("decltype", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("delete", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("dynamic_cast", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("export", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("friend", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("inline", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("mutable", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("noexcept", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("not", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("not_eq", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("nullptr", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("or", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("or_eq", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("register", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("reinterpret_cast", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("signed", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("static_assert", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("static_cast", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("template", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("thread_local", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("typedef", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("typeid", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("typename", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("union", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("unsigned", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("wchar_t", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("xor", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("xor_eq", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("bycopy", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("byref", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("id", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("imp", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("inout", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("nil", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("no", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("oneway", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("protocol", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("sel", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("self", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("super", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("yes", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("end", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("restrict", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("implementation", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("property", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("synthesize", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("selector", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("_bool", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("atomic", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("_complex", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("nonatomic", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("_imaginery", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("retain", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("alloc", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("autorelease", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("release", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("assert", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("package", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("synchronized", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("implements", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("throws", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("instanceof", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("transient", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("final", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("strictfp", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("native", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("del", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("elif", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("with", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("pass", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("except", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("print", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("exec", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("raise", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("def", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("lambda", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__halt_compiler", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("array", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("callable", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("clone", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("declare", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("die", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("echo", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("elseif", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("empty", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("enddeclare", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("endfor", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("endforeach", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("endif", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("endswitch", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("endwhile", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("eval", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("exit", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("function", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("include", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("include_once", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("insteadof", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("isset", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("list", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("require", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("require_once", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("trait", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("unset", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("use", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__class__", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__dir__", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__file__", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__function__", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__line__", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__method__", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__namespace__", new Integer(ParserTokens.KWD_RESERVED));
		keywords.put("__trait__", new Integer(ParserTokens.KWD_RESERVED));

	}

	public static Integer getKeyword(String key) {
		return keywords.get(key.toLowerCase());
	}
}
