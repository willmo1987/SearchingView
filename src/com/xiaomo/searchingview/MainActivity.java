package com.xiaomo.searchingview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiaomo.searchingview.customviews.SearchingView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private SearchingView searchingView;
	private Button startButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		searchingView = (SearchingView) findViewById(R.id.searchingView);
		searchingView.setOnClickListener(this);
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.startButton) {
			searchingView.initialize();
			searchingView.startRunning();
			startButton.setVisibility(View.GONE);
		}
		else {
			searchingView.stopRunning();
			startButton.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		searchingView.destroy();
	}
	
}
