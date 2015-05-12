package de.doerl.hqm.ui.treetable;

import java.util.Collections;
import java.util.List;

import de.doerl.hqm.base.AParameter;

abstract class AParameterRow extends ARow {
	private ATreeTableRow mParent;

	public AParameterRow( ATreeTableRow parent) {
		mParent = parent;
	}

	@Override
	public List<ATreeTableRow> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public abstract AParameter getElementObject();

	@Override
	public String getName() {
		return getElementObject().getName();
	}

	@Override
	public ATreeTableRow getParent() {
		return mParent;
	}

	@Override
	public String getValue() {
		return getElementObject().toString();
	}

	@Override
	public String toString() {
		return getElementObject().getName();
	}
}
