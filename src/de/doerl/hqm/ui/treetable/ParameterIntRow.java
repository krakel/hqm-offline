package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.FParameterInt;

public class ParameterIntRow extends AParameterRow {
	private FParameterInt mParameter;

	public ParameterIntRow( ATreeTableRow parent, FParameterInt param) {
		super( parent);
		mParameter = param;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public FParameterInt getElementObject() {
		return mParameter;
	}
}
