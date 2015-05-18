package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.ABase;

class RootRow extends AParentRow {
	public RootRow() {
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public ABase getElementObject() {
		return null;
	}

	@Override
	public String getName() {
		return "Root";
	}

	@Override
	public ATreeTableRow getParent() {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}
}
