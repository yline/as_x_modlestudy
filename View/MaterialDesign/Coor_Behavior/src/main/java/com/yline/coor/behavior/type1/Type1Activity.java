package com.yline.coor.behavior.type1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseActivity;
import com.yline.coor.behavior.R;
import com.yline.coor.behavior.common.SimpleRecyclerAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Type1Activity extends BaseActivity {
    public static void launch(Context context){
    	if (null != context){
    		Intent intent = new Intent();
    		intent.setClass(context, Type1Activity.class);
    		if (!(context instanceof Activity)){
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type1);

        RecyclerView recyclerView = findViewById(R.id.type1_recycler);
        recyclerView.setAdapter(SimpleRecyclerAdapter.create(Type1Activity.this, 20));
        recyclerView.setLayoutManager(new LinearLayoutManager(Type1Activity.this));
    }
}
