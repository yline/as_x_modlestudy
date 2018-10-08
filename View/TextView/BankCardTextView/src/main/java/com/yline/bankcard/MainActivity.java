package com.yline.bankcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.yline.bankcard.view.BankCardTextWatcher;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		EditText bankCardEditText = findViewById(R.id.main_bank_card);
		bankCardEditText.addTextChangedListener(new BankCardTextWatcher(bankCardEditText));
		bankCardEditText.append(" "); // 手动添加一个空格，从而引起格式的变化；
	}
}
