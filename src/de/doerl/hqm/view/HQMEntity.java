package de.doerl.hqm.view;

import de.doerl.hqm.base.ABase;

public class HQMEntity extends AEntity<ABase> {
	private ABase mBase;

	public HQMEntity( EditView view, ABase base) {
		super( view);
		mBase = base;
	}

	@Override
	public ABase getBase() {
		return mBase;
	}

	@Override
	public void update() {
	}
}
