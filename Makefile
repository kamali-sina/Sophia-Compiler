CC := gcc
# CCFLAGS += -l SDL2 -l SDL2_image -l SDL2_ttf -l SDL2_mixer

BUILD_DIR=.

all: setup antlr javac

setup:
	export CLASSPATH=".:/home/papa/Antlr/antlr-4.8-complete.jar:$CLASSPATH"

antlr:
	java -Xmx500M -cp ".:/home/papa/Antlr/antlr-4.8-complete.jar:$CLASSPATH" org.antlr.v4.Tool Lexer.g4

javac:
	javac Lexer*.java

run:
	.:/home/papa/Antlr/antlr-4.8-complete.jar:$CLASSPATH" org.antlr.v4.gui.TestRig Lexer main_scope -gui < ../test_case.txt

.PHONY: clean

clean:
	rm -rf ./*.class ./*.java ./*.interp ./*.tokens

