package com.yline.runtime.xutils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yline.runtime.xutils.lib.ViewInjectorManager;
import com.yline.runtime.xutils.lib.utils.LogIoc;
import com.yline.runtime.xutils.lib.view.annotation.ContentView;
import com.yline.runtime.xutils.lib.view.annotation.Event;
import com.yline.runtime.xutils.lib.view.annotation.ViewInject;


@ContentView(R.layout.fragment_second)
public class SecondFragment extends Fragment {
    private boolean injected = false;

    @ViewInject(R.id.fragment_second_tv)
    private TextView mTvSecond;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injected = true;
        return ViewInjectorManager.getInstance().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected){
            ViewInjectorManager.getInstance().inject(view, this.getView());
        }

        mTvSecond.setText("second After");
    }

    @Event(R.id.fragment_second_btn)
    private void onFinishClick(View v) {
        LogIoc.v("");
        if (getContext() instanceof Activity) {
            LogIoc.v("");
            ((Activity) getContext()).finish();
        }
    }
}













