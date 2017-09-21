package com.sqlite.green.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sqlite.green.SQLiteManager;
import com.sqlite.green.test.NetCacheModel;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseTestActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, TestActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private String url = "1234567890";

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("insert", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleModel save = new SampleModel();
                long rowId = SQLiteManager.insertOrReplace(new NetCacheModel(url, save));
                LogUtil.i("rowId = " + rowId);
            }
        });

        addButton("读取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleModel result = SQLiteManager.load(url, SampleModel.class);
                if (null == result) {
                    LogFileUtil.i("xxx-","result is null");
                } else {
                    LogFileUtil.i("xxx-","result = " + result.toString());
                }
            }
        });
    }

    private class SampleModel {
        private String test;

        private List<TModel> list;

        public SampleModel() {
            this.test = "123";
            this.list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                list.add(new TModel("i-" + i));
            }
        }

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public List<TModel> getList() {
            return list;
        }

        public void setList(List<TModel> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "SampleModel{" +
                    "test='" + test + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    public class TModel {

        private String name;

        public TModel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
