package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.ANamed;

class NamedRow extends AParentRow {
	private ATreeTableRow mParent;
	private ANamed mBase;

	public NamedRow( ATreeTableRow parent, ANamed base) {
		mParent = parent;
		mBase = base;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public ANamed getElementObject() {
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
		return mBase.getName();
	}
}
