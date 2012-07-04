package org.ikazone.cast.mvpmvc.mvc.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.ikazone.cast.mvpmvc.model.CalculationData;
import org.ikazone.cast.mvpmvc.model.Operation;
import org.ikazone.cast.mvpmvc.mvc.MvcCalculationDataChangeEvent;
import org.ikazone.cast.mvpmvc.mvc.MvcCalculationDataChangeEvent.MvcCalculationDataChangeHandler;

@Singleton
public class MvcCalculatorController {
	private BigDecimal current = new BigDecimal(0);

	private String number = "";
	private String lastNumber = "";

	private Operation lastOperation;
	private boolean clearOperation;

	private List<MvcCalculationDataChangeHandler> handlers = new ArrayList<MvcCalculationDataChangeHandler>();

	public void addCalculationChangeHandler(
			MvcCalculationDataChangeHandler handler) {
		handlers.add(handler);
	}

	public void appendDecimal() {
		if (!number.contains(".")) {
			number += '.';
		}

		if (clearOperation) {
			lastOperation = null;
			clearOperation = false;
		}

		MvcCalculationDataChangeEvent event = new MvcCalculationDataChangeEvent();
		event.setCalculation(new CalculationData(current, number, null));
		fireCalcuationChangeEvent(event);

	}

	public void appendNumber(int number) {
		if (this.number.length() > 0 || (!this.number.equals("0") && number != 0)) {
			this.number += number;
		}

		if (clearOperation) {
			lastOperation = null;
			clearOperation = false;
		}

		MvcCalculationDataChangeEvent event = new MvcCalculationDataChangeEvent();
		event.setCalculation(new CalculationData(current, this.number, null));
		fireCalcuationChangeEvent(event);
	}

	public void clear() {
		number = "";
		current = new BigDecimal(0);
		lastOperation = null;
		clearOperation = false;

		MvcCalculationDataChangeEvent event = new MvcCalculationDataChangeEvent();
		event.setCalculation(new CalculationData(current, number, null));
		fireCalcuationChangeEvent(event);
	}

	public void onCalculate(Operation operation) {
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

		Operation processedOperation = lastOperation;

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

		MvcCalculationDataChangeEvent event = new MvcCalculationDataChangeEvent();
		event.setCalculation(new CalculationData(current, current.toString(),
				processedOperation));
		fireCalcuationChangeEvent(event);
	}

	public void removeCalculationChangeHandler(
			MvcCalculationDataChangeHandler handler) {
		handlers.remove(handler);
	}

	private void fireCalcuationChangeEvent(MvcCalculationDataChangeEvent event) {
		for (MvcCalculationDataChangeHandler h : handlers) {
			event.onFire(h);
		}
	}
}
