package org.ikazone.cast.mvpmvc.mvc;

import javax.inject.Inject;

import org.ikazone.cast.mvpmvc.CalculatorButtonAdapter;
import org.ikazone.cast.mvpmvc.R;
import org.ikazone.cast.mvpmvc.model.CalculationData;
import org.ikazone.cast.mvpmvc.model.Operation;
import org.ikazone.cast.mvpmvc.mvc.MvcCalculationDataChangeEvent.MvcCalculationDataChangeHandler;
import org.ikazone.cast.mvpmvc.mvc.controller.MvcCalculatorController;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * We will treat Activity as part of the view. We will be reusing the layout for
 * both MVC and MVP, but the underlying Activity will be different.
 * 
 * @author anozaki
 */
public class MvcCalculatorActivity extends RoboActivity implements
		MvcCalculationDataChangeHandler {

	private String buttons[] = { " ", " ", " ", "C", "1", "2", "3", "+", "4", "5",
			"6", "-", "7", "8", "9", "*", ".", "0", "=", "/" };

	private CalculatorButtonAdapter adapter;

	@InjectView(R.id.buttonGridView)
	GridView gridView;

	@InjectView(R.id.calculationView)
	TextView calculationView;

	@Inject
	MvcCalculatorController controller;

	@Override
	public void onCalculationChange(MvcCalculationDataChangeEvent event) {
		CalculationData data = event.getCalculation();
		calculationView.setText(data.getDisplayValue());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);

		adapter = new CalculatorButtonAdapter(this, buttons,
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						Button b = (Button) view;
						char val = ((String) b.getTag()).charAt(0);

						if (val >= '0' && val <= '9') {
							controller.appendNumber(val - '0');
						} else {
							switch (val) {
							case '.':
								controller.appendDecimal();
								break;
							case '+':
								controller.onCalculate(Operation.ADD);
								break;
							case '-':
								controller.onCalculate(Operation.SUBTRACT);
								break;
							case '*':
								controller.onCalculate(Operation.MULTIPLY);
								break;
							case '/':
								controller.onCalculate(Operation.DIVIDE);
								break;
							case '=':
								controller.onCalculate(Operation.EQUAL);
								break;
							case 'C':
								controller.clear();
								break;
							default:
								break;
							}
						}
					}

				});

		gridView.setAdapter(adapter);

		controller.addCalculationChangeHandler(this);

		controller.clear();

	}

	/*
	 * Anything we attached in create must be removed here.
	 */
	@Override
	protected void onStop() {
		super.onStop();

		controller.removeCalculationChangeHandler(this);
	}
}
