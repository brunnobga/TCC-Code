#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <getopt.h>
#include <fstream>
#include <list>
#include "commons.h"

#define DEBUG_INPUT
#define DEBUG_OUTPUT

using namespace std;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"output", required_argument, 0, 'o'},
		{"size", required_argument, 0, 's'},
		{"blur", required_argument, 0, 'b'},
		{"window", required_argument, 0, 'w'},
	};

static char short_options[] = "i:o:s:b:w:";

#define DURATIONDIST 0
#define FRAMEDIST 1

#include "blur.h"

class FilterTool{
private:
	int artifactType, frameWidth, frameHeight, frameTotal, frameSize, blockSize, levels[32], opt_index, c;
	char *inputFileName, *outputFileName, *tmp;
	ifstream input;
	ofstream output;
	byte * frame, *outframe;
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
				case 'b':
					if(strcmp(optarg, "average") == 0) set.blurType = 1;
					else if(strcmp(optarg, "median") == 0) set.blurType = 2;
					else printf("Argumento invalido para -b...\n"), exit(1);
					break;
				case 'w':
					set.blockSize = atoi(optarg);
					blockSize =  set.blockSize;
					if(set.blockSize == 0) printf("Argumento invalido para -w...\n"), exit(1);
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

	void performFiltering(){
		for(int fc = 1; fc <= frameTotal; fc++){
			input.read((char*)frame, frameSize);
			memcpy(outframe, frame, frameSize);
			
			blur(frame, outframe, frameHeight, frameWidth, &set);
			
			output.write((char*) outframe, frameSize);
			input.read((char*) frame, frameSize/2);
			output.write((char*) frame, frameSize/2);
			#ifdef DEBUG_OUTPUT
				printf("At %3d\n", fc);
			#endif
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

}; /*end of class FilterTools*/

int main(int argc, char* argv[]){

	//1. Process options
	//2. verify if there are enough argumentes
	FilterTool f(argc, argv);

	//3. open IO and count number of frames
	f.performIO();

	//5 read Y component
	//6. apply artifacts to Y component
	//7. write Y component and copy UV to output
	f.performFiltering();

	f.closeIO();
	return 0;
} // blur
