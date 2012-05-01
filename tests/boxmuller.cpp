#include <cstdio>
#include <stdlib.h>
#include <ctime>
#include <cmath>

double box_mueller(){
	double z0, z1, u, v, s;
	do {
		u = ((double)rand()/((double)(RAND_MAX+1.)));
		u = 2*u - 1;
		v = ((double)rand()/((double)(RAND_MAX+1.)));
		v = 2*v - 1;
		s = pow(u, 2)+pow(v, 2);
	} while(s == 0 || s >= 1);
	z0 = u*sqrt(-2*log(s)/s);
	return z0;
}

int main(void){
	srand(time(NULL));
	int count = 0;
	double d;
	for(int i = 0; i < 100; i++){
		d = box_mueller();
		printf("%lf\n", d);
	}
	printf("\n");
	return 0;
}
