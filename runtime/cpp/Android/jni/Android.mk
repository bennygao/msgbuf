LOCAL_PATH := $(call my-dir)
LOCAL_CPP_EXTENSION := .cpp
include $(CLEAR_VARS)  
LOCAL_ARM_MODE  := arm
LOCAL_MODULE    := msgbuf
LOCAL_CXXFLAGS	:= -std=c++11
LOCAL_LDLIBS	:= -lz
LOCAL_SRC_FILES := ../../msgbuf.cpp ../../protocol.cpp
include $(BUILD_SHARED_LIBRARY)
