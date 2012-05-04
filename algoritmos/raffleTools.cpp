#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <getopt.h>
#include <fstream>
#include <list>
#include "commons.h"
#include <cmath>
#include <iostream>

/*
 ./raffleTool -o output.rff | -f uniform	-r a,b	| -d constant 	-u a
 							|						| -d uniform 	-u a,b
							|						| -d triangular	-u a,b,c
							|
 							| -f triangular	-r a,b,c	| -d constant	-u a
							|							| -d uniform	-u a,b
							|							| -d triangular	-u a,b,c
							| -f packets?	-
 */
using namespace std;

#define FRAMEDIST 0
#define DURATIONDIST 1

#define DEBUG_INPUT 0

static struct option long_options[] = 
	{
		{"output", required_argument, 0, 'o'},
		{"framedist", required_argument, 0, 'f'},
		{"frame", required_argument, 0, 'r'},
		{"durationdist", required_argument, 0, 'd'},
		{"duration", required_argument, 0, 'u'},
		{"elements", required_argument, 0, 'e'},
		{"total", required_argument, 0, 't'},
		{"size", required_argument, 0, 's'},
	};

static char short_options[] = "o:f:r:d:u:e:s:t:";

bool compara(Raffle s1, Raffle s2){
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

class RaffleTemp{
private:
	int c, opt_index, elements, width, height, numberFrames;
	Distribution frame, duration;
	char *inputFileName, *outputFileName, *tmp;
	ofstream output;

public:
	RaffleTemp(int argc, char* argv[]){
		while((c = getopt_long(argc, argv, short_options, long_options, &opt_index)) != -1){
			switch(c){
				case 'o':
					outputFileName = (char*)malloc(strlen(optarg)+1); 
					strcpy(outputFileName, optarg);
					break;
				case 'f':
					if(strcmp(optarg, "uniform") == 0) frame.type = UNIFORM;
					else if(strcmp(optarg, "triangular") == 0) frame.type = TRIANGULAR;
					else printf("Argumento invalido para -f...\n"), exit(1);
					break;
				case 'r':
					parseDistParams(optarg, FRAMEDIST);
					break;
				case 'd':
					if(strcmp(optarg, "constant") == 0) duration.type = CONSTANT;
					else if(strcmp(optarg, "uniform") == 0) duration.type = UNIFORM;
					else if(strcmp(optarg, "triangular") == 0) duration.type = TRIANGULAR;
					else printf("Argumento invalido para -d...\n"), exit(1);
					break;
				case 'u':
					parseDistParams(optarg, DURATIONDIST);
					break;
				case 'e':
					elements = atoi(optarg);
					if(elements == 0) printf("Argumento invalido para -e...\n"), exit(2);
					break;
				case 's':
					tmp = strtok(optarg, "x");
					if(tmp == NULL) printf("Argumento invalido para -s...\n"), exit(1);
					width = atoi(tmp);
					tmp = strtok(NULL, "x");
					if(tmp == NULL) printf("Argumento invalido para -s...\n"), exit(1);
					height = atoi(tmp);
					break;
				case 't':
					numberFrames = atoi(optarg);
					if(numberFrames == 0) printf("Argumento inválido para -t...\n"), exit(2);
					break;
				default:
					break;
			}
		}

		#ifdef DEBUG_INPUT
			printf("\n");
			printf("|= INPUT ======================================\n");
			printf("|  Output: \t\t\t%s\n", outputFileName);
			printf("|  Dist Frame: \t\t\t%s\n", frame.type == UNIFORM ? "Uniform": frame.type == TRIANGULAR ? "Triangular": "nada");
			printf("|  Frame params: \t\t[%d,%d,%d]\n", frame.a, frame.b, frame.c);
			printf("|  Dist Duration: \t\t%s\n", duration.type == CONSTANT ? "Constant" : duration.type == UNIFORM ? "Uniform": duration.type == TRIANGULAR ? "Triangular" : "nada");
			printf("|  Duration params: \t\t[%d,%d,%d]\n", duration.a, duration.b, duration.c);
			printf("|  Elements \t\t\t%d\n", elements);
			printf("|  W x H \t\t\t%dx%d\n", width, height);
			printf("|  Frames \t\t\t%d\n", numberFrames);
			printf("|==============================================\n");
			printf("\n");
		#endif

	//TODO verify if there are enough arguments

	}

	void parseDistParams(char * arg, int dist){
		tmp = strtok(arg, ",");
		if(dist == FRAMEDIST)  frame.a = atoi(tmp);
		else if(dist == DURATIONDIST) duration.a = atoi(tmp);
		tmp = strtok(NULL, ",");
		if(tmp != NULL){
			if(dist == FRAMEDIST) frame.b = atoi(tmp);
			else if(dist == DURATIONDIST) duration.b = atoi(tmp);
			tmp = strtok(NULL, ",");
		}
		if(tmp != NULL){
			if(dist == FRAMEDIST) frame.c = atoi(tmp);
			else if(dist == DURATIONDIST) duration.c = atoi(tmp);
			tmp = strtok(NULL, ",");
		}
		if(tmp != NULL){
			if(dist == FRAMEDIST) printf("Excesso de parametros para -r...\n"), exit(2);
			else if(dist == DURATIONDIST) printf("Excesso de parametros para -u...\n"), exit(2);
		}
	}

	void raffle(){
		int dur;
		srand(time(NULL));
		list<Raffle> vetor;
		list<Raffle>::iterator it;
		int j, i = elements;
		for(; i > 0;){
			Raffle *s = new Raffle();
			if(duration.type == CONSTANT) dur = duration.a;
			else if(duration.type == TRIANGULAR) dur = raffleDuration();
			(*s).f = raffleFrame(dur);
			(*s).x = rand()%height;
			(*s).y = rand()%width;
			//printf("F = %d X = %d Y = %d", (*s).f, (*s).x, (*s).y);
			for(j = 0; j < dur && i > 0; j++){
				if(((*s).f) < numberFrames && ((*s).f) >= 0){
					vetor.push_back(*s);
					i--;
				}
				(*s).f++;
			}
		}
		vetor.sort(compara);
		for(it = vetor.begin(); it != vetor.end(); ++it)
			output << (*it).f << " " << (*it).x << " " << (*it).y << endl;
		return;
	}


	int triangular(int a, int b, int c){
		/* Distribuição triangular {{{*/

		/* Sorteio de variavel numa distribuicao triangular
		   baseada em sorteio de valores numa distribuicao uniforme
Formula: dado u -> (0, 1)
x = a + sqrt(u * (b - a) * (c - a)), for 0 < u < fc
x = b - sqrt((1 - u) * (b - a) * (b - c)), for fc <= u < 1
		 */
		double fc = (double)(c - a)/(b - a);
		double u;
		int t;

		do{
			u = ((double)rand())/(RAND_MAX + 1.);
		}while(u < 0 || u > 1);
		if(fc > u) t =  (int)(a + sqrt(u*(b-a)*(c-a)));
		else t =  (int)(b - sqrt((1-u)*(b-a)*(b-c)));	
		return t;
		/*}}}*/
	}

	int raffleFrame(int d){
		/* Sorteio do frame {{{*/
		if(frame.type == UNIFORM){
			return frame.a + 2 - d + rand()%(frame.b-frame.a + d - 1);
		}
		else{
			if(frame.type == TRIANGULAR){
				// verificar parametros [a, b, c]
				return triangular(frame.a, frame.b, frame.c);
			}
		}
		/*}}}*/
	}

	int raffleDuration(){
		/* Raffle Duration {{{*/

		if(duration.type == CONSTANT){
			return duration.a;
		} else if(duration.type == NORMAL){
			// TODO
			return duration.a;
		} else if(duration.type == TRIANGULAR){
			return triangular(duration.a, duration.b, duration.c);
		} else if(duration.type == UNIFORM){
			// uniforme no intervalo [a, b]
			return ((double) rand() / (RAND_MAX+1.)) * (duration.b - duration.a + 1) + duration.a;
		}
		/*}}}*/
	}


	void performIO(){
		output.open(outputFileName, ios::binary);
		// TODO Checar se arquivo ja existe. Inserir caracter ate que o resultado seja novo.
		free(outputFileName);
	}

	void closeIO(){
		output.close();
	}

}; /*end of class RaffleTools*/

int main(int argc, char* argv[]){
	// 1. Process options
	// 2. verify if there are enough arguments
	RaffleTemp r(argc, argv);

	// 3. Open IO
	r.performIO();

	// 4. Execute raffle
	r.raffle();

	// 5. Close IO
	r.closeIO();
	return 0;
}
