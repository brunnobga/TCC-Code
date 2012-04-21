#ifndef COMMONS
#define COMMONS

const int CONSTANTE = 1;
const int TRIANGULAR = 2;
const int NORMAL = 3;
const int UNIFORME = 4;

typedef unsigned char byte;

typedef struct raffle{
	int f, x, y;
} Raffle;

typedef struct settings{
	int blockSize, removalsSize, *removals, duration, durationDist, a, b, c, artifactDist, frameDist, blurType;
	double percent;
	Distribution *durationDist, *frameDist;
} Settings;

typedef struct distribution{
	int type;
	int a, b, c;
} Distribution;
#endif

