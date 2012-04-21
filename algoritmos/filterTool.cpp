#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <getopt.h>
#include <fstream>
#include <list>
#include "commons.h"

#define DEBUG_INPUT
//#define DEBUG_RAFFLE

using namespace std;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"output", required_argument, 0, 'o'},
		{"size", required_argument, 0, 's'},
		{"artifact", required_argument, 0, 'a'},
		{"window", required_argument, 0, 'w'},
		{"percentage", required_argument, 0, 'p'},
		{"duration", required_argument, 0, 'd'},
		{"blur", required_argument, 0, 'b'}
	};

static char short_options[] = "i:o:s:a:w:p:d:b:";

#include "dctTools.h"
#include "raffleTools.h"
#include "blurTools.h"

class FilterTool{
private:
	int artifactType, frameWidth, frameHeight, frameTotal, frameSize, blockSize, opt_index, c;
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
					set.duration = atoi(optarg);
					if(set.duration == 0) printf("Argumento invalido para -d...\n"), exit(1);
					break;
				case 'b':
					if(strcmp(optarg, "average") == 0) set.blurType = 1;
					else if(strcmp(optarg, "median") == 0) set.blurType = 2;
					else printf("Argumento invalido para -b...\n"), exit(1);
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
			printf("Duration %d\n", set.duration);
			printf("BlurType %d\n", set.blurType);
		#endif

	//TODO verify if there are enough arguments

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
		if(artifactType == 0){
			pixelList = raffle(frameTotal, frameWidth/set.blockSize, frameHeight/set.blockSize, &set);
			printf("%d\n", pixelList.size());
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
		//TODO remove this
		int rem[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		set.removals = rem;
		set.removalsSize = 14;
		// end
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
