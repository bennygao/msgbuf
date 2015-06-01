#################################################
## Makefile for Byacc/Java
## Date: 06 Aug 00
## Author: Bob Jamison
#################################################

#CC      = i686-pc-mingw32-gcc
CC      = gcc
#CFLAGS  = -O -m32
CFLAGS  = -O
#LDFLAGS = -s -static -m32
LDFLAGS = -s -static
TAR     = /bin/tar
GZIP    = /usr/sbin/gzip
VERS    = byaccj1.1
JAVA    = /usr/java
JAR     = $(JAVA)/bin/jar

OBJ 	= closure.o \
	error.o \
	lalr.o \
	lr0.o \
	main.o \
	mkpar.o \
	output.o \
	reader.o \
	skeleton.o \
	symtab.o \
	verbose.o \
	warshall.o \
	output_java.o

all: yacc

irix: yacc
	mv yacc yacc.irix
linux: yacc
	mv yacc yacc.linux
solaris: yacc
	mv yacc yacc.solaris


dist:
	( \
	cd .. ; \
	/bin/rm -rf $(VERS) ; \
	/bin/mkdir $(VERS) ; \
        /bin/cp -r doc $(VERS) ; \
	/bin/mkdir $(VERS)/src ; \
        /bin/cp src/*.c $(VERS)/src ; \
        /bin/cp src/*.h $(VERS)/src ; \
        /bin/cp src/Makefile $(VERS)/src ; \
        /bin/cp src/Makefile.bcc $(VERS)/src ; \
        /bin/cp -f src/yacc.irix $(VERS) ; \
        /bin/cp -f src/yacc.solaris $(VERS) ; \
        /bin/cp -f src/yacc.linux $(VERS) ; \
        /bin/cp -f src/yacc.exe $(VERS) ; \
        /bin/cp src/README $(VERS) ; \
	$(TAR) -cvf - $(VERS) | $(GZIP) > $(VERS).tar.gz \
	$(JAR) cMvf $(VERS).zip $(VERS) \
	)

SRCS   = closure.c \
	error.c \
	lalr.c \
	lr0.c \
	main.c \
	mkpar.c \
	output.c \
	reader.c \
	skeleton.c \
	symtab.c \
	verbose.c \
	warshall.c

yacc: $(OBJ)
	$(CC) -o yacc $(OBJ)
	@echo "done"

clean:
	rm -rf *.o yacc y.tab.c *.java *.class

###
closure.o: closure.c defs.h
error.o: error.c defs.h
lalr.o: lalr.c defs.h
lr0.o: lr0.c defs.h
main.o: main.c defs.h
mkpar.o: mkpar.c defs.h
output.o: output.c defs.h
reader.o: reader.c defs.h
skeleton.o: skeleton.c defs.h
symtab.o: symtab.c defs.h
verbose.o: verbose.c defs.h
warshall.o: warshall.c defs.h

