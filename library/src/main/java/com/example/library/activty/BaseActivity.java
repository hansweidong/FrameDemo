package com.example.library.activty;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResource());

		initVariables();
		initViews(savedInstanceState);
		loadData();
	}
	protected abstract int getLayoutResource();
	protected abstract void initVariables();
	protected abstract void initViews(Bundle savedInstanceState);
	protected abstract void loadData();
}