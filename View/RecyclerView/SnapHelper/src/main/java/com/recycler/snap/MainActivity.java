package com.recycler.snap;

import android.os.Bundle;
import android.view.View;

import com.recycler.snap.snap.LinearSnapActivity;
import com.recycler.snap.snap.PagerSnapActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Linear", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearSnapActivity.launcher(MainActivity.this);
            }
        });

        addButton("Pager", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagerSnapActivity.launcher(MainActivity.this);
            }
        });
    }
}
