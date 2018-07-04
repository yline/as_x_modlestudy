package com.dm.yuanxing.cloneable;

import android.util.Log;

import com.dm.yuanxing.activity.MainApplication;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档类型,扮演的是ConcretePrototype角色,而cloneable是代表prototype角色
 * 模拟文本和图片
 */
public class WordDocument implements Cloneable {
    /**
     * 文本
     */
    private String mText;

    /**
     * 图片名列表
     */
    private ArrayList<String> mImages = new ArrayList<String>();

    public WordDocument() {
        Log.v("yuanxing_tag", "---------WordDocument构造函数---------");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone()
            throws CloneNotSupportedException {
        WordDocument doc = (WordDocument) super.clone();
        doc.mText = this.mText;
        // doc.mImages = this.mImages;  
        // 对mImages对象也调用clone()函数,实现深拷贝
        doc.mImages = (ArrayList<String>) this.mImages.clone();

        return doc;
    }

    public String getText() {
        return this.mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public List<String> getImages() {
        return this.mImages;
    }

    public void addImage(String img) {
        this.mImages.add(img);
    }

    public void showDocument() {
        LogFileUtil.v(MainApplication.TAG, "---------Word Content Start---------");
        LogFileUtil.v(MainApplication.TAG, "Text : " + mText);
        LogFileUtil.v(MainApplication.TAG, "mImages list : ");
        for (String imgName : mImages) {
            LogFileUtil.v(MainApplication.TAG, "image name : " + imgName);
        }
        LogFileUtil.v(MainApplication.TAG, "---------Word Content End---------");
    }

}
