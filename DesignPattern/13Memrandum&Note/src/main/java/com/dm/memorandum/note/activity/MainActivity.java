package com.dm.memorandum.note.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.design.pattern.memrandum.note.R;
import com.dm.memorandum.note.caretaker.Caretaker;
import com.yline.base.BaseActivity;

import dm.bwl_note.view.NoteEditText;

public class MainActivity extends BaseActivity implements View.OnClickListener
{
	private NoteEditText mNetMy;

	private Button mBtnUndo, mBtnSave, mBtnRedo;

	private Caretaker caretaker;

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
		mNetMy = (NoteEditText) findViewById(R.id.et_note);
		mBtnUndo = (Button) findViewById(R.id.btn_undo);
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnRedo = (Button) findViewById(R.id.btn_redo);
	}

	private void initData()
	{
		mBtnUndo.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
		mBtnRedo.setOnClickListener(this);

		caretaker = new Caretaker();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_undo:
				if (null != caretaker.getPrevMemoto())
				{
					mNetMy.restoreEditText(caretaker.getPrevMemoto());
				}
				showToast("撤销:");
				break;
			case R.id.btn_save:
				caretaker.saveMemoto(mNetMy.createMemotoForEditText());
				showToast("保存笔记:");
				break;
			case R.id.btn_redo:
				mNetMy.restoreEditText(caretaker.getNextMemoto());
				showToast("重做:");
				break;
		}
	}

	private void showToast(String msg)
	{
		String content = ",文字内容" + mNetMy.getText() + "\n光标位置 : " + mNetMy.getSelectionStart();
		Toast.makeText(this, msg + content, Toast.LENGTH_SHORT).show();
	}

}
