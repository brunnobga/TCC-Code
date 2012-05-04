#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <cmath>
#include <getopt.h>
#include <fstream>

#define DEBUG_INPUT
#define DEBUG_OUTPUT

using namespace std;

typedef unsigned char byte;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"output", required_argument, 0, 'o'},
		{"burst", required_argument, 0, 'b'},
		{"duration", required_argument, 0, 'd'},
		{"ts", no_argument, 0, 't'}
	};

static char short_options[] = "i:o:b:d:nt";

static const int ts_size = 188;

class NetSimTool{
private:
	bool normal_flag, ts_flag;
	double burst_mean, burst_dev, duration_mean, duration_dev;
	int c, opt_index, entity_size, total_entities, total_lost;
	char *inputFileName, *outFileName, *tmp;
	ifstream input;
	ofstream output;

public:
	NetSimTool(int argc, char* argv[]){
		normal_flag = false;
		ts_flag = false;
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
				case 'b':
					tmp = strtok(optarg, ",");
					if(tmp == NULL) printf("Argumento invalido para -b...\n"), exit(1);
					burst_mean = atof(tmp);
					if(!normal_flag) break;
					tmp = strtok(NULL, ",");
					if(tmp == NULL) printf("Argumento invalido para -b...\n"), exit(1);
					burst_dev = atof(tmp);
					break;
				case 'd':
					tmp = strtok(optarg, ",");
					if(tmp == NULL) printf("Argumento invalido para -d...\n"), exit(1);
					duration_mean = atof(tmp);
					if(!normal_flag) break;
					tmp = strtok(NULL, ",");
					if(tmp == NULL) printf("Argumento invalido para -d...\n"), exit(1);
					duration_dev = atof(tmp);
					break;
				case 'n':
					normal_flag = true;
					break;
				case 't':
					ts_flag = true;
					break;
				default:
					break;
			}
		}

		entity_size = ts_flag ? ts_size : 7*ts_size;

		#ifdef DEBUG_INPUT
			printf("Input: %s\n", inputFileName);
			printf("Output: %s\n", outFileName);
			printf("BMean %lf BDev %lf\n", burst_mean, burst_dev);
			printf("DMean %lf DDev %lf\n", duration_mean, duration_dev);
			printf("TS %d\n", ts_flag);
		#endif

	//TODO verify if there are enough arguments

	}
	
	void performIO(){
		input.open(inputFileName, ifstream::binary);
		output.open(outFileName, ofstream::binary);
		if(input.eof() || input.fail()) printf("Arquivo inexistente: %s\n", inputFileName), exit(2);
		input.seekg(0, ios::end);
		total_entities = input.tellg()/entity_size;
		input.seekg(0, ios::beg);
		free(inputFileName);
		free(outFileName);
		printf("Total entities %d\n", total_entities);
	}

	void closeIO(){
		input.close();
		output.close();
	}

	void raffle(int * burst, int * duration){
		*burst = 100;
		*duration = 10;
		return;
	}

	void simulate(){
		int burst, duration, i;
		byte * pkg, * lost_pkg;

		pkg = (byte*)malloc(entity_size);
		lost_pkg = (byte*)malloc(entity_size);
		
		for(i = 0, raffle(&burst, &duration); i < total_entities; i++){
			input.read((char*)pkg, entity_size);
			if(burst > 0){
				output.write((char*)pkg, entity_size);
				burst--;
			} else if(duration > 0){
				output.write((char*)lost_pkg, entity_size);
				duration--;
			} else {
				output.write((char*)pkg, entity_size);
				raffle(&burst, &duration);
			}
			//printf("%d\n", i);
		}
		input.read((char*)pkg, entity_size);
		output.write((char*)pkg, input.gcount());
	}

}; /*end of class FilterTools*/

int main(int argc, char* argv[]){

	//1. Process options
	//2. verify if there are enough arguments
	NetSimTool m(argc, argv);

	//3. open IO 
	m.performIO();

	//4. call simulation
	m.simulate();
	
	m.closeIO();
	return 0;
}
