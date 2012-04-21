#ifndef COMMONS
#define COMMONS

const int CONSTANT = 1;
const int TRIANGULAR = 2;
const int NORMAL = 3;
const int UNIFORM = 4;

typedef unsigned char byte;

typedef struct raffle{
	int f, x, y;
} Raffle;

typedef struct distribution{
	int type;
	int a, b, c;
} Distribution;

typedef struct settings{
	int blockSize, removalsSize, *removals, duration, artifactDist, blurType;
	double percent;
	Distribution durationDist, frameDist;
} Settings;

#endif

