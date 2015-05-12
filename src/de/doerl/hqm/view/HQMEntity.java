package de.doerl.hqm.view;

import de.doerl.hqm.base.FHqm;

class HQMEntity extends AEntity<FHqm> {
	private FHqm mBase;

	public HQMEntity( EditView view, FHqm base) {
		super( view);
		mBase = base;
	}

	@Override
	public FHqm getBase() {
		return mBase;
	}

	@Override
	public void update() {
	}
}
