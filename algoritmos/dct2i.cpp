/*
 Inverse Discrete Cosine Transform 2
	http://reference.wolfram.com/legacy/applications/digitalimage/FunctionIndex/InverseDiscreteCosineTransform.html
	http://www.mathworks.com/help/toolbox/images/ref/idct2.html
 */
#include <cstdio>
#include <math.h>
#include <stdlib.h>
#include <iostream>

using namespace std;

int main(void){
	int m, n;
	scanf("%d%d", &m, &n);
	double a, b, r[m][n], o[m][n];

	// read input file dct2.out
	for(int i = 0; i < m; i++){
		for(int j = 0; j < n; j++){
			scanf("%lf", &o[i][j]);
		}
	}
	for(int mm = 0; mm < m; mm++){
		for(int nn = 0; nn < n; nn++){
			r[mm][nn] = 0;
			for(int p = 0; p < m; p++){
				for(int q = 0; q < n; q++){
					if(p == 0) a = 1/sqrt(m);
					else a = sqrt(2)/sqrt(m);
					if(q == 0) b = 1/sqrt(n);
					else b = sqrt(2)/sqrt(n);
					r[mm][nn] += a*b*o[p][q]*cos(M_PI*(2*mm+1)*p/(2*m))*cos(M_PI*(2*nn+1)*q/(2*n));
				}
			}
		}
	}
	printf("%d %d\n", m, n);
	for(int i = 0; i < m; i++){
		for(int j = 0; j < n; j++) printf("%3.4lf  ", r[i][j]);
		printf("\n");
	}
	printf("\n");

	return 0;
}
