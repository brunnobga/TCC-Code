#ifndef DCT_TOOLS
#define DCT_TOOLS

typedef unsigned char byte;

void applyDCT2(byte* blockFrom, float* blockTo, int blockSize, int frameW);

void applyIDCT2(float* blockFrom, byte* blockTo, int blockSize, int frameW);

#endif
