#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG_INIT "xxx-init"

jclass clazz;

void InitJniProvider(JNIEnv *env) {
    __android_log_print(ANDROID_LOG_INFO, TAG_INIT, "start");

    if (NULL == env) {
        __android_log_print(ANDROID_LOG_INFO, TAG_INIT, "env is null");
        return;
    }

    clazz = env->FindClass("com/ndk/jni/JniProvider");
    if (NULL == clazz) {
        __android_log_print(ANDROID_LOG_INFO, TAG_INIT, "clazz is null");
        return;
    }
}

extern "C" {

/**
 * 被Java层调用，测试 C调用Java的普通方法
 * @param env
 */
void Java_com_ndk_jni_JniProvider_doProvider(JNIEnv *env, jobject) {
    InitJniProvider(env);

    jobject obj = env->AllocObject(clazz);
    if (NULL == obj) {
        __android_log_print(ANDROID_LOG_INFO, TAG_INIT, "obj is null");
        return;
    }

    jmethodID method = env->GetMethodID(clazz, "testProvider", "()Ljava/lang/String;");
    if (NULL == method) {
        __android_log_print(ANDROID_LOG_INFO, "xxx-doProvider", "method is null");
        return;
    }

    jstring result = (jstring) env->CallObjectMethod(obj, method);
    char *msg = NULL;
    msg = (char *) env->GetStringUTFChars(result, 0);

    __android_log_print(ANDROID_LOG_INFO, "xxx-doProvider", "%s", msg);
}

/**
 * 被Java层调用，测试  C调用Java的静态方法
 * @param env
 */
void Java_com_ndk_jni_JniProvider_doStaticProvider(JNIEnv *env, jobject) {
    InitJniProvider(env);

    jmethodID method = env->GetStaticMethodID(clazz, "testStaticProvider", "()Ljava/lang/String;");
    if (NULL == method) {
        __android_log_print(ANDROID_LOG_INFO, "xxx-doStaticProvider", "method is null");
        return;
    }

    jstring result = (jstring) env->CallStaticObjectMethod(clazz, method);
    char *msg = NULL;
    msg = (char *) env->GetStringUTFChars(result, 0);

    __android_log_print(ANDROID_LOG_INFO, "xxx-doStaticProvider", "%s", msg);
}

}
