package de.doerl.hqm.ui.tree;

import java.net.URI;

import de.doerl.hqm.base.FHqm;

class MainNode extends ANode {
	private FHqm mHqm;

	public MainNode( FHqm hqm) {
		mHqm = hqm;
	}

	@Override
	public FHqm getBase() {
		return mHqm;
	}

	@Override
	public String toString() {
		URI uri = mHqm.getURI();
		return String.valueOf( uri != null ? uri.getPath() : "new hqm");
	}
}
