/******************************************************************************
 * protocol.h
 * GENERATED BY MessageCompiler, DON'T MODIFY MANULLY.
 * Generated Time: Tue Jan 22 10:50:24 CST 2013
 *****************************************************************************/

#ifndef ____PROTOCOL____H____
#define ____PROTOCOL____H____

#include <iostream>
#include <string>
#include <vector>
#include <map>

#include "msgbuf.h"

using namespace std;
using namespace msgbuf;

// Begin of namespace protocol
namespace protocol {
///////////////////////////////////////////////////////////////////////////////
class Backpack;
class Broadcast;
class CommandResponse;
class Login;
class NullMessageBean;
class Prop;
class Walk;
class AllType;
class LoginResponse;
class LogoutResponse;
class SceneMap;

class ObjectFactory: public MessageBeanFactory {
public:
	static inline ObjectFactory* instance() {
		static ObjectFactory instance;
		return &instance;
	}

	virtual MessageBean* createMessageBean(const string &typeName);

private:
	map<string, int> mbMap;
	
	ObjectFactory(void);
	~ObjectFactory(void);
	ObjectFactory(const ObjectFactory&);
	ObjectFactory& operator= (const ObjectFactory&);

protected:
	virtual int getTypeId(const string &typeName);
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息Backpack
 * 玩家背包
 * 服务器下发玩家背包中的详细物品列表
 */
class Backpack: public MessageBean
{
	friend class ObjectFactory;
	friend class LoginResponse;

public:
	static Backpack* instance(int cmdId = 0) {
		Backpack *instance = (Backpack*) ObjectFactory::instance()->borrowMessageBean("Backpack");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	Backpack(int cmdId = 0);
	virtual ~Backpack(void);
	
	vector<Prop*> __props;

public:	
	/**
	 * 角色ID
	 */
	int32_t folderNum;

	/**
	 * 背包中的物品列表
	 */
	vector<Prop*>* props;
	Property<Backpack, vector<Prop*>*, READ_ONLY> pProps;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline vector<Prop*>* getProps(void) { return (props = &__props); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息Broadcast
 * 系统广播
 */
class Broadcast: public MessageBean
{
	friend class ObjectFactory;

public:
	static Broadcast* instance(int cmdId = 0) {
		Broadcast *instance = (Broadcast*) ObjectFactory::instance()->borrowMessageBean("Broadcast");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	Broadcast(int cmdId = 0);
	virtual ~Broadcast(void);
	
	string __msg;

public:	
	/**
	 * 系统广播内容
	 */
	string* msg;
	Property<Broadcast, string*, READ_ONLY> pMsg;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline string* getMsg(void) { return (msg = &__msg); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息CommandResponse
 */
class CommandResponse: public MessageBean
{
	friend class ObjectFactory;
	friend class LoginResponse;
	friend class LogoutResponse;
	friend class AllType;
	friend class SceneMap;

public:
	static CommandResponse* instance(int cmdId = 0) {
		CommandResponse *instance = (CommandResponse*) ObjectFactory::instance()->borrowMessageBean("CommandResponse");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	CommandResponse(int cmdId = 0);
	virtual ~CommandResponse(void);
	
	string __errorMessage;

public:	
	/**
	 * 错误码
	 */
	int32_t errorCode;

	/**
	 * 错误信息
	 */
	string* errorMessage;
	Property<CommandResponse, string*, READ_ONLY> pErrorMessage;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline string* getErrorMessage(void) { return (errorMessage = &__errorMessage); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息Login
 * 玩家登陆请求
 */
class Login: public MessageBean
{
	friend class ObjectFactory;

public:
	static Login* instance(int cmdId = 0) {
		Login *instance = (Login*) ObjectFactory::instance()->borrowMessageBean("Login");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	Login(int cmdId = 0);
	virtual ~Login(void);
	
	string __passport;
	string __password;

public:	
	/**
	 * 通行证帐号
	 */
	string* passport;
	Property<Login, string*, READ_ONLY> pPassport;

	/**
	 * 密码
	 */
	string* password;
	Property<Login, string*, READ_ONLY> pPassword;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline string* getPassport(void) { return (passport = &__passport); }
	virtual inline string* getPassword(void) { return (password = &__password); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息NullMessageBean
 * null消息的MessageBean定义
 */
class NullMessageBean: public MessageBean
{
	friend class ObjectFactory;

public:
	static NullMessageBean* instance(int cmdId = 0) {
		NullMessageBean *instance = (NullMessageBean*) ObjectFactory::instance()->borrowMessageBean("NullMessageBean");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	NullMessageBean(int cmdId = 0);
	virtual ~NullMessageBean(void);
	

public:	
	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息Prop
 * 道具
 */
class Prop: public MessageBean
{
	friend class ObjectFactory;
	friend class Backpack;
	friend class AllType;
	friend class SceneMap;

public:
	static Prop* instance(int cmdId = 0) {
		Prop *instance = (Prop*) ObjectFactory::instance()->borrowMessageBean("Prop");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	Prop(int cmdId = 0);
	virtual ~Prop(void);
	
	string __name;
	vector<byte> __image;

public:	
	/**
	 * 道具编号
	 */
	int32_t propId;

	/**
	 * 道具名称
	 */
	string* name;
	Property<Prop, string*, READ_ONLY> pName;

	/**
	 * 道具类型
	 */
	byte type;

	/**
	 * 道具显示图标编号
	 */
	int16_t icon;

	/**
	 * 道具数量
	 */
	int64_t quantity;

	/**
	 * 道具图像PNG流
	 */
	vector<byte>* image;
	Property<Prop, vector<byte>*, READ_ONLY> pImage;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline string* getName(void) { return (name = &__name); }
	virtual inline vector<byte>* getImage(void) { return (image = &__image); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息Walk
 * 玩家走动
 */
class Walk: public MessageBean
{
	friend class ObjectFactory;

public:
	static Walk* instance(int cmdId = 0) {
		Walk *instance = (Walk*) ObjectFactory::instance()->borrowMessageBean("Walk");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	Walk(int cmdId = 0);
	virtual ~Walk(void);
	

public:	
	/**
	 * 当前位置X坐标
	 */
	int32_t x;

	/**
	 * 当前位置Y坐标
	 */
	int32_t y;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息AllType
 * 测试所有可能的数据类型
 */
class AllType: public CommandResponse
{
	friend class ObjectFactory;
	friend class CommandResponse;

public:
	static AllType* instance(int cmdId = 0) {
		AllType *instance = (AllType*) ObjectFactory::instance()->borrowMessageBean("AllType");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	AllType(int cmdId = 0);
	virtual ~AllType(void);
	
	vector<byte> __byteVector;
	varray<byte> __byteArray;
	vector<bool> __boolVector;
	varray<bool> __boolArray;
	vector<int16_t> __shortVector;
	varray<int16_t> __shortArray;
	vector<int32_t> __intVector;
	varray<int32_t> __intArray;
	vector<int64_t> __longVector;
	varray<int64_t> __longArray;
	vector<float> __floatVector;
	varray<float> __floatArray;
	vector<double> __doubleVector;
	varray<double> __doubleArray;
	string __stringValue;
	vector<string> __stringVector;
	Prop __prop;
	vector<Prop*> __propVector;
	map<int, Prop*> __folders;

public:	
	/**
	 */
	byte byteValue;

	/**
	 */
	vector<byte>* byteVector;
	Property<AllType, vector<byte>*, READ_ONLY> pByteVector;

	/**
	 */
	varray<byte>* byteArray;
	Property<AllType, varray<byte>*, READ_ONLY> pByteArray;

	/**
	 */
	bool boolValue;

	/**
	 */
	vector<bool>* boolVector;
	Property<AllType, vector<bool>*, READ_ONLY> pBoolVector;

	/**
	 */
	varray<bool>* boolArray;
	Property<AllType, varray<bool>*, READ_ONLY> pBoolArray;

	/**
	 */
	int16_t shortValue;

	/**
	 */
	vector<int16_t>* shortVector;
	Property<AllType, vector<int16_t>*, READ_ONLY> pShortVector;

	/**
	 */
	varray<int16_t>* shortArray;
	Property<AllType, varray<int16_t>*, READ_ONLY> pShortArray;

	/**
	 */
	int32_t intValue;

	/**
	 */
	vector<int32_t>* intVector;
	Property<AllType, vector<int32_t>*, READ_ONLY> pIntVector;

	/**
	 */
	varray<int32_t>* intArray;
	Property<AllType, varray<int32_t>*, READ_ONLY> pIntArray;

	/**
	 */
	int64_t longValue;

	/**
	 */
	vector<int64_t>* longVector;
	Property<AllType, vector<int64_t>*, READ_ONLY> pLongVector;

	/**
	 */
	varray<int64_t>* longArray;
	Property<AllType, varray<int64_t>*, READ_ONLY> pLongArray;

	/**
	 */
	float floatValue;

	/**
	 */
	vector<float>* floatVector;
	Property<AllType, vector<float>*, READ_ONLY> pFloatVector;

	/**
	 */
	varray<float>* floatArray;
	Property<AllType, varray<float>*, READ_ONLY> pFloatArray;

	/**
	 */
	double doubleValue;

	/**
	 */
	vector<double>* doubleVector;
	Property<AllType, vector<double>*, READ_ONLY> pDoubleVector;

	/**
	 */
	varray<double>* doubleArray;
	Property<AllType, varray<double>*, READ_ONLY> pDoubleArray;

	/**
	 */
	string* stringValue;
	Property<AllType, string*, READ_ONLY> pStringValue;

	/**
	 */
	vector<string>* stringVector;
	Property<AllType, vector<string>*, READ_ONLY> pStringVector;

	/**
	 */
	Prop* prop;
	Property<AllType, Prop*, READ_ONLY> pProp;

	/**
	 */
	vector<Prop*>* propVector;
	Property<AllType, vector<Prop*>*, READ_ONLY> pPropVector;

	/**
	 */
	map<int, Prop*>* folders;
	Property<AllType, map<int, Prop*>*, READ_ONLY> pFolders;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline vector<byte>* getByteVector(void) { return (byteVector = &__byteVector); }
	virtual inline varray<byte>* getByteArray(void) { return (byteArray = &__byteArray); }
	virtual inline vector<bool>* getBoolVector(void) { return (boolVector = &__boolVector); }
	virtual inline varray<bool>* getBoolArray(void) { return (boolArray = &__boolArray); }
	virtual inline vector<int16_t>* getShortVector(void) { return (shortVector = &__shortVector); }
	virtual inline varray<int16_t>* getShortArray(void) { return (shortArray = &__shortArray); }
	virtual inline vector<int32_t>* getIntVector(void) { return (intVector = &__intVector); }
	virtual inline varray<int32_t>* getIntArray(void) { return (intArray = &__intArray); }
	virtual inline vector<int64_t>* getLongVector(void) { return (longVector = &__longVector); }
	virtual inline varray<int64_t>* getLongArray(void) { return (longArray = &__longArray); }
	virtual inline vector<float>* getFloatVector(void) { return (floatVector = &__floatVector); }
	virtual inline varray<float>* getFloatArray(void) { return (floatArray = &__floatArray); }
	virtual inline vector<double>* getDoubleVector(void) { return (doubleVector = &__doubleVector); }
	virtual inline varray<double>* getDoubleArray(void) { return (doubleArray = &__doubleArray); }
	virtual inline string* getStringValue(void) { return (stringValue = &__stringValue); }
	virtual inline vector<string>* getStringVector(void) { return (stringVector = &__stringVector); }
	virtual inline Prop* getProp(void) { return (prop = &__prop); }
	virtual inline vector<Prop*>* getPropVector(void) { return (propVector = &__propVector); }
	virtual inline map<int, Prop*>* getFolders(void) { return (folders = &__folders); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息LoginResponse
 * 玩家登陆结果
 */
class LoginResponse: public CommandResponse
{
	friend class ObjectFactory;
	friend class CommandResponse;

public:
	static LoginResponse* instance(int cmdId = 0) {
		LoginResponse *instance = (LoginResponse*) ObjectFactory::instance()->borrowMessageBean("LoginResponse");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	LoginResponse(int cmdId = 0);
	virtual ~LoginResponse(void);
	
	Backpack __backpack;

public:	
	/**
	 * 玩家角色id
	 */
	int64_t playerId;

	/**
	 * 玩家的背包信息
	 */
	Backpack* backpack;
	Property<LoginResponse, Backpack*, READ_ONLY> pBackpack;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline Backpack* getBackpack(void) { return (backpack = &__backpack); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息LogoutResponse
 * 登出游戏响应
 */
class LogoutResponse: public CommandResponse
{
	friend class ObjectFactory;
	friend class CommandResponse;

public:
	static LogoutResponse* instance(int cmdId = 0) {
		LogoutResponse *instance = (LogoutResponse*) ObjectFactory::instance()->borrowMessageBean("LogoutResponse");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	LogoutResponse(int cmdId = 0);
	virtual ~LogoutResponse(void);
	
	string __grace;

public:	
	/**
	 */
	string* grace;
	Property<LogoutResponse, string*, READ_ONLY> pGrace;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline string* getGrace(void) { return (grace = &__grace); }
};

///////////////////////////////////////////////////////////////////////////////
/**
 * 消息SceneMap
 * 地图数据
 */
class SceneMap: public CommandResponse
{
	friend class ObjectFactory;
	friend class CommandResponse;

public:
	static SceneMap* instance(int cmdId = 0) {
		SceneMap *instance = (SceneMap*) ObjectFactory::instance()->borrowMessageBean("SceneMap");
		instance->commandId = cmdId;
		return instance;
	}
		
private:
	SceneMap(int cmdId = 0);
	virtual ~SceneMap(void);
	
	vector<byte> __data;
	Prop __prop;

public:	
	/**
	 * 地图编号
	 */
	int32_t mapId;

	/**
	 * 地图数据
	 */
	vector<byte>* data;
	Property<SceneMap, vector<byte>*, READ_ONLY> pData;

	/**
	 * 测试单个Bean
	 */
	Prop* prop;
	Property<SceneMap, Prop*, READ_ONLY> pProp;

	
	virtual void serialize(ostream &output);
	virtual void serialize(MessageBeanFormat &format);
	virtual void deserialize(istream &input);
    virtual void deserialize(MessageBeanFormat &format);
    virtual void clear(void);
    virtual void release();
	
	virtual inline vector<byte>* getData(void) { return (data = &__data); }
	virtual inline Prop* getProp(void) { return (prop = &__prop); }
};


// End of namespace protocol
}

#endif
