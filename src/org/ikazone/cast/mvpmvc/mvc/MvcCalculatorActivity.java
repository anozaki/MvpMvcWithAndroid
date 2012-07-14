package org.ikazone.cast.mvpmvc.mvc;

import org.ikazone.cast.mvpmvc.CalculatorButtonAdapter;
import org.ikazone.cast.mvpmvc.R;
import org.ikazone.cast.mvpmvc.model.CalculationData;
import org.ikazone.cast.mvpmvc.model.Operation;
import org.ikazone.cast.mvpmvc.mvc.DataChangeEvent.DataChangeHandler;

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
		DataChangeHandler {
	
	private String buttons[] = { " ", " ", " ", "C", "1", "2", "3", "+", "4", "5",
			"6", "-", "7", "8", "9", "*", ".", "0", "=", "/" };

	private CalculatorButtonAdapter adapter;

	@InjectView(R.id.buttonGridView)
	GridView gridView;

	@InjectView(R.id.calculationView)
	TextView calculationView;
	
	MvcEventConductor conductor = MvcEventConductor.getInstance();

	@Override
	public void onDataChange(DataChangeEvent event) {
		CalculationData data = (CalculationData) event.getDate();
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
							conductor.action("appendNumber", val - '0');
						} else {
							switch (val) {
							case '.':
								conductor.action("appendDecimal");
								break;
							case '+':
								conductor.action("operation", Operation.ADD);
								break;
							case '-':
								conductor.action("operation", Operation.SUBTRACT);
								break;
							case '*':
								conductor.action("operation", Operation.MULTIPLY);
								break;
							case '/':
								conductor.action("operation", Operation.DIVIDE);
								break;
							case '=':
								conductor.action("operation", Operation.EQUAL);
								break;
							case 'C':
								conductor.action("clear"); 
								break;
							default:
								break;
							}
						}
					}

				});

		gridView.setAdapter(adapter);

		conductor.addDataChangeHandler(this);

		conductor.action("clear"); 

	}

	/*
	 * Anything we attached in create must be removed here.
	 */
	@Override
	protected void onStop() {
		super.onStop();

		MvcEventConductor.getInstance().addDataChangeHandler(this);
	}
}
