package com.progressbar;

import android.os.Bundle;
import android.widget.RatingBar;

import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity {
	private RatingBar ratingBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ratingBar = (RatingBar) findViewById(R.id.rating_bar);
		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				SDKManager.toast("rating = " + rating + ", fromUser = " + fromUser);
			}
		});
	}
}
