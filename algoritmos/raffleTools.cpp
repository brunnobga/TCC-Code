#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <getopt.h>
#include <fstream>
#include <list>
#include "commons.h"
#include <cmath>
#include <iostream>
#include <list>

/*
 ./raffle -o output.rff 
 		  -d constant | triangular | normal | uniform
		  -p 	a		  a,b,c		  a,b		a,b
		  -n number
 */

using namespace std;

#define DISTDURATION 0
#define DISTVALUES 1

#define DEBUG_INPUT 0

static struct option long_options[] = 
	{
		{"output", required_argument, 0, 'o'},
		{"dist", required_argument, 0, 'd'},
		{"params", required_argument, 0, 'p'},
		{"elements", required_argument, 0, 'e'},
		{"durationdist", required_argument, 0, 'u'},
		{"durationparams", required_argument, 0, 'r'},
		{"help", no_argument, 0, 'h'}
	};

static char short_options[] = "o:d:p:e:u:r:h:";

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

class RaffleTool{
private:
	int c, opt_index, elements;
	list<Distribution> dist;
	list<Distribution>::iterator distIt;
	char *inputFileName, *outputFileName, *tmp;
	ofstream output;
	Distribution *distTemp, *duration;
	bool help;

public:
	RaffleTool(int argc, char* argv[]){
		help = false;
		elements = 0;
		srand(time(NULL));
		while((c = getopt_long(argc, argv, short_options, long_options, &opt_index)) != -1){
			switch(c){
				case 'o':
					outputFileName = (char*)malloc(strlen(optarg)+1); 
					strcpy(outputFileName, optarg);
					break;
				case 'd':
					distTemp = new Distribution();
					distTemp->type = 0;
					distTemp->a = 0;
					distTemp->b = 0;
					distTemp->c = 0;
					if(strcmp(optarg, "constant") == 0) distTemp->type = CONSTANT;
					else if(strcmp(optarg, "uniform") == 0) distTemp->type = UNIFORM;
					else if(strcmp(optarg, "normal") == 0) distTemp->type = NORMAL;
					else if(strcmp(optarg, "triangular") == 0) distTemp->type = TRIANGULAR;
					else printf("Argumento invalido para -d...\n"), exit(2);
					break;
				case 'p':
					parseDistParams(optarg, DISTVALUES);
					dist.push_back(*distTemp);
					break;
				case 'e':
					elements = atoi(optarg);
					if(elements == 0) printf("Argumento invalido para -e...\n"), exit(2);
					break;
				case 'u':
					duration = new Distribution();
					duration->a = 0;
					duration->b = 0;
					duration->c = 0;
					if(strcmp(optarg, "constant") == 0) duration->type = CONSTANT;
					else if(strcmp(optarg, "uniform") == 0) duration->type = UNIFORM;
					else if(strcmp(optarg, "normal") == 0) duration->type = NORMAL;
					else if(strcmp(optarg, "triangular") == 0) duration->type = TRIANGULAR;
					else printf("Argumento invalido para -u...\n"), exit(2);
					break;
				case 'r':
					parseDistParams(optarg, DISTDURATION);
					break;
				case 'h':
					help = true;
					printf("  raffleTools\n");
					printf("    Exemplo: raffle -o raffleout.rff -u normal -r 2,6 -e 1000 -d uniform -p 1,5 -d constant -p 3 -d triangular -p 3,20,10\n");
					printf("\n");
					printf("\t--output \t\t-o\tnome do arquivo a ser criado/sobrescrito.\n");
					printf("\t--dist \t\t\t-d\ttipo da [distribuição], cria nova coluna.\n");
					printf("\t--params \t\t-p\t[parâmetros] da distribuição.\n");
					printf("\t--elements \t\t-e\tnúmero de elementos (linhas) a serem gerados.\n");
					printf("\t--durationdist \t\t-u\ttipo da [distribuição] da duração de cada elemento.\n");
					printf("\t--durationparams \t-r\t[parâmetros] da [distribuição] da duração.\n");
					printf("\n");
					printf("  [distribuição]:\n");
					printf("\tconstant : \tvalor constante.\n");
					printf("\tuniform : \tdistribuição uniforme dentro do intervalo [a, b].\n");
					printf("\tnormal : \tdistribuição normal dadas média e variância[m, v].\n");
					printf("\ttriangular : \tdistribuição triangular no intervalo [a, b] com pico no ponto c.\n");
					printf("\n");
					printf("  [parâmetros]:\n");
					printf("\tconstant : \tc\n");
					printf("\tuniform : \ta,b\n");
					printf("\tnormal : \tm,v\n");
					printf("\ttriangular : \ta,b,c\n");
					printf("\n");
					break;
				default:
					break;
			}
		}

		#ifdef DEBUG_INPUT
			printf("\n");
			printf("|= INPUT ======================================\n");
			printf("|  Output: %s\n", outputFileName);
			printf("|  Elements: %d\n", elements);
			for(distIt = dist.begin(); distIt != dist.end(); ++distIt){
				printf("|  Dist: %s [%d,%d,%d]\n", (*distIt).type == CONSTANT ? "Constant" : (*distIt).type == UNIFORM ? "Uniform": (*distIt).type == TRIANGULAR ? "Triangular" : "Normal", (*distIt).a, (*distIt).b, (*distIt).c);
			}
			printf("|  Dur: %s [%d,%d,%d]\n", duration->type == CONSTANT ? "Constant" : duration->type == UNIFORM ? "Uniform": duration->type == TRIANGULAR ? "Triangular" : "Normal", duration->a, duration->b, duration->c);
			printf("|==============================================\n");
			printf("\n");
		#endif

	//TODO verify if there are enough arguments

	}

	bool askedHelp(){
		return help;
	}

	void parseDistParams(char * arg, int OPTION){
		tmp = strtok(arg, ",");
		if(OPTION == DISTVALUES) distTemp->a = atoi(tmp);
		else if(OPTION == DISTDURATION) duration->a = atoi(tmp);
		tmp = strtok(NULL, ",");
		if(tmp != NULL){
			if(OPTION == DISTVALUES) distTemp->b = atoi(tmp);
			else if(OPTION == DISTDURATION) duration->b = atoi(tmp);
			tmp = strtok(NULL, ",");
		}
		if(tmp != NULL){
			if(OPTION == DISTVALUES) distTemp->c = atoi(tmp);
			else if(OPTION == DISTDURATION) duration->c = atoi(tmp);
			tmp = strtok(NULL, ",");
		}
		if(tmp != NULL) printf("Excesso de parametros para -p...\n"), exit(2);
	}

	int triangular(int a, int b, int c){
		c++; // correcao do pico
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
	
	int normal(int mean, int variance){
		// box-mueller method. m.ean v.ariance
		double z0, u, v, s;
		do {
			u = ((double)rand()/((double)(RAND_MAX+1.)));
			u = 2*u - 1;
			v = ((double)rand()/((double)(RAND_MAX+1.)));
			v = 2*v - 1;
			s = pow(u, 2)+pow(v, 2);
		} while(s == 0 || s >= 1);
		z0 = u*sqrt(-2*log(s)/s);
		return (int)(mean+sqrt(variance)*z0);
	}

	int uniform(int a, int b){
		return rand()/(RAND_MAX+1.)*(b-a)+a;
	}
	
	void raffle(){
		int dur = 9, i = 0;
		int values[dist.size()];
		for(; elements;){
			// sortear duração
			if(duration->type == CONSTANT) dur = duration->a;
			else if(duration->type == UNIFORM) dur = uniform(duration->a, duration->b);
			else if(duration->type == NORMAL) dur = normal(duration->a, duration->b);
			else if(duration->type == TRIANGULAR) dur = triangular(duration->a, duration->b, duration->c);
			// sortear um elemento (linha completa)
			for(i = 0, distIt = dist.begin(); distIt != dist.end() && elements; ++i, ++distIt){
				//do{
					if((*distIt).type == CONSTANT) values[i] = (*distIt).a;
					if(i == 0){
						if((*distIt).type == UNIFORM) values[i] = uniform((*distIt).a+1-dur, (*distIt).b);
						else if((*distIt).type == NORMAL) values[i] = normal((*distIt).a+1-dur, (*distIt).b);
						else if((*distIt).type == TRIANGULAR) values[i] = triangular((*distIt).a+1-dur, (*distIt).b, (*distIt).c);
					} else {
						if((*distIt).type == UNIFORM) values[i] = uniform((*distIt).a, (*distIt).b);
						else if((*distIt).type == NORMAL) values[i] = normal((*distIt).a, (*distIt).b);
						else if((*distIt).type == TRIANGULAR) values[i] = triangular((*distIt).a, (*distIt).b, (*distIt).c);
					}
				//}while(values[i]+dur >= (*distIt).b);
			}
			// output
			bool exceeded = false;
			for(int j = 0; j < dur && elements && !exceeded; --elements, j++){
				for(i = 0, distIt = dist.begin(); !exceeded && i < dist.size(); ++i, ++distIt){
					if(i == 0){
						if((*distIt).a <= values[i]+j && (*distIt).b > values[i]+j) output << values[i]+j << " ";
						else{
							exceeded = true;
							elements++;
						}
					}
					else {
						output << values[i] << " ";
					}
				}
				if(!exceeded) output << endl;
			}
		}
	}

	void performIO(){
		output.open(outputFileName, ios::binary);
		// TODO Checar se arquivo ja existe. Inserir caracter ate que o resultado seja novo.
		free(outputFileName);
	}

	void closeIO(){
		output.close();
	}
	int maxElements(){
		if(dist.empty()) return 0;
		int totalElements = 1;
		for(distIt = dist.begin(); distIt != dist.end(); ++distIt){
			if((*distIt).type != CONSTANT){
				totalElements *= ((*distIt).b-(*distIt).a);
			}
		}
		return totalElements;
	}
}; /*end of class RaffleTools*/

int main(int argc, char* argv[]){
	// 1. Process options
	// 2. verify if there are enough arguments
	RaffleTool r(argc, argv);
	if(!r.askedHelp()){
		// 3. Open IO
		r.performIO();
	
		// 4. Raffle values
		r.raffle();

		printf("maximo = %d\n\n", r.maxElements());

		// 5. Close IO
		r.closeIO();
	}
	return 0;
}
