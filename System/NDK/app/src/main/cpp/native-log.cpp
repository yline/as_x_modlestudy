#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "xxx-doProvider"
#define TAG_STATIC "xxx-doStaticProvider"

extern "C" {

/**
 * 被Java层调用，测试 C调用Java的普通方法
 * @param env
 */
void Java_com_ndk_jni_JniProvider_doProvider(JNIEnv *env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "start");

    if (NULL == env) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "env is null");
        return;
    }

    jclass clazz = env->FindClass("com/ndk/jni/JniProvider");
    if (NULL == clazz) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "clazz is null");
        return;
    }

    jmethodID method = env->GetMethodID(clazz, "testProvider", "()Ljava/lang/String;");
    if (NULL == method) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "method is null");
        return;
    }

    jobject obj = env->AllocObject(clazz);
    if (NULL == obj) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "obj is null");
        return;
    }

    env->CallObjectMethod(obj, method);
}

/**
 * 被Java层调用，测试  C调用Java的静态方法
 * @param env
 */
void Java_com_ndk_jni_JniProvider_doStaticProvider(JNIEnv *env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG_STATIC, "start");
}

}
