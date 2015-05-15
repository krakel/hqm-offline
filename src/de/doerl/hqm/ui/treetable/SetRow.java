package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.ACategory;

public class SetRow extends AParentRow {
	private ATreeTableRow mParent;
	private ACategory<?> mBase;

	public SetRow( ATreeTableRow parent, ACategory<?> base) {
		mParent = parent;
		mBase = base;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public ACategory<?> getElementObject() {
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
