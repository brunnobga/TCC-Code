#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <getopt.h>
#include <fstream>
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

static char short_options[] = "i:o:s:f:w:";

int main(int argc, char* argv[]){
	int filterType, frameWidth, frameHeight, opt_index, c;
	char *inputFileName, *outputFileName, *tmp;
	ifstream input;
	ofstream output;

	tmp = (char*)malloc(64); 

	//argument parsing
	while((c = getopt_long(argc, argv, short_options, long_options, &opt_index)) != -1){
		switch(c){
			case 'i':
				inputFileName = (char*)malloc(strlen(optarg)+1); //TODO free memory
				strcpy(inputFileName, optarg);
				break;
			case 'o':
				outputFileName = (char*)malloc(strlen(optarg)+1); //TODO free memory
				strcpy(outputFileName, optarg);
				break;
			case 's':
				tmp = strtok(optarg, "x");
				if(tmp == NULL) printf("Argumento invalido para -s...\n"), exit(1);
				frameWidth = atoi(tmp);
				tmp = strtok(NULL, "x");
				if(tmp == NULL) printf("Argumento invalido para -s...\n"), exit(1);
				frameHeight = atoi(tmp);
				break;
			case 'a':
				printf("Argumento -f: %s\n", optarg);
				break;
			case 'w':
				printf("Argumento -w: %s\n", optarg);
				break;
			default:
				break;
		}
	}

	free(tmp);

#ifdef DEBUG
	printf("Input: %s\n", inputFileName);
	printf("Output: %s\n", outputFileName);
	printf("Frame W: %d\n", frameWidth);
	printf("Frame H %d\n", frameHeight);
#endif

	//TODO open file streams

	//TODO process artifact arguments and raffle pixels

	//TODO read Y component

	//TODO apply artifacts to Y component

	//TODO write Y component to output and copy UV to output

	return 0;
}
