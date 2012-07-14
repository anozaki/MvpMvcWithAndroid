package org.ikazone.cast.mvpmvc.mvc;

public class DataChangeEvent {

	public interface DataChangeHandler {
		void onDataChange(DataChangeEvent event);
	}

	private String context;

	private Object data;

	public Object getDate() {
		return data;
	}

	public String getContext() {
		return context;
	}

	public void onFire(DataChangeHandler handler) {
		handler.onDataChange(this);
	}

	public void setDate(String context, Object data) {
		this.context = context;
		this.data = data;
	}
}
