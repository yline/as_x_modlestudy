package com.dm.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.design.pattern.mvp.R;
import com.dm.mvp.bean.UserLogin;
import com.dm.mvp.presenter.UserLoginPresenter;
import com.dm.mvp.view.IUserLoginView;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity implements IUserLoginView
{
	private EditText mEtUsername, mEtPassword;

	private Button mBtnLogin, mBtnClear;

	private ProgressBar mPbLoading;

	private UserLoginPresenter mUserLoginPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initView()
	{
		mEtUsername = (EditText) findViewById(R.id.id_et_username);
		mEtPassword = (EditText) findViewById(R.id.id_et_password);

		mBtnClear = (Button) findViewById(R.id.id_btn_clear);
		mBtnLogin = (Button) findViewById(R.id.id_btn_login);

		mPbLoading = (ProgressBar) findViewById(R.id.id_pb_loading);
	}

	private void initData()
	{
		mUserLoginPresenter = new UserLoginPresenter(this);

		mBtnLogin.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				mPbLoading.setVisibility(View.VISIBLE);
				mUserLoginPresenter.login();
			}
		});

		mBtnClear.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				mEtUsername.setText("");
				mEtPassword.setText("");
			}
		});
	}

	@Override
	public String getUserName()
	{
		return mEtUsername.getText().toString();
	}

	@Override
	public String getPassword()
	{
		return mEtPassword.getText().toString();
	}

	@Override
	public void LoginSuccess(UserLogin userBean)
	{
		mPbLoading.setVisibility(View.GONE);

		Toast.makeText(this, userBean.getUsername() + " login success , to MainActivity", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void LoginFailed(String error)
	{
		mPbLoading.setVisibility(View.GONE);

		Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
	}

}
