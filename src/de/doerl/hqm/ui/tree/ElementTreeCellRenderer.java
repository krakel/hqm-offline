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

import de.doerl.hqm.base.ABase;

public class ElementTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -6396554354333766086L;
	private static final Color DARK_GREEN = new Color( 0, 128, 0);

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
			ABase base = nn.getBase();
			switch (base.getInformation()) {
				case BASE:
					setFont( tree.getFont().deriveFont( Font.BOLD));
					setForeground( DARK_GREEN);
					break;
				case PREF:
					setFont( tree.getFont().deriveFont( Font.BOLD));
					setForeground( Color.BLUE);
					break;
				case POST:
					setFont( tree.getFont().deriveFont( Font.BOLD));
					setForeground( Color.RED);
					break;
				default:
					setFont( tree.getFont());
			}
		}
		catch (Exception ex) {
			setFont( tree.getFont());
		}
		return this;
	}
}
