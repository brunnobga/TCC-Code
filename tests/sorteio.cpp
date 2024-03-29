/*
	http://en.wikipedia.org/wiki/Box_Muller_transform
	http://m.seehuhn.de/data/ziggurat/ziggurat.c
	http://www.jstatsoft.org/v05/i08/paper

 */

#include <list>
#include <cstdlib>
#include <time.h>
#include <cstdio>
#include <cmath>
#define CONSTANTE 1
#define NORMAL 2

using namespace std;

struct Sorteio{
	int f, x, y;
};

int duracao(int d, int distribuicao){
	if(distribuicao == CONSTANTE) return d;
	else{
		if(distribuicao == NORMAL) return d;
	}
}


bool ordenar(Sorteio s1, Sorteio s2){
	if(s1.f < s2.f) return true;
	else{
		if(s1.f == s2.f){
			if(s1.x < s2.x) return true;
			else{
				if(s1.x == s2.x){
					if(s1.y < s2.y) return true;
					else return false;
				}
				else return false;
			}
		}
		else return false;
	}
}

list<Sorteio> sorteio(int f, int w, int h, float p, int d, int dist){
	/*
	 f = Total de frames do video
	 w = Largura, em pixels, de cada frame
	 h = Altura, em pixels, de cada frame
	 p = Porcentagem de degradacao, no intervalo [0, 1]
	 d = Duracao de cada artefato, em frames
	 dist = Tipo de distribuicao para geracao da duracao do artefato
	 */
	srand(time(NULL));
	list<Sorteio> vetor;
	list<Sorteio>::iterator it;
	int j, i = round(f*w*h*p);
	//printf("p=%d \n",  i);
	for(i; i >= 0; i){
		Sorteio *s = new Sorteio();
		(*s).f = 2-d+rand()%(f+d-1);//(f-d);
		(*s).x = rand()%w;
		(*s).y = rand()%h;
		for(j = 0; j < d && i >= 0; j++){
			if(((*s).f) <= f && ((*s).f) > 0){
				vetor.push_back(*s);
				i--;
			}
			(*s).f++;
		}
	}
	return vetor;
}

int main(){
	int F = 30, W = 256, H = 192, D = 1;
	float P = 0.03;
	list<Sorteio> resultado = sorteio(F, W, H, P, D, CONSTANTE);
	list<Sorteio>::iterator it;
	//for(it = resultado.begin(); it != resultado.end(); it++)
	//	printf("F=%d\tX=%d\tY=%d\t\n", (*it).f, (*it).x, (*it).y);
	//printf("it=%d\n", resultado.size());
	resultado.sort(ordenar);
	for(it = resultado.begin(); it != resultado.end(); it++)
		//printf("F=%d\tX=%d\tY=%d\t\n", (*it).f, (*it).x, (*it).y);
		printf("%d %d %d\n", (*it).f, (*it).x, (*it).y);

	return 0;
}
