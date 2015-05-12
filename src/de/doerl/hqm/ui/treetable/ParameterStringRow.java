package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.FParameterString;

class ParameterStringRow extends AParameterRow {
	private FParameterString mParameter;

	public ParameterStringRow( ATreeTableRow parent, FParameterString param) {
		super( parent);
		mParameter = param;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public FParameterString getElementObject() {
		return mParameter;
	}
}
