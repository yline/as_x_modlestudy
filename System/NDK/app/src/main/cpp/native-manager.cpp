#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "xxx-manager"

/* ------------------------------------- 声明调用其它文件的函数 -------------------------------------- */
extern void PrintStart();

extern char *b64_encode(const unsigned char *src, size_t len); // Base64 加密

extern unsigned char *b64_decode(const char *src, size_t len); // Base64 解密

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
        __android_log_print(ANDROID_LOG_INFO, TAG, "msg is null");
        return env->NewStringUTF("msg is null");
    }

    // 赋值
    char *c_msg = NULL;
    c_msg = (char *) env->GetStringUTFChars(msg, 0);

    // 打印内容
    __android_log_print(ANDROID_LOG_INFO, TAG, "logByJni, %s", c_msg);

    std::string hello = "Hello C，manager logByJni";
    return env->NewStringUTF(hello.c_str());
}

/**
 * Java层 使用 C层 加密数据
 * @param env
 * @param type 加密的方式
 * @return 加密后的数据
 */
jstring Java_com_ndk_jni_JniManager_encode(JNIEnv *env, jobject, jstring msg, jint type) {
    PrintStart();

    // 字符长度
    size_t length = (size_t) env->GetStringUTFLength(msg);

    // 字符内容
    unsigned char *c_msg = NULL;
    c_msg = (unsigned char *) env->GetStringUTFChars(msg, 0);

    // 加密后的数组
    char *result = NULL;
    result = b64_encode(c_msg, length);

    // 加密后的字符串
    return env->NewStringUTF(result);
}

/**
 * Java层 使用 C层 解密数据
 * @param env
 * @param type 解密的方式
 * @return 解密后的数据
 */
jstring Java_com_ndk_jni_JniManager_decode(JNIEnv *env, jobject, jstring msg, jint type) {
    PrintStart();

    // 解密字符串的 长度
    size_t length = (size_t) env->GetStringUTFLength(msg);

    // 解密字符串的 内容
    char *c_msg = NULL;
    c_msg = (char *) env->GetStringUTFChars(msg, 0);

    // 解密后的字符串数组
    unsigned char *result = NULL;
    result = b64_decode(c_msg, length);

    // 解密后的字符串
    return env->NewStringUTF((const char *) result);
}


/* ----------------------------------------------------- */
void willCrash() {
    // 将会发生一次 Crash, 空指针异常
    std::string boolKey = NULL;
}

JNIEXPORT void Java_com_ndk_jni_JniManager_doCrash(JNIEnv *env, jobject, jstring msg) {
    willCrash();
}


}