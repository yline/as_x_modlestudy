#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {

/**
 * 被Java层调用，测试 C调用Java的普通方法
 * @param env
 */
void Java_com_ndk_jni_JniProvider_doProvider(JNIEnv *env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, "xxx-", "doProvider start");
}

/**
 * 被Java层调用，测试  C调用Java的静态方法
 * @param env
 */
void Java_com_ndk_jni_JniProvider_doStaticProvider(JNIEnv *env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, "xxx-", "doStaticProvider start");
}

}