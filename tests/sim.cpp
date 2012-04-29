#include <fstream>
#include <cstring>
#include <ctime>
#include <cstdlib>
#include <list>

#define INPUT "/mnt/vbox/video/bustsmpeg"
#define OUTPUT "/mnt/vbox/video/bustsout"
#define TSSIZE 188
#define PERC 0.01

typedef unsigned char byte;

using namespace std;

main(){
	int ts_total, pkg_total, read, n_lost, i, front;
	ifstream input(INPUT, ifstream::binary);
	ofstream output(OUTPUT, ofstream::binary);
	byte ip_packet[7*TSSIZE], lost_packet[7*TSSIZE];
	list<int> lost;
	list<int>::iterator it;

	srand(time(NULL));
	memset(lost_packet, 0, 7*TSSIZE);

	input.seekg(0, ios::end);
	ts_total = input.tellg();
	ts_total /= TSSIZE;
	pkg_total = ts_total/7;
	n_lost = pkg_total*PERC;
	input.seekg(0, ios::beg);

	printf("Total de TSs %d\nTotal de Pkgs %d\nTotal de lost %d\n", ts_total, pkg_total, n_lost);

	for(i = 0; i < n_lost; i++){
		lost.push_back(rand()%pkg_total);
	}
	
	lost.sort();

	for(it = lost.begin(); it != lost.end(); it++)
		printf("%d\n", *it);

	for(i = 0, front = lost.front(); i < pkg_total; i++){
		input.read((char*)ip_packet, 7*TSSIZE);
		if(i != front){
			output.write((char*)ip_packet, 7*TSSIZE);
			continue;
		}
		output.write((char*)lost_packet, 7*TSSIZE);
		lost.pop_front();
		front = lost.front();
	}

	input.read((char*)ip_packet, 7*TSSIZE);
	read = input.gcount();
	output.write((char*)ip_packet, read);

	input.close();
	output.close();
}
