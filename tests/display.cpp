#include <cstdlib>
#include <iostream>
#include <fstream>
#include <cstring>
#include "SDL/SDL.h"

#define WIDTH 1280 
#define HEIGHT 720

using namespace std;

main(){
	//inicializa SDL
	if(SDL_Init(SDL_INIT_VIDEO)){
		fprintf(stderr, "Unable to init SDL: %s", SDL_GetError());
		exit(1);
	}

	//variaveis	
	SDL_Surface * screen;
	SDL_Overlay * overlay;
	SDL_Rect rect;
	
	//cria tela de exibicao
	screen = SDL_SetVideoMode(WIDTH, HEIGHT, 0, 0);
	if(!screen){
		fprintf(stderr, "Unable to create screen!!!\n");
	}

	overlay = SDL_CreateYUVOverlay(WIDTH, HEIGHT, SDL_IYUV_OVERLAY, screen);
	ifstream fileYUV("/mnt/vbox/video/parkrun_1280x720_50Hz_P420.yuv", ifstream::binary);
	rect.x = 0;
	rect.y = 0;
	rect.h = HEIGHT;
	rect.w = WIDTH;

	while(!fileYUV.eof()){
		//tranca pixels para edicao
		SDL_LockYUVOverlay(overlay);
	
		fileYUV.read((char*)overlay->pixels[0], overlay->h*overlay->pitches[0]);
		fileYUV.read((char*)overlay->pixels[1], overlay->h*overlay->pitches[1]);
		
		//memcpy(overlay->pixels[0], Ydata, overlay->h*overlay->pitches[0]);
		//*(overlay->pixels[0] + 100 * overlay->pitches[0] + 150) = 0xFF;
	
		SDL_UnlockYUVOverlay(overlay);
		//pixels liberados
		
		SDL_DisplayYUVOverlay(overlay, &rect);

		system("sleep 0.04");
	}
	fileYUV.close();
	atexit(SDL_Quit);
}
