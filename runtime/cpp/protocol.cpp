/******************************************************************************
 * protocol.cpp
 * GENERATED BY MessageCompiler, DON'T MODIFY MANULLY.
 * Generated Time: Tue Jan 22 10:50:24 CST 2013
 *****************************************************************************/

#include <iostream>
#include <string>
#include <vector>
#include <map>

#include "msgbuf.h"
#include "protocol.h"

using namespace std;
using namespace msgbuf;
using namespace protocol;

///////////////////////////////////////////////////////////////////////////////
// Implementation of Backpack
///////////////////////////////////////////////////////////////////////////////	
Backpack::Backpack(int cmdId) : MessageBean(cmdId) {
	__typeId = 4;
	__typeName = "Backpack";
		
	clear();

	pProps.setContainer(this);
	pProps.getter(&Backpack::getProps);

}

Backpack::~Backpack(void) {
}

void Backpack::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void Backpack::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(Backpack), "Backpack");

	// serialize base
	MessageBean::serialize(format);

	// serialize field: folderNum
	format.write<int32_t>(&this->folderNum, "int folderNum", "int32_t", dt_int, dt_none, 0);
	// serialize field: props
	format.writeVector<Prop*>(this->props, "vector<Prop> props", "Prop", dt_message_ptr);

	format.endWriteBean(typeid(*this), typeid(Backpack), "Backpack");
}

void Backpack::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void Backpack::deserialize(MessageBeanFormat &format) {
	// serialize base
	MessageBean::deserialize(format);

	// deserialize field: folderNum
	format.read<int32_t>(&this->folderNum, "int32_t", dt_int);
	// deserialize field: props
	props = format.readVector<Prop*>(&this->__props, "Prop", dt_message_ptr);
}
    
void Backpack::clear(void) {
   	// clear base
   	MessageBean::clear();
    	
	// clear field: folderNum
	folderNum = 0;
	// clear field: props
	__props.clear();
	props = NULL;
}

void Backpack::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of Broadcast
///////////////////////////////////////////////////////////////////////////////	
Broadcast::Broadcast(int cmdId) : MessageBean(cmdId) {
	__typeId = 9;
	__typeName = "Broadcast";
		
	clear();

	pMsg.setContainer(this);
	pMsg.getter(&Broadcast::getMsg);

}

Broadcast::~Broadcast(void) {
}

void Broadcast::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void Broadcast::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(Broadcast), "Broadcast");

	// serialize base
	MessageBean::serialize(format);

	// serialize field: msg
	format.write<string>(this->msg, "string msg", "string", dt_string, dt_none, 0);

	format.endWriteBean(typeid(*this), typeid(Broadcast), "Broadcast");
}

void Broadcast::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void Broadcast::deserialize(MessageBeanFormat &format) {
	// serialize base
	MessageBean::deserialize(format);

	// deserialize field: msg
	msg = format.read<string>(&this->__msg, "string", dt_string);
}
    
void Broadcast::clear(void) {
   	// clear base
   	MessageBean::clear();
    	
	// clear field: msg
	__msg.clear();
	msg = NULL;
}

void Broadcast::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of CommandResponse
///////////////////////////////////////////////////////////////////////////////	
CommandResponse::CommandResponse(int cmdId) : MessageBean(cmdId) {
	__typeId = 3;
	__typeName = "CommandResponse";
		
	clear();

	pErrorMessage.setContainer(this);
	pErrorMessage.getter(&CommandResponse::getErrorMessage);

}

CommandResponse::~CommandResponse(void) {
}

void CommandResponse::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void CommandResponse::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(CommandResponse), "CommandResponse");

	// serialize base
	MessageBean::serialize(format);

	// serialize field: errorCode
	format.write<int32_t>(&this->errorCode, "int errorCode", "int32_t", dt_int, dt_none, 0);
	// serialize field: errorMessage
	format.write<string>(this->errorMessage, "string errorMessage", "string", dt_string, dt_none, 0);

	format.endWriteBean(typeid(*this), typeid(CommandResponse), "CommandResponse");
}

void CommandResponse::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void CommandResponse::deserialize(MessageBeanFormat &format) {
	// serialize base
	MessageBean::deserialize(format);

	// deserialize field: errorCode
	format.read<int32_t>(&this->errorCode, "int32_t", dt_int);
	// deserialize field: errorMessage
	errorMessage = format.read<string>(&this->__errorMessage, "string", dt_string);
}
    
void CommandResponse::clear(void) {
   	// clear base
   	MessageBean::clear();
    	
	// clear field: errorCode
	errorCode = 0;
	// clear field: errorMessage
	__errorMessage.clear();
	errorMessage = NULL;
}

void CommandResponse::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of Login
///////////////////////////////////////////////////////////////////////////////	
Login::Login(int cmdId) : MessageBean(cmdId) {
	__typeId = 6;
	__typeName = "Login";
		
	clear();

	pPassport.setContainer(this);
	pPassport.getter(&Login::getPassport);

	pPassword.setContainer(this);
	pPassword.getter(&Login::getPassword);

}

Login::~Login(void) {
}

void Login::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void Login::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(Login), "Login");

	// serialize base
	MessageBean::serialize(format);

	// serialize field: passport
	format.write<string>(this->passport, "string passport", "string", dt_string, dt_none, 0);
	// serialize field: password
	format.write<string>(this->password, "string password", "string", dt_string, dt_none, 0);

	format.endWriteBean(typeid(*this), typeid(Login), "Login");
}

void Login::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void Login::deserialize(MessageBeanFormat &format) {
	// serialize base
	MessageBean::deserialize(format);

	// deserialize field: passport
	passport = format.read<string>(&this->__passport, "string", dt_string);
	// deserialize field: password
	password = format.read<string>(&this->__password, "string", dt_string);
}
    
void Login::clear(void) {
   	// clear base
   	MessageBean::clear();
    	
	// clear field: passport
	__passport.clear();
	passport = NULL;
	// clear field: password
	__password.clear();
	password = NULL;
}

void Login::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of NullMessageBean
///////////////////////////////////////////////////////////////////////////////	
NullMessageBean::NullMessageBean(int cmdId) : MessageBean(cmdId) {
	__typeId = 1;
	__typeName = "NullMessageBean";
		
	clear();

}

NullMessageBean::~NullMessageBean(void) {
}

void NullMessageBean::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void NullMessageBean::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(NullMessageBean), "NullMessageBean");

	// serialize base
	MessageBean::serialize(format);


	format.endWriteBean(typeid(*this), typeid(NullMessageBean), "NullMessageBean");
}

void NullMessageBean::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void NullMessageBean::deserialize(MessageBeanFormat &format) {
	// serialize base
	MessageBean::deserialize(format);

}
    
void NullMessageBean::clear(void) {
   	// clear base
   	MessageBean::clear();
    	
}

void NullMessageBean::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of Prop
///////////////////////////////////////////////////////////////////////////////	
Prop::Prop(int cmdId) : MessageBean(cmdId) {
	__typeId = 2;
	__typeName = "Prop";
		
	clear();

	pName.setContainer(this);
	pName.getter(&Prop::getName);

	pImage.setContainer(this);
	pImage.getter(&Prop::getImage);

}

Prop::~Prop(void) {
}

void Prop::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void Prop::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(Prop), "Prop");

	// serialize base
	MessageBean::serialize(format);

	// serialize field: propId
	format.write<int32_t>(&this->propId, "int propId", "int32_t", dt_int, dt_none, 0);
	// serialize field: name
	format.write<string>(this->name, "string name", "string", dt_string, dt_none, 0);
	// serialize field: type
	format.write<byte>(&this->type, "byte type", "byte", dt_byte, dt_none, 0);
	// serialize field: icon
	format.write<int16_t>(&this->icon, "short icon", "int16_t", dt_short, dt_none, 0);
	// serialize field: quantity
	format.write<int64_t>(&this->quantity, "long quantity", "int64_t", dt_long, dt_none, 0);
	// serialize field: image
	format.writeVector<byte>(this->image, "vector<byte> image", "byte", dt_byte);

	format.endWriteBean(typeid(*this), typeid(Prop), "Prop");
}

void Prop::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void Prop::deserialize(MessageBeanFormat &format) {
	// serialize base
	MessageBean::deserialize(format);

	// deserialize field: propId
	format.read<int32_t>(&this->propId, "int32_t", dt_int);
	// deserialize field: name
	name = format.read<string>(&this->__name, "string", dt_string);
	// deserialize field: type
	format.read<byte>(&this->type, "byte", dt_byte);
	// deserialize field: icon
	format.read<int16_t>(&this->icon, "int16_t", dt_short);
	// deserialize field: quantity
	format.read<int64_t>(&this->quantity, "int64_t", dt_long);
	// deserialize field: image
	image = format.readVector<byte>(&this->__image, "byte", dt_byte);
}
    
void Prop::clear(void) {
   	// clear base
   	MessageBean::clear();
    	
	// clear field: propId
	propId = 0;
	// clear field: name
	__name.clear();
	name = NULL;
	// clear field: type
	type = 0;
	// clear field: icon
	icon = 0;
	// clear field: quantity
	quantity = 0L;
	// clear field: image
	__image.clear();
	image = NULL;
}

void Prop::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of Walk
///////////////////////////////////////////////////////////////////////////////	
Walk::Walk(int cmdId) : MessageBean(cmdId) {
	__typeId = 8;
	__typeName = "Walk";
		
	clear();

}

Walk::~Walk(void) {
}

void Walk::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void Walk::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(Walk), "Walk");

	// serialize base
	MessageBean::serialize(format);

	// serialize field: x
	format.write<int32_t>(&this->x, "int x", "int32_t", dt_int, dt_none, 0);
	// serialize field: y
	format.write<int32_t>(&this->y, "int y", "int32_t", dt_int, dt_none, 0);

	format.endWriteBean(typeid(*this), typeid(Walk), "Walk");
}

void Walk::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void Walk::deserialize(MessageBeanFormat &format) {
	// serialize base
	MessageBean::deserialize(format);

	// deserialize field: x
	format.read<int32_t>(&this->x, "int32_t", dt_int);
	// deserialize field: y
	format.read<int32_t>(&this->y, "int32_t", dt_int);
}
    
void Walk::clear(void) {
   	// clear base
   	MessageBean::clear();
    	
	// clear field: x
	x = 0;
	// clear field: y
	y = 0;
}

void Walk::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of AllType
///////////////////////////////////////////////////////////////////////////////	
AllType::AllType(int cmdId) : CommandResponse(cmdId) {
	__typeId = 11;
	__typeName = "AllType";
		
	clear();

	pByteVector.setContainer(this);
	pByteVector.getter(&AllType::getByteVector);

	pByteArray.setContainer(this);
	pByteArray.getter(&AllType::getByteArray);

	pBoolVector.setContainer(this);
	pBoolVector.getter(&AllType::getBoolVector);

	pBoolArray.setContainer(this);
	pBoolArray.getter(&AllType::getBoolArray);

	pShortVector.setContainer(this);
	pShortVector.getter(&AllType::getShortVector);

	pShortArray.setContainer(this);
	pShortArray.getter(&AllType::getShortArray);

	pIntVector.setContainer(this);
	pIntVector.getter(&AllType::getIntVector);

	pIntArray.setContainer(this);
	pIntArray.getter(&AllType::getIntArray);

	pLongVector.setContainer(this);
	pLongVector.getter(&AllType::getLongVector);

	pLongArray.setContainer(this);
	pLongArray.getter(&AllType::getLongArray);

	pFloatVector.setContainer(this);
	pFloatVector.getter(&AllType::getFloatVector);

	pFloatArray.setContainer(this);
	pFloatArray.getter(&AllType::getFloatArray);

	pDoubleVector.setContainer(this);
	pDoubleVector.getter(&AllType::getDoubleVector);

	pDoubleArray.setContainer(this);
	pDoubleArray.getter(&AllType::getDoubleArray);

	pStringValue.setContainer(this);
	pStringValue.getter(&AllType::getStringValue);

	pStringVector.setContainer(this);
	pStringVector.getter(&AllType::getStringVector);

	pProp.setContainer(this);
	pProp.getter(&AllType::getProp);

	pPropVector.setContainer(this);
	pPropVector.getter(&AllType::getPropVector);

	pFolders.setContainer(this);
	pFolders.getter(&AllType::getFolders);

}

AllType::~AllType(void) {
}

void AllType::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void AllType::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(AllType), "AllType");

	// serialize base
	CommandResponse::serialize(format);

	// serialize field: byteValue
	format.write<byte>(&this->byteValue, "byte byteValue", "byte", dt_byte, dt_none, 0);
	// serialize field: byteVector
	format.writeVector<byte>(this->byteVector, "vector<byte> byteVector", "byte", dt_byte);
	// serialize field: byteArray
	format.writeArray<byte>(this->byteArray, "byte[] byteArray", "byte", dt_byte);

	// serialize field: boolValue
	format.write<bool>(&this->boolValue, "boolean boolValue", "bool", dt_boolean, dt_none, 0);
	// serialize field: boolVector
	format.writeVector<bool>(this->boolVector, "vector<boolean> boolVector", "bool", dt_boolean);
	// serialize field: boolArray
	format.writeArray<bool>(this->boolArray, "boolean[] boolArray", "bool", dt_boolean);

	// serialize field: shortValue
	format.write<int16_t>(&this->shortValue, "short shortValue", "int16_t", dt_short, dt_none, 0);
	// serialize field: shortVector
	format.writeVector<int16_t>(this->shortVector, "vector<short> shortVector", "int16_t", dt_short);
	// serialize field: shortArray
	format.writeArray<int16_t>(this->shortArray, "short[] shortArray", "int16_t", dt_short);

	// serialize field: intValue
	format.write<int32_t>(&this->intValue, "int intValue", "int32_t", dt_int, dt_none, 0);
	// serialize field: intVector
	format.writeVector<int32_t>(this->intVector, "vector<int> intVector", "int32_t", dt_int);
	// serialize field: intArray
	format.writeArray<int32_t>(this->intArray, "int[] intArray", "int32_t", dt_int);

	// serialize field: longValue
	format.write<int64_t>(&this->longValue, "long longValue", "int64_t", dt_long, dt_none, 0);
	// serialize field: longVector
	format.writeVector<int64_t>(this->longVector, "vector<long> longVector", "int64_t", dt_long);
	// serialize field: longArray
	format.writeArray<int64_t>(this->longArray, "long[] longArray", "int64_t", dt_long);

	// serialize field: floatValue
	format.write<float>(&this->floatValue, "float floatValue", "float", dt_float, dt_none, 0);
	// serialize field: floatVector
	format.writeVector<float>(this->floatVector, "vector<float> floatVector", "float", dt_float);
	// serialize field: floatArray
	format.writeArray<float>(this->floatArray, "float[] floatArray", "float", dt_float);

	// serialize field: doubleValue
	format.write<double>(&this->doubleValue, "double doubleValue", "double", dt_double, dt_none, 0);
	// serialize field: doubleVector
	format.writeVector<double>(this->doubleVector, "vector<double> doubleVector", "double", dt_double);
	// serialize field: doubleArray
	format.writeArray<double>(this->doubleArray, "double[] doubleArray", "double", dt_double);

	// serialize field: stringValue
	format.write<string>(this->stringValue, "string stringValue", "string", dt_string, dt_none, 0);
	// serialize field: stringVector
	format.writeVector<string>(this->stringVector, "vector<string> stringVector", "string", dt_string);
	// serialize field: prop
	format.write<Prop>(this->prop, "Prop prop", "Prop", dt_message, dt_none, 0);
	// serialize field: propVector
	format.writeVector<Prop*>(this->propVector, "vector<Prop> propVector", "Prop", dt_message_ptr);
	// serialize field: folders
	format.writeMap<int, Prop*>(this->folders, "map<int, Prop> folders", "int32_t", dt_int, "Prop", dt_message_ptr);

	format.endWriteBean(typeid(*this), typeid(AllType), "AllType");
}

void AllType::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void AllType::deserialize(MessageBeanFormat &format) {
	// serialize base
	CommandResponse::deserialize(format);

	// deserialize field: byteValue
	format.read<byte>(&this->byteValue, "byte", dt_byte);
	// deserialize field: byteVector
	byteVector = format.readVector<byte>(&this->__byteVector, "byte", dt_byte);
	// deserialize field: byteArray
	byteArray = format.readArray<byte>(&this->__byteArray, "byte", dt_byte);
	// deserialize field: boolValue
	format.read<bool>(&this->boolValue, "bool", dt_boolean);
	// deserialize field: boolVector
	boolVector = format.readVector<bool>(&this->__boolVector, "bool", dt_boolean);
	// deserialize field: boolArray
	boolArray = format.readArray<bool>(&this->__boolArray, "bool", dt_boolean);
	// deserialize field: shortValue
	format.read<int16_t>(&this->shortValue, "int16_t", dt_short);
	// deserialize field: shortVector
	shortVector = format.readVector<int16_t>(&this->__shortVector, "int16_t", dt_short);
	// deserialize field: shortArray
	shortArray = format.readArray<int16_t>(&this->__shortArray, "int16_t", dt_short);
	// deserialize field: intValue
	format.read<int32_t>(&this->intValue, "int32_t", dt_int);
	// deserialize field: intVector
	intVector = format.readVector<int32_t>(&this->__intVector, "int32_t", dt_int);
	// deserialize field: intArray
	intArray = format.readArray<int32_t>(&this->__intArray, "int32_t", dt_int);
	// deserialize field: longValue
	format.read<int64_t>(&this->longValue, "int64_t", dt_long);
	// deserialize field: longVector
	longVector = format.readVector<int64_t>(&this->__longVector, "int64_t", dt_long);
	// deserialize field: longArray
	longArray = format.readArray<int64_t>(&this->__longArray, "int64_t", dt_long);
	// deserialize field: floatValue
	format.read<float>(&this->floatValue, "float", dt_float);
	// deserialize field: floatVector
	floatVector = format.readVector<float>(&this->__floatVector, "float", dt_float);
	// deserialize field: floatArray
	floatArray = format.readArray<float>(&this->__floatArray, "float", dt_float);
	// deserialize field: doubleValue
	format.read<double>(&this->doubleValue, "double", dt_double);
	// deserialize field: doubleVector
	doubleVector = format.readVector<double>(&this->__doubleVector, "double", dt_double);
	// deserialize field: doubleArray
	doubleArray = format.readArray<double>(&this->__doubleArray, "double", dt_double);
	// deserialize field: stringValue
	stringValue = format.read<string>(&this->__stringValue, "string", dt_string);
	// deserialize field: stringVector
	stringVector = format.readVector<string>(&this->__stringVector, "string", dt_string);
	// deserialize field: prop
	prop = format.read<Prop>(&this->__prop, "Prop", dt_message);
	// deserialize field: propVector
	propVector = format.readVector<Prop*>(&this->__propVector, "Prop", dt_message_ptr);
	// deserialize field: folders
	folders = format.readMap<int, Prop*>(&this->__folders, "int32_t", dt_int, "Prop", dt_message_ptr);
}
    
void AllType::clear(void) {
   	// clear base
   	CommandResponse::clear();
    	
	// clear field: byteValue
	byteValue = 0;
	// clear field: byteVector
	__byteVector.clear();
	byteVector = NULL;
	// clear field: byteArray
	__byteArray.clear();
	byteArray = NULL;
	// clear field: boolValue
	boolValue = false;
	// clear field: boolVector
	__boolVector.clear();
	boolVector = NULL;
	// clear field: boolArray
	__boolArray.clear();
	boolArray = NULL;
	// clear field: shortValue
	shortValue = 0;
	// clear field: shortVector
	__shortVector.clear();
	shortVector = NULL;
	// clear field: shortArray
	__shortArray.clear();
	shortArray = NULL;
	// clear field: intValue
	intValue = 0;
	// clear field: intVector
	__intVector.clear();
	intVector = NULL;
	// clear field: intArray
	__intArray.clear();
	intArray = NULL;
	// clear field: longValue
	longValue = 0L;
	// clear field: longVector
	__longVector.clear();
	longVector = NULL;
	// clear field: longArray
	__longArray.clear();
	longArray = NULL;
	// clear field: floatValue
	floatValue = 0;
	// clear field: floatVector
	__floatVector.clear();
	floatVector = NULL;
	// clear field: floatArray
	__floatArray.clear();
	floatArray = NULL;
	// clear field: doubleValue
	doubleValue = 0;
	// clear field: doubleVector
	__doubleVector.clear();
	doubleVector = NULL;
	// clear field: doubleArray
	__doubleArray.clear();
	doubleArray = NULL;
	// clear field: stringValue
	__stringValue.clear();
	stringValue = NULL;
	// clear field: stringVector
	__stringVector.clear();
	stringVector = NULL;
	// clear field: prop
	__prop.clear();
	prop = NULL;
	// clear field: propVector
	__propVector.clear();
	propVector = NULL;
	// clear field: folders
	__folders.clear();
	folders = NULL;
}

void AllType::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of LoginResponse
///////////////////////////////////////////////////////////////////////////////	
LoginResponse::LoginResponse(int cmdId) : CommandResponse(cmdId) {
	__typeId = 7;
	__typeName = "LoginResponse";
		
	clear();

	pBackpack.setContainer(this);
	pBackpack.getter(&LoginResponse::getBackpack);

}

LoginResponse::~LoginResponse(void) {
}

void LoginResponse::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void LoginResponse::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(LoginResponse), "LoginResponse");

	// serialize base
	CommandResponse::serialize(format);

	// serialize field: playerId
	format.write<int64_t>(&this->playerId, "long playerId", "int64_t", dt_long, dt_none, 0);
	// serialize field: backpack
	format.write<Backpack>(this->backpack, "Backpack backpack", "Backpack", dt_message, dt_none, 0);

	format.endWriteBean(typeid(*this), typeid(LoginResponse), "LoginResponse");
}

void LoginResponse::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void LoginResponse::deserialize(MessageBeanFormat &format) {
	// serialize base
	CommandResponse::deserialize(format);

	// deserialize field: playerId
	format.read<int64_t>(&this->playerId, "int64_t", dt_long);
	// deserialize field: backpack
	backpack = format.read<Backpack>(&this->__backpack, "Backpack", dt_message);
}
    
void LoginResponse::clear(void) {
   	// clear base
   	CommandResponse::clear();
    	
	// clear field: playerId
	playerId = 0L;
	// clear field: backpack
	__backpack.clear();
	backpack = NULL;
}

void LoginResponse::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of LogoutResponse
///////////////////////////////////////////////////////////////////////////////	
LogoutResponse::LogoutResponse(int cmdId) : CommandResponse(cmdId) {
	__typeId = 10;
	__typeName = "LogoutResponse";
		
	clear();

	pGrace.setContainer(this);
	pGrace.getter(&LogoutResponse::getGrace);

}

LogoutResponse::~LogoutResponse(void) {
}

void LogoutResponse::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void LogoutResponse::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(LogoutResponse), "LogoutResponse");

	// serialize base
	CommandResponse::serialize(format);

	// serialize field: grace
	format.write<string>(this->grace, "string grace", "string", dt_string, dt_none, 0);

	format.endWriteBean(typeid(*this), typeid(LogoutResponse), "LogoutResponse");
}

void LogoutResponse::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void LogoutResponse::deserialize(MessageBeanFormat &format) {
	// serialize base
	CommandResponse::deserialize(format);

	// deserialize field: grace
	grace = format.read<string>(&this->__grace, "string", dt_string);
}
    
void LogoutResponse::clear(void) {
   	// clear base
   	CommandResponse::clear();
    	
	// clear field: grace
	__grace.clear();
	grace = NULL;
}

void LogoutResponse::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}

///////////////////////////////////////////////////////////////////////////////
// Implementation of SceneMap
///////////////////////////////////////////////////////////////////////////////	
SceneMap::SceneMap(int cmdId) : CommandResponse(cmdId) {
	__typeId = 5;
	__typeName = "SceneMap";
		
	clear();

	pData.setContainer(this);
	pData.getter(&SceneMap::getData);

	pProp.setContainer(this);
	pProp.getter(&SceneMap::getProp);

}

SceneMap::~SceneMap(void) {
}

void SceneMap::serialize(ostream &output) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setOutput(&output);
	serialize(format);
}

void SceneMap::serialize(MessageBeanFormat &format) {
	format.beginWriteBean(typeid(*this), typeid(SceneMap), "SceneMap");

	// serialize base
	CommandResponse::serialize(format);

	// serialize field: mapId
	format.write<int32_t>(&this->mapId, "int mapId", "int32_t", dt_int, dt_none, 0);
	// serialize field: data
	format.writeVector<byte>(this->data, "vector<byte> data", "byte", dt_byte);
	// serialize field: prop
	format.write<Prop>(this->prop, "Prop prop", "Prop", dt_message, dt_none, 0);

	format.endWriteBean(typeid(*this), typeid(SceneMap), "SceneMap");
}

void SceneMap::deserialize(istream &input) {
	BinaryMessageBeanFormat format(ObjectFactory::instance());
	format.setInput(&input);
	deserialize(format);
}

void SceneMap::deserialize(MessageBeanFormat &format) {
	// serialize base
	CommandResponse::deserialize(format);

	// deserialize field: mapId
	format.read<int32_t>(&this->mapId, "int32_t", dt_int);
	// deserialize field: data
	data = format.readVector<byte>(&this->__data, "byte", dt_byte);
	// deserialize field: prop
	prop = format.read<Prop>(&this->__prop, "Prop", dt_message);
}
    
void SceneMap::clear(void) {
   	// clear base
   	CommandResponse::clear();
    	
	// clear field: mapId
	mapId = 0;
	// clear field: data
	__data.clear();
	data = NULL;
	// clear field: prop
	__prop.clear();
	prop = NULL;
}

void SceneMap::release() {
	ObjectFactory::instance()->returnMessageBean(this);
}


///////////////////////////////////////////////////////////////////////////////
// Implementation of ObjectFactory
///////////////////////////////////////////////////////////////////////////////	
ObjectFactory::ObjectFactory(void) {
	mbMap["Backpack"] = 4;
	mbMap["Broadcast"] = 9;
	mbMap["CommandResponse"] = 3;
	mbMap["Login"] = 6;
	mbMap["NullMessageBean"] = 1;
	mbMap["Prop"] = 2;
	mbMap["Walk"] = 8;
	mbMap["AllType"] = 11;
	mbMap["LoginResponse"] = 7;
	mbMap["LogoutResponse"] = 10;
	mbMap["SceneMap"] = 5;
}
	
ObjectFactory::~ObjectFactory() {
}

int ObjectFactory::getTypeId(const string &typeName) {
	map<string, int>::iterator itr = mbMap.find(typeName);
	if (itr == mbMap.end()) {
		return -1;
	} else {
		return itr->second;
	}
}
	
MessageBean* ObjectFactory::createMessageBean(const string &typeName) {
	int typeId = getTypeId(typeName);
	MessageBean *mbean = NULL;
	switch (typeId) {
	case 4:
		mbean = new Backpack();
		break;
	case 9:
		mbean = new Broadcast();
		break;
	case 3:
		mbean = new CommandResponse();
		break;
	case 6:
		mbean = new Login();
		break;
	case 1:
		mbean = new NullMessageBean();
		break;
	case 2:
		mbean = new Prop();
		break;
	case 8:
		mbean = new Walk();
		break;
	case 11:
		mbean = new AllType();
		break;
	case 7:
		mbean = new LoginResponse();
		break;
	case 10:
		mbean = new LogoutResponse();
		break;
	case 5:
		mbean = new SceneMap();
		break;
	default:
		mbean = NULL;
		break;
	}
		
	if (mbean != NULL) {
		mbean->__instanceId = getInstanceId();
		mbean->__typeId = typeId;
	}
		
	return mbean;
}
