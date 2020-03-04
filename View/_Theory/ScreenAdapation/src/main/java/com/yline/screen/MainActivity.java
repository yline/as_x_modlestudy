package com.yline.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yline.screen.percentdp.DensityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DensityUtils.setCustomDensity(this, MainApplication.getApplication());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
