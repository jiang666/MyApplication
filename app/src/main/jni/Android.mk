LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := Testjni
LOCAL_SRC_FILES =: presure.cpp
include $(BUILD_SHARED_LIBRARY)
