/*
 * msgbuf.h
 *
 *  Created on: 2012-11-21
 *      Author: gaobo.benny
 */

#ifndef MSGBUF_H_
#define MSGBUF_H_

#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <queue>
#include <typeinfo>

#include <assert.h>
#include <stdint.h>
#include <string.h>
#include <zlib.h>

using namespace std;

#define LLABS(v)	((v) < 0 ? -(v) : (v))

#define READ_ONLY	1
#define WRITE_ONLY	2
#define READ_WRITE	3

template<typename Container, typename ValueType, int nPropType>
class Property {
public:
	Property() {
		m_cObject = NULL;
		Set = NULL;
		Get = NULL;
	}

	//-- Set a pointer to the class that contain the Property --
	void setContainer(Container* cObject) {
		m_cObject = cObject;
	}

	//-- Set the set member function that will change the value --
	void setter(void (Container::*pSet)(ValueType value)) {
		if ((nPropType == WRITE_ONLY) || (nPropType == READ_WRITE))
			Set = pSet;
		else
			Set = NULL;
	}

	//-- Set the get member function that will retrieve the value --
	void getter(ValueType (Container::*pGet)()) {
		if ((nPropType == READ_ONLY) || (nPropType == READ_WRITE))
			Get = pGet;
		else
			Get = NULL;
	}

	//-- Overload the = operator to set the value using the set member --
	ValueType operator =(const ValueType& value) {
		assert(m_cObject != NULL);
		assert(Set != NULL);
		(m_cObject->*Set)(value);
		return value;
	}

	//-- Cast the Property cl​ass to the internal type --
	operator ValueType() {
		assert(m_cObject != NULL);
		assert(Get != NULL);
		return (m_cObject->*Get)();
	}

private:
	//-- Pointer to the module that contains the Property --
	Container* m_cObject;
	//-- Pointer to set member function --
	void (Container::*Set)(ValueType value);
	//-- Pointer to get member function --
	ValueType (Container::*Get)();
};

///////////////////////////////////////////////////////////////////////////////
// begin msgbuf namespace
///////////////////////////////////////////////////////////////////////////////
namespace msgbuf {

/**
 * Zlib wrapper for C++ std::iostream.
 * http://www.zlib.net/zlib_how.html
 */
class ZlibWrapper {
private:
	int decompressSegment(z_stream *strm, char *segment, size_t offset,
			size_t length, ostream &output);

	int compressSegment(z_stream *strm, char *segment, size_t offset,
			size_t length, bool eof, ostream &output);

	size_t availBytes(istream &input);

public:
	int decompress(istream &input, ostream &output);
	int compress(istream &input, ostream &output);
};

template<typename T>
class varray {
private:
	T* m_array;
	size_t m_size;

public:
	varray(int size) :
			varray() {
		this->m_array = new T[size];
		this->m_size = size;
	}

	varray() {
		m_array = NULL;
		m_size = 0;
	}

	~varray() {
		clear();
	}

	T& operator[](int idx) {
		return m_array[idx];
	}

	void resize(size_t size) {
		if (m_size == size) {
			return;
		} else if (m_array != NULL) {
			delete m_array;
			m_array = NULL;
		}

		if (size > 0) {
			m_array = new T[size];
		}

		m_size = size;
	}

	int size() {
		return m_size;
	}

	T* array() {
		return m_array;
	}

	void clear() {
		if (m_array != NULL) {
			delete m_array;
		}

		m_array = NULL;
		m_size = 0;
	}
};

typedef unsigned char byte;

const static byte MB_NULL = 0XFF;
const static byte MB_NOT_NULL = 0;
const static byte MB_TRUE = 0XFF;
const static byte MB_FALSE = 0;

typedef enum {
	dt_none,
	dt_byte,
	dt_boolean,
	dt_short,
	dt_int,
	dt_long,
	dt_float,
	dt_double,
	dt_string,
	dt_message,
	dt_message_ptr,
	dt_vector,
	dt_array,
	dt_map
} DataType;

#define CAST_PTR(t, p)	((t*) p)
#define DEFAULT_BUFFER_SIZE	1024

typedef enum {
	COMPRESS, DECOMPRESS,
} CompressionMode;
//
//class IoBuffer {
//private:
//	byte *m_buffer;
//
//	size_t m_capacity;
//	size_t m_current;
//	size_t m_limit;
//
//	int inflateTo(IoBuffer&) const;
//	int deflateTo(IoBuffer&) const;
//
//public:
//	IoBuffer(size_t init_capa = DEFAULT_BUFFER_SIZE);
//	virtual ~IoBuffer();
//
//	inline size_t capacity(void) {
//		return m_capacity;
//	}
//
//	inline size_t limit(void) {
//		return m_limit;
//	}
//
//	inline size_t current(void) {
//		return m_current;
//	}
//
//	inline byte* buffer(void) {
//		return m_buffer;
//	}
//
//	virtual int extendTo(size_t size);
//	virtual int extend(void);
//	virtual int extend(size_t increment);
//
//	virtual void write(byte b);
//	virtual int read(void);
//
//	virtual int put(byte *array, int start, int length);
//	virtual int get(byte *array, int start, int length);
//
//	virtual void rewind(void);
//	virtual void flip(void);
//	virtual void clear(void);
//
//	virtual size_t inputRemaining(void) const;
//	virtual size_t outputRemaining(void) const;
//
//	virtual int copy(IoBuffer&) const;
//	virtual int copy(IoBuffer&, CompressionMode) const;
//};

class MessageBean;
class MessageBeanFactory;
class BinaryMessageBeanFormat;
class TextMessageBeanFormat;

class ReentrantLock {
private:
	pthread_mutex_t mutex;

public:
	ReentrantLock(void);
	~ReentrantLock();

	void lock(void);
	void unlock(void);
};

class MessageBeanFormat;

class MessageBean {
public:
	int __typeId;
	string __typeName;
	int __instanceId;

	int commandId;

	MessageBean(int cmdId = 0) :
			commandId(cmdId) {
		__instanceId = 0;
		__typeId = 0;
	}

	virtual ~MessageBean() {
	}

	virtual void serialize(ostream &output) {
	}
	virtual void serialize(MessageBeanFormat &format) {
	}

	virtual void deserialize(istream &input) {
	}
	virtual void deserialize(MessageBeanFormat &format) {
	}

	virtual void clear(void) {
		commandId = 0;
	}

	virtual void release() {
	}

	virtual void print(ostream &os);
};

typedef map<int, MessageBean*> ObjectQueue;
typedef pair<ObjectQueue*, ObjectQueue*> QueuePair;

class MessageBeanPool {
private:
	map<string, QueuePair*> pool;
	MessageBeanFactory *factory;
	ReentrantLock lock;

	QueuePair* getQueuePair(const string&);
	ObjectQueue* getIdleQueue(const string&);
	ObjectQueue* getActiveQueue(const string&);

	MessageBean* getElement(ObjectQueue*);
	MessageBean* getElement(ObjectQueue*, int);
public:
	MessageBeanPool(MessageBeanFactory *factory);
	~MessageBeanPool();

	MessageBean* borrowObject(const string&);
	bool returnObject(MessageBean*);
	void print(ostream&);
};

class MessageBeanFactory {
private:
	int m_instanceId;
	MessageBeanPool *m_pool;
	MessageBeanFormat *m_recycler;

protected:
	ReentrantLock lock;
	int getInstanceId(void);
	virtual int getTypeId(const string&) = 0;

public:
	MessageBeanFactory(void);
	virtual ~MessageBeanFactory(void);

	inline MessageBeanPool* pool() const {
		return m_pool;
	}

	virtual MessageBean* createMessageBean(const string&) = 0;

	virtual MessageBean* borrowMessageBean(const string&);
	virtual bool returnMessageBean(MessageBean*);
};

typedef struct {
	byte byteValue;
	bool boolValue;
	int16_t shortValue;
	int intValue;
	int64_t longValue;
	float floatValue;
	double doubleValue;
	void* pointerValue;
	string stringValue;
} FieldValue;

class MessageBeanFormat {
public:
	virtual ~MessageBeanFormat() {
	}

	inline bool isBasicType(DataType type) {
		return type == dt_byte || type == dt_boolean || type == dt_short
				|| type == dt_int || type == dt_long || type == dt_float
				|| type == dt_double;
	}

	inline bool isValueType(DataType type) {
		return isBasicType(type) || type == dt_string;
	}

	virtual bool readIsNull(void) = 0;
	virtual bool writeIsNull(void*) = 0;
	virtual int writeSize(size_t) = 0;
	virtual int readSize(void) = 0;
	virtual void writeField(const char*, const char*, const DataType,
			const DataType, const int, void*) = 0;
	virtual void* readField(const char*, const DataType, void*) = 0;

	virtual void beginWriteBean(const type_info&, const type_info&,
			const char*) {
	}
	virtual void endWriteBean(const type_info&, const type_info&, const char*) {
	}
	virtual void beginWriteVector(const char *prototype, void *ptr) {
	}
	virtual void endWriteVector(const char *prototype, void *ptr) {
	}
	virtual void beginWriteArray(const char *prototype) {
	}
	virtual void endWriteArray(void) {
	}
	virtual void beginWriteMap(const char *prototype, void *ptr) {
	}
	virtual void endWriteMap(const char *prototype, void *ptr) {
	}

	template<typename K, typename V> int writeContainerIndicator(DataType type,
			void *cptr) {
		int len = -1;
		if (writeIsNull(cptr)) {
			len = -1;
		} else {
			if (type == dt_vector) {
				vector<K> *pvec = (vector<K>*) cptr;
				len = writeSize(pvec->size());
			} else if (type == dt_array) {
				varray<K> *pary = (varray<K>*) cptr;
				len = writeSize(pary->size());
			} else {
				map<K, V> *pmap = (map<K, V>*) cptr;
				len = writeSize(pmap->size());
			}
		}

		return len;
	}

	int readContainerIndicator() {
		if (readIsNull()) {
			return -1;
		} else {
			return readSize();
		}

	}

	template<typename T> T* read(T* pf, const char *typeName,
			DataType dataType) {
		return (T*) readField(typeName, dataType, pf);
	}

	template<typename T> vector<T>* readVector(vector<T>* pf,
			const char *typeName, DataType dataType) {
		int len = readContainerIndicator();
		if (len < 0) {
			return NULL;
		}

		T *elem;
		for (int i = 0; i < len; ++i) {
			elem = read<T>(NULL, typeName, dataType);
			pf->push_back(*elem);
		}

		return pf;
	}

	template<typename T> varray<T>* readArray(varray<T>* pf,
			const char *typeName, DataType dataType) {
		int len = readContainerIndicator();
		if (len < 0) {
			return NULL;
		}

		pf->resize(len);
		T *elem;
		for (int i = 0; i < len; ++i) {
			elem = read<T>(NULL, typeName, dataType);
			(*pf)[i] = *elem;
		}

		return pf;
	}

	template<typename K, typename V> map<K, V>* readMap(map<K, V>* pf,
			const char *keyTypeName, DataType keyDataType,
			const char *valueTypeName, DataType valueDataType) {
		int len = readContainerIndicator();
		if (len < 0) {
			return NULL;
		}

		vector<K> key_vec;
		vector<V> value_vec;
		K *pk;
		V *pv;
		for (int i = 0; i < len; ++i) {
			pk = (K*) read<K>(NULL, keyTypeName, keyDataType);
			key_vec.push_back(*pk);

			pv = (V*) read<V>(NULL, valueTypeName, valueDataType);
			value_vec.push_back(*pv);
		}

		for (int i = 0; i < len; ++i) {
			(*pf)[key_vec[i]] = value_vec[i];
		}

		key_vec.clear();
		value_vec.clear();
		return pf;

	}

	/**
	 * 向format中写入一个数据项
	 * @param pf 数据的指针
	 * @param prototype 数据申明的原型，例如: int folderNum
	 * @param typeName 数据的类型名称，当数据是MessageBean时，是MessageBean的类名。
	 * @param dataType 数据的类型，当MessageBean被放在容器类中时，是dt_message_ptr。
	 * @param containerType 数据被放置的容器(dt_vector/dt_map)，如果数据没有被放在容器中，是dt_none。
	 * @param index 数据在容器中的位置编号，从0开始。
	 */
	template<typename T> void write(T* pf, const char *prototype,
			const char *typeName, DataType dataType, DataType containerType =
					dt_none, int index = 0) {
		writeField(prototype, typeName, dataType, containerType, index, pf);
	}

	template<typename T> void writeVector(vector<T>* vp, const char *prototype,
			const char *typeName, DataType dataType) {
		beginWriteVector(prototype, vp);
		int len = writeContainerIndicator<T, T>(dt_vector, vp);
		if (len >= 0) {
			T elem;
			for (int i = 0; i < len; ++i) {
				elem = (*vp)[i];
				write<T>(&elem, NULL, typeName, dataType, dt_vector, i);
			}
		}
		endWriteVector(prototype, vp);
	}

	template<typename T> void writeArray(varray<T>* vp, const char *prototype,
			const char *typeName, DataType dataType) {
		beginWriteVector(prototype, vp);
		int len = writeContainerIndicator<T, T>(dt_array, vp);
		if (len >= 0) {
			T elem;
			for (int i = 0; i < len; ++i) {
				elem = (*vp)[i];
				write<T>(&elem, NULL, typeName, dataType, dt_array, i);
			}
		}
		endWriteVector(prototype, vp);
	}

	template<typename K, typename V> void writeMap(map<K, V> *mp,
			const char *prototype, const char *keyTypeName,
			DataType keyDataType, const char *valueTypeName,
			DataType valueDataType) {
		beginWriteMap(prototype, mp);
		int len = writeContainerIndicator<K, V>(dt_map, mp);
		if (len >= 0) {
			typename map<K, V>::iterator it;
			K key;
			V value;
			for (it = mp->begin(); it != mp->end(); ++it) {
				key = it->first;
				value = it->second;
				write<K>(&key, NULL, keyTypeName, keyDataType, dt_map, 0);
				write<V>(&value, NULL, valueTypeName, valueDataType, dt_map, 1);
			}
		}
		endWriteMap(prototype, mp);
	}
};

class BinaryMessageBeanFormat: public MessageBeanFormat {
private:
	istream *input;
	ostream *output;

	MessageBeanFactory *factory;
	FieldValue fieldValue;

	void reverseBytesOrder(byte carray[], int offset, int len);
	void* getFieldValuePtr(DataType);

	int putData(byte *array, int start, int length);
	int getData(byte *array, int start, int length);

	int16_t readInt16(void);
	void writeInt16(int16_t v);

	float readFloat(void);
	void writeFloat(float v);

	double readDouble(void);
	void writeDouble(double v);

	int numberOfLeadingZeros(uint64_t);

	string* readString(string*);
	void writeString(string*);

	void writeVarint(int64_t v);
	int64_t readVarint(void);

public:
	BinaryMessageBeanFormat(MessageBeanFactory *factory);
	virtual ~BinaryMessageBeanFormat(void);

	MessageBean* readBean(const char *typeName, void* pm);
	void writeBean(MessageBean *bean);

	inline void setOutput(ostream *output) {
		this->output = output;
	}

	inline void setInput(istream *input) {
		this->input = input;
	}

	virtual bool readIsNull(void);
	virtual bool writeIsNull(void*);
	virtual int writeSize(size_t);
	virtual int readSize(void);
	virtual void writeField(const char*, const char*, const DataType,
			const DataType, const int, void*);
	virtual void* readField(const char*, const DataType, void*);
};

class WriteOnlyMessageBeanFormat: public MessageBeanFormat {
public:
	virtual bool readIsNull(void) {
		return true;
	}

	virtual int readSize(void) {
		return -1;
	}

	virtual void* readField(const char*, const DataType, void*) {
		return NULL;
	}

	virtual bool writeIsNull(void *ptr) {
		return ptr == NULL;
	}

	virtual int writeSize(size_t size) {
		return size;
	}

	virtual void beginWriteBean(const type_info &it, const type_info &st,
			const char *typeName) {
	}

	virtual void endWriteBean(const type_info&, const type_info&, const char*) {
	}

	virtual void beginWriteVector(const char *prototype, void *ptr) {
	}

	virtual void endWriteVector(const char *prototype, void *ptr) {
	}

	virtual void beginWriteMap(const char *prototype, void *ptr) {
	}

	virtual void endWriteMap(const char *prototype, void *ptr) {
	}
};

class TextMessageBeanFormat: public WriteOnlyMessageBeanFormat {
private:
	ostream& os;
	int indentLevel;

	void indent(void);

public:
	TextMessageBeanFormat(ostream& outs) :
			os(outs) {
		indentLevel = 0;
	}

	~TextMessageBeanFormat(void) {
	}

	virtual bool writeIsNull(void *ptr) {
		return ptr == NULL;
	}

	virtual int writeSize(size_t size) {
		return size;
	}

	virtual void writeField(const char *prototype, const char *typeName,
			const DataType dataType, const DataType containerType,
			const int index, void *pf);
	virtual void beginWriteBean(const type_info &it, const type_info &st,
			const char *typeName);
	virtual void endWriteBean(const type_info&, const type_info&, const char*);
	virtual void beginWriteVector(const char *prototype, void *ptr);
	virtual void endWriteVector(const char *prototype, void *ptr);
	virtual void beginWriteMap(const char *prototype, void *ptr);
	virtual void endWriteMap(const char *prototype, void *ptr);
};

class ReleaseMessageBeanFormat: public WriteOnlyMessageBeanFormat {
private:
	MessageBeanFactory *factory;

public:
	ReleaseMessageBeanFormat(MessageBeanFactory *factory) :
			factory(factory) {
	}

	~ReleaseMessageBeanFormat(void) {
	}

	virtual void writeField(const char *prototype, const char *typeName,
			const DataType dataType, const DataType containerType,
			const int index, void *pf);
};

///////////////////////////////////////////////////////////////////////////////
// end msgbuf namespace
///////////////////////////////////////////////////////////////////////////////
}

#endif /* MSGBUF_H_ */
