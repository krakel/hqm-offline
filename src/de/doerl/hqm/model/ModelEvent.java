package de.doerl.hqm.model;

import de.doerl.hqm.base.ABase;

public class ModelEvent {
	public final ABase mBase;
	public final boolean mCtrlKey;

	public ModelEvent( ABase base, boolean ctrlKey) {
		mBase = base;
		mCtrlKey = ctrlKey;
	}
}
