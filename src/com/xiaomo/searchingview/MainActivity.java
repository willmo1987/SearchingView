package com.xiaomo.searchingview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiaomo.searchingview.customviews.SearchingView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private SearchingView findView;
	private Button startButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView = (SearchingView) findViewById(R.id.findView);
		findView.setOnClickListener(this);
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.startButton) {
			findView.initialize();
			findView.startRunning();
			startButton.setVisibility(View.GONE);
		}
		else {
			findView.stopRunning();
			startButton.setVisibility(View.VISIBLE);
		}
	}
	
}
