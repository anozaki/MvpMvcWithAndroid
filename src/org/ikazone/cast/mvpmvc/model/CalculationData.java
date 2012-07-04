package org.ikazone.cast.mvpmvc.model;

import java.math.BigDecimal;

public class CalculationData {
	private BigDecimal value;
	private String displayValue;
	private Operation lastOperation;

	public CalculationData(BigDecimal value, String displayValue,
			Operation lastOperation) {
		this.value = value;
		this.displayValue = displayValue;
		this.lastOperation = lastOperation;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public Operation getLastOperation() {
		return lastOperation;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public void setLastOperation(Operation lastOperation) {
		this.lastOperation = lastOperation;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
