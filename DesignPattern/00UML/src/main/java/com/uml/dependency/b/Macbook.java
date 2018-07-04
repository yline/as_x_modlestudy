package com.uml.dependency.b;

public class Macbook {
    private String mBoard;
    private String mDisplay;
    private String mOS;

    public Macbook() {
    }

    // 设置CPU核心数
    public void setBoard(String board) {
        this.mBoard = board;
    }

    // 设置内存
    public void setDisplay(String display) {
        this.mDisplay = display;
    }

    // 设置操作系统
    public void setOS() {
        mOS = "Mac OS X 10.10";
    }

    @Override
    public String toString() {
        return "Computer [mBoard=" + mBoard + ", mDisplay=" + mDisplay + ", mOS=" + mOS + "]";
    }
}
