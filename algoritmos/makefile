CC=g++
CFLAGS=-c -Wall

all: filterTool

filterTool: filterTool.o dctTools.o raffleTools.o commons.h dctTools.h raffleTools.h
	$(CC) filterTool.o dctTools.o raffleTools.o -o filterTool

testes: testes.o dctTools.o
	$(CC) testes.o dctTools.o -o testes

filterTool.o: filterTool.cpp dctTools.h raffleTools.h commons.h
	$(CC) $(CFLAGS) filterTool.cpp

testes.o: testes.cpp dctTools.h commons.h
	$(CC) $(CFLAGS) testes.cpp

dctTools.o: dctTools.cpp dctTools.h commons.h
	$(CC) $(CFLAGS) dctTools.cpp

raffleTools.o: raffleTools.cpp raffleTools.h commons.h
	$(CC) $(CFLAGS) raffleTools.cpp

clean:
	rm -rf *.o testes filterTools

