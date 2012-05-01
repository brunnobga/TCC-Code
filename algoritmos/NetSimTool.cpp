#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <cmath>
#include <getopt.h>
#include <fstream>

#define DEBUG_INPUT
#define DEBUG_OUTPUT

#define L 255

using namespace std;

typedef unsigned char byte;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"output", required_argument, 0, 'o'},
		{"packetloss", required_argument, 0, 'l'},
		{"packetcorruption", required_argument, 0, 'c'}
	};

static char short_options[] = "i:o:l:c:";

class NetSimTool{
private:
	double packetLoss, packetCorruption;
	int c, opt_index;
	char *inputFileName, *outFileName, *tmp;
	ifstream input, output;

public:
	NetSimTool(int argc, char* argv[]){
		while((c = getopt_long(argc, argv, short_options, long_options, &opt_index)) != -1){
			switch(c){
				case 'i':
					inputFileName = (char*)malloc(strlen(optarg)+1); 
					strcpy(inputFileName, optarg);
					break;
				case 'o':
					outFileName = (char*)malloc(strlen(optarg)+1); 
					strcpy(outFileName, optarg);
					break;
				case 'l':
					packetLoss = atof(optarg);
					if(packetLoss <= 0 || packetLoss > 1) printf("Argumento invalido para -l...\n"), exit(1);
					break;
				case 'c':
					packetCorruption = atof(optarg);
					if(packetCorruption <= 0 || packetLoss > 1) printf("Argumento invalido para -l...\n"), exit(1);
					
					break;
				default:
					break;
			}
		}

		#ifdef DEBUG_INPUT
			printf("Input: %s\n", inputFileName);
			printf("Reference: %s\n", refFileName);
			printf("Frame W: %d\n", frameWidth);
			printf("Frame H %d\n", frameHeight);
			printf("Metric Type %d\n", metricType);
			printf("Block Size %d\n", window);
		#endif

	//TODO verify if there are enough arguments

	}
	
	void performIO(){
		input.open(inputFileName, ifstream::binary);
		ref.open(refFileName, ifstream::binary);
		if(input.eof() || input.fail()) printf("Arquivo inexistente: %s\n", inputFileName), exit(2);
		if(ref.eof() || ref.fail()) printf("Arquivo inexistente: %s\n", refFileName), exit(2);
		input.seekg(0, ios::end);
		frameTotal = input.tellg();
		frameTotal /= frameSize*1.5;
		input.seekg(0, ios::beg);
		ref.seekg(0, ios::end);
		if(frameTotal != ref.tellg()/(frameSize*1.5)) printf("Arquivos de tamanho diferente!\n"), exit(2);
		ref.seekg(0, ios::beg);
		free(inputFileName);
		free(refFileName);
		#ifdef DEBUG_INPUT
			printf("FrameTotal: %d\n", frameTotal);
		#endif
	}

	void closeIO(){
		input.close();
		ref.close();
	}

}; /*end of class FilterTools*/

int main(int argc, char* argv[]){

	//1. Process options
	//2. verify if there are enough arguments
	NetSimTool m(argc, argv);

	//3. open IO 
	m.performIO();
	
	//4. Raffle packets and apply 
	

	//5 read Y component and skip 
	//6. metrics to Y component
	m.closeIO();
	return 0;
}
