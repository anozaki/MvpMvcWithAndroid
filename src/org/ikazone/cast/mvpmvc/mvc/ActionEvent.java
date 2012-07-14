package org.ikazone.cast.mvpmvc.mvc;


public class ActionEvent {

	public interface ActionHandler {
		void onAction(ActionEvent event);
	}

	private String action;

	private Object[] data;

	public Object[] getData() {
		return data;
	}

	public String getAction() {
		return action;
	}

	public void onFire(ActionHandler handler) {
		handler.onAction(this);
	}

	public void setAction(String action, Object... data) {
		this.action = action;
		this.data = data;
	}
}
