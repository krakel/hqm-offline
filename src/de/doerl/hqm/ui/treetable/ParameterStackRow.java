package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.FParameterStack;

class ParameterStackRow extends AParameterRow {
	private FParameterStack mParameter;

	public ParameterStackRow( ATreeTableRow parent, FParameterStack param) {
		super( parent);
		mParameter = param;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public FParameterStack getElementObject() {
		return mParameter;
	}
}
