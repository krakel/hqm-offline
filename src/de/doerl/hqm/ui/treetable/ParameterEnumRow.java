package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.FParameterEnum;

public class ParameterEnumRow extends AParameterRow {
	private FParameterEnum<?> mParameter;

	public ParameterEnumRow( ATreeTableRow parent, FParameterEnum<?> param) {
		super( parent);
		mParameter = param;
	}

	@Override
	public void apply( TreeTableModel model, String value) {
	}

	@Override
	public FParameterEnum<?> getElementObject() {
		return mParameter;
	}
}
