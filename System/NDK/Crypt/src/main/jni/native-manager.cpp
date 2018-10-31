//
// Created by linjiang on 2018/10/24.
//
#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {
/**
 * 获取 RSA 的公钥，提供（客户端解密有问题）
 */
jstring Java_com_kjtpay_ndk_JniManager_getRSAPublicKey(JNIEnv *env, jobject) {
    return env->NewStringUTF(
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqgPhwmId68+Xv7YdoBUFuwZ5UzT+ZzWa"
            "StSUnkyote17Br5jB7NIE3aa22juKQ2qLDwoTzkKr7fl6ftOvB1hEL2TZO3uWyJChLX661Asga6kC"
            "t6tl2S3FgPBQqeup9SGkS1OxcSa5z2aJlT78kEHde2HgwrWYP6TSWVI00HPc8T4VsqF84fdLd1mBv"
            "WKQ0GJmFog2nrZF563Coaqv2xno13cAFfOOMOily68isr2UJDBheibXAixQVKOfWCl+vUxgVqxED4"
            "yDCMl3AficlLr26z1xsIp3eJ4vsKKceiIewFVpQaySxhXLqvvXK03qij1iQ2kQeWA5drm/czpX9nNdwIDAQAB");
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