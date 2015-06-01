/*
 * calibur.cpp
 *
 *  Created on: 2013-1-1
 *      Author: gaobo.benny
 */

#include <iomanip>
#include <stdio.h>
#include <string.h>
#include <endian.h>
#include <zlib.h>

#include "msgbuf.h"

using namespace std;
using namespace msgbuf;

//IoBuffer::IoBuffer(size_t init_capa) {
//	m_limit = m_capacity = init_capa;
//	m_buffer = new byte[m_capacity];
//	m_current = 0;
//}
//
//IoBuffer::~IoBuffer(void) {
//	delete m_buffer;
//}
//
//void IoBuffer::rewind(void) {
//	m_current = 0;
//}
//
//void IoBuffer::flip(void) {
//	m_limit = m_current;
//	m_current = 0;
//}
//
//void IoBuffer::clear(void) {
//	m_current = 0;
//	m_limit = m_capacity;
//}
//
//size_t IoBuffer::inputRemaining(void) const {
//	return m_limit - m_current;
//}
//
//size_t IoBuffer::outputRemaining(void) const {
//	return m_capacity - m_current;
//}
//
//int IoBuffer::read(void) {
//	if (inputRemaining() > 0) {
//		return m_buffer[m_current++];
//	} else {
//		return EOF;
//	}
//}
//
//void IoBuffer::write(byte v) {
//	if (outputRemaining() == 0) {
//		extend();
//	}
//
//	m_buffer[m_current++] = v;
//}
//
//int IoBuffer::get(byte *array, int start, int length) {
//	int v;
//	for (int i = 0; i < length; ++i) {
//		if ((v = read()) == EOF) {
//			return i;
//		} else {
//			array[start + i] = v;
//		}
//	}
//
//	return length;
//}
//
//int IoBuffer::put(byte *array, int start, int length) {
//	for (int i = 0; i < length; ++i) {
//		write(array[start + i]);
//	}
//
//	return length;
//}
//
//int IoBuffer::extendTo(size_t size) {
//	if (size > m_capacity) {
//		int old_capa = m_capacity;
//		m_capacity = size;
//		byte *new_buffer = new byte[m_capacity];
//		memcpy(new_buffer, m_buffer, old_capa);
//
//		delete m_buffer;
//		m_buffer = new_buffer;
//	}
//
//	return m_capacity;
//}
//
//int IoBuffer::extend(void) {
//	return extendTo(m_capacity + (m_capacity >> 1));
//}
//
//int IoBuffer::extend(size_t increment) {
//	return extendTo(m_capacity + increment);
//}
//
//int IoBuffer::copy(IoBuffer &dest) const {
//	return dest.put(m_buffer, m_current, m_limit);
//}
//
//int IoBuffer::copy(IoBuffer &dest, CompressionMode mode) const {
//	if (mode == COMPRESS) {
//		return deflateTo(dest);
//	} else {
//		return inflateTo(dest);
//	}
//}
//
//int IoBuffer::inflateTo(IoBuffer &dest) const {
//	if (m_limit == 0) {
//		return 0;
//	}
//
//	byte tmpbuf[256];
//	bool done = false;
//	int status;
//
//	z_stream strm;
//	strm.next_in = m_buffer + m_current;
//	strm.avail_in = inputRemaining();
//	strm.total_out = 0;
//	strm.zalloc = Z_NULL;
//	strm.zfree = Z_NULL;
//
//	if (inflateInit2(&strm, (15 + 32)) != Z_OK) {
//		return -1;
//	}
//
//	int last_out = 0;
//	while (!done) {
//		strm.next_out = tmpbuf;
//		strm.avail_out = sizeof(tmpbuf);
//		// Inflate another chunk.
//		status = inflate(&strm, Z_SYNC_FLUSH);
//		dest.put(tmpbuf, 0, strm.total_out - last_out);
//		last_out = strm.total_out;
//
//		if (status == Z_STREAM_END) {
//			done = true;
//		} else if (status != Z_OK) {
//			break;
//		}
//	}
//
//	if (inflateEnd(&strm) != Z_OK) {
//		return -1;
//	}
//
//	// Set real length.
//	return strm.total_out;
//}
//
//int IoBuffer::deflateTo(IoBuffer &dest) const {
//	if (m_limit == 0) {
//		return 0;
//	}
//
//	z_stream strm;
//	strm.zalloc = Z_NULL;
//	strm.zfree = Z_NULL;
//	strm.opaque = Z_NULL;
//	strm.total_out = 0;
//	strm.next_in = m_buffer + m_current;
//	strm.avail_in = inputRemaining();
//
//	// Compresssion Levels:
//	//   Z_NO_COMPRESSION
//	//   Z_BEST_SPEED
//	//   Z_BEST_COMPRESSION
//	//   Z_DEFAULT_COMPRESSION
//
//	if (deflateInit2(&strm, Z_DEFAULT_COMPRESSION, Z_DEFLATED, (15 + 16), 8, Z_DEFAULT_STRATEGY)
//			!= Z_OK) {
//		return -1;
//	}
//
//	byte tmpbuf[256];
//	int last_out = 0;
//	do {
//		strm.next_out = tmpbuf;
//		strm.avail_out = sizeof(tmpbuf);
//		deflate(&strm, Z_FINISH);
//		dest.put(tmpbuf, 0, strm.total_out - last_out);
//		last_out = strm.total_out;
//	} while (strm.avail_out == 0);
//
//	deflateEnd(&strm);
//	return strm.total_out;
//}

int ZlibWrapper::decompressSegment(z_stream *strm, char *segment, size_t offset,
		size_t length, ostream &output) {
	if (length <= 0) {
		return 0;
	}

	char buf[1024];
	int curr_out = strm->total_out;
	int last_out = strm->total_out;
	bool done = false;
	strm->next_in = (Bytef*) (segment + offset);
	strm->avail_in = length;
	int status;
	while (!done) {
		strm->next_out = (Bytef*) buf;
		strm->avail_out = sizeof(buf);
		// Inflate another chunk.
		status = inflate(strm, Z_SYNC_FLUSH);
		output.write((char*) buf, strm->total_out - last_out);
		last_out = strm->total_out;

		if (status == Z_STREAM_END) {
			done = true;
		} else if (status != Z_OK) {
			break;
		}
	}

	return strm->total_out - curr_out;
}

int ZlibWrapper::compressSegment(z_stream *strm, char *segment, size_t offset,
		size_t length, bool eof, ostream &output) {
	if (length <= 0) {
		return 0;
	}

	char buf[1024];
	int total = 0;
	strm->next_in = (Bytef*) (segment + offset);
	strm->avail_in = length;
	int status;
	int flush = eof ? Z_FINISH : Z_NO_FLUSH;
	int have = 0;

	do {
		strm->next_out = (Bytef*) buf;
		strm->avail_out = sizeof(buf);
		status = deflate(strm, flush);
		if (status == Z_STREAM_ERROR) {
			cout << "errorCode=" << status << " errorMessage=" << strm->msg
					<< endl;
			return -1;
		}

		have = sizeof(buf) - strm->avail_out;
		output.write(buf, have);
		total += have;

	} while (strm->avail_out == 0);

	return total;
}

size_t ZlibWrapper::availBytes(istream &input) {
	streampos currg = input.tellg();
	input.seekg(0, ios::end);
	size_t avail_in = input.tellg() - currg;
	input.seekg(currg, ios::beg);
	return avail_in;
}

int ZlibWrapper::decompress(istream &input, ostream &output) {
	// 获得输入流中可读的字节数
	size_t avail_in = availBytes(input);

	if (avail_in <= 0) {
		return 0;
	}

	char buf[1024];
	int count = avail_in / sizeof(buf);
	int reset = avail_in % sizeof(buf);

	z_stream strm;
	/* allocate inflate state */
	strm.next_in = (Bytef*) buf;
	strm.avail_in = avail_in;
	strm.total_out = 0;
	strm.zalloc = Z_NULL;
	strm.zfree = Z_NULL;

	if (inflateInit2(&strm, (15 + 32)) != Z_OK) {
		return -1;
	}

	size_t total = 0;
	for (int i = 0; i < count; ++i) {
		input.read((char*) buf, sizeof(buf));
		total += decompressSegment(&strm, buf, 0, sizeof(buf), output);
	}

	if (reset > 0) {
		input.read((char*) buf, reset);
		total += decompressSegment(&strm, buf, 0, reset, output);
	}

	return total;
}

int ZlibWrapper::compress(istream &input, ostream &output) {
	size_t avail_in = availBytes(input);
	if (avail_in <= 0) {
		return 0;
	}

	char buf[1024];
	int count = avail_in / sizeof(buf);
	int reset = avail_in % sizeof(buf);

	z_stream strm;
	strm.zalloc = Z_NULL;
	strm.zfree = Z_NULL;
	strm.opaque = Z_NULL;
	strm.total_out = 0;
	strm.next_in = (Bytef*) buf;
	strm.avail_in = avail_in;

	// Compresssion Levels:
	//   Z_NO_COMPRESSION
	//   Z_BEST_SPEED
	//   Z_BEST_COMPRESSION
	//   Z_DEFAULT_COMPRESSION
	if (deflateInit2(&strm, Z_DEFAULT_COMPRESSION, Z_DEFLATED, (15 + 16), 8, Z_DEFAULT_STRATEGY)
			!= Z_OK) {
		return -1;
	}

	size_t total = 0;
	for (int i = 0; i < count; ++i) {
		input.read((char*) buf, sizeof(buf));
		total += compressSegment(&strm, buf, 0, sizeof(buf), input.eof(),
				output);
	}

	if (reset > 0) {
		input.read((char*) buf, reset);
		total += compressSegment(&strm, buf, 0, reset, true, output);
	}

	deflateEnd(&strm);
	return strm.total_out;
}

void MessageBean::print(ostream &os) {
	TextMessageBeanFormat format(os);
	serialize(format);
}

BinaryMessageBeanFormat::BinaryMessageBeanFormat(MessageBeanFactory *factory) {
	this->factory = factory;
	input = NULL;
	output = NULL;
}

BinaryMessageBeanFormat::~BinaryMessageBeanFormat(void) {
}

void BinaryMessageBeanFormat::writeField(const char *prototype,
		const char *typeName, const DataType dataType, DataType containerType,
		const int index, void* pf) {
	if (dataType == dt_byte) {
		output->put(*(CAST_PTR(byte, pf)));
	} else if (dataType == dt_boolean) {
		output->put(*CAST_PTR(bool, pf) ? MB_TRUE : MB_FALSE);
	} else if (dataType == dt_short) {
		writeVarint(*CAST_PTR(int16_t, pf));
	} else if (dataType == dt_int) {
		writeVarint(*CAST_PTR(int32_t, pf));
	} else if (dataType == dt_long) {
		writeVarint(*CAST_PTR(int64_t, pf));
	} else if (dataType == dt_float) {
		writeFloat(*CAST_PTR(float, pf));
	} else if (dataType == dt_double) {
		writeDouble(*CAST_PTR(double, pf));
	} else if (dataType == dt_string) {
		writeString(CAST_PTR(string, pf));
	} else if (dataType == dt_message) {
		writeBean(CAST_PTR(MessageBean, pf));
	} else if (dataType == dt_message_ptr) {
		MessageBean **ppmb = (MessageBean **) pf;
		writeBean(*ppmb);
	} else {
		// TODO: throws exception
	}
}

void* BinaryMessageBeanFormat::getFieldValuePtr(DataType type) {
	if (type == dt_byte) {
		return &fieldValue.byteValue;
	} else if (type == dt_boolean) {
		return &fieldValue.boolValue;
	} else if (type == dt_short) {
		return &fieldValue.shortValue;
	} else if (type == dt_int) {
		return &fieldValue.intValue;
	} else if (type == dt_long) {
		return &fieldValue.longValue;
	} else if (type == dt_float) {
		return &fieldValue.floatValue;
	} else if (type == dt_double) {
		return &fieldValue.doubleValue;
	} else if (type == dt_string) {
		fieldValue.stringValue.clear();
		return &fieldValue.stringValue;
	} else {
		return &fieldValue.pointerValue;
	}
}

void* BinaryMessageBeanFormat::readField(const char *typeName,
		DataType dataType, void *pf) {
	void* ptr = pf != NULL ? pf : getFieldValuePtr(dataType);
	if (dataType == dt_byte) {
		*CAST_PTR(byte, ptr) = input->get();
		return ptr;
	} else if (dataType == dt_boolean) {
		*CAST_PTR(bool, ptr) = input->get() == MB_TRUE;
		return ptr;
	} else if (dataType == dt_short) {
		*CAST_PTR(int16_t, ptr) = readVarint();
		return ptr;
	} else if (dataType == dt_int) {
		*CAST_PTR(int32_t, ptr) = readVarint();
		return ptr;
	} else if (dataType == dt_long) {
		*CAST_PTR(int64_t, ptr) = readVarint();
		return ptr;
	} else if (dataType == dt_float) {
		*CAST_PTR(float, ptr) = readFloat();
		return ptr;
	} else if (dataType == dt_double) {
		*CAST_PTR(double, ptr) = readDouble();
		return ptr;
	} else if (dataType == dt_string) {
		string * str = readString(CAST_PTR(string, ptr) );
		if (str != NULL) {
			return str;
		} else {
			fieldValue.stringValue.clear();
			return &fieldValue.stringValue;
		}
	} else if (dataType == dt_message) {
		return readBean(typeName, pf);
	} else if (dataType == dt_message_ptr) {
		fieldValue.pointerValue = readBean(typeName, pf);
		return &fieldValue.pointerValue;
	} else {
		// TODO: throws exception
		return NULL;
	}
}

void BinaryMessageBeanFormat::writeBean(MessageBean *bean) {
	if (writeIsNull(bean)) {
		return;
	} else {
		bean->serialize(*this);
	}
}

MessageBean* BinaryMessageBeanFormat::readBean(const char *typeName, void* pm) {
	if (readIsNull()) {
		return NULL;
	} else {
		MessageBean* bean;
		if (pm != NULL) {
			bean = (MessageBean*) pm;
		} else {
			bean = factory->borrowMessageBean(typeName);
		}
		bean->deserialize(*this);
		return bean;
	}
}

int64_t BinaryMessageBeanFormat::readVarint(void) {
	int cnt = 0;
	int64_t value = 0;
	byte b = 0;
	bool neo = false;
	int64_t tmp = 0;

	while (true) {
		b = input->get();
		if (cnt == 0) {
			neo = (b & 0X40) != 0;
			tmp = (long) (b & 0X3F);
			value |= tmp;
		} else {
			tmp = (long) (b & 0X7F);
			value |= tmp;
		}

		if ((b & 0X80) != 0) {
			value <<= 7;
		} else {
			break;
		}

		++cnt;
	}

	return neo ? -value : value;
}

void BinaryMessageBeanFormat::writeVarint(int64_t v) {
	/* 绝对值 */
	uint64_t absValue = LLABS(v);
	int totalBits = sizeof(int64_t) * 8;
	/* 绝对值中有值的位(bit)数 */
	int cnt = totalBits - numberOfLeadingZeros(absValue);
	/* 计算实际需要占用的位数(bits)和字节数(len) */
	int bits = 6;
	/* 结果字节数 */
	int len = 1;
	/* beginBit是结果字节流中，需要开始存储数据的起始bit位置。 */
	int beginBit = cnt % 7;
	while (bits < cnt) {
		bits += 7;
		beginBit += 8;
		++len;
	}

	uint64_t sourceMask = (uint64_t) 1L << (cnt - 1);
	byte b = 0;
	byte targetMask = (byte) 0X80;
	int bitIdx = len * 8;
	for (int i = len; i > 0; --i) {
		b = 0;
		targetMask = (byte) 0X80;
		for (int j = 0; j < 8; ++j) {
			if (j == 0) { /* 字节最高位 */
				if (i > 1) { /* 不是最后一个字节 */
					/* 设置后续还有数据标志 */
					b |= targetMask;
				}
			} else if (j == 1 && i == len && v < 0) { /* 第一个字节的次高位，如果值为负数 */
				/* 设置符号位（设置了为负数） */
				b |= targetMask;
			} else if (beginBit >= bitIdx) {
				if ((sourceMask & absValue) != 0) {
					b |= targetMask;
				}

				sourceMask >>= 1;
			}

			targetMask >>= 1;
			targetMask &= 0X7F;
			--bitIdx;
		}

		output->put(b);
	}
}

int BinaryMessageBeanFormat::numberOfLeadingZeros(uint64_t v) {
	int totalBits = sizeof(uint64_t) * 8;
	uint64_t mask = (uint64_t) 1L << (totalBits - 1);
	int num = 0;
	for (num = 0; num < totalBits; ++num) {
		if ((mask & v) != 0) {
			return num;
		} else {
			mask >>= 1;
		}
	}

	return num;
}

int BinaryMessageBeanFormat::putData(byte *array, int start, int length) {
#if __BYTE_ORDER == __LITTLE_ENDIAN
	reverseBytesOrder(array, 0, length);
#endif
	output->write((char*) (array + start), length);
	return length;
}

int BinaryMessageBeanFormat::getData(byte *array, int start, int length) {
	input->read((char*) (array + start), length);
#if __BYTE_ORDER == __LITTLE_ENDIAN
	reverseBytesOrder(array, 0, length);
#endif
	return length;
}

int16_t BinaryMessageBeanFormat::readInt16(void) {
	int16_t value = 0;
	getData((byte*) &value, 0, sizeof(int16_t));
	return value;
}

void BinaryMessageBeanFormat::writeInt16(int16_t v) {
	putData((byte*) &v, 0, sizeof(int16_t));
}

float BinaryMessageBeanFormat::readFloat(void) {
	float value = 0;
	getData((byte*) &value, 0, sizeof(float));
	return value;
}

void BinaryMessageBeanFormat::writeFloat(float v) {
	putData((byte*) &v, 0, sizeof(float));
}

double BinaryMessageBeanFormat::readDouble(void) {
	double value = 0;
	getData((byte*) &value, 0, sizeof(double));
	return value;
}

void BinaryMessageBeanFormat::writeDouble(double v) {
	putData((byte*) &v, 0, sizeof(double));
}

bool BinaryMessageBeanFormat::readIsNull(void) {
	return input->get() == MB_NULL;
}

bool BinaryMessageBeanFormat::writeIsNull(void* ptr) {
	bool is_null = ptr == NULL;
	output->put(is_null ? MB_TRUE : MB_FALSE);
	return is_null;
}

int BinaryMessageBeanFormat::readSize(void) {
	return readVarint();
}

int BinaryMessageBeanFormat::writeSize(size_t size) {
	writeVarint(size);
	return size;
}

string* BinaryMessageBeanFormat::readString(string *str) {
	byte bytes[16];
	if (readIsNull()) {
		return NULL;
	} else {
		int len = readInt16();
		int count = len / sizeof(bytes);
		if (count > 0) {
			for (int i = 0; i < count; ++i) {
				input->read((char*) bytes, sizeof(bytes));
				str->append((char*) bytes, 0, sizeof(bytes));
			}
		}

		int reset = len % sizeof(bytes);
		if (reset > 0) {
			input->read((char*) bytes, reset);
			str->append((char*) bytes, 0, reset);
		}

		return str;
	}
}

void BinaryMessageBeanFormat::writeString(string *ps) {
	if (writeIsNull(ps)) {
		return;
	} else {
		int len = ps->length();
		writeInt16(len);
		for (int i = 0; i < len; ++i) {
			output->put((*ps)[i]);
		}
	}
}

void BinaryMessageBeanFormat::reverseBytesOrder(byte carray[], int offset,
		int len) {
	int half = len >> 1;
	byte v;
	for (int i = 0, j = len - 1; i < half; ++i, --j) {
		v = carray[i + offset];
		carray[i + offset] = carray[j + offset];
		carray[j + offset] = v;
	}
}

void TextMessageBeanFormat::indent(void) {
	for (int i = 0; i < indentLevel; ++i) {
		os << "    ";
	}
}

void TextMessageBeanFormat::writeField(const char *prototype,
		const char *typeName, const DataType dataType,
		const DataType containerType, const int index, void *pf) {
	if (prototype != NULL) {
		indent();
		os << prototype << " = ";
	}

	if (containerType != dt_none) {
		indent();
		if (containerType == dt_vector) {
			os << '<' << index << "> = ";
		} else if (containerType == dt_array) {
			os << '[' << index << "] = ";
		} else if (containerType == dt_map) {
			os << (index == 0 ? "KEY  " : "VALUE") << " : ";
		}
	}

	if (pf == NULL) {
		os << "NULL" << endl;
	} else {
		if (dataType == dt_byte) {
			os << ((int) *CAST_PTR(byte, pf) ) << endl;
		} else if (dataType == dt_boolean) {
			os << (*CAST_PTR(bool, pf) ? "true" : "false") << endl;
		} else if (dataType == dt_short) {
			os << *CAST_PTR(int16_t, pf) << endl;
		} else if (dataType == dt_int) {
			os << *CAST_PTR(int32_t, pf) << endl;
		} else if (dataType == dt_long) {
			os << *CAST_PTR(int64_t, pf) << endl;
		} else if (dataType == dt_float) {
			os << *CAST_PTR(float, pf) << endl;
		} else if (dataType == dt_double) {
			os << *CAST_PTR(double, pf) << endl;
		} else if (dataType == dt_string) {
			os << '"' << *CAST_PTR(string, pf) << '"' << endl;
		} else if (dataType == dt_message) {
			MessageBean *pmb = (MessageBean*) pf;
			if (pmb == NULL) {
				os << "NULL" << endl;
			} else {
				pmb->serialize(*this);
			}
		} else {
			MessageBean **ppmb = (MessageBean**) pf;
			MessageBean *pmb = *ppmb;
			if (pmb == NULL) {
				os << "NULL" << endl;
			} else {
				pmb->serialize(*this);
			}
		}
	}
}

void TextMessageBeanFormat::beginWriteBean(const type_info &it,
		const type_info &st, const char *typeName) {
	if (it != st) { // 是基类
		indent();
		os << "base : ";
	}

	os << "message " << typeName << " {" << endl;
	++indentLevel;
}

void TextMessageBeanFormat::endWriteBean(const type_info&, const type_info&,
		const char*) {
	--indentLevel;
	indent();
	os << '}' << endl;
}

void TextMessageBeanFormat::beginWriteVector(const char *prototype, void *ptr) {
	indent();
	os << prototype << " = ";
	if (ptr == NULL) {
		os << "NULL" << endl;
	} else {
		os << " {" << endl;
	}
	++indentLevel;
}

void TextMessageBeanFormat::endWriteVector(const char *prototype, void *ptr) {
	--indentLevel;
	if (ptr != NULL) {
		indent();
		os << '}' << endl;
	}
}

void TextMessageBeanFormat::beginWriteMap(const char *prototype, void *ptr) {
	indent();
	os << prototype << " = ";
	if (ptr == NULL) {
		os << "NULL" << endl;
	} else {
		os << " {" << endl;
	}
	++indentLevel;
}

void TextMessageBeanFormat::endWriteMap(const char *prototype, void *ptr) {
	--indentLevel;
	if (ptr != NULL) {
		indent();
		os << '}' << endl;
	}
}

void ReleaseMessageBeanFormat::writeField(const char *prototype,
		const char *typeName, const DataType dataType,
		const DataType containerType, const int index, void *pf) {
	if (pf == NULL) {
		return;
	}

	MessageBean *pmb = NULL;
	if (dataType == dt_message) {
		pmb = (MessageBean*) pf;
	} else if (dataType == dt_message_ptr) {
		MessageBean **ppmb = (MessageBean**) pf;
		pmb = *ppmb;
	}

	if (pmb != NULL) {
		factory->returnMessageBean(pmb);
	}
}

ReentrantLock::ReentrantLock(void) {
	mutex = PTHREAD_MUTEX_INITIALIZER;

	// PTHREAD_MUTEX_RECURSIVE属性让同一线程可以多次加锁
	pthread_mutexattr_t mta;
	pthread_mutexattr_init(&mta);
	pthread_mutexattr_settype(&mta, PTHREAD_MUTEX_RECURSIVE);
	pthread_mutex_init(&mutex, &mta);
}

ReentrantLock::~ReentrantLock() {
	pthread_mutex_destroy(&mutex);
}

void ReentrantLock::lock(void) {
	pthread_mutex_lock(&mutex);
}

void ReentrantLock::unlock(void) {
	pthread_mutex_unlock(&mutex);
}

MessageBeanFactory::MessageBeanFactory(void) {
	// 正常的ID从1开始
	// instanceId==0的实例是用户自己用new创建的，非受控，需要用户自己用delete释放。
	m_instanceId = 1;

	// 初始化MessageBeanPool对象池
	m_pool = new MessageBeanPool(this);

	// 初始化对象回收器
	m_recycler = new ReleaseMessageBeanFormat(this);
}

MessageBeanFactory::~MessageBeanFactory() {
	delete m_pool;
	delete m_recycler;
}

int MessageBeanFactory::getInstanceId(void) {
	lock.lock();
	int id = m_instanceId++;
	lock.unlock();
	return id;
}

MessageBean* MessageBeanFactory::borrowMessageBean(const string &typeName) {
	return m_pool->borrowObject(typeName);
}

bool MessageBeanFactory::returnMessageBean(MessageBean *mbean) {
	if (mbean == NULL) {
		return false;
	}

	mbean->serialize(*m_recycler);
	return m_pool->returnObject(mbean);
}

MessageBeanPool::MessageBeanPool(MessageBeanFactory *factory) {
	this->factory = factory;
}

MessageBeanPool::~MessageBeanPool() {
	map<string, QueuePair*>::iterator itr = pool.begin();
	QueuePair *qpair = NULL;
	ObjectQueue *idleQueue = NULL;
	ObjectQueue *activeQueue = NULL;
	map<int, MessageBean*>::iterator elitr;
	MessageBean *mbean = NULL;

	for (; itr != pool.end(); ++itr) {
		qpair = itr->second;

		idleQueue = qpair->first;
		elitr = idleQueue->begin();
		for (; elitr != idleQueue->end(); ++elitr) {
			mbean = elitr->second;
			delete mbean;
		}
		delete idleQueue;

		activeQueue = qpair->second;
		elitr = activeQueue->begin();
		for (; elitr != activeQueue->end(); ++elitr) {
			mbean = elitr->second;
			delete mbean;
		}
		delete activeQueue;

		delete qpair;
	}
}

QueuePair* MessageBeanPool::getQueuePair(const string &name) {
	map<string, QueuePair*>::iterator itr = pool.find(name);
	QueuePair *qpair = NULL;
	if (itr == pool.end()) {
		qpair = new QueuePair();
		qpair->first = new ObjectQueue();
		qpair->second = new ObjectQueue();
		pool[name] = qpair;
	} else {
		qpair = itr->second;
	}

	return qpair;
}

ObjectQueue* MessageBeanPool::getIdleQueue(const string &name) {
	return getQueuePair(name)->first;
}

ObjectQueue* MessageBeanPool::getActiveQueue(const string &name) {
	return getQueuePair(name)->second;
}

MessageBean* MessageBeanPool::getElement(ObjectQueue *queue) {
	MessageBean *mbean = NULL;
	if (queue->size() > 0) {
		map<int, MessageBean*>::iterator itr = queue->begin();
		for (; itr != queue->end(); ++itr) {
			mbean = itr->second;
			break;
		}

		queue->erase(mbean->__instanceId);
	}

	return mbean;
}

MessageBean* MessageBeanPool::getElement(ObjectQueue *queue, int instanceId) {
	MessageBean *mbean = NULL;
	if (queue->size() > 0) {
		map<int, MessageBean*>::iterator itr = queue->find(instanceId);
		if (itr != queue->end()) {
			mbean = itr->second;
			queue->erase(instanceId);
		}
	}

	return mbean;
}

void MessageBeanPool::print(ostream &os) {
	lock.lock();

	map<string, QueuePair*>::iterator itr = pool.begin();
	string key;
	ObjectQueue *idleQ = NULL, *activeQ = NULL;
	for (; itr != pool.end(); ++itr) {
		key = itr->first;
		idleQ = getIdleQueue(key);
		activeQ = getActiveQueue(key);
		os << setw(20) << key << " : " << "idle=" << setw(3) << idleQ->size()
				<< " active=" << setw(3) << activeQ->size() << endl;
	}

	lock.unlock();
}

MessageBean* MessageBeanPool::borrowObject(const string &name) {
	lock.lock();

	ObjectQueue *idleQueue = getIdleQueue(name);
	MessageBean *mbean = getElement(idleQueue);
	if (mbean == NULL) {
		mbean = factory->createMessageBean(name);
	}

	ObjectQueue *activeQueue = getActiveQueue(name);
	(*activeQueue)[mbean->__instanceId] = mbean;

	lock.unlock();
	return mbean;
}

bool MessageBeanPool::returnObject(MessageBean *mbean) {
	if (mbean == NULL) {
		return false;
	} else if (mbean->__typeId <= 0 || mbean->__instanceId <= 0) {
		return false;
	}

	bool retval = false;
	lock.lock();

	ObjectQueue *activeQueue = getActiveQueue(mbean->__typeName);
	MessageBean *cached = getElement(activeQueue, mbean->__instanceId);
	if (cached != NULL) {
		cached->clear();
		ObjectQueue *idleQueue = getIdleQueue(mbean->__typeName);
		(*idleQueue)[cached->__instanceId] = cached;
		retval = true;
	}

	lock.unlock();
	return retval;
}
