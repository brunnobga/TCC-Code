CC=g++
CFLAGS=-c -Wall

all: filterTool metricTool

metricTool: metricTool.o
	$(CC) metricTool.o -o metricTool

metricTool.o: metricTool.cpp
	$(CC) $(CFLAGS) metricTool.cpp

filterTool: filterTool.o dctTools.o raffleTools.o blurTools.o commons.h dctTools.h raffleTools.h
	$(CC) filterTool.o dctTools.o raffleTools.o blurTools.o -o filterTool

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

blurTools.o: blurTools.cpp blurTools.h commons.h
	$(CC) $(CFLAGS) blurTools.cpp

clean:
	rm -rf *.o testes filterTool metricTool

