package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.ASet;

public class SetRow extends AParentRow {
	private ATreeTableRow mParent;
	private ASet<?> mBase;

	public SetRow( ATreeTableRow parent, ASet<?> base) {
		mParent = parent;
		mBase = base;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public ASet<?> getElementObject() {
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
