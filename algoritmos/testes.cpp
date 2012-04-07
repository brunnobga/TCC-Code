#include "dctTools.h"
#include <cstdio>
#include <fstream>
#include <cstdlib>

#define W 352
#define H 288
#define BLOCK_SIZE 8 

using namespace std;

int main(){
	ifstream fileYUV("/mnt/vbox/video/waterfall_352x288_25Hz_P420.yuv", ifstream::binary);
	//ifstream fileYUV("/mnt/vbox/video/parkrun_1280x720_25Hz_P420.yuv", ifstream::binary);
	ofstream fileBlock("/mnt/vbox/video/waterblur_352x288_25Hz_P420.yuv", ofstream::binary);
	//ofstream fileBlock("/mnt/vbox/video/parkblur_1280x720_25Hz_P420.yuv", ofstream::binary);
	byte *frame, *outframe;
	int i, j, k = 0;
	int removals[] = {2, 3};
	double dct[BLOCK_SIZE][BLOCK_SIZE];
	//alocar espaço para frames
	frame = (byte*)malloc(W*H*sizeof(byte));
	outframe = (byte*)malloc(W*H*sizeof(byte));

	//enqto não chegar ao fim do video
	//while(!fileYUV.eof()){
	for(k = 0; k < 10;){

		//ler 1 frame
		fileYUV.read((char*)frame, H*W);

		if(fileYUV.eof() || fileYUV.fail()) return 0;

		for(i = 0; i < 288/BLOCK_SIZE; i++){
			for(j = 0; j < 352/BLOCK_SIZE; j++){
				//printf("i-> %d j-> %d\n", i, j);
				//applyDCT2(frame + i*W*BLOCK_SIZE + j*BLOCK_SIZE, &dct[0][0], BLOCK_SIZE, W);
				//applyIDCT2(&dct[0][0], outframe + i*W*BLOCK_SIZE + j*BLOCK_SIZE, BLOCK_SIZE, W);
				blockage(frame + i*W*BLOCK_SIZE + j*BLOCK_SIZE, outframe + i*W*BLOCK_SIZE + j*BLOCK_SIZE, BLOCK_SIZE, W, removals, 2);
			}
		}
		
		fileBlock.write((char*)outframe, H*W);
		
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
