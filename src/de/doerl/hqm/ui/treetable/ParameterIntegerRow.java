package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.FParameterInteger;

class ParameterIntegerRow extends AParameterRow {
	private FParameterInteger mParameter;

	public ParameterIntegerRow( ATreeTableRow parent, FParameterInteger param) {
		super( parent);
		mParameter = param;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public FParameterInteger getElementObject() {
		return mParameter;
	}
}
