package de.doerl.hqm.ui.treetable;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.quest.ElementTyp;

public class TreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -372343892307571540L;

	@Override
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus);
		if (!(value instanceof ARow)) {
			modifyStateElement( (ATreeTableRow) value);
		}
		return this;
	}

	private void modifyStateElement( ATreeTableRow node) {
		ABase base = node.getElementObject();
		ElementTyp type = base.getElementTyp();
		Icon icon = type.getIcon();
		if (icon != null) {
			setIcon( icon);
		}
	}
}
