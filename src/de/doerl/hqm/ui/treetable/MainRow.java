package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.ABase;

public class MainRow extends AParentRow {
	private ATreeTableRow mParent;
	private ABase mBase;

	public MainRow( ATreeTableRow parent, ABase base) {
		mParent = parent;
		mBase = base;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public ABase getElementObject() {
		return mBase;
	}

	@Override
	public String getName() {
		return mBase.getElementTyp().getToken();
	}

	@Override
	public ATreeTableRow getParent() {
		return mParent;
	}

	@Override
	public String getValue() {
		return null;
	}
}
