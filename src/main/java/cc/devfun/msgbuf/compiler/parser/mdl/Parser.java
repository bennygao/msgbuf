//### This file created by BYACC 1.8(/Java extension  1.1)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//### Please send bug reports to rjamison@lincom-asg.com
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package cc.devfun.msgbuf.compiler.parser.mdl;



//#line 3 "mc.y"
import java.io.*;
import java.util.*;

import cc.devfun.msgbuf.compiler.model.*;
import cc.devfun.msgbuf.compiler.Context;
import cc.devfun.msgbuf.compiler.ModelBuilder;
import cc.devfun.msgbuf.compiler.parser.Location;
import cc.devfun.msgbuf.compiler.parser.ParserException;
//#line 23 "Parser.java"




/**
 * Encapsulates yacc() parser functionality in a Java
 *        class for quick code development
 */
public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[],stateptr;           //state stack
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
void state_push(int state)
{
  if (stateptr>=YYSTACKSIZE)         //overflowed?
    return;
  statestk[++stateptr]=state;
  if (stateptr>statemax)
    {
    statemax=state;
    stateptrmax=stateptr;
    }
}
int state_pop()
{
  if (stateptr<0)                    //underflowed?
    return -1;
  return statestk[stateptr--];
}
void state_drop(int cnt)
{
int ptr;
  ptr=stateptr-cnt;
  if (ptr<0)
    return;
  stateptr = ptr;
}
int state_peek(int relative)
{
int ptr;
  ptr=stateptr-relative;
  if (ptr<0)
    return -1;
  return statestk[ptr];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
boolean init_stacks()
{
  statestk = new int[YYSTACKSIZE];
  stateptr = -1;
  statemax = -1;
  stateptrmax = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal(0);
  yylval=new ParserVal(0);
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal(-1);
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal(-1);
  return valstk[ptr];
}
//#### end semantic value section ####
public final static short YYERRCODE = 256;
static short[] yylhs = null;
class YYLHS0 {
  final short yylhs[] = {
     -1,     0,     1,     1,     2,    24,     4,     4,     5,     5, 
      6,     7,     7,     7,     7,     8,     8,    25,     9,     3, 
      3,    10,    10,    11,    11,    11,    11,    12,    12,    12, 
     13,    14,    15,    23,    23,    16,    16,    17,    17,    18, 
     18,    18,    18,    18,    18,    18,    19,    20,    21,    22, 
     22, 
  };
}

static short[] yylen = null;
class YYLEN0 {
  final short yylen[] = {
      2,     3,     2,     0,     3,     0,     4,     0,     2,     1, 
      4,     1,     4,     1,     1,     0,     2,     0,     6,     2, 
      0,     2,     1,     1,     1,     1,     1,     1,     1,     1, 
      6,     5,     8,     1,     1,     3,     5,     3,     5,     1, 
      1,     1,     1,     1,     1,     1,     1,     3,     1,     1, 
      1, 
  };
}

static short[] yydefred = null;
class YYDEFRED0 {
  final short yydefred[] = {
      0,     0,     0,     0,     0,     0,     0,     0,     0,     2, 
      4,     0,     5,     1,    16,     0,    17,     0,    19,     0, 
      0,     0,     0,     9,     6,     0,     0,    40,    39,    41, 
     42,    43,    44,    45,    46,     0,    48,    22,    18,     0, 
     23,    24,    25,    26,    27,    28,     0,     0,    29,     0, 
     34,     0,    13,    14,     0,     0,     8,     0,     0,    21, 
      0,     0,     0,     0,     0,     0,    49,    50,    33,     0, 
      0,    35,     0,    37,     0,    47,     0,     0,    10,     0, 
      0,     0,     0,     0,    12,     0,     0,    36,    38,    31, 
     30,     0,     0,    32, 
  };
}

final static short yydgoto[] = {                          2,
    3,    4,   16,   13,   24,   25,   55,    7,    8,   38,
   39,   40,   41,   42,   43,   44,   45,   66,   67,   48,
   68,   50,   51,   17,   19,
};
static short[] yysindex = null;
class YYSINDEX0 {
  final short yysindex[] = {
   -235,  -253,     0,  -234,  -235,   -27,  -242,  -238,  -234,     0, 
      0,  -233,     0,     0,     0,  -241,     0,   -86,     0,   -85, 
   -122,  -121,  -239,     0,     0,  -122,   -20,     0,     0,     0, 
      0,     0,     0,     0,     0,   -19,     0,     0,     0,  -121, 
      0,     0,     0,     0,     0,     0,  -232,  -231,     0,  -230, 
      0,   -45,     0,     0,     7,  -239,     0,  -246,  -246,     0, 
    -49,   -48,   -17,   -44,  -223,    -9,     0,     0,     0,   -11, 
      8,     0,  -224,     0,  -222,     0,  -221,    14,     0,  -218, 
   -246,    -1,     3,     4,     0,     5,    -3,     0,     0,     0, 
      0,  -215,     6,     0, 
  };
}

static short[] yyrindex = null;class YYRINDEX0 {
  final short yyrindex[] = {
      1,     0,     0,     2,     1,     0,     0,    61,     2,     0, 
      0,   -86,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,   -25,   -24,     0,   -23, 
      0,     0,     0,     0,   -59,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0, 
  };
}

static short[] yygindex = null;
class YYGINDEX0 {
  final short yygindex[] = {
      0,    65,     0,     0,     0,    45,     0,    16,    64,     0, 
     34,     0,     0,     0,     0,     0,     0,     0,   -14,   -13, 
      0,   -12,     0,   -52,     0,     0, 
  };
}

final static int YYTABLESIZE=275;
static short[] yytable = null;
class YYTABLE0 {
  final short yytable[] = {
     11,     3,    15,    23,    37,    69,    70,    46,    47,    49, 
     71,    73,    72,    74,    27,    28,    29,    30,    31,    32, 
     33,    34,     1,     5,     6,    46,    47,    49,    86,    36, 
     52,    53,    10,    11,    18,    12,    54,    20,    21,    15, 
     57,    58,    75,    60,    61,    62,    63,    64,    77,    76, 
     78,    79,    80,    81,    83,    84,    82,    85,    87,    91, 
     92,     7,    88,    89,    90,    93,    49,    50,    33,     9, 
     56,    65,    14,    59,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,    26,    27, 
     28,    29,    30,    31,    32,    33,    34,    35,     0,     0, 
      0,     0,     0,     0,    36,    22,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
     11,    11,     0,     0,     0,     0,    11,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     3, 
      0,     0,     0,     0,     0,     0,     0,     0,     0,     0, 
      0,     0,     0,     0,     3,    15, 
  };
}

static short[] yycheck = null;
class YYCHECK0 {
  final short yycheck[] = {
     59,     0,     0,   125,   125,    57,    58,    21,    21,    21, 
     59,    59,    61,    61,   260,   261,   262,   263,   264,   265, 
    266,   267,   257,   276,   258,    39,    39,    39,    80,   275, 
    269,   270,    59,   275,   275,   273,   275,   123,   123,   272, 
     60,    60,    59,   275,   275,   275,    91,    40,   271,    93, 
     59,    62,    44,   277,   275,    41,   278,   275,    59,    62, 
    275,     0,    59,    59,    59,    59,    91,    91,    91,     4, 
     25,    55,     8,    39,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   259,   260, 
    261,   262,   263,   264,   265,   266,   267,   268,    -1,    -1, 
     -1,    -1,    -1,    -1,   275,   277,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
    269,   270,    -1,    -1,    -1,    -1,   275,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   258, 
     -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1, 
     -1,    -1,    -1,    -1,   273,   273, 
  };
}

boolean bYYDataInitialized = initYYData();
/** Initialize the internal data of Parser. */
private boolean initYYData() {
  int dataNumber = 0;
  int i = 0;
  int j = 0;


  /** Initialize yylhs. */
  if (yylhs == null) {
    dataNumber = 0;
    YYLHS0 YYLHS_0 = new YYLHS0();
    dataNumber += YYLHS_0.yylhs.length;
    yylhs = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYLHS_0.yylhs.length; ++i, ++j) {
      yylhs[j] = YYLHS_0.yylhs[i];
    }
  }

  /** Initialize yylen. */
  if (yylen == null) {
    dataNumber = 0;
    YYLEN0 YYLEN_0 = new YYLEN0();
    dataNumber += YYLEN_0.yylen.length;
    yylen = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYLEN_0.yylen.length; ++i, ++j) {
      yylen[j] = YYLEN_0.yylen[i];
    }
  }

  /** Initialize yydefred. */
  if (yydefred == null) {
    dataNumber = 0;
    YYDEFRED0 YYDEFRED_0 = new YYDEFRED0();
    dataNumber += YYDEFRED_0.yydefred.length;
    yydefred = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYDEFRED_0.yydefred.length; ++i, ++j) {
      yydefred[j] = YYDEFRED_0.yydefred[i];
    }
  }

  /** Initialize yysindex. */
  if (yysindex == null) {
    dataNumber = 0;
    YYSINDEX0 YYSINDEX_0 = new YYSINDEX0();
    dataNumber += YYSINDEX_0.yysindex.length;
    yysindex = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYSINDEX_0.yysindex.length; ++i, ++j) {
      yysindex[j] = YYSINDEX_0.yysindex[i];
    }
  }

  /** Initialize yyrindex. */
  if (yyrindex == null) {
    dataNumber = 0;
    YYRINDEX0 YYRINDEX_0 = new YYRINDEX0();
    dataNumber += YYRINDEX_0.yyrindex.length;
    yyrindex = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYRINDEX_0.yyrindex.length; ++i, ++j) {
      yyrindex[j] = YYRINDEX_0.yyrindex[i];
    }
  }

  /** Initialize yygindex. */
  if (yygindex == null) {
    dataNumber = 0;
    YYGINDEX0 YYGINDEX_0 = new YYGINDEX0();
    dataNumber += YYGINDEX_0.yygindex.length;
    yygindex = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYGINDEX_0.yygindex.length; ++i, ++j) {
      yygindex[j] = YYGINDEX_0.yygindex[i];
    }
  }

  /** Initialize yytable. */
  if (yytable == null) {
    dataNumber = 0;
    YYTABLE0 YYTABLE_0 = new YYTABLE0();
    dataNumber += YYTABLE_0.yytable.length;
    yytable = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYTABLE_0.yytable.length; ++i, ++j) {
      yytable[j] = YYTABLE_0.yytable[i];
    }
  }

  /** Initialize yycheck. */
  if (yycheck == null) {
    dataNumber = 0;
    YYCHECK0 YYCHECK_0 = new YYCHECK0();
    dataNumber += YYCHECK_0.yycheck.length;
    yycheck = new short[dataNumber];
    j = 0;
    for (i = 0; i < YYCHECK_0.yycheck.length; ++i, ++j) {
      yycheck[j] = YYCHECK_0.yycheck[i];
    }
  }

  return true;
}

final static short YYFINAL=2;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,null,"','",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"KWD_IMPORT","KWD_MESSAGE","KWD_VECTOR",
"KWD_BOOLEAN","KWD_BYTE","KWD_SHORT","KWD_INT","KWD_LONG","KWD_FLOAT",
"KWD_DOUBLE","KWD_STRING","KWD_MAP","KWD_NULL","KWD_VOID","KWD_COMPRESSED",
"KWD_EXTENDS","KWD_COMMAND","KWD_RESERVED","SKWD_IDENT","SKWD_FILENAME",
"SKWD_INTEGER_CONST","SKWD_STRING_CONST",
};
final static String yyrule[] = {
"$accept : message_define",
"message_define : imports message_classes command_message_map",
"imports : import imports",
"imports :",
"import : KWD_IMPORT SKWD_FILENAME ';'",
"$$1 :",
"command_message_map : KWD_COMMAND $$1 '{' commands",
"command_message_map :",
"commands : command commands",
"commands : '}'",
"command : SKWD_INTEGER_CONST command_arg command_arg ';'",
"command_arg : SKWD_IDENT",
"command_arg : SKWD_IDENT '(' KWD_COMPRESSED ')'",
"command_arg : KWD_NULL",
"command_arg : KWD_VOID",
"message_classes :",
"message_classes : class message_classes",
"$$2 :",
"class : KWD_MESSAGE SKWD_IDENT extends $$2 '{' properties",
"extends : KWD_EXTENDS SKWD_IDENT",
"extends :",
"properties : property properties",
"properties : '}'",
"property : simple_type_property",
"property : vector_type_property",
"property : array_type_property",
"property : map_type_property",
"simple_type_property : numeric_type_property",
"simple_type_property : string_type_property",
"simple_type_property : class_type_property",
"vector_type_property : KWD_VECTOR '<' single_type '>' SKWD_IDENT ';'",
"array_type_property : single_type '[' ']' SKWD_IDENT ';'",
"map_type_property : KWD_MAP '<' single_type ',' single_type '>' SKWD_IDENT ';'",
"single_type : class_type",
"single_type : basic_type",
"numeric_type_property : numeric_type SKWD_IDENT ';'",
"numeric_type_property : numeric_type SKWD_IDENT '=' SKWD_INTEGER_CONST ';'",
"string_type_property : string_type SKWD_IDENT ';'",
"string_type_property : string_type SKWD_IDENT '=' SKWD_STRING_CONST ';'",
"numeric_type : KWD_BYTE",
"numeric_type : KWD_BOOLEAN",
"numeric_type : KWD_SHORT",
"numeric_type : KWD_INT",
"numeric_type : KWD_LONG",
"numeric_type : KWD_FLOAT",
"numeric_type : KWD_DOUBLE",
"string_type : KWD_STRING",
"class_type_property : class_type SKWD_IDENT ';'",
"class_type : SKWD_IDENT",
"basic_type : numeric_type",
"basic_type : string_type",
};

//#line 313 "mc.y"

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
//#line 340 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    else	//non-terminal reduced by EMPTY
      yyval = new ParserVal(0);	//Generate a empty token value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 64 "mc.y"
{
	}
break;
case 2:
//#line 69 "mc.y"
{
	}
break;
case 3:
//#line 72 "mc.y"
{
	}
break;
case 4:
//#line 77 "mc.y"
{
		builder.importMdl(getImportFile(val_peek(1).sval));
	}
break;
case 5:
//#line 83 "mc.y"
{
		clearLastJavadoc();
	}
break;
case 6:
//#line 86 "mc.y"
{
	}
break;
case 7:
//#line 89 "mc.y"
{
	}
break;
case 8:
//#line 94 "mc.y"
{
	}
break;
case 9:
//#line 97 "mc.y"
{
	}
break;
case 10:
//#line 102 "mc.y"
{
		builder.addCommand(val_peek(3).ival, val_peek(2).obj, val_peek(1).obj, getLastJavadoc());
	}
break;
case 11:
//#line 108 "mc.y"
{
		yyval.obj = new CommandArgument(val_peek(0).sval, getLocation());
	}
break;
case 12:
//#line 112 "mc.y"
{
		yyval.obj = new CommandArgument(val_peek(3).sval, true, getLocation());
	}
break;
case 13:
//#line 116 "mc.y"
{
		yyval.obj = new CommandArgument(null, getLocation());
	}
break;
case 14:
//#line 120 "mc.y"
{
		yyval.obj = new CommandArgument();
	}
break;
case 15:
//#line 126 "mc.y"
{
	}
break;
case 16:
//#line 129 "mc.y"
{
	}
break;
case 17:
//#line 134 "mc.y"
{
		builder.newMessage(val_peek(1).sval, getLastJavadoc(), val_peek(0).sval, getLocation());
	}
break;
case 18:
//#line 137 "mc.y"
{
		clearLastJavadoc();
	}
break;
case 19:
//#line 143 "mc.y"
{
		yyval.sval = val_peek(0).sval;
	}
break;
case 20:
//#line 147 "mc.y"
{
		yyval.sval = null;
	}
break;
case 21:
//#line 153 "mc.y"
{
	}
break;
case 22:
//#line 156 "mc.y"
{
	}
break;
case 23:
//#line 161 "mc.y"
{
		clearLastJavadoc();
	}
break;
case 24:
//#line 165 "mc.y"
{
		clearLastJavadoc();
	}
break;
case 25:
//#line 169 "mc.y"
{
		clearLastJavadoc();
	}
break;
case 26:
//#line 173 "mc.y"
{
		clearLastJavadoc();
	}
break;
case 30:
//#line 184 "mc.y"
{
		builder.addField(new VectorType(val_peek(3).obj), val_peek(1).sval, getLastJavadoc());
	}
break;
case 31:
//#line 190 "mc.y"
{
		builder.addField(new ArrayType(val_peek(4).obj), val_peek(1).sval, getLastJavadoc());
	}
break;
case 32:
//#line 196 "mc.y"
{
		builder.addField(new MapType(val_peek(5).obj, val_peek(3).obj), val_peek(1).sval, getLastJavadoc());
	}
break;
case 33:
//#line 202 "mc.y"
{
		yyval.obj = val_peek(0).obj;
	}
break;
case 34:
//#line 206 "mc.y"
{
		yyval.obj = val_peek(0).obj;
	}
break;
case 35:
//#line 212 "mc.y"
{
		builder.addField(val_peek(2).obj, val_peek(1).sval, getLastJavadoc());
	}
break;
case 36:
//#line 216 "mc.y"
{
		builder.addField(val_peek(4).obj, val_peek(3).sval, getLastJavadoc());
	}
break;
case 37:
//#line 222 "mc.y"
{
		builder.addField(val_peek(2).obj, val_peek(1).sval, getLastJavadoc());
	}
break;
case 38:
//#line 226 "mc.y"
{
		builder.addField(val_peek(4).obj, val_peek(3).sval, getLastJavadoc());
	}
break;
case 39:
//#line 232 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 40:
//#line 236 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 41:
//#line 240 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 42:
//#line 244 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 43:
//#line 248 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 44:
//#line 252 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 45:
//#line 256 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 46:
//#line 262 "mc.y"
{
		yyval.obj = SimpleType.getInstance(val_peek(0).sval);
	}
break;
case 47:
//#line 268 "mc.y"
{
		builder.addField(val_peek(2).obj, val_peek(1).sval, getLastJavadoc());
	}
break;
case 48:
//#line 274 "mc.y"
{
		yyval.obj = MessageBean.getMessageBean(val_peek(0).sval, getLocation());
	}
break;
case 49:
//#line 304 "mc.y"
{
		yyval.obj = val_peek(0).obj;
	}
break;
case 50:
//#line 308 "mc.y"
{
		yyval.obj = val_peek(0).obj;
	}
break;
//#line 758 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug)
        debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug)
        debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
