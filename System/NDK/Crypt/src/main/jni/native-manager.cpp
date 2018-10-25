//
// Created by linjiang on 2018/10/24.
//
#include <jni.h>
#include <string>
#include <android/log.h>

#define AES_KEY "1234567887654321"

extern "C" {
/**
 * 获取 RSA 的公钥
 */
jstring Java_com_kjtpay_ndk_JniManager_getRSAPublicKey(JNIEnv *env, jobject) {
    return env->NewStringUTF(
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQkiFAp5GHuW8koHAw2H5xl7IZU0tyME8xUpe"
            "CagNPB8MJZeKuXnqwy/J06Sp6ClbR0QqUVyCT8NCEoRJWOky3wDbTR9T3/rcPpXSZYFC+zIkLOMZ"
            "z/aKNKXfesw4Vmpf5OD2IQQGZOuKXNJnsaQvEPzhoc55NQUf1pK0UmmN/uwIDAQAB");
}

/**
 * 获取 RSA 的私钥
 */
jstring Java_com_kjtpay_ndk_JniManager_getRSAPrivateKey(JNIEnv *env, jobject) {
    return env->NewStringUTF(
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANCSIUCnkYe5bySgcDDYfnGXshl"
            "TS3IwTzFSl4JqA08Hwwll4q5eerDL8nTpKnoKVtHRCpRXIJPw0IShElY6TLfANtNH1Pf+tw+ldJl"
            "gUL7MiQs4xnP9oo0pd96zDhWal/k4PYhBAZk64pc0mexpC8Q/OGhznk1BR/WkrRSaY3+7AgMBAAE"
            "CgYEAjScoa/o76m/bwRz3cIdD45p3RN5zQ99f6RBtSyx1+slU/IpAhCOawwXzm52lSpyurybbExN"
            "4D8c9R1U+9K5V9hd1/hpVLi9X8kh9Jw6VJXExotJ99LY6PYBAs4TqTwfE7oPP/Y+79u2wI240QIJ"
            "kSwTEtIV4LyKdHQzRmLllSeECQQD9qgtyilj3f4HFC47xswdYrXKC3d/CWjTuD/YqM94LrEWYHeV"
            "WlFTnXX3Af+/YjjXQq2Go1Wbww1aHhrYR5MirAkEA0n3JrCpNLhKQfR64C74gJBEi5+Zm4AeOkkh"
            "yeRuT+53six3nFUqgRLnpUeM980V/X2a79usQ0GMCETorYIlFMQJAOHK2yW5wDeOaBTdlP/QPFnT"
            "CnsyxFpbsYG284fdY2lAjzI4akwG/Qx1S9puBzDcZUq5QtTmIBtvxTYd0zNaUsQJBAM7qqF1+F/C"
            "6fx8AG5wvghjyX4XnkCmaRCS44w76dTZbwDPhaVAc0/+7YgkFgdiq8NMvgobv/M9dBKM6s3lqd4EC"
            "QQCGfie3P1D40ZPGTgJImm/ly4leWloV7NjxMEBem1xIGGEHht+9bV4qexbTReAIHyYeMK2WGtzCq2oy6rYnJ/Cj");
}

/**
 * 获取 AES 秘钥
 */
jstring Java_com_kjtpay_ndk_JniManager_getAESKey(JNIEnv *env, jobject) {
    return env->NewStringUTF(AES_KEY);
}
}