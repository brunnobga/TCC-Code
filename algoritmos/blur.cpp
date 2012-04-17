#include <iostream>
#include <cstdlib>
#include <fstream>
#include <cstring>
#include <list>

#define DEBUG

#define LAPLACIAN 0
#define AVERAGE 1
#define MEDIAN 2

using namespace std;

typedef unsigned char byte;

int min(int a, int b){ return a < b ? a : b; }
bool compare(double a, double b){ return a < b ? true : false; }

void blur(byte *originalFrame, byte *bluredFrame, int H, int W, int fType, int fSize){
	int lowerLimit = (fSize-fSize%2)/-2;
	int upperLimit = (fSize-fSize%2)/2;
	int elements = fSize*fSize;

	if(fType == AVERAGE){
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				// filters 3x3 or bigger
				*(bluredFrame + i*W + j) = 0;
				for(int k = lowerLimit; k <= upperLimit; k++)
					for(int m = lowerLimit; m <= upperLimit; m++)
						*(bluredFrame + i*W + j) += *(originalFrame + (i+k)*W +(j+m))/elements;
			}
		}
	} else if(fType == MEDIAN){
		list<double> median;
		list<double>::iterator it;
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				// filters 3x3 or bigger
				for(int k = (fSize-fSize%2)/-2; k <= (fSize-fSize%2)/2; k++){
					for(int m = (fSize-fSize%2)/-2; m <= (fSize-fSize%2)/2; m++){
						median.push_back((double)*(originalFrame+(i+k)*W+(j+m)));
					}
				}
				if(fType == MEDIAN){
					median.sort(compare);
					it = median.begin();
					for(int k = 0; k < fSize*fSize/2; k++)
						it++;
					*(bluredFrame + i*W + j) = *it;
					median.clear();
				}
			}
		}
	

	} else if(fType == LAPLACIAN){
		printf("To be implemented!\n");
	}
}
