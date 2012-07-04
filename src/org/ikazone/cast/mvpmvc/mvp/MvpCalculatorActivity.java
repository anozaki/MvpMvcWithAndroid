package org.ikazone.cast.mvpmvc.mvp;

import org.ikazone.cast.mvpmvc.CalculatorButtonAdapter;
import org.ikazone.cast.mvpmvc.R;
import org.ikazone.cast.mvpmvc.model.Operation;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Doesn't the view look familiar? The code is pretty close to the Mvc version.
 * 
 */
public class MvpCalculatorActivity extends RoboActivity implements
		MvpCalculatorView {

	private String buttons[] = { " ", " ", " ", "C", "1", "2", "3", "+", "4", "5",
			"6", "-", "7", "8", "9", "*", ".", "0", "=", "/" };

	private CalculatorButtonAdapter adapter;

	@InjectView(R.id.buttonGridView)
	GridView gridView;

	@InjectView(R.id.calculationView)
	TextView calculationView;

	private MvpCalculatorPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);

		presenter = new MvpCalculatorPresenter(this);

		adapter = new CalculatorButtonAdapter(this, buttons,
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						Button b = (Button) view;
						char val = ((String) b.getTag()).charAt(0);

						if (val >= '0' && val <= '9') {
							presenter.appendNumber(val - '0');
						} else {
							switch (val) {
							case '.':
								presenter.appendDecimal();
								break;
							case '+':
								presenter.onCalculate(Operation.ADD);
								break;
							case '-':
								presenter.onCalculate(Operation.SUBTRACT);
								break;
							case '*':
								presenter.onCalculate(Operation.MULTIPLY);
								break;
							case '/':
								presenter.onCalculate(Operation.DIVIDE);
								break;
							case '=':
								presenter.onCalculate(Operation.EQUAL);
								break;
							case 'C':
								presenter.clear();
								break;
							default:
								break;
							}
						}
					}

				});

		gridView.setAdapter(adapter);
	}

	@Override
	public void setDisplay(String display) {
		calculationView.setText(display);
	}

}
