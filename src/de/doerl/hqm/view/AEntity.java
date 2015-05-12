package de.doerl.hqm.view;

import de.doerl.hqm.base.ABase;

abstract class AEntity<T extends ABase> {
	protected EditView mView;

	public AEntity( EditView view) {
		mView = view;
	}

	public abstract T getBase();

	public abstract void update();
}
