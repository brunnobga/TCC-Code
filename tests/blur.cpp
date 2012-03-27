#include <iostream>
#include <cstdlib>
#include <fstream>
#include <cstring>

#define W 352
#define H 288

using namespace std;

typedef unsigned char byte;

main(){
	ifstream fileYUV("/mnt/vbox/video/waterfall_352x288_25Hz_P420.yuv", ifstream::binary);
	//ifstream fileYUV("/mnt/vbox/video/parkrun_1280x720_25Hz_P420.yuv", ifstream::binary);
	ofstream fileBlur("/mnt/vbox/video/blured_352x288_25Hz_P420.yuv", ofstream::binary);
	//ofstream fileBlur("/mnt/vbox/video/blured_1280x720_25Hz_P420.yuv", ofstream::binary);
	byte *frame, *blur;
	int i, j, k;
	
	//alocar espaço para frames
	frame = (byte*)malloc(W*H*sizeof(byte));
	blur = (byte*)malloc(W*H*sizeof(byte));

	//enqto não chegar ao fim do video
	while(!fileYUV.eof()){
	//for(k = 0; k < 50; k++){
		//ler 1 frame
		fileYUV.read((char*)frame, H*W);

		//copiar para frame editavel
		memcpy(blur, frame, H*W);

		//blurring em cruz
		for(i = 1; i < H-1; i++){
			for(j = 1; j < W-1; j++){
				*(blur + i*W + j) /= 5;
				*(blur + i*W + j) += *(frame + i*W + j+1)/5 + *(frame + i*W + j-1)/5 + *(frame + (i+1)*W + j)/5 + *(frame + (i-1)*W + j)/5;	
			}	
		}

		fileBlur.write((char*)blur, H*W);
		
		//copia chromas
		fileYUV.read((char*)frame, H*W/2);
		fileBlur.write((char*)frame, H*W/2);
	}
	
	//termina arquivos
	fileYUV.close();
	fileBlur.close();

	return 0;
}
