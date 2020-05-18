package com.yline.coor.behavior.type2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseActivity;
import com.yline.coor.behavior.R;
import com.yline.coor.behavior.common.SimpleRecyclerAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Type2Activity extends BaseActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, Type2Activity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type2);

        RecyclerView recyclerView = findViewById(R.id.type2_recycler);
        recyclerView.setAdapter(SimpleRecyclerAdapter.create(this, 40));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
