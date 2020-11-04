CC := gcc
# CCFLAGS += -l SDL2 -l SDL2_image -l SDL2_ttf -l SDL2_mixer

BUILD_DIR=.

all: $antlr $javac $run_test

$antlr:
	antlr4 Lexer.g4

$javac:
	javac Lexer*.java

$run_test:
	grun Lexer main_scope -gui < ./test_case.txt

.PHONY: clean
clean:
	rm -rf ./*.class ./*.java ./*.interp ./*.tokens
run:
	./${BUILD_DIR}/executable.out
