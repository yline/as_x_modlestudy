package com.yline.finger.manager;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.yline.utils.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自己做的兼容类
 *
 * @author yline 2019/3/4 -- 13:28
 */
class FingerCompat {
    private FingerprintManagerCompat mManagerCompat;
    private FingerprintManager mManager;

    public static FingerCompat from() {
        return new FingerCompat();
    }

    FingerCompat() {
    }

    public boolean isFingerEnable(Context context) {
        FingerCompat compat = from();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.v("Android 版本低");
            return false;
        }

        if (!compat.isSupport(context)) {
            LogUtil.v("手机硬件不支持");
            return false;
        }

        if (!compat.hasEnrolledFinger(context)) {
            LogUtil.v("手机还未录入指纹");
            return false;
        }

        if (!compat.isKeyguardSecure(context)) {
            LogUtil.v("手机屏幕未打开");
            return false;
        }

        return true;
    }

    /**
     * 验证指纹;
     * 用于首次验证指纹，以及之后校验指纹
     *
     * @param crypto   加密工具
     * @param cancel   取消控制
     * @param callback 回调
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authenticate(Context context, @Nullable FingerprintManagerCompat.CryptoObject crypto, @Nullable CancellationSignal cancel,
                             @NonNull FingerprintManagerCompat.AuthenticationCallback callback) {
        LogUtil.v("请输入指纹-----------");
        authenticateInner(context, crypto, cancel, callback);
    }

    /**
     * 硬件，是否支持，指纹识别
     *
     * @param context 上下文
     * @return true(支持)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isSupport(Context context) {
        try {
            FingerprintManagerCompat managerCompat = getFingerprintManagerCompat(context);
            if (null != managerCompat) {
                return managerCompat.isHardwareDetected();
            } else {
                return getFingerManager(context).isHardwareDetected();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 是否录入指纹；
     * PS：有些设备上即使录入了指纹，但是没有开启锁屏密码的话此方法还是返回false
     *
     * @param context 上下文
     * @return true(有录入指纹)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasEnrolledFinger(Context context) {
        try {
            FingerprintManagerCompat managerCompat = getFingerprintManagerCompat(context);
            if (null != managerCompat) {
                return managerCompat.hasEnrolledFingerprints();
            } else {
                return getFingerManager(context).hasEnrolledFingerprints();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 判断系统有没有设置锁屏
     *
     * @return true(设置了)
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean isKeyguardSecure(Context context) {
        try {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            return (null != keyguardManager && keyguardManager.isKeyguardSecure());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 验证指纹;
     * 用于首次验证指纹，以及之后校验指纹
     *
     * @param crypto   加密工具
     * @param cancel   取消控制
     * @param callback 回调
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void authenticateInner(Context context, @Nullable FingerprintManagerCompat.CryptoObject crypto, @Nullable CancellationSignal cancel, @NonNull FingerprintManagerCompat.AuthenticationCallback callback) {
        FingerprintManagerCompat managerCompat = getFingerprintManagerCompat(context);
        if (null != managerCompat) {
            managerCompat.authenticate(crypto, 0, cancel, callback, null);
        } else {
            android.os.CancellationSignal cancelSignal = (cancel != null ? (android.os.CancellationSignal) cancel.getCancellationSignalObject() : null);
            getFingerManager(context).authenticate(wrapCryptoObject(crypto), cancelSignal, 0, wrapCallback(callback), null);
        }
    }

    private FingerprintManagerCompat getFingerprintManagerCompat(Context context) {
        if (null == mManagerCompat) {
            boolean isSystemFeature = context.getPackageManager().hasSystemFeature("android.hardware.fingerprint");
            if (isSystemFeature) {
                mManagerCompat = FingerprintManagerCompat.from(context.getApplicationContext());
                return mManagerCompat;
            } else {
                return null;
            }
        }
        return mManagerCompat;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private FingerprintManager getFingerManager(Context context) {
        if (null == mManager) {
            mManager = (FingerprintManager) context.getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
        }
        return mManager;
    }

    /* ------------------------------------------- 兼容工具类[参考FingerprintManagerCompat] ------------------------------------------ */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static android.hardware.fingerprint.FingerprintManager.CryptoObject wrapCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        } else if (cryptoObject.getCipher() != null) {
            return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getCipher());
        } else if (cryptoObject.getSignature() != null) {
            return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getSignature());
        } else {
            return cryptoObject.getMac() != null ? new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getMac()) : null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static FingerprintManagerCompat.CryptoObject unwrapCryptoObject(android.hardware.fingerprint.FingerprintManager.CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        } else if (cryptoObject.getCipher() != null) {
            return new FingerprintManagerCompat.CryptoObject(cryptoObject.getCipher());
        } else if (cryptoObject.getSignature() != null) {
            return new FingerprintManagerCompat.CryptoObject(cryptoObject.getSignature());
        } else {
            return cryptoObject.getMac() != null ? new FingerprintManagerCompat.CryptoObject(cryptoObject.getMac()) : null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static android.hardware.fingerprint.FingerprintManager.AuthenticationCallback wrapCallback(final FingerprintManagerCompat.AuthenticationCallback callback) {
        return new android.hardware.fingerprint.FingerprintManager.AuthenticationCallback() {
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                callback.onAuthenticationError(errMsgId, errString);
            }

            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                callback.onAuthenticationHelp(helpMsgId, helpString);
            }

            public void onAuthenticationSucceeded(android.hardware.fingerprint.FingerprintManager.AuthenticationResult result) {
                printFingerInfo(result);
                callback.onAuthenticationSucceeded(new FingerprintManagerCompat.AuthenticationResult(unwrapCryptoObject(result.getCryptoObject())));
            }

            public void onAuthenticationFailed() {
                callback.onAuthenticationFailed();
            }
        };
    }

    @SuppressLint("PrivateApi")
    private static void printFingerInfo(android.hardware.fingerprint.FingerprintManager.AuthenticationResult result) {
        try {
            Field field = result.getClass().getDeclaredField("mFingerprint");
            field.setAccessible(true);
            Object fingerprint = field.get(result);
            if (null == fingerprint) {
                LogUtil.e("fingerprint is null");
                return;
            }

            Class<?> clzz = Class.forName("android.hardware.fingerprint.Fingerprint");
            Method getName = clzz.getDeclaredMethod("getName");
            Method getFingerId = clzz.getDeclaredMethod("getFingerId");
            Method getGroupId = clzz.getDeclaredMethod("getGroupId");
            Method getDeviceId = clzz.getDeclaredMethod("getDeviceId");

            Object nameObj = getName.invoke(fingerprint);
            CharSequence name = nameObj instanceof CharSequence ? (CharSequence) nameObj : "null";

            Object fingerIdObj = getFingerId.invoke(fingerprint);
            int fingerId = fingerIdObj instanceof Integer ? (int) fingerIdObj : -1;

            Object groupIdObj = getGroupId.invoke(fingerprint);
            int groupId = groupIdObj instanceof Integer ? (int) groupIdObj : -1;

            Object deviceIdObj = getDeviceId.invoke(fingerprint);
            long deviceId = deviceIdObj instanceof Long ? (long) deviceIdObj : -1L;

            LogUtil.v("name = " + name + ", fingerId = " + fingerId
                    + ", groupId = " + groupId + ", deviceId = " + deviceId);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
