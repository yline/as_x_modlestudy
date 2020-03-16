package com.yline.compile.knife.lib.view;

/**
 * 统一调用的接口实现
 */
public interface Unbinder {
    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unbind() {
        }
    };

    /**
     * 解绑
     */
    void unbind();
}
