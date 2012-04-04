/*
 Discrete Cosine Transform 2
	http://www.mathworks.com/help/toolbox/images/f21-16366.html
	http://reference.wolfram.com/mathematica/ref/FourierDCT.html
	http://forums.anandtech.com/showthread.php?t=2029136
	http://www.tiexpert.net/programacao/c/math.php 
 */
#include <cstdio>
#include <math.h>
#include <stdlib.h>
#include <iostream>

using namespace std;

int main(void){
	int m = 8, n = 8;
	float a, b, r[m][n], o[m][n];

	// read input file dct2.in
	for(int i = 0; i < m; i++){
		for(int j = 0; j < n; j++){
			scanf("%f", &o[i][j]);
		}
	}

	for(int p = 0; p < m; p++){
		for(int q = 0; q < n; q++){
			if(p == 0) a = 1/sqrt(m);
			else a = sqrt(2/m);
			if(q == 0) b = 1/sqrt(n);
			else b = sqrt(2/n);
			r[p][q] = 0;
			for(int mm = 0; mm < m; mm++){
				for(int nn = 0; nn < n; nn++){
					r[p][q] += a*b*o[mm][nn]*cos(M_PI*(2*mm+1)*p/(2*m))*cos(M_PI*(2*nn+1)*q/(2*n));
				}
			}
		}
	}
	for(int i = 0; i < m; i++){
		printf("\n");
		for(int j = 0; j < n; j++) printf("%3.4f  ", r[i][j]);
	}
	printf("\n");

	return 0;
}
