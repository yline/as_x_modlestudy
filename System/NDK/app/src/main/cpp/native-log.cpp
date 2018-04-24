#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {

jstring Java_com_ndk_jni_JniManager_logByJni(JNIEnv *env, jobject, jstring msg) {
    if (NULL == msg) {
        __android_log_print(ANDROID_LOG_INFO, "xxx-", "msg is null");
        return env->NewStringUTF("msg is null");
    }

    // 赋值
    char *c_msg = NULL;
    c_msg = (char *) env->GetStringUTFChars(msg, 0);

    // 打印内容
    __android_log_print(ANDROID_LOG_INFO, "xxx-", "log logByJni, %s", c_msg);

    std::string hello = "Hello C，log logByJni";
    return env->NewStringUTF(hello.c_str());
}

}