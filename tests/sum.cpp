#include <cstdio>

int main(void){
	int f[30], tf, tx, ty;
	for(int i = 0; i < 30; f[i++] = 0);
	for(int i = 0; i < 44236; i++){
		scanf("%d %d %d", &tf, &tx, &ty);
		f[tf-1]++;
	}
	for(int i = 0; i < 30; printf("F[%d] = %d\n", i+1, f[i]), i++);
	
	return 0;
}
