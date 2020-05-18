package com.yline.coor.behavior.type4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yline.base.BaseActivity;
import com.yline.coor.behavior.R;
import com.yline.coor.behavior.common.SimpleRecyclerAdapter;

public class Type4Activity extends BaseActivity {
    public static void launch(Context context){
        if (null != context){
            Intent intent = new Intent();
            intent.setClass(context, Type4Activity.class);
            if (!(context instanceof Activity)){
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type4);

        RecyclerView recyclerView = findViewById(R.id.type4_recycler);
        SimpleRecyclerAdapter recyclerAdapter = SimpleRecyclerAdapter.create(this, 40);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
