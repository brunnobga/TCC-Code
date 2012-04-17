#include "commons.h"
#include "dctTools.h"
#include <cstdio>
#include <fstream>
#include <cstdlib>

void blur(byte *originalFrame, byte *bluredFrame, int H, int W, int fType, int fSize);

#define W 176
#define H 144
#define BLOCK_SIZE 8 

using namespace std;

int main(){
	ifstream fileYUV("/media/hd/UT/11 Semestre/TCC2/git/TCC-Code/algoritmos/akiyo_qcif.yuv", ifstream::binary);
	//ifstream fileYUV("/mnt/vbox/video/parkrun_1280x720_25Hz_P420.yuv", ifstream::binary);
	ofstream fileBlock("/media/hd/UT/11 Semestre/TCC2/git/TCC-Code/algoritmos/akiyo_qcif_blur_176x144_25Hz_P420.yuv", ofstream::binary);
	//ofstream fileBlock("/mnt/vbox/video/parkblur_1280x720_25Hz_P420.yuv", ofstream::binary);
	byte *frame, *outframe;
	int i, j, k = 0;
	//int removals[] = {2, 3};
	//double dct[BLOCK_SIZE][BLOCK_SIZE];
	//alocar espaço para frames
	frame = (byte*)malloc(W*H*sizeof(byte));
	outframe = (byte*)malloc(W*H*sizeof(byte));
	//Settings set;
	//set.blockSize = 3;
	//set.removals = removals;
	//set.removalsSize = 2;
	//set.blockSize = 8;

	//enqto não chegar ao fim do video
	//while(!fileYUV.eof()){
	for(k = 0; k < 10;){

		//ler 1 frame
		fileYUV.read((char*)frame, H*W);

		if(fileYUV.eof() || fileYUV.fail()) return 0;

		blur(frame, outframe, H, W, 1, 3);
		fileBlock.write((char*)frame, H*W);
		
		//copia chromas
		fileYUV.read((char*)frame, H*W/2);
		fileBlock.write((char*)frame, H*W/2);

		//progresso
		fprintf(stdout, "Output at frame %d...\n", k++);
	}

	free(frame);
	free(outframe);
	
	//termina arquivos
	fileYUV.close();
	fileBlock.close();

}
