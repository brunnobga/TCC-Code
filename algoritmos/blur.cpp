#include <iostream>
#include <cstdlib>
#include <fstream>
#include <cstring>
#include <list>
#include "commons.h"
#include "blur.h"

#define DEBUG

using namespace std;

typedef unsigned char byte;

int min(int a, int b){ return a < b ? a : b; }
int max(int a, int b){ return a > b ? a : b; }
bool comparar(double a, double b){ return a < b ? true : false; }

void blur(byte *originalFrame, byte *bluredFrame, int H, int W, Settings * set){
	int fType = set->blurType;
	int fSize = set->blockSize;
	if(fSize < 3) return;
	int lowerLimit = (fSize-fSize%2)/-2;
	int upperLimit = (fSize-fSize%2)/2;
	double elements = fSize*fSize, result;

	if(fType == AVERAGE){
		/*
		 	1 - Para cada janela
				1.1 - limpar a posicao de memoria destino
				1.2 - acumular o valor de cada vizinho dividido pelo valor da media (fSize*fSize)
		 */
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				result = 0;
				for(int k = lowerLimit; k <= upperLimit; k++)
					for(int m = lowerLimit; m <= upperLimit; m++)
						result += *(originalFrame + max(min(i+k, H-1), 0)*W + max(min(j+m, W-1), 0))/elements;
				*(bluredFrame + i*W + j) = result;
			}
		}
	} else if(fType == MEDIAN){
		/*
		 	1 - Varrer todo o frame
				1.1 - para cada janela fSizexfSize, armazenar os valores em uma lista
				1.2 - ordenar os valores da lista
				1.3 - pegar o valor mediano
				1.4 - copiar o valor mediano para a vizinhanca
				1.5 - limpar a lista para armazenar os valores da proxima janela
		 */
		list<unsigned char> median;
		list<unsigned char>::iterator it;
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				for(int k = lowerLimit; k <= upperLimit; k++){
					for(int m = lowerLimit; m <= upperLimit; m++){
						// Adicionar o valor do frame original a lista
						median.push_back(*(originalFrame + max(min(i+k, H-1), 0)*W + max(min(j+m, W-1), 0)));
					}
				}
				// Ordenar a lista
				median.sort(comparar);
				it = median.begin();
				// Obter posicao do valor mediano
				for(int k = 0; k < fSize*fSize/2; k++)
					it++;
				// Copiar o valor mediano para a vizinhanca
				for(int k = lowerLimit; k <= upperLimit; k++)
					for(int m = lowerLimit; m <= upperLimit; m++)
						*(bluredFrame + max(min(i+k, H-1), 0)*W + max(min(j+m, W-1), 0)) = *it;
				// Limpar a lista para a proxima janela
				median.clear();
			}
		}
	} else if(fType == LAPLACIAN){
		printf("To be implemented!\n");
	}
}
