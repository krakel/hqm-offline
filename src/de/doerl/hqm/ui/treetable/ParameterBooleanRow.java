package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.FParameterBoolean;

public class ParameterBooleanRow extends AParameterRow {
	private FParameterBoolean mParameter;

	public ParameterBooleanRow( ATreeTableRow parent, FParameterBoolean param) {
		super( parent);
		mParameter = param;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public FParameterBoolean getElementObject() {
		return mParameter;
	}
}
