package org.ikazone.cast.mvpmvc.mvc;

import java.util.ArrayList;
import java.util.List;

import org.ikazone.cast.mvpmvc.mvc.ActionEvent.ActionHandler;
import org.ikazone.cast.mvpmvc.mvc.DataChangeEvent.DataChangeHandler;

public class MvcEventConductor {

	public static final MvcEventConductor instance = new MvcEventConductor();

	public static final MvcEventConductor getInstance() {
		return instance;
	}

	private List<DataChangeHandler> handlers = new ArrayList<DataChangeHandler>();
	private List<ActionHandler> actionHandlers = new ArrayList<ActionHandler>();

	public void addDataChangeHandler(DataChangeHandler handler) {
		handlers.add(handler);
	}

	public void removeDataChangeHandler(DataChangeHandler handler) {
		handlers.remove(handler);
	}

	public void fireDataChangeEvent(DataChangeEvent event) {
		for (DataChangeHandler h : handlers) {
			event.onFire(h);
		}
	}

	public void addActionHandler(ActionHandler handler) {
		actionHandlers.add(handler);
	}

	public void removeDataChangeHandler(ActionHandler handler) {
		actionHandlers.remove(handler);
	}

	public void action(String action, Object ... param) {
		ActionEvent event = new ActionEvent();
		event.setAction(action, param);
		
		for (ActionHandler h : actionHandlers) {
			event.onFire(h);
		}
	}
}
