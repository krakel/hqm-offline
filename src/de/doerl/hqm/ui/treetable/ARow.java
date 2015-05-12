package de.doerl.hqm.ui.treetable;

abstract class ARow extends ATreeTableRow {
	@Override
	public Class<?> getRowClass() {
		return String.class;
	}

	@Override
	public boolean hasConfig() {
		return false;
	}
}
