package de.doerl.hqm.ui.treetable;

import java.util.ArrayList;
import java.util.List;

abstract class AParentRow extends ARow {
	private List<ATreeTableRow> mChildren = new ArrayList<ATreeTableRow>();

	public AParentRow() {
	}

	@Override
	public List<ATreeTableRow> getChildren() {
		return mChildren;
	}

	@Override
	public String toString() {
		return getName();
	}
}
