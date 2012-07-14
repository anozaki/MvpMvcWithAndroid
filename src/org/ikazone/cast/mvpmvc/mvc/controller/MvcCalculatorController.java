package org.ikazone.cast.mvpmvc.mvc.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Singleton;

import org.ikazone.cast.mvpmvc.model.CalculationData;
import org.ikazone.cast.mvpmvc.model.Operation;
import org.ikazone.cast.mvpmvc.mvc.ActionEvent;
import org.ikazone.cast.mvpmvc.mvc.ActionEvent.ActionHandler;
import org.ikazone.cast.mvpmvc.mvc.DataChangeEvent;
import org.ikazone.cast.mvpmvc.mvc.MvcEventConductor;

@Singleton
public class MvcCalculatorController implements ActionHandler {
	private BigDecimal current = new BigDecimal(0);

	public static String CalculatorContext = MvcCalculatorController.class
			.getSimpleName() + "/CalculationUpdated";

	private String number = "";
	private String lastNumber = "";

	private Operation lastOperation;
	private boolean clearOperation;
	
	public MvcCalculatorController() {
		MvcEventConductor.getInstance().addActionHandler(this);
	}

	public void appendDecimal() {
		if (!number.contains(".")) {
			number += '.';
		}

		if (clearOperation) {
			lastOperation = null;
			clearOperation = false;
		}

		DataChangeEvent event = new DataChangeEvent();
		event.setDate(CalculatorContext, new CalculationData(current, number,
				null));
		MvcEventConductor.getInstance().fireDataChangeEvent(event);

	}

	public void appendNumber(int number) {
		if (this.number.length() > 0
				|| (!this.number.equals("0") && number != 0)) {
			this.number += number;
		}

		if (clearOperation) {
			lastOperation = null;
			clearOperation = false;
		}

		DataChangeEvent event = new DataChangeEvent();
		event.setDate(CalculatorContext, new CalculationData(current,
				this.number, null));
		MvcEventConductor.getInstance().fireDataChangeEvent(event);
	}

	public void clear() {
		number = "";
		current = new BigDecimal(0);
		lastOperation = null;
		clearOperation = false;

		DataChangeEvent event = new DataChangeEvent();
		event.setDate(CalculatorContext, new CalculationData(current, number,
				null));
		MvcEventConductor.getInstance().fireDataChangeEvent(event);
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
				case EQUAL:
				default:
					// don't do anything.
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

		DataChangeEvent event = new DataChangeEvent();
		event.setDate(CalculatorContext,
				new CalculationData(current, current.toString(),
						processedOperation));
		MvcEventConductor.getInstance().fireDataChangeEvent(event);
	}

	@Override
	public void onAction(ActionEvent event) {
		String action = event.getAction();
		if ("appendDecimal".equals(action)) {
			appendDecimal();
		} else if ("appendNumber".equals(action)) {
			appendNumber((Integer) event.getData()[0]);
		} else if ("operation".equals(action)) {
			onCalculate((Operation) event.getData()[0]);
		} else if ("clear".equals(action)) {
			clear();
		}
	}

}
