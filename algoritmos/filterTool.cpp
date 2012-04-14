#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <getopt.h>
#include <fstream>
#include <list>
#include "dctTools.h"
#include "raffleTools.h"
#define DEBUG

using namespace std;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"output", required_argument, 0, 'o'},
		{"size", required_argument, 0, 's'},
		{"artifact", required_argument, 0, 'a'},
		{"window", required_argument, 0, 'w'}
	};

static char short_options[] = "i:o:s:a:w:";

void blockFilter();

void blurFilter();

int main(int argc, char* argv[]){
	int artifactType, frameWidth, frameHeight, frameTotal, frameSize, opt_index, c;
	char *inputFileName, *outputFileName, *tmp;
	ifstream input;
	ofstream output;
	byte * frame;
	list<Sorteio> pixelList;
	Sorteio pixel;

	/*1. Argument parsing*/
	while((c = getopt_long(argc, argv, short_options, long_options, &opt_index)) != -1){
		switch(c){
			case 'i':
				inputFileName = (char*)malloc(strlen(optarg)+1); 
				strcpy(inputFileName, optarg);
				break;
			case 'o':
				outputFileName = (char*)malloc(strlen(optarg)+1); 
				strcpy(outputFileName, optarg);
				break;
			case 's':
				tmp = strtok(optarg, "x");
				if(tmp == NULL) printf("Argumento invalido para -s...\n"), exit(1);
				frameWidth = atoi(tmp);
				tmp = strtok(NULL, "x");
				if(tmp == NULL) printf("Argumento invalido para -s...\n"), exit(1);
				frameHeight = atoi(tmp);
				frameSize = frameHeight*frameWidth;
				frame = (byte*)malloc(frameSize); //TODO free memory
				break;
			case 'a':
				printf("Argumento -a: %s\n", optarg);
				break;
			case 'w':
				printf("Argumento -w: %s\n", optarg);
				break;
			default:
				break;
		}
	}
	/*1. end*/

#ifdef DEBUG
	printf("Input: %s\n", inputFileName);
	printf("Output: %s\n", outputFileName);
	printf("Frame W: %d\n", frameWidth);
	printf("Frame H %d\n", frameHeight);
#endif

	//TODO 2.verify if there are enough arguments

	/*3. Opening file and counting number of frames*/
	input.open(inputFileName, ifstream::binary);
	output.open(outputFileName, ofstream::binary);
	if(input.eof() || input.fail()) printf("Arquivo inexistente: %s\n", inputFileName), exit(2);
	input.seekg(0, ios::end);
	frameTotal = input.tellg();
	frameTotal /= frameSize*1.5;
	input.seekg(0, ios::beg);
	free(inputFileName);
	free(outputFileName);
	/*3. end*/

#ifdef DEBUG
	printf("FrameTotal: %d\n", frameTotal);
#endif

	//TODO 4.process artifact arguments and raffle pixels

	for(int fc = 1; fc <= frameTotal; fc++){
		//5. read Y component
		input.read((char*)frame, frameSize);

		//TODO apply artifacts to Y component
		if(artifactType == 0){
		}
		else{continue;}

		/*7. write Y component to output and copy UV to output*/
		output.write((char*) frame, frameSize);
		input.read((char*) frame, frameSize/2);
		output.write((char*) frame, frameSize/2);
		/*7. end*/
	}

	input.close();
	output.close();

	return 0;
}
