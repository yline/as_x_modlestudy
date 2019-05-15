package com.yline.crypt;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kjtpay.ndk.AESUtils;
import com.kjtpay.ndk.Base64Utils;
import com.kjtpay.ndk.JniManager;
import com.yline.utils.LogUtil;
import com.yline.utils.crypt.HexUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.yline.crypt", appContext.getPackageName());

        String rsaPublicKey = JniManager.getInstance().getRSAPublicKey();
        /*byte[] rsaKeyByte = rsaPublicKey.getBytes();

        byte[] aesKeyByte = AESUtils.createAESKey(16 * 8);
        byte[] encryptByte = AESUtils.encrypt(rsaKeyByte, aesKeyByte);

        byte[] saveData = merge(encryptByte, aesKeyByte);
        String result = new String(HexUtils.encodeHex(saveData));
        LogUtil.v("result = " + result);*/

        String result = JniManager.getInstance().getRSAEncodePublicKey();

        // 开始解密
        byte[] saveDataA = HexUtils.decodeHex(result.toCharArray());
        byte[] encryptByteA = unMergeContent(saveDataA);
        byte[] aesKeyA = unMergeJudge(saveDataA);

        byte[] rsaPublicKeyByteA = AESUtils.decrypt(encryptByteA, aesKeyA);
        String rsaPublicKeyA = new String(rsaPublicKeyByteA);

        assertEquals(rsaPublicKey, rsaPublicKeyA);
    }


    /**
     * 将两个数组，合并为一个
     *
     * @param contentBytes 内容
     * @param judgeBytes   校验，长度大于8
     * @return 合并的结果
     */
    private static byte[] merge(byte[] contentBytes, byte[] judgeBytes) {
        byte[] mergeData = new byte[contentBytes.length + 16];
        System.arraycopy(judgeBytes, 0, mergeData, 0, 8); // 前8个
        System.arraycopy(contentBytes, 0, mergeData, 8, contentBytes.length); // 中间内容
        for (int i = 0; i < 8; i++) { // 后8个
            mergeData[i + 8 + contentBytes.length] = judgeBytes[judgeBytes.length - 8 + i];
        }

        return mergeData;
    }

    /**
     * 从数组中，取出内容
     *
     * @param mergeData 合并的结果
     * @return 内容
     */
    private static byte[] unMergeContent(byte[] mergeData) {
        byte[] contentBytes = new byte[mergeData.length - 16];
        System.arraycopy(mergeData, 8, contentBytes, 0, contentBytes.length);
        return contentBytes;
    }

    /**
     * 从数组中，取出校验
     *
     * @param mergeData 合并的结果
     * @return 内容
     */
    private static byte[] unMergeJudge(byte[] mergeData) {
        byte[] judgeBytes = new byte[16];
        System.arraycopy(mergeData, 0, judgeBytes, 0, 8);
        System.arraycopy(mergeData, mergeData.length - 8, judgeBytes, 8, 8);
        return judgeBytes;
    }
}
