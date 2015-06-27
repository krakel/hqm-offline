package de.doerl.hqm.ui.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ElementTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -6396554354333766086L;

	public ElementTreeCellRenderer() {
		super.setIcon( null);
//		super.setBorder( BorderFactory.createLineBorder( Color.RED));
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		FontRenderContext frc = new FontRenderContext( null, true, true);
		Rectangle2D bounds = getFont().getStringBounds( getText(), frc);
		dim.width = (int) bounds.getWidth() + getIconTextGap() + 20;
		return dim;
	}

	@Override
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus);
		try {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			ANode nn = (ANode) node.getUserObject();
			if (nn.getBase().isInformation()) {
				setFont( tree.getFont().deriveFont( Font.BOLD));
				setForeground( Color.MAGENTA);
			}
			else {
				setFont( tree.getFont());
			}
		}
		catch (Exception ex) {
		}
		return this;
	}
}
