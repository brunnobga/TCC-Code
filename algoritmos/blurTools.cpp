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
		{"rafflelist", required_argument, 0, 'r'},
		{"help", no_argument, 0, 'h'},
	};

static char short_options[] = "i:o:s:b:w:h:r:";

#define DURATIONDIST 0
#define FRAMEDIST 1

#include "blur.h"

class FilterTool{
private:
	int artifactType, frameWidth, frameHeight, frameTotal, frameSize, blockSize, levels[32], opt_index, c;
	char *inputFileName, *outputFileName, *raffleFileName, *tmp;
	ifstream input, raffleFile;
	ofstream output;
	byte * frame, *outframe;
	Settings set;
	bool help, full_flag;
	list<Raffle> frames;

public:
	FilterTool(int argc, char* argv[]){
		help = false;
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
				case 'r':
					if(strcmp(optarg, "full") == 0){
						full_flag = true;
						break;
					}
					full_flag = false;
					raffleFileName = (char*)malloc(strlen(optarg)+1);
					strcpy(raffleFileName, optarg);
					break;
				case 'h':
					help = true;
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

	bool askedHelp(){
		return help;
	}

	void nextRaffle(int* n, int * d){
		if(frames.empty()){
			*n = 0;
			*d = 0;
			return;
		}
		Raffle a = frames.front();
		frames.pop_front();
		*n = a.f;
		*d = a.x;
		return;
	}

	void performFiltering(){
		int next, duration;
		nextRaffle(&next, &duration);
		for(int fc = 1; fc <= frameTotal; fc++){
			input.read((char*)frame, frameSize);
			memcpy(outframe, frame, frameSize);
			
			printf("!!! %d %d ", next, duration);	
			if(fc >= next && duration == 0)
				nextRaffle(&next, &duration);
			if(full_flag || (duration > 0 && fc >= next)){
				blur(frame, outframe, frameHeight, frameWidth, &set);
				duration--;
			}
			
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
		raffleFile.open(raffleFileName, ifstream::binary);
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

	void readRaffleFile(){
		int tmpB, tmpD;
		while(raffleFile >> tmpB >> tmpD){
			Raffle a;
			a.f = tmpB;
			a.x = tmpD;
			frames.push_back(a);
		}
		printf("list size %d\n", frames.size());
		return;
	}

	void closeIO(){
		input.close();
		raffleFile.close();
		output.close();
	}

}; /*end of class FilterTools*/

int main(int argc, char* argv[]){

	//1. Process options
	//2. verify if there are enough argumentes
	FilterTool f(argc, argv);

	if(!f.askedHelp()){
		//3. open IO and count number of frames
		f.performIO();

		//5 read Y component
		//6. apply artifacts to Y component
		//7. write Y component and copy UV to output
		f.readRaffleFile();
		f.performFiltering();

		f.closeIO();
	}
	return 0;
} // blur
