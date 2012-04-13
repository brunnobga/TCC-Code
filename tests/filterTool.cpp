#include <cstdio>
#include <getopt.h>

using namespace std;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"output", required_argument, 0, 'o'},
		{"size", required_argument, 0, 's'},
		{"filtertype", required_argument, 0, 'f'},
		{"window", required_argument, 0, 'w'}
	};

static char short_options[] = "i:o:s:f:w:";

int main(int argc, char* argv[]){
	int filterType, frameWidth, frameHeight, opt_index, c;
	char* inputFileName, outputFileName;

	//getopt_long(argc, argv, short, long, index)
	while((c = getopt_long(argc, argv, short_options, long_options, &opt_index)) != -1){
		switch(c){
			case 'i':
				printf("Argumento -i: %s\n", optarg);
				break;
			case 'o':
				printf("Argumento -o: %s\n", optarg);
				break;
			case 's':
				printf("Argumento -s: %s\n", optarg);
				break;
			case 'f':
				printf("Argumento -f: %s\n", optarg);
				break;
			case 'w':
				printf("Argumento -w: %s\n", optarg);
				break;
			default:
				break;
		}
	}

	return 0;
}
