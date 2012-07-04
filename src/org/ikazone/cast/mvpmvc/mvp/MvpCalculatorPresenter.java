package org.ikazone.cast.mvpmvc.mvp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.ikazone.cast.mvpmvc.model.Operation;

/**
 * As an example I pretty much copy and pasted the code from the controller. You
 * will see that most of the function is exactly the same. One major different
 * is that the presenter works very closely with the view interface. Also the
 * View and the Presenter are usually 1 to 1 relation.
 * 
 */
public class MvpCalculatorPresenter {

	private BigDecimal current = new BigDecimal(0);

	private String number = "";

	private String lastNumber = "";
	private Operation lastOperation;

	private boolean clearOperation;

	private final MvpCalculatorView view;

	public MvpCalculatorPresenter(MvpCalculatorView view) {
		this.view = view;

	}

	public void appendDecimal() {
		if (!number.contains(".")) {
			number += '.';
		}

		if (clearOperation) {
			lastOperation = null;
			clearOperation = false;
		}

		updateView(number);
	}

	public void appendNumber(int number) {
		if (this.number.length() > 0 || !this.number.equals("0") && number != 0) {
			this.number += number;
		}

		if (clearOperation) {
			lastOperation = null;
			clearOperation = false;
		}

		updateView(this.number);
	}

	public void clear() {
		number = "";
		current = new BigDecimal(0);
		lastOperation = null;
		clearOperation = false;

		updateView(number);
	}

	public void onCalculate(Operation operation) {
		/*
		 * I event left the comment in the same way to show how close things
		 * are.
		 */

		// These are special cases when the last operation was equal.
		if (clearOperation == true && !Operation.EQUAL.equals(operation)) {
			/*
			 * if the next operation is a non-equal operation. We want to
			 * replace the last operation with the new operation.
			 * 
			 * example input: [1] [+] [1] [=] (displays 2) [+]
			 * 
			 * if the user pressed [+] this is the equivalent of [2] [+]
			 */
			lastOperation = operation;
			clearOperation = false;
			lastNumber = "";
			return;
		} else if (clearOperation == true) {
			/*
			 * If the next operation is a equal operation. Then we want to
			 * operate the same operation again.
			 * 
			 * example input: [1] [+] [1] [=] (displays 2) [=] (displays 3)
			 * 
			 * In this case the 2nd [=] is the equivalent of [2] [+] [1] [=]
			 */
			number = lastNumber;

			// fall through to actually do the calculation.
		}

		if (!number.equals("")) {
			BigDecimal n = new BigDecimal(number);
			if (lastOperation != null) {
				/*
				 * If there was was a number inputed, we want to process the
				 * number into the current running value.
				 */
				switch (lastOperation) {
				case ADD:
					current = current.add(n);
					break;
				case DIVIDE:
					current = current.divide(n, 10, RoundingMode.HALF_UP);
					break;
				case MULTIPLY:
					current = current.multiply(n);
					break;
				case SUBTRACT:
					current = current.subtract(n);
					break;
				}
			} else {
				/*
				 * If there is no operation specified, this the first time the
				 * user hit the operation key. In this case the number is the
				 * current value.
				 */
				current = n;
			}
		} else {
			/*
			 * If the user click on a operation key in a row for example [+] [-]
			 * then we will just reset the last operation the user specified.
			 */
			lastOperation = operation;
			return;
		}

		if (!Operation.EQUAL.equals(operation)) {
			lastOperation = operation;
		} else {
			/*
			 * This tells us the last operation was equal, but in more general
			 * term the controller knows to clear the values if anything else
			 * then operation is pressed.
			 * 
			 * example: [1] [+] [1] [=] (display 2) [3]
			 * 
			 * [3] should be start of new operation.
			 */
			clearOperation = true;
		}
		lastNumber = number;
		number = "";

		updateView(current.toString());
	}

	private void updateView(String display) {
		view.setDisplay(display);
	}

}
