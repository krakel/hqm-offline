package de.doerl.hqm.ui.treetable;

import java.util.List;

import de.doerl.hqm.base.ABase;

public abstract class ATreeTableRow {
	protected ATreeTableRow() {
	}

	public abstract void apply( TreeTableModel model, String value);

	public abstract List<ATreeTableRow> getChildren();

	public abstract ABase getElementObject();

	public abstract String getName();

	public abstract ATreeTableRow getParent();

	public abstract Class<?> getRowClass();

	public abstract String getValue();

	public abstract boolean hasConfig();
}
