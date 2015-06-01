
%{
import java.io.*;
import java.util.*;

import net.sf.calibur.msgbuf.compiler.Context;
import net.sf.calibur.msgbuf.compiler.ModelBuilder;
import net.sf.calibur.msgbuf.compiler.model.*;
import net.sf.calibur.msgbuf.compiler.parser.*;
%}

%token	<sval>	KWD_IMPORT
				KWD_MESSAGE
				KWD_VECTOR
				KWD_BOOLEAN
				KWD_BYTE
				KWD_SHORT
				KWD_INT
				KWD_LONG
				KWD_FLOAT
				KWD_DOUBLE
				KWD_STRING
				KWD_MAP
				KWD_NULL
				KWD_VOID
				KWD_COMPRESSED
				KWD_EXTENDS
				KWD_COMMAND
				KWD_RESERVED

/* Special keywords not in the query language - see the "lex" file */
%token	<sval>	SKWD_IDENT
%token	<sval>	SKWD_FILENAME
%token	<ival>	SKWD_INTEGER_CONST
%token	<sval>	SKWD_STRING_CONST

%type	<obj>	message_define;
%type	<obj>	imports;
%type	<obj>	import;
%type	<sval>	extends;
%type	<obj>	command_message_map;
%type	<obj>	commands;
%type	<obj>	command;
%type	<obj>	command_arg;
%type	<obj>	message_classes;
%type	<obj>	class;
%type	<obj>	properties;
%type	<obj>	property;
%type	<obj>	simple_type_property;
%type	<obj>	vector_type_property;
%type	<obj>	array_type_property;
%type	<obj>	map_type_property;
%type	<obj>	numeric_type_property;
%type	<obj>	string_type_property;
%type	<obj>	numeric_type;
%type	<obj>	string_type;
%type	<obj>	class_type_property;
%type	<obj>	class_type;
%type	<obj>	basic_type;
%type	<obj>	single_type;

%%
message_define: imports message_classes command_message_map 
	{
	}
	;

imports: import imports
	{
	}
	|
	{
	}
	;

import: KWD_IMPORT SKWD_FILENAME ';'
	{
		builder.importMdl(getImportFile($2));
	}
	;

command_message_map: KWD_COMMAND
	{
		clearLastJavadoc();
	} '{' commands
	{
	}
	|
	{
	}
	;

commands: command commands
	{
	}
	| '}'
	{
	}
	;

command: SKWD_INTEGER_CONST command_arg command_arg ';'
	{
		builder.addCommand($1, $2, $3, getLastJavadoc());
	}
	;

command_arg: SKWD_IDENT
	{
		$$ = new CommandArgument($1, getLocation());
	}
	| SKWD_IDENT '(' KWD_COMPRESSED ')'
	{
		$$ = new CommandArgument($1, true, getLocation());
	}
	| KWD_NULL
	{
		$$ = new CommandArgument(null, getLocation());
	}
	| KWD_VOID
	{
		$$ = new CommandArgument();
	}
	;

message_classes: 
	{
	}
	| class message_classes
	{
	}
	;

class: KWD_MESSAGE SKWD_IDENT extends
	{
		builder.newMessage($2, getLastJavadoc(), $3, getLocation());
	} '{' properties
	{
		clearLastJavadoc();
	}
	;

extends: KWD_EXTENDS SKWD_IDENT
	{
		$$ = $2;
	}
	|
	{
		$$ = null;
	}
	;

properties: property properties
	{
	}
	| '}'
	{
	}
	;

property: simple_type_property
	{
		clearLastJavadoc();
	}
	| vector_type_property
	{
		clearLastJavadoc();
	}
	| array_type_property
	{
		clearLastJavadoc();
	}
	| map_type_property
	{
		clearLastJavadoc();
	}
	;

simple_type_property: numeric_type_property
	| string_type_property
	| class_type_property
	;

vector_type_property: KWD_VECTOR '<' single_type '>' SKWD_IDENT ';'
	{
		builder.addField(new VectorType($3), $5, getLastJavadoc());
	}
	;

array_type_property: single_type '[' ']' SKWD_IDENT ';'
	{
		builder.addField(new ArrayType($1), $4, getLastJavadoc());
	}
	;

map_type_property: KWD_MAP '<' single_type ',' single_type '>' SKWD_IDENT ';'
	{
		builder.addField(new MapType($3, $5), $7, getLastJavadoc());
	}
	;

single_type: class_type
	{
		$$ = $1;
	}
	| basic_type
	{
		$$ = $1;
	}
	;

numeric_type_property: numeric_type SKWD_IDENT ';'
	{
		builder.addField($1, $2, getLastJavadoc());
	}
	| numeric_type SKWD_IDENT '=' SKWD_INTEGER_CONST ';'
	{
		builder.addField($1, $2, getLastJavadoc());
	}
	;

string_type_property: string_type SKWD_IDENT ';'
	{
		builder.addField($1, $2, getLastJavadoc());
	}
	| string_type SKWD_IDENT '=' SKWD_STRING_CONST ';'
	{
		builder.addField($1, $2, getLastJavadoc());
	}
	;

numeric_type: KWD_BYTE
	{
		$$ = SimpleType.getInstance($1);
	}
	| KWD_BOOLEAN
	{
		$$ = SimpleType.getInstance($1);
	}
	| KWD_SHORT
	{
		$$ = SimpleType.getInstance($1);
	}
	| KWD_INT
	{
		$$ = SimpleType.getInstance($1);
	}
	| KWD_LONG
	{
		$$ = SimpleType.getInstance($1);
	}
	| KWD_FLOAT
	{
		$$ = SimpleType.getInstance($1);
	}
	| KWD_DOUBLE
	{
		$$ = SimpleType.getInstance($1);
	}
	;

string_type: KWD_STRING
	{
		$$ = SimpleType.getInstance($1);
	}
	;

class_type_property: class_type SKWD_IDENT ';'
	{
		builder.addField($1, $2, getLastJavadoc());
	}
	;

class_type: SKWD_IDENT
	{
		$$ = MessageBean.getMessageBean($1, getLocation());
	}
	;
	
/***************************************************
complex_type: list_type
	{
		$$ = $1;
	}
	| array_type
	{
		$$ = $1;
	}
	;

list_type: KWD_LIST '<' class_type '>'
	{
		$$ = new SetType($1, (AbstractType) $3);
	}
	;

array_type: basic_type '[' ']'
	{
		$$ = new SetType("Array", (AbstractType) $1);
	}
	;
***************************************************/

basic_type: numeric_type
	{
		$$ = $1;
	}
	| string_type
	{
		$$ = $1;
	}
	;
%%

	private File mdlFile = null;
	private Lexer lexer = null;
	private ModelBuilder builder = null;
	private StringBuffer errorMessage = new StringBuffer();
	private List<String> lastJavadoc = new ArrayList<String>();

	public static void parseMdl(String filePath, ModelBuilder builder) {
		Parser parser = new Parser();
		parser.parse(filePath, builder);
	}

	public void parse(String filePath, ModelBuilder builder) {
		Reader reader = null;
		try {
			this.builder = builder;
			this.mdlFile = new File(filePath);
			String encoding = Context.getInstance().getMdlEncoding();
			if (encoding == null) {
				reader = new FileReader(mdlFile);
			} else {
				reader = new InputStreamReader(new FileInputStream(mdlFile), encoding);
			}
			this.lexer = new Lexer(reader, this);
			if (yyparse() != 0) {
				throw new ParserException(errorMessage.toString());
			}
		} catch (IOException ioe) {
			throw new ParserException("打开消息定义文件" + filePath 
					+ "错误, 请检查文件路径及访问权限。", ioe);
		} catch (ParserException pe) {
			throw pe; 
		} catch (Exception e) {
			throw new ParserException(getLocation().toString() 
					+ "错误: " + e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private int yylex() {
		int yyl_return = -1;
		try {
			yylval = new ParserVal(0);
			yyl_return = lexer.yylex();
		} catch (IOException e) {
			System.err.println("IOException: " + e);
			e.printStackTrace();
		}

		return yyl_return;
	}

	private Location getLocation() {
		return new Location(mdlFile, lexer.getLine(), 
				lexer.getColumn());
	}

	private void yyerror(String error) {
		Location loc = getLocation();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.print(loc.toString());
		pw.println(String.format("错误: 在\"%s\"附近语法错。",
				lexer.yytext()));

		String lexmsg = lexer.getErrorMessage();
		if (lexmsg != null) {
			pw.println(lexmsg);
		}
		
		throw new ParserException(sw.toString());
	}

	private List<String> getLastJavadoc() {
		List<String> javadoc = new ArrayList<String>();
		javadoc.addAll(lastJavadoc);
		lastJavadoc.clear();
		return javadoc;
	}

	public void clearLastJavadoc() {
		lastJavadoc.clear();
	}

	public void setLastJavadoc(String line) {
		line = trimJavadocLine(line);
		if (line.length() > 0) {
			lastJavadoc.add(line);
		}
	}


	public void setLastJavadoc(StringBuffer javadoc) {
		if (javadoc == null) {
			return;
		} 
		
		if (javadoc.length() == 0) {
			return;
		}
		
		/*
		if (javadoc.charAt(0) != '*') {
			return;
		}
		*/
		
		BufferedReader reader = new BufferedReader(new StringReader(javadoc.toString()));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				setLastJavadoc(line);
			}
		} catch (IOException e) {
		}
	}
	
	private String trimJavadocLine(String line) {
		int cb = 0;
		int len = line.length();
		for (cb = 0; cb < len; ++cb) {
			char ch = line.charAt(cb);
			if (ch == ' ' || ch == '\t' || ch == '*' || ch == '/') {
				continue;
			} else {
				break;
			}
		}
		
		return line.substring(cb).trim();
	}

	private String getImportFile(String name) {
		String absPath = mdlFile.getAbsolutePath();
		int posi = absPath.lastIndexOf(File.separatorChar);
		return absPath.substring(0, posi + 1) + name;
	}
