/*Arquivo contendo os metodos de manipulação no dominio da frequencia*/

#include <cmath>
#include <cstdlib>
#include <cstdio>

typedef unsigned char byte;

// Aplica DCT2 em um bloco blockSize X blockSize contido em um frame de largura frameW.
// blockFrom aponta para o primeiro pixel a ser processado em um frame de largura frameW
// blockTo deve ter tamanho blockSize*blockSize
void applyDCT2(byte *blockFrom, double* blockTo, int blockSize, int frameW){
	float a, b;
	int p, q, mm, nn;

	for(p = 0; p < blockSize; p++){
		for(q = 0; q < blockSize; q++){
			if(p == 0) a = 1/sqrt(blockSize);
			else a = sqrt(2)/sqrt(blockSize);
			if(q == 0) b = 1/sqrt(blockSize);
			else b = sqrt(2)/sqrt(blockSize);
			//r[p][q] = 0;
			*(blockTo + p*blockSize + q) = 0;
			for(mm = 0; mm < blockSize; mm++){
				for(nn = 0; nn < blockSize; nn++){
					//r[p][q] += a*b*o[mm][nn]*cos(M_PI*(2*mm+1)*p/(2*m))*cos(M_PI*(2*nn+1)*q/(2*n));
					*(blockTo + p*blockSize + q) += *(blockFrom + mm*frameW + nn)*a*b*cos(M_PI*(2*mm+1)*p/(2*blockSize))*cos(M_PI*(2*nn+1)*q/(2*blockSize));
				}
			}
		}
	}
	return;
}

//Aplica IDCT2 em um bloco blockSize X blockSize contido em um frame de largura frameW.
void applyIDCT2(double* blockFrom, byte* blockTo, int blockSize, int frameW){
	float a, b;
	int p, q, mm, nn, result;
	
	for(int mm = 0; mm < blockSize; mm++){
		for(int nn = 0; nn < blockSize; nn++){
			//r[mm][nn] = 0;
			result = 0;
			for(int p = 0; p < blockSize; p++){
				for(int q = 0; q < blockSize; q++){
					if(p == 0) a = 1/sqrt(blockSize);
					else a = sqrt(2)/sqrt(blockSize);
					if(q == 0) b = 1/sqrt(blockSize);
					else b = sqrt(2)/sqrt(blockSize);
					//r[mm][nn] += a*b*o[p][q]*cos(M_PI*(2*mm+1)*p/(2*blockSize))*cos(M_PI*(2*nn+1)*q/(2*blockSize));
					result += *(blockFrom + p*blockSize + q)*a*b*cos(M_PI*(2*mm+1)*p/(2*blockSize))*cos(M_PI*(2*nn+1)*q/(2*blockSize));
				}
			}
			if(result < 0) result = 0; 
			*(blockTo +mm*frameW + nn) = result;
		}
	}
}

void removeDiag(double* matrix, int mSize, int level){
	int i, j, k;
	if(level >= 2*mSize - 2) return;
	j = (mSize-1 > level ? level : mSize -1);
	i = level - j;
	k = mSize - (level-(mSize-1) > 0 ? level-(mSize-1) : -(level-(mSize-1)));
	for(; k; k--, i++, j--)
		*(matrix + i*mSize + j) = 0;
}

void removeDiagBelow(double* matrix, int mSize, int level){
	for(int i = level; i < 2*mSize - 1; i++);
}

void blockage(byte* blockFrom, byte* blockTo, int blockSize, int frameW, int * removals, int removalsSize){
	double dct[blockSize][blockSize];
	applyDCT2(blockFrom, &dct[0][0], blockSize, frameW);
	removeDiag(&dct[0][0], blockSize, 1);
	applyIDCT2(&dct[0][0], blockTo, blockSize, frameW);
}
