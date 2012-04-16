#ifndef COMMONS
#define COMMONS

typedef unsigned char byte;

typedef struct raffle{
	int f, x, y;
} Raffle;

typedef struct settings{
	int blockSize, removalsSize, *removals;
} Settings;

#endif

