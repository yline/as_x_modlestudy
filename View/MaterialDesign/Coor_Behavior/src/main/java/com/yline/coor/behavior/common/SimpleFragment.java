package com.yline.coor.behavior.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.coor.behavior.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleFragment extends Fragment {

    public static SimpleFragment newInstance() {
        SimpleFragment simpleFragment = new SimpleFragment();
        return simpleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_simple_recycler);
        recyclerView.setAdapter(SimpleRecyclerAdapter.create(view.getContext(), 40));
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}
