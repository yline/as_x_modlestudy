#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {

/**
 * Java层 获取 C层 字符串
 * @param env
 * @return 指定字符串
 */
jstring Java_com_ndk_jni_JniManager_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello C，manager stringFromJNI";
    return env->NewStringUTF(hello.c_str());
}

/**
 * Java层 使用 C层打印 字符串
 * @param env
 * @param clazz
 * @param msg 打印的信息内容
 * @return 指定字符串
 */
jstring Java_com_ndk_jni_JniManager_logByJni(JNIEnv *env, jobject, jstring msg) {
    if (NULL == msg) {
        __android_log_print(ANDROID_LOG_INFO, "xxx-", "msg is null");
        return env->NewStringUTF("msg is null");
    }

    // 赋值
    char *c_msg = NULL;
    c_msg = (char *) env->GetStringUTFChars(msg, 0);

    // 打印内容
    __android_log_print(ANDROID_LOG_INFO, "xxx-", "manager logByJni, %s", c_msg);

    std::string hello = "Hello C，manager logByJni";
    return env->NewStringUTF(hello.c_str());
}

}