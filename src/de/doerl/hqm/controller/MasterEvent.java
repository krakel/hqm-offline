package de.doerl.hqm.controller;

import java.util.EventObject;

import de.doerl.hqm.view.EditView;

public class MasterEvent extends EventObject {
	private static final long serialVersionUID = -6831324451338334610L;
	private EditView mView;

	public MasterEvent( EditController master, EditView view) {
		super( master);
		mView = view;
	}

	public EditView getView() {
		return mView;
	}
}
