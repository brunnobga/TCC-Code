/*Arquivo contendo os metodos de manipulação no dominio da frequencia*/

#include <cmath>
#include <cstdlib>
#include <cstdio>

typedef unsigned char byte;

// Aplica DCT2 em um bloco blockSize X blockSize contido em um frame de largura frameW.
// blockFrom aponta para o primeiro pixel a ser processado em um frame de largura frameW
// blockTo deve ter tamanho blockSize*blockSize
void applyDCT2(byte *blockFrom, float *blockTo, int blockSize, int frameW){
	float a, b;
	int p, q, mm, nn;

	for(p = 0; p < blockSize; p++){
		for(q = 0; q < blockSize; q++){
			if(p == 0) a = 1/sqrt(blockSize);
			else a = sqrt(2/blockSize);
			if(q == 0) b = 1/sqrt(blockSize);
			else b = sqrt(2/blockSize);
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
void applyIDCT2(float* blockFrom, byte* blockTo, int blockSize, int frameW){
	float a, b;
	int p, q, mm, nn;
	
	for(int mm = 0; mm < blockSize; mm++){
		for(int nn = 0; nn < blockSize; nn++){
			//r[mm][nn] = 0;
			*(blockTo + mm*frameW + nn) = 0;
			for(int p = 0; p < blockSize; p++){
				for(int q = 0; q < blockSize; q++){
					if(p == 0) a = 1/sqrt(blockSize);
					else a = sqrt(2/blockSize);
					if(q == 0) b = 1/sqrt(blockSize);
					else b = sqrt(2/blockSize);
					//r[mm][nn] += a*b*o[p][q]*cos(M_PI*(2*mm+1)*p/(2*blockSize))*cos(M_PI*(2*nn+1)*q/(2*blockSize));
					*(blockTo + mm*frameW + nn) += *(blockFrom + p*blockSize + q)*a*b*cos(M_PI*(2*mm+1)*p/(2*blockSize))*cos(M_PI*(2*nn+1)*q/(2*blockSize));
				}
			}	
		}
	}
}
