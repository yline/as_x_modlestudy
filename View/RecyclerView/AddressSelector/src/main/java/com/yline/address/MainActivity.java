package com.yline.address;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yline.address.dialog.AddressSelectDialog;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity {
    private String selectedProvince;
    private String selectedCity;
    private String selectedArea;

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.main_result);
        findViewById(R.id.main_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegionDialog();
            }
        });
    }

    private void showRegionDialog() {
        AddressSelectDialog selectDialog = new AddressSelectDialog(this);
        selectDialog.setOnRegionListener(new AddressSelectDialog.OnRegionListener() {
            @Override
            public void onRegionListener(String province, String city, String area) {
                // 选择完回调结果赋值给当前
                selectedProvince = province;
                selectedCity = city;
                selectedArea = area;

                resultTextView.setText(selectedProvince + " " + selectedCity + " " + selectedArea);
            }
        });
        selectDialog.setData(selectedProvince, selectedCity, selectedArea, null);
        selectDialog.show();
    }
}
