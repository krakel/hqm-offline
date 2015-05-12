package de.doerl.hqm.model;

import de.doerl.hqm.base.ABase;

public class ModelEvent {
	private ABase mBase;

	public ModelEvent( ABase base) {
		mBase = base;
	}

	public ABase getBase() {
		return mBase;
	}
}
