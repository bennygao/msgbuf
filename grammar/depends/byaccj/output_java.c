/*
 *
 * $Id: output_java.c,v 1.9 2003/05/27 04:54:42 gaobo Exp $
 */
#include <stdio.h>
#include <stdarg.h>
#include <string.h>
#include <stdlib.h>

#include "defs.h"

#define PATH_NAME_LEN	512


static void error_exit(char* fmt, ...)
{
	va_list ap;

	va_start(ap, fmt);
	vfprintf(stderr, fmt, ap);
	va_end(ap);
	done(2);
}

static char* java_init_code[] = {
	"boolean bYYDataInitialized = initYYData();",
	"/** Initialize the internal data of Parser. */",
	"private boolean initYYData() {",
	"  int dataNumber = 0;",
	"  int i = 0;",
	"  int j = 0;",
	"",
	NULL
};

void output_java_init_code_header(void)
{
	int i;

	for (i = 0; java_init_code[i] != NULL; ++i) {
		fprintf(init_file, "%s\n", java_init_code[i]);
	}
}

void output_java_init_code_tail(void)
{
	char buf[256];

	fprintf(init_file, "\n  return true;\n}\n");
	fclose(init_file);
	init_file = fopen(init_file_name, "r");
	while (fgets(buf, 255, init_file)) {
		fputs(buf, output_file);
	}

	fprintf(output_file, "\n");
	fclose(init_file);
	unlink(init_file_name);
	init_file = NULL;
}

void output_java_init_code(char *classBase, char *varName, char *dataType, int classNum)
{
	char className[256];
	char instName[256];
	int i;

	fprintf(init_file, "\n  /** Initialize %s. */\n", varName);
	fprintf(init_file, "  if (%s == null) {\n", varName);
	fprintf(init_file, "    dataNumber = 0;\n");

	for (i = 0; i < classNum; ++i) {
		sprintf(className, "%s%d", classBase, i);
		sprintf(instName, "%s_%d", classBase, i);
		fprintf(init_file, "    %s %s = new %s();\n", 
			className, instName, className);
		fprintf(init_file, "    dataNumber += %s.%s.length;\n", 
			instName, varName);
	}

	fprintf(init_file, "    %s = new %s[dataNumber];\n", 
		varName, dataType);
	fprintf(init_file, "    j = 0;\n");

	for (i = 0; i < classNum; ++i) {
		sprintf(instName, "%s_%d", classBase, i);
		fprintf(init_file, 
			"    for (i = 0; i < %s.%s.length; ++i, ++j) {\n",
			instName, varName);
		fprintf(init_file, "      %s[j] = %s.%s[i];\n", 
			varName, instName, varName);
		fprintf(init_file, "    }\n");
	}
	fprintf(init_file, "  }\n");
}

void output_java_defines(void)
{
    int c, i;
    char *s;
	char definesClassName[PATH_NAME_LEN];
	FILE *jfp;

	sprintf(definesClassName, "%sTokens.java", jclass_name);
	if ((jfp = fopen(definesClassName, "w+")) == NULL) {
		error_exit("Open %s for write error.\n", definesClassName);
	}

	fprintf(jfp, 
		"/** BYACC/J generated class containing symbol constants. */\n");
	if (jpackage_name) {
		if (strlen(jpackage_name) > 0) {
			fprintf(jfp, "package %s;\n\n", jpackage_name);
		}
	}

	fprintf(jfp, "public class %sTokens {\n", jclass_name);

    for (i = 2; i < ntokens; ++i)
    {
        s = symbol_name[i];
        if (is_C_identifier(s)) {
            fprintf(jfp, "  public final static short ");
 
            c = *s;
            if (c == '"') {
                while ((c = *++s) != '"') {
                    putc(c, jfp);
                }
            } else {
                do {
                    putc(c, jfp);
                } while ((c = *++s)!=0);
            }
 
            fprintf(jfp, " = %d;\n", symbol_value[i]);
        }
    }

    fprintf(code_file, "public final static short YYERRCODE = %d;\n", 
		symbol_value[1]);

	fprintf(jfp, "}");
    fflush(code_file);
	fclose(jfp);
}

void output_java_rule_data(void)
{
	int i;
	int j;
	int innerClassNum = 0;
	int symNum = 1 + (nrules - 3);
	short* symValue;
	int ncount = 0;

	/* initialize the symbole values into A short array */
	symValue = (short*)calloc(sizeof(short), symNum);
	symValue[0] = symbol_value[start_symbol];
	for (i = 3, j = 1; i < nrules; ++i, ++j)
		symValue[j] = symbol_value[rlhs[i]]; 
		
	fprintf(output_file, "static short[] yylhs = null;\n");

	/* output the inner class definition for yylhs */
	for (i = 0; i < symNum; ++i) {
		if ((i == 0) || ((i) % MAX_DATA_NUM == 0)) {
			if (i > 0) {
				fprintf(output_file, "\n  };\n}\n\n");
			}

			fprintf(output_file, "class YYLHS%d {\n", innerClassNum++);
			fprintf(output_file, "  final short yylhs[] = {");
			ncount = 0;
		}

		if ((ncount == 0) || (ncount % LINE_DATA_NUM == 0)) {
			fprintf(output_file, "\n  %5d, ", symValue[i]);
		} else
			fprintf(output_file, "%5d, ", symValue[i]);

		++ncount;
	}

	fprintf(output_file, "\n  };\n}\n\n");

	output_java_init_code("YYLHS", "yylhs", "short", innerClassNum);

	/* output the inner class definition for yylhs */
	symValue[0] = 2;
	for (i = 3, j = 1; i < nrules; ++i, ++j)
		symValue[j] = rrhs[i + 1] - rrhs[i] - 1;

    fprintf(output_file, "static short[] yylen = null;\n");
	innerClassNum = 0;

	/* output the inner class definition for yylen */
	for (i = 0; i < symNum; ++i) {
		if ((i == 0) || ((i) % MAX_DATA_NUM == 0)) {
			if (i > 0) {
				fprintf(output_file, "\n  };\n}\n\n");
			}

			fprintf(output_file, "class YYLEN%d {\n", innerClassNum++);
			fprintf(output_file, "  final short yylen[] = {");
			ncount = 0;
		}

		if ((ncount == 0) || (ncount % LINE_DATA_NUM == 0)) {
			fprintf(output_file, "\n  %5d, ", symValue[i]);
		} else
			fprintf(output_file, "%5d, ", symValue[i]);

		++ncount;
	}

	fprintf(output_file, "\n  };\n}\n\n");
	output_java_init_code("YYLEN", "yylen", "short", innerClassNum);

	fflush(output_file);
	free(symValue);
}

void output_java_yydefred(void)
{
	int i;
	int ncount = 0;
	int innerClassNum = 0;

	fprintf(output_file, "static short[] yydefred = null;\n");

	for (i = 0; i < nstates; ++i) {
		if ((i == 0) || (i % MAX_DATA_NUM == 0)) {
			if (i > 0) {
				fprintf(output_file, "\n  };\n}\n\n");
			}

			fprintf(output_file, "class YYDEFRED%d {\n", 
				innerClassNum++);
			fprintf(output_file, "  final short yydefred[] = {");
			ncount = 0;
		}

		if ((ncount == 0) || (ncount % LINE_DATA_NUM == 0)) {
			fprintf(output_file, "\n  %5d, ",
				(defred[i] ? defred[i] - 2 : 0));
		} else {
			fprintf(output_file, "%5d, ",
				(defred[i] ? defred[i] - 2 : 0));
		}

		++ncount;
	}

	fprintf(output_file, "\n  };\n}\n\n");
	output_java_init_code(
		"YYDEFRED", "yydefred", "short", innerClassNum);

	fflush(output_file);
}

void output_new_yyval(void) {
	FILE *fp = code_file;

	fprintf(fp, "    else\t//non-terminal reduced by EMPTY\n");
	fprintf(fp, "      yyval = new %sVal(0);\t//Generate a empty token value\n",
		jclass_name);
}