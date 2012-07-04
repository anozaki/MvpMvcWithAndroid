package org.ikazone.cast.mvpmvc.mvc;

import org.ikazone.cast.mvpmvc.model.CalculationData;

public class MvcCalculationDataChangeEvent {

	public interface MvcCalculationDataChangeHandler {
		void onCalculationChange(MvcCalculationDataChangeEvent event);
	}

	private CalculationData calculation;

	public CalculationData getCalculation() {
		return calculation;
	}

	public void onFire(MvcCalculationDataChangeHandler handler) {
		handler.onCalculationChange(this);
	}

	public void setCalculation(CalculationData calculation) {
		this.calculation = calculation;
	}
}
