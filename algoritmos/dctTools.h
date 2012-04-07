#ifndef DCT_TOOLS
#define DCT_TOOLS

typedef unsigned char byte;

void applyDCT2(byte* blockFrom, double* blockTo, int blockSize, int frameW);

void applyIDCT2(double* blockFrom, byte* blockTo, int blockSize, int frameW);

void blockage(byte* blockFrom, byte* blockTo, int blockSize, int frameW, int * removals, int removalsSize);

void removeDiag();

void removeDiagBelow(double* matrix, int mSize, int level);

#endif
