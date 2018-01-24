package com.view.recycler.choice;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("单选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceActivity.actionStart(MainActivity.this);
            }
        });

        addButton("多选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleChoiceActivity.actionStart(MainActivity.this);
            }
        });
    }
}
