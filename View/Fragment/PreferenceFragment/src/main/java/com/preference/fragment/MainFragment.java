package com.preference.fragment;

import android.os.Bundle;

import com.preference.R;
import com.yline.base.BasePreferenceFragment;

public class MainFragment extends BasePreferenceFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.fragment_main_preferences);
	}
}
