CC=g++
CFLAGS=-c -Wall

#all programs
all: block blur metric netsim raffle

#separate programs
blur: blurTools.o blur.o
	$(CC) blurTools.o blur.o -o blur

block: blockTools.o dctTools.o
	$(CC) blockTools.o dctTools.o -o block

metric: metricTool.o
	$(CC) metricTool.o -o metric

netsim: NetSimTool.o
	$(CC) NetSimTool.o -o netsim

raffle: raffleTools.o
	$(CC) raffleTools.o -o raffle

#object files
blur.o: blur.cpp blur.h commons.h
	$(CC) $(CFLAGS) blur.cpp

blurTools.o: blurTools.cpp blur.h commons.h
	$(CC) $(CFLAGS) blurTools.cpp

blockTools.o: blockTools.cpp dctTools.h commons.h
	$(CC) $(CFLAGS) blockTools.cpp

dctTools.o: dctTools.cpp dctTools.h commons.h
	$(CC) $(CFLAGS) dctTools.cpp

metricTool.o: metricTool.cpp
	$(CC) $(CFLAGS) metricTool.cpp

NetSimTool.o: NetSimTool.cpp
	$(CC) $(CFLAGS) NetSimTool.cpp

raffleTools.o: raffleTools.cpp
	$(CC) $(CFLAGS) raffleTools.cpp

clean:
	rm -rf *.o blur block metric netsim raffle

