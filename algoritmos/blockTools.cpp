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
		{"window", required_argument, 0, 'w'},
		{"percentage", required_argument, 0, 'p'},
		{"levelsdct", required_argument, 0, 'l'},
		{"rafflelist", required_argument, 0, 'r'}
	};

static char short_options[] = "i:o:s:w:p:l:r:";

bool compara(Raffle s1, Raffle s2){
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
}

#include "dctTools.h"

#define DURATIONDIST 0
#define FRAMEDIST 1

class FilterTool{
private:
	int artifactType, frameWidth, frameHeight, frameTotal, frameSize, blockSize, levels[32], opt_index, c;
	char *inputFileName, *outputFileName, *raffleFileName, *tmp;
	bool full_flag;
	ifstream input, raffleFile;
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
				case 'w':
					set.blockSize = atoi(optarg);
					blockSize =  set.blockSize;
					if(set.blockSize == 0) printf("Argumento invalido para -w...\n"), exit(1);
					break;
				case 'p':
					set.percent = atof(optarg);
					if(set.percent <= 0) printf("Argumento invalido para -p...\n"), exit(1);
					break;
				case 'l':
					parseLevels(optarg);
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

	void blockFilter(int fc){
		Raffle current;
		if(full_flag){
			int i, j;
			for(i = 0; i < frameHeight/blockSize; i++){
				for(j = 0; j < frameWidth/blockSize; j++){
					blockage(frame + i*frameWidth*blockSize + j*blockSize,
							outframe + i*frameWidth*blockSize + j*blockSize, 
							frameWidth, 
							&set);
				}
			}
		} else {
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
	}

	void performFiltering(){
		for(int fc = 1; fc <= frameTotal; fc++){
			input.read((char*)frame, frameSize);
			memcpy(outframe, frame, frameSize);

			blockFilter(fc);

			output.write((char*) outframe, frameSize);
			input.read((char*) frame, frameSize/2);
			output.write((char*) frame, frameSize/2);
			#ifdef DEBUG_OUTPUT
				printf("At %3d\n", fc);
			#endif
		}
	}

	void generateFullRaffle(){
		int i, j, k;
		for(i = 1; i <= frameTotal; i++){
			for(j = 0; j < frameHeight; j++){
				for(k = 0; k < frameWidth; k++){
					Raffle *tmp = new Raffle();
					tmp->f = i;
					tmp->x = j;
					tmp->y = k;
					pixelList.push_back(*tmp);
				}
			}
		}
	}

	void readRaffleList(){
		int tmpF, tmpX, tmpY;
		if(full_flag)
			return;
		while(raffleFile >> tmpF >> tmpY >> tmpX){
			Raffle *tmpRaffle = new Raffle();
			(*tmpRaffle).f = tmpF;
			(*tmpRaffle).x = tmpX;
			(*tmpRaffle).y = tmpY;
			pixelList.push_back(*tmpRaffle);
		}
		pixelList.sort(compara);
		#ifdef DEBUG_RAFFLE
			list<Raffle>::iterator it;
			printf("RAFFLE RESULT:\n");
			for(it = pixelList.begin(); it != pixelList.end(); it++){
				printf("F %d X %d Y %d\n", it->f, it->x, it->y);
			}
		#endif
	}

	void performIO(){
		input.open(inputFileName, ifstream::binary);
		if(!full_flag)
			raffleFile.open(raffleFileName, ifstream::binary);
		output.open(outputFileName, ofstream::binary);
		if(input.eof() || input.fail()) printf("Arquivo inexistente: %s\n", inputFileName), exit(2);
		if(!full_flag)
			if(raffleFile.eof() || raffleFile.fail()) printf("Arquivo inexistente: %s\n", raffleFileName), exit(2);
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

	//4. process artifact arguments and raffle pixels
	f.readRaffleList();

	//5 read Y component
	//6. apply artifacts to Y component
	//7. write Y component and copy UV to output
	f.performFiltering();

	f.closeIO();
	return 0;
}
