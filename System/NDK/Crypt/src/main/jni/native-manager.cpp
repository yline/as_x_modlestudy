//
// Created by linjiang on 2018/10/24.
//
#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {
/**
 * 获取 RSA 的公钥，提供
 */
jstring Java_com_kjtpay_ndk_JniManager_getRSAPublicKey(JNIEnv *env, jobject) {
    return env->NewStringUTF(
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdFkhUJBKI9JDQzSTw5h387ujbu/e6M6fRl1W"
            "MY7j/yUWtEpXVsmTrjbhGRr7SO4ddvIqK+Up4/pBqP2HmPJL4A0THkK9dfIn6aOTI5TqdSnJN8SS"
            "qBbaV6/ve3PMLph9cwhe0IavInqIFdJr4cvC9oYRoWAs52Ay/k78DBGtoJwIDAQAB");
}

/**
 * 获取 RSA 的公钥，和上面的一样，专门提供给安全键盘使用的RSA公钥
 */
jstring Java_com_kjtpay_ndk_JniManager_getRSABoardPublicKey(JNIEnv *env, jobject) {
    return env->NewStringUTF(
            "308189028181009d164854241288f490d0cd24f0e61dfceee8dbbbf7ba33a7d197558c63b8ff"
            "c945ad1295d5b264eb8db84646bed23b875dbc8a8af94a78fe906a3f61e63c92f80344c790af"
            "5d7c89fa68e4c8e53a9d4a724df124aa05b695ebfbdedcf30ba61f5cc217b421abc89ea205749"
            "af872f0bda18468580b39d80cbf93bf03046b68270203010001");
}

/**
 * 获取 RSA 的公钥，测试
 */
jstring Java_com_kjtpay_ndk_JniManager_getRSAPublicKeyTest(JNIEnv *env, jobject) {
    return env->NewStringUTF(
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQkiFAp5GHuW8koHAw2H5xl7IZU0tyME8xUpeCagNPB8MJ"
            "ZeKuXnqwy/J06Sp6ClbR0QqUVyCT8NCEoRJWOky3wDbTR9T3/rcPpXSZYFC+zIkLOMZz/aKNKXfesw4Vmpf5O"
            "D2IQQGZOuKXNJnsaQvEPzhoc55NQUf1pK0UmmN/uwIDAQAB");
}
}