#include <cstdlib>
#include <time.h>
#include <cstdio>
#include <cmath>
#include <list>
#include "commons.h"

using namespace std;

bool sort(Raffle s1, Raffle s2){
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

list<Raffle> raffle(int f, int w, int h, Settings * set){
	/*
	 f = Total de frames do video
	 w = Largura, em pixels, de cada frame
	 h = Altura, em pixels, de cada frame
	 p = Porcentagem de degradacao, no intervalo [0, 1]
	 d = Duracao de cada artefato, em frames
	 dist = Tipo de distribuicao para geracao da duracao do artefato
	 */
	int d;
	double p;
	p = set->percent;
	d = set->duration;

	srand(time(NULL));
	list<Raffle> vetor;
	list<Raffle>::iterator it;
	int j, i = round(f*w*h*p);
	//printf("p=%d \n",  i);
	for(; i >= 0;){
		Raffle *s = new Raffle();
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

