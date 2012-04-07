CC=g++
CFLAGS=-c -Wall

all: testes blocking

testes: testes.o dctTools.o
	$(CC) testes.o dctTools.o -o testes

blocking: blocking.o dctTools.o
	$(CC) blocking.o dctTools.o -o blocking

blocking.o: blocking.cpp
	$(CC) $(CFLAGS) blocking.cpp

testes.o: testes.cpp
	$(CC) $(CFLAGS) testes.cpp

dctTools.o: dctTools.cpp
	$(CC) $(CFLAGS) dctTools.cpp

clean:
	rm -rf *.o testes blocking

