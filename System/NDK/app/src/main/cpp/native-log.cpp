#include <jni.h>
#include <string>
#include <android/log.h>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_ndk_jni_JniManager_logByJni(JNIEnv *env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, "Jni Msg", "fuck");

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
