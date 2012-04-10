/*
	http://en.wikipedia.org/wiki/Box_Muller_transform
	http://m.seehuhn.de/data/ziggurat/ziggurat.c
	http://www.jstatsoft.org/v05/i08/paper

 */

#include <list>
#include <cstdlib>
#include <time.h>
#include <cstdio>
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
	int i = (int)f*w*h*p;
	//printf("p=%d %f\n", i, (int)100*p);
	for(i; i; i){
		Sorteio *s = new Sorteio();
		(*s).f = rand()%f;
		(*s).x = rand()%w;
		(*s).y = rand()%h;
		for(int j = 0; j < d && i--; j++) vetor.push_back(*s);
	}
	return vetor;
}

int main(){
	int F = 30, W = 20, H = 10, D = 1;
	float P = 0.01;
	list<Sorteio> resultado = sorteio(F, W, H, P, D, CONSTANTE);
	list<Sorteio>::iterator it;
	//for(it = resultado.begin(); it != resultado.end(); it++)
	//	printf("F=%d\tX=%d\tY=%d\t\n", (*it).f, (*it).x, (*it).y);
	printf("it=%d\n", resultado.size());
	resultado.sort(ordenar);
	//for(it = resultado.begin(); it != resultado.end(); it++)
	//	printf("F=%d\tX=%d\tY=%d\t\n", (*it).f, (*it).x, (*it).y);

	return 0;
}
