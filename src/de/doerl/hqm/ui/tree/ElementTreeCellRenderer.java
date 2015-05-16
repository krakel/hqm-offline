package de.doerl.hqm.ui.tree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ElementTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -6396554354333766086L;

	public ElementTreeCellRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		JLabel lbl = (JLabel) super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus);
		lbl.setIcon( null);
		return lbl;
	}
}
