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
jstring Java_com_kjtpay_ndk_JniManager_getRSAEncodePublicKey(JNIEnv *env, jobject) {
    return env->NewStringUTF(
            "8a066fbd1ef06bede813223a6bc758fda2ed9f37e0a5298d0b6ccc1d375587118301f45dc31"
            "ab77b259e343b1f833a0f20553b2625cedd5e1ff7a50f049050e7e47038bfb6c2692534649f"
            "b29223a9f998e5d4bc92c3e6d74c8700e8c2d11ea137ae258edd1bd2fc79094ec500b2eea7e"
            "4e76f6f1802270165ca476a4b0b22447deffce0f140f89fa963f52f06465b6612c1c910ecbe"
            "50be471f753246aa626353021cc344d232259aa5b7d43ee33770d0489568da15393c612348b"
            "e31b693896b97446b39d5d4393e759c842d8febf77598206b610e7a64711e04321e1442e219"
            "205b238dc26816");
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
}