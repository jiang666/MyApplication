//
// Created by jianglei on 2017/9/10.
//
#include <stdio.h>
#include <stdlib.h>
#include "com_iflytek_mscv5plusdemo_jni_JNIActivity.h"
extern "C" {
/*
 * Class:     com_iflytek_mscv5plusdemo_jni_JNIActivity
 * Method:    getStrFromJni
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_iflytek_mscv5plusdemo_jni_JNIActivity_getStrFromJni
        (JNIEnv *env, jobject){
    return (*env).NewStringUTF("This is mylibrary !!!");
}

/*
 * Class:     com_iflytek_mscv5plusdemo_jni_JNIActivity
 * Method:    getIntpro
 * Signature: ()I
 */
int getpresure(){
    return rand()%100;
}
JNIEXPORT jint JNICALL Java_com_iflytek_mscv5plusdemo_jni_JNIActivity_getIntpro
        (JNIEnv *, jobject){
    return getpresure();
}

}
