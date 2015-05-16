package de.doerl.hqm.ui.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ElementTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -6396554354333766086L;

	public ElementTreeCellRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		setText( tree.convertValueToText( value, sel, expanded, leaf, row, hasFocus));
		if (sel) {
			setForeground( getTextSelectionColor());
		}
		else {
			setForeground( getTextNonSelectionColor());
		}
//		boolean isEnabled = tree.isEnabled();
//		setEnabled( isEnabled);
//		if (isEnabled) {
//			selected = sel;
//			if (leaf) {
//				setIcon( getLeafIcon());
//			}
//			else if (expanded) {
//				setIcon( getOpenIcon());
//			}
//			else {
//				setIcon( getClosedIcon());
//			}
//		}
//		else {
//			selected = false;
//			if (leaf) {
//				setDisabledIcon( getLeafIcon());
//			}
//			else if (expanded) {
//				setDisabledIcon( getOpenIcon());
//			}
//			else {
//				setDisabledIcon( getClosedIcon());
//			}
//		}
		return this;
	}
}
