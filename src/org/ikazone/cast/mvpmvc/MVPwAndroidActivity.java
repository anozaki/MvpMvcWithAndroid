package org.ikazone.cast.mvpmvc;

import org.ikazone.cast.mvpmvc.mvc.MvcCalculatorActivity;
import org.ikazone.cast.mvpmvc.mvp.MvpCalculatorActivity;

import roboguice.activity.RoboActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This is the entry point of the application. You will find the MVC and MVP
 * implementation in there own package. Also this is a concept and probably
 * would need more work to say its production ready.
 * 
 * I am also using a DI (dependency injection) library here to help me keep the
 * code organized.
 */
public class MVPwAndroidActivity extends RoboActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onMvpButtonClick(View view) {
		startActivity(new Intent(this, MvpCalculatorActivity.class));
	}

	public void onMvcButtonClick(View view) {
		startActivity(new Intent(this, MvcCalculatorActivity.class));
	}
}