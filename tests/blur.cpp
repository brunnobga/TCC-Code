#include <iostream>
#include <cstdlib>
#include <fstream>
#include <cstring>

#define W 1280
#define H 720

using namespace std;

typedef unsigned char byte;

main(){
	//ifstream fileYUV("/mnt/vbox/video/waterfall_352x288_25Hz_P420.yuv", ifstream::binary);
	ifstream fileYUV("/mnt/vbox/video/parkrun_1280x720_25Hz_P420.yuv", ifstream::binary);
	//ofstream fileBlur("/mnt/vbox/video/waterblur_352x288_25Hz_P420.yuv", ofstream::binary);
	ofstream fileBlur("/mnt/vbox/video/parkblur_1280x720_25Hz_P420.yuv", ofstream::binary);
	byte *frame, *blur;
	int i, j, k = 0;
	
	//alocar espaço para frames
	frame = (byte*)malloc(W*H*sizeof(byte));
	blur = (byte*)malloc(W*H*sizeof(byte));

	//enqto não chegar ao fim do video
	//while(!fileYUV.eof()){
	for(k = 0; k < 50;){

		//ler 1 frame
		fileYUV.read((char*)frame, H*W);

		if(fileYUV.eof() || fileYUV.fail()) return 0;
		
		//copiar para frame editavel
		memcpy(blur, frame, H*W);

		//blurring em cruz
		for(i = 1; i < H-1; i++){
			for(j = 1; j < W-1; j++){
				*(blur + i*W + j) /= 9;
				*(blur + i*W + j) += *(frame + i*W + j+1)/9 + *(frame + i*W + j-1)/9 + *(frame + (i+1)*W + j)/9 + *(frame + (i-1)*W + j)/9;
				*(blur + i*W + j) += *(frame + (i+1)*W + j+1)/9 + *(frame + (i+1)*W + j-1)/9 + *(frame + (i-1)*W + j+1)/9 + *(frame + (i-1)*W + j-1)/9;
			}	
		}

		fileBlur.write((char*)blur, H*W);
		
		//copia chromas
		fileYUV.read((char*)frame, H*W/2);
		fileBlur.write((char*)frame, H*W/2);

		//progresso
		fprintf(stdout, "Iput frame %d at %d\n", k, (int)fileYUV.tellg());
		fprintf(stdout, "Oput frame %d at %d\n", k++, (int)fileBlur.tellp());
	}
	
	//termina arquivos
	fileYUV.close();
	fileBlur.close();

	return 0;
}
