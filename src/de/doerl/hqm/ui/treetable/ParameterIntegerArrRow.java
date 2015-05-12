package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.FParameterIntegerArr;

class ParameterIntegerArrRow extends AParameterRow {
	private FParameterIntegerArr mParameter;

	public ParameterIntegerArrRow( ATreeTableRow parent, FParameterIntegerArr param) {
		super( parent);
		mParameter = param;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public FParameterIntegerArr getElementObject() {
		return mParameter;
	}
}
