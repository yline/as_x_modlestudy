package com.yline.coor.behavior.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.coor.behavior.R;
import com.yline.test.StrConstant;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    public static SimpleRecyclerAdapter create(Context context, int size) {
        SimpleRecyclerAdapter recyclerAdapter = new SimpleRecyclerAdapter(context);
        recyclerAdapter.setDataList(StrConstant.getListRandom(Math.max(15, size)));
        return recyclerAdapter;
    }

    private List<String> mDataList;
    private static final int[] ColorArray = new int[3];

    private SimpleRecyclerAdapter(Context context) {
        int colorAccent = ContextCompat.getColor(context, R.color.item_1);
        int colorPrimary = ContextCompat.getColor(context, R.color.item_2);
        int colorPrimaryDark = ContextCompat.getColor(context, R.color.item_3);

        ColorArray[0] = colorAccent;
        ColorArray[1] = colorPrimary;
        ColorArray[2] = colorPrimaryDark;
    }

    public int getItemRes() {
        return R.layout.item_simple_recycler;
    }

    public void setDataList(List<String> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtil.v("viewType = " + viewType);
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemRes(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        LogUtil.v("position = " + position);
        String model = mDataList.get(position);

        TextView textView = holder.get(R.id.item_simple_recycler_text);

        textView.setText(String.format(Locale.CHINA, "%d - %s", position, model));
        textView.setBackgroundColor(ColorArray[position % 3]);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
