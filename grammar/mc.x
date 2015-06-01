package net.sf.calibur.msgbuf.compiler.parser.mdl; 

import java.io.*;

%%

%byaccj
%class Lexer
%unicode
%16bit
%char
%line
%column
%caseless

%{
	private String errorMessage = null;
	private Parser parser = null;
	private StringBuffer literal = null;

	public Lexer(Reader reader, Parser parser) {
		this(reader);
		this.parser = parser;
		literal = new StringBuffer();
	}

	/**
	 * 获得当前行号
	 * @return 读取文件的当前行号
	 */
	public int getLine() {
		return yyline + 1;
	}

	public int getColumn() {
		return yycolumn + 1;
	}

	public int getCharNumber() {
		return yychar + 1;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	private void startLiteral() {
		literal.setLength(0);
	}

	private void addToLiteral() {
		literal.append(yytext());
	}
%}

%x xb
%x xc
%x xd
%x xh
%x xq

dquote			= \"
xdstart			= {dquote}
xdstop			= {dquote}
xdinside		= [^\"]+
xcstart			= \/\*{op_chars}*
xcstop			= \*+\/
xcinside		= ([^*]+)|(\*+[^/])
digit			= [0-9]
letter			= [_A-Za-z]
letter_or_digit	= [_A-Za-z0-9]
identifier		= {letter}{letter_or_digit}*
suffix			= \.{identifier}
filename		= {identifier}{suffix}+
op_chars		= [\~\!\@\#\^\&\|\`\:\+\-\*\/\%\<\>\=]
integer			= [-+]?{digit}+
space			= [ \t\n\r\f]
non_newline		= [^\n\r]
comment			= ("//"{non_newline}*)
whitespace		= ({space})
other			= .

%%

/* 滤掉空字符 */
{whitespace} {
	/* ignore */
}

{comment} {
	/* 单行注释也当作JavaDoc保存 */
	parser.setLastJavadoc(yytext());
}



/* 处理字符串常量 */
{xdstart}		{
					yybegin(xd);
					startLiteral();
				}
<xd>{xdstop}	{
					yybegin(YYINITIAL);
					parser.yylval = new ParserVal(literal.toString());
					return ParserTokens.SKWD_STRING_CONST;
				}

<xd>{xdinside}	{ addToLiteral(); }

/* 字符串常量定义双引号不匹配 */
<xd><<EOF>>		{ 
					errorMessage = "missing terminating \" character";
					return -1;
				}

/* 处理C语言风格的多行注释 */
{xcstart}		{
					yybegin(xc);
					startLiteral();
					parser.clearLastJavadoc();
					/* Put back any characters past slash-star; see above */
					yypushback(yylength() - 2);
				}

<xc>{xcstop}	{ 
					yybegin(YYINITIAL);
					parser.setLastJavadoc(literal);
				}

<xc>{xcinside}	{ addToLiteral(); }

/* 未关闭的C风格多行注释 */
<xc><<EOF>>		{
			
					errorMessage = "unterminated comment";
					return -1;
				}

":"	|
","	|
"="	|
"."	|
";"	|
"("	|
")"	|
"["	|
"]"	|
"{"	| 
"}"	| 
"<"	| 
">"	{
	return (int) yycharat(0);
}

{integer} {
	parser.yylval = new ParserVal(Integer.parseInt(yytext()));
	return ParserTokens.SKWD_INTEGER_CONST;
}

{filename} {
	parser.yylval = new ParserVal(yytext());
	return ParserTokens.SKWD_FILENAME;
}

{identifier} {
	String identifier = yytext();
	Integer kwd = KeywordSet.getKeyword(identifier);

	if (kwd == null) {
		parser.yylval = new ParserVal(identifier);
		return ParserTokens.SKWD_IDENT;
	} else {
		parser.yylval = new ParserVal(identifier.toLowerCase());
		return kwd.intValue();
	}
}

{other} {
	return -1;
}
