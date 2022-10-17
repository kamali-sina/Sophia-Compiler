CC := gcc

BUILD_DIR=.

all: setup antlr javac

setup:
	export CLASSPATH=".:/home/papa/Antlr/antlr-4.8-complete.jar:$CLASSPATH"

antlr:
	java -Xmx500M -cp ".:/home/papa/Antlr/antlr-4.8-complete.jar:$CLASSPATH" org.antlr.v4.Tool Sophia.g4

javac:
	javac Sophia*.java

run:
	.:/home/papa/Antlr/antlr-4.8-complete.jar:$CLASSPATH" org.antlr.v4.gui.TestRig Sophia sophia -gui < ../test_case.txt

.PHONY: clean

clean:
	rm -rf ./*.class ./*.java ./*.interp ./*.tokens

