package com.yline.reflex.sample.construct;

import androidx.annotation.NonNull;

import com.yline.utils.LogUtil;

public class ViewFindHelper {
    public ViewFindHelper() {
        LogUtil.v("init");
    }

    public ViewFindHelper(String param) {
        LogUtil.v("init param = " + param);
    }

    public ViewFindHelper(String param, int second) {
        LogUtil.v("init param = " + param + ", second = " + second);
    }

    private String mSingleString;

    protected int mSingleInt;

    public long mSingleLong;

    private void testPrivate() {
        LogUtil.v("");
    }

    protected void testProtect(String param) {
        LogUtil.v("param = " + param);
    }

    public void testPublic() {
        LogUtil.v("");
    }

    @NonNull
    @Override
    public String toString() {
        return "string = " + mSingleString + ", int = " + mSingleInt
                + ", long = " + mSingleLong;
    }
}
