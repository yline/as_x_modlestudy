package com.yline.annotation;

import android.os.Bundle;
import android.view.View;

import com.yline.annotation.start.RuntimeFieldTag;
import com.yline.annotation.start.TagManager;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import main.java.ClassTag;

public class MainActivity extends BaseTestActivity {

    @RuntimeFieldTag("Jay Zhou")
    private String mString;

    @RuntimeFieldTag()
    private int mInt;

    //    @RuntimeFieldTag("not match") 这个值还是不能乱来啊
    private float mFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        printFiled();
        TagManager.init(this);
        printFiled();
    }

    private void printFiled() {
        LogUtil.v("int = " + mInt + ", mString = " + mString + ", mFloat = " + mFloat);
    }


    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        test();

        addButton("test", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    @ClassTag("fuck")
    private void test() {
//        LogUtil.v("");
    }
}
