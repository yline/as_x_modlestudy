package com.view.animate.transition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.adapter.CommonListAdapter;
import com.yline.view.recycler.holder.ViewHolder;

public class MainActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.lv);
        MainAdapter adapter = new MainAdapter(this);
        listView.setAdapter(adapter);

        adapter.setDataList(StrConstant.getListEnglish(20), true);
    }

    private class MainAdapter extends CommonListAdapter<String> {

        public MainAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemRes(int position) {
            return R.layout.item_main_list;
        }

        @Override
        protected void onBindViewHolder(ViewGroup parent, final ViewHolder viewHolder, final int position) {
            viewHolder.setText(R.id.tv_name, sList.get(position));

            viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ready for intent
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("name", sList.get(position));

                    // start transition
                    EasyTransition.startActivity(MainActivity.this, intent,
                            viewHolder.get(R.id.iv_icon),
                            viewHolder.get(R.id.tv_name),
                            findViewById(R.id.v_top_card));
                }
            });
        }
    }
}
