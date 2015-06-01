/*
 * main.cpp
 *
 *  Created on: 2013-1-2
 *      Author: gaobo.benny
 */

#include <iostream>
#include <fstream>
#include <sstream>
#include <stdio.h>
#include "protocol.h"

using namespace std;
using namespace msgbuf;
using namespace protocol;

void readFile(const char *file, ostream &output) {
	char tmp[1024];
	FILE *fp = fopen(file, "r");
	int cnt;
	do {
		cnt = fread(tmp, 1, 1024, fp);
		output.write(tmp, cnt);
	} while (cnt == 1024);
	fclose(fp);
}

void writeFile(const char *file, istream &input) {
	ofstream ofs(file, ios::trunc | ios::binary);
	int ch;
	while (true) {
		ch = input.get();
		if (input.eof()) {
			break;
		}
		ofs.put(ch);
	}
	ofs.close();
}

void test1(void) {
	stringstream ss;
	readFile("c:\\tmp\\AllType.gz", ss);
	cout << "buffer size=" << ss.tellp() << endl;

	stringstream sd;
	ZlibWrapper wrapper;
	wrapper.decompress(ss, sd);
	cout << "decompressed size=" << sd.tellp() << endl;

	AllType *allType = AllType::instance();
	sd.get(); // 读出一个表示是否null的字节
	allType->deserialize(sd);
	allType->print(cout);

	ss.seekg(0, ios::beg);
	ss.seekp(0, ios::beg);
	sd.seekg(0, ios::beg);
	cout << "compressed bytes=" << wrapper.compress(sd, ss) << endl;
	writeFile("c:\\tmp\\2", ss);
	allType->release();
}

void test2(void) {
	stringstream src;
	readFile("c:\\tmp\\AllType.gz", src);

	stringstream dst;
	ZlibWrapper zw;
	zw.decompress(src, dst);
	dst.get(); // 读出一个表示是否null的字节
	clock_t begin = clock();
	for (int i = 0; i < 10000; ++i) {
		AllType *mb = AllType::instance();
		mb->deserialize(dst);
//		mb->print(cout);

		dst.seekg(0, ios::beg);
		dst.seekp(0, ios::beg);
		mb->serialize(dst);

		mb->release();
	}
	clock_t end = clock();
	cout << "spent=" << (end - begin) << endl;
}

int main(void) {
	test2();
	ObjectFactory::instance()->pool()->print(cout);
}

