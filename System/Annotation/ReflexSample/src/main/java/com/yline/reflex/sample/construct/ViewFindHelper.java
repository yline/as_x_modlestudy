package com.yline.reflex.sample.construct;

import com.yline.utils.LogUtil;

public class ViewFindHelper {
    public ViewFindHelper() {
        LogUtil.v("");
    }

    private void testPrivate() {
        LogUtil.v("");
    }

    protected void testProtect(String param) {
        LogUtil.v("param = " + param);
    }

    public void testPublic() {
        LogUtil.v("");
    }
}
