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

static const double c1 = 0.0001*L*L;
static const double c2 = 0.0009*L*L;

typedef unsigned char byte;

static struct option long_options[] = 
	{
		{"input", required_argument, 0, 'i'},
		{"reference", required_argument, 0, 'r'},
		{"size", required_argument, 0, 's'},
		{"window", required_argument, 0, 'w'},
		{"metric", required_argument, 0, 'm'}
	};

static char short_options[] = "i:r:s:w:m:";

class MetricTool{
private:
	int metricType, window, frameWidth, frameHeight, frameSize, frameTotal, c, opt_index;
	char *inputFileName, *refFileName, *metricstr, *tmp;
	ifstream input, ref;
	byte * frame, *refframe;

public:
	MetricTool(int argc, char* argv[]){
		while((c = getopt_long(argc, argv, short_options, long_options, &opt_index)) != -1){
			switch(c){
				case 'i':
					inputFileName = (char*)malloc(strlen(optarg)+1); 
					strcpy(inputFileName, optarg);
					break;
				case 'r':
					refFileName = (char*)malloc(strlen(optarg)+1); 
					strcpy(refFileName, optarg);
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
					refframe = (byte*)malloc(frameSize); //TODO free memory	
					break;
				case 'w':
					window = atoi(optarg);
					if(window == 0) printf("Argumento invalido para -w...\n"), exit(1);
					break;
				case 'm':
					metricstr = (char*)malloc(strlen(optarg) + 1);
					strcpy(metricstr, optarg);
					if(strcmp(optarg, "MSE") == 0) metricType = 0;
					else if(strcmp(optarg, "PSNR") == 0) metricType = 1;
					else if(strcmp(optarg, "MSSIM") == 0) metricType = 2;
					else printf("Metrica desconhecida. \n"), exit(2);
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

	int pow2(int a) {return a*a;}
	double pow2(double a) {return a*a;}

	double mean(double * in, int size){
		double result = 0;
		for(int i = 0; i < size; i++)
				result += *(in + i);
		return result/size;
	}

	double mse(){
		double rateperframe[frameTotal];
		int fc, i;
		for(fc = 0; fc < frameTotal; fc++){
			rateperframe[fc] = 0;
			input.read((char*)frame, frameSize);
			ref.read((char*)refframe, frameSize);
			for(i = 0; i < frameSize; i++){
					rateperframe[fc] += pow2(*(frame + i) - *(refframe + i));
			}
			rateperframe[fc] /= frameSize;
			input.read((char*)frame, frameSize/2);
			ref.read((char*)refframe, frameSize/2);
			#ifdef DEBUG_OUTPUT
				printf("Frame %d\n", fc);
			#endif
		}
		return mean(rateperframe, frameTotal);
	}

	double psnr(){
		double m = mse();
		return 10.*log10(pow2(255)/m);
	}

	double blockMean(byte * frame, int x, int y){
		double result = 0;
		int i, j;
		for(i = 0; i < window; i++)
			for(j = 0; j < window; j++)
				result += *(frame + frameWidth*(x*window + i) + y*window + j);
		return result / pow2(window);
	}

	double blockVariance(byte * frame, double m, int x, int y){
		double result = 0;
		int i, j;
		for(i = 0; i < window; i++)
			for(j = 0; j < window; j++)
				result += pow2(*(frame + frameWidth*(x*window + i) + y*window + j) - m);
		return sqrt(result/pow2(window));
	}

	double blockCovariance(byte * frame, byte * refframe, double m1, double m2, int x, int y){
		double result = 0;
		int i, j;
		for(i = 0; i < window; i++)
			for(j = 0; j < window; j++)
				result += (*(frame + frameWidth*(x*window + i) + y*window + j) - m1) * (*(refframe + frameWidth*(x*window + i) + y*window + j) - m2);
		return result/pow2(window);
	}

	double ssim(int x, int y){
		double m1, m2, z1, z2, z12, num, den;
		m1 = blockMean(frame, x, y);
		m2 = blockMean(refframe, x, y);
		z1 = blockVariance(frame, m1, x, y);
		z2 = blockVariance(refframe, m2, x, y);
		z12 = blockCovariance(frame, refframe, m1, m2, x, y);
		num = (2 * m1 * m2 + c1) * (2 * z12 + c2);
		den = (pow2(m1) + pow2(m2) + c1) * (pow2(z1) + pow2(z2) + c2);
		return num/den;
	}

	double mssim(){
		int i, j, numberW = frameWidth/window, numberH = frameHeight/window;
		double ssimperframe[numberH][numberW], mssimperframe[frameTotal];
		for(int fc = 0; fc < frameTotal; fc++){
			input.read((char*)frame, frameSize);
			ref.read((char*)refframe, frameSize);
			for(i = 0; i < numberH; i++){
				for(j = 0; j < numberW; j++){
					ssimperframe[i][j] = ssim(i, j);
				}
			}
			input.read((char*)frame, frameSize/2);
			ref.read((char*)refframe, frameSize/2);
			mssimperframe[fc] = mean(&ssimperframe[0][0], numberW*numberH);
			#ifdef DEBUG_OUTPUT
				printf("Frame %d\n", fc);
			#endif
		}
		return mean(mssimperframe, frameTotal);
	}

	void performMetric(){
		double result;
		if(metricType == 0) result = mse();
		else if(metricType == 1) result = psnr();
		else if(metricType == 2) result = mssim();
		printf("Result %s: %lf\n", metricstr, result);
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
	//2. verify if there are enough argumentes
	MetricTool m(argc, argv);

	//3. open IO 
	m.performIO();

	//5 read Y component and skip 
	//6. metrics to Y component
	m.performMetric();
	
	m.closeIO();
	return 0;
}
