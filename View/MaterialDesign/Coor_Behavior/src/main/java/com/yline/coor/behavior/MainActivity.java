package com.yline.coor.behavior;

import android.os.Bundle;
import android.view.View;

import com.yline.coor.behavior.type1.Type1Activity;
import com.yline.coor.behavior.type2.Type2Activity;
import com.yline.coor.behavior.type3.Type3Activity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Type1", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type1Activity.launch(MainActivity.this);
            }
        });

        addButton("Type2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type2Activity.launch(MainActivity.this);
            }
        });

        addButton("Type3", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type3Activity.launch(MainActivity.this);
            }
        });

//        addButton("Type3", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Type3Activity.launch(MainActivity.this);
//            }
//        });
    }
}
