#ifndef BLUR_TOOLS
#define BLUR_TOOLS

const int LAPLACIAN = 0;

const int AVERAGE = 1;

const int MEDIAN = 2;

typedef unsigned char byte;

void blur(byte *originalFrame, byte *bluredFrame, int H, int W, int fType, int fSize);

#endif
