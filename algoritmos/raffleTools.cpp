#include <cstdlib>
#include <time.h>
#include <cstdio>
#include <cmath>
#include <list>
#include "commons.h"

using namespace std;

bool sort(Raffle s1, Raffle s2){
	/* Ordenação do vetor {{{*/
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
	/*}}}*/
}

int raffleFrame(int frames, Settings *set){
	/* Sorteio do frame {{{*/
	if(set->frameDist == CONSTANTE){
		return (2 - set->duration + rand()%(frames + set->duration - 1));
	else{
		if(set-frameDist == TRIANGULAR){
			// verificar parametros [a, b, c]
			return triangular(set->frameDist->a, set->frameDist->b, set->frameDist->c);
		}
	}
	/*}}}*/
}
int triangular(int a, int b, int c){
	/* Distribuição triangular {{{*/
	
	/* Sorteio de variavel numa distribuicao triangular
	 	baseada em sorteio de valores numa distribuicao uniforme
		Formula: dado u -> (0, 1)
			x = a + sqrt(u * (b - a) * (c - a)), for 0 < u < fc
			x = b - sqrt((1 - u) * (b - a) * (b - c)), for fc <= u < 1
	 */
	double fc = (c - a)/(b - a);
	double u = ((double)rand())/(RAND_MAX + 1.);
	double x;
	if(fc > u) return (int)(a + sqrt(u*(b-a)*(c-a)));
	else if(fc < u) return (int)(b - sqrt((1-u)*(b-a)*(b-c)));
	/*}}}*/
}

int raffleDuration(Settings *set){
/* Raffle Duration {{{*/

	if(set->durationDist->type == CONSTANTE){
		return d;
	} else{
		if(set->durationDist->type == NORMAL){
			// TODO
			return d;
		} else{
			if(set->durationDist->type == TRIANGULAR){
				return triangular(set->durationDist->a, set->durationDist->b, set->durationDist->c);
			}
		}
	}
	/*}}}*/
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
	set->frameDist;

	srand(time(NULL));
	list<Raffle> vetor;
	list<Raffle>::iterator it;
	int j, i = round(f*w*h*p);
	bool constantDurationDist = false;
	if(set->durationDist->type == CONSTANTE){
		d = set->duration;
		constantDurationDist = true;
	}
	for(; i >= 0;){
		Raffle *s = new Raffle();
		// Verificar a distribuição da duração
		if(!constantDurationDist){
			d = raffleDuration(&set);
		}
		// Verificar a distribuição dos frames
		(*s).f = raffleFrame(f, &set);
		(*s).x = rand()%h;
		(*s).y = rand()%w;
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
