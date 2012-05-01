#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <getopt.h>
#include <fstream>
#include <list>
#include "commons.h"

#define DEBUG_INPUT
#define DEBUG_RAFFLE
#define DEBUG_OUTPUT

using namespace std;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"output", required_argument, 0, 'o'},
		{"size", required_argument, 0, 's'},
		{"artifact", required_argument, 0, 'a'},
		{"window", required_argument, 0, 'w'},
		{"percentage", required_argument, 0, 'p'},
		{"durationdist", required_argument, 0, 'd'},
		{"blur", required_argument, 0, 'b'},
		{"levelsdct", required_argument, 0, 'l'},
		{"paramduration", required_argument, 0, 'u'},
		{"paramframe", required_argument, 0, 'r'},
		{"framedist", required_argument, 0, 'f'}
	};

static char short_options[] = "i:o:s:a:w:p:d:b:l:u:f:r:";

#include "dctTools.h"
#include "raffleTools.h"
#include "blurTools.h"

#define DURATIONDIST 0
#define FRAMEDIST 1

class FilterTool{
private:
	int artifactType, frameWidth, frameHeight, frameTotal, frameSize, blockSize, levels[32], opt_index, c;
	char *inputFileName, *outputFileName, *tmp;
	ifstream input;
	ofstream output;
	byte * frame, *outframe;
	list<Raffle> pixelList;
	Settings set;

public:
	FilterTool(int argc, char* argv[]){
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
					outframe = (byte*)malloc(frameSize); //TODO free memory	
					break;
				case 'a':
					if(strcmp(optarg, "block") == 0) artifactType = 0;
					else if(strcmp(optarg, "blur") == 0) artifactType = 1;
					else printf("Argumento invalido para -a...\n"), exit(1);
					break;
				case 'w':
					set.blockSize = atoi(optarg);
					blockSize =  set.blockSize;
					if(set.blockSize == 0) printf("Argumento invalido para -w...\n"), exit(1);
					break;
				case 'p':
					set.percent = atof(optarg);
					if(set.percent <= 0) printf("Argumento invalido para -p...\n"), exit(1);
					break;
				case 'd':
					if(strcmp(optarg, "constant") == 0) set.durationDist.type = CONSTANT;
					else if(strcmp(optarg, "uniform") == 0) set.durationDist.type = UNIFORM;
					else if(strcmp(optarg, "triangular") == 0) set.durationDist.type = TRIANGULAR;
					else printf("Argumento invalido para -d...\n"), exit(1);
					break;
				case 'u':
					parseDistParams(optarg, DURATIONDIST);
					break;
				case 'b':
					if(strcmp(optarg, "average") == 0) set.blurType = 1;
					else if(strcmp(optarg, "median") == 0) set.blurType = 2;
					else printf("Argumento invalido para -b...\n"), exit(1);
					break;
				case 'l':
					parseLevels(optarg);
					break;
				case 'f':
					if(strcmp(optarg, "uniform") == 0) set.frameDist.type = UNIFORM;
					else if(strcmp(optarg, "triangular") == 0) set.frameDist.type = TRIANGULAR;
					else printf("Argumento invalido para -f...\n"), exit(1);
					break;
				case 'r':
					parseDistParams(optarg, FRAMEDIST);
					break;
				default:
					break;
			}
		}

		#ifdef DEBUG_INPUT
			printf("Input: %s\n", inputFileName);
			printf("Output: %s\n", outputFileName);
			printf("Frame W: %d\n", frameWidth);
			printf("Frame H %d\n", frameHeight);
			printf("Artifact type %d\n", artifactType);
			printf("Block Size %d\n", set.blockSize);
			printf("Percentage %lf\n", set.percent);
			printf("BlurType %d\n", set.blurType);
			printf("Levels Size %d\n", set.removalsSize);
			printf("Duration dist %d\n", set.durationDist.a);
		#endif

	//TODO verify if there are enough arguments

	}

	void parseLevels(char * arg){
		int i;
		tmp = strtok(arg, ",");
		levels[0] = atoi(tmp);
		tmp = strtok(NULL, ",");
		for(i = 1; tmp != NULL; i++, tmp = strtok(NULL, ",")){
			levels[i] = atoi(tmp);
		}
		set.removalsSize = i;
		set.removals = levels;
	}

	void parseDistParams(char * arg, int dist){
		printf("dist %d arg %s\n", dist, arg);
		tmp = strtok(arg, ",");
		if(dist == FRAMEDIST) set.frameDist.a = atoi(tmp);
		else if(dist == DURATIONDIST){
			set.durationDist.a = atoi(tmp);
			set.duration = set.durationDist.a;
		}
		tmp = strtok(NULL, ",");
		if(tmp != NULL){
			if(dist == FRAMEDIST) set.frameDist.b = atoi(tmp);
			else if(dist == DURATIONDIST) set.durationDist.b = atoi(tmp);
			tmp = strtok(NULL, ",");
		}
		if(tmp != NULL){
			if(dist == FRAMEDIST) set.frameDist.c = atoi(tmp);
			else if(dist == DURATIONDIST) set.durationDist.c = atoi(tmp);
			tmp = strtok(NULL, ",");
		}
		if(tmp != NULL){
			if(dist == FRAMEDIST) printf("Excesso de parametros para -u...\n"), exit(1);
			else if(dist == DURATIONDIST) printf("Excesso de parametros para -u...\n"), exit(1);
		}
	}

	void performIO(){
		input.open(inputFileName, ifstream::binary);
		output.open(outputFileName, ofstream::binary);
		if(input.eof() || input.fail()) printf("Arquivo inexistente: %s\n", inputFileName), exit(2);
		input.seekg(0, ios::end);
		frameTotal = input.tellg();
		frameTotal /= frameSize*1.5;
		input.seekg(0, ios::beg);
		free(inputFileName);
		free(outputFileName);
		#ifdef DEBUG_INPUT
			printf("FrameTotal: %d\n", frameTotal);
		#endif
	}

	void closeIO(){
		input.close();
		output.close();
	}

	void processArtifact(){
		printf("Ola!\n");
		if(artifactType == 0){
			pixelList = raffle(frameTotal, frameWidth/set.blockSize, frameHeight/set.blockSize, &set);
			pixelList.sort(sort);
		}
		#ifdef DEBUG_RAFFLE
			list<Raffle>::iterator it;
			printf("RAFFLE RESULT:\n");
			for(it = pixelList.begin(); it != pixelList.end(); it++){
				printf("F %d X %d Y %d\n", it->f, it->x, it->y);
			}
		#endif
	}

	void blockFilter(int fc){
		Raffle current;
		if(pixelList.size() > 0) current = pixelList.front();
		while(current.f == fc && pixelList.size() > 0){
			blockage(frame + current.x*frameWidth*blockSize + current.y*blockSize,
					outframe + current.x*frameWidth*blockSize + current.y*blockSize, 
					frameWidth, 
					&set);
			pixelList.pop_front();
			current = pixelList.front();
		}
	}

	void blurFilter(){
		blur(frame, outframe, frameHeight, frameWidth, &set);
	}

	int min(int a, int b){ return a < b ? a : b; }
	int max(int a, int b){ return a > b ? a : b; }

	void performFiltering(){
		for(int fc = 1; fc <= frameTotal; fc++){
			input.read((char*)frame, frameSize);
			memcpy(outframe, frame, frameSize);

			if(artifactType == 0){
				blockFilter(fc);
			} else{
				blurFilter();
			}

			output.write((char*) outframe, frameSize);
			input.read((char*) frame, frameSize/2);
			output.write((char*) frame, frameSize/2);
			#ifdef DEBUG_OUTPUT
				printf("At %3d\n", fc);
			#endif
		}
	}

}; /*end of class FilterTools*/

int main(int argc, char* argv[]){

	//1. Process options
	//2. verify if there are enough argumentes
	FilterTool f(argc, argv);

	//3. open IO and count number of frames
	f.performIO();

	//4. process artifact arguments and raffle pixels
	f.processArtifact();

	//5 read Y component
	//6. apply artifacts to Y component
	//7. write Y component and copy UV to output
	f.performFiltering();

	f.closeIO();
	return 0;
}
