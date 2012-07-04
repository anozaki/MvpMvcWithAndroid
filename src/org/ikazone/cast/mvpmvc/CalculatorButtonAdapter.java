package org.ikazone.cast.mvpmvc;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class CalculatorButtonAdapter extends BaseAdapter {
	private final String buttons[];
	private final Context context;
	private final OnClickListener listener;

	public CalculatorButtonAdapter(Context context, String buttons[],
			OnClickListener listener) {
		this.context = context;
		this.buttons = buttons;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return buttons.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return buttons[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Button button;
		if (view == null) {
			button = new Button(context);
			button.setOnClickListener(listener);
		} else {
			button = (Button) view;
		}
		button.setTag(buttons[position]);
		button.setText(buttons[position]);

		return button;
	}
}
