package de.doerl.hqm.ui.tree;

import de.doerl.hqm.base.ABase;

public abstract class ANode {
	protected ANode() {
	}

	public abstract ABase getBase();

	@Override
	public String toString() {
		return String.valueOf( getBase().getElementTyp().getToken());
	}
}
