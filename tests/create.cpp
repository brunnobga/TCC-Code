#include <iostream>
#include <fstream>

using namespace std;

int main(){
	ofstream fileY("pic.Y", ofstream::binary),
	fileU("pic.U", ofstream::binary),
	fileV("pic.V", ofstream::binary);

	for(int i = 0; i < 720; i++){
		for(int j = 0; j < 1280; j++){
			fileY.put(0xFF - i/2);
			if(i % 4 == 0) fileU.put(0x80);
			if(j % 4 == 2) fileV.put(0x80);
		}
	}

	fileY.close();
	fileU.close();
	fileV.close();
	
	return 0;
}
