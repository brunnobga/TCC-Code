#include "dctTools.h"
#include <cstdio>

int main(){
	byte a[4][4] = {{1,1,1,1},{1,1,1,1},{1,1,1,1},{1,1,1,1}};
	float b[4][4];

	applyDCT2(&a[0][0], &b[0][0], 4, 4);

	for(int i = 0; i < 4; i++){
		for(int j = 0; j < 4; j++){
			printf("%f ", b[i][j]);
		}
		printf("\n");
	}
}
