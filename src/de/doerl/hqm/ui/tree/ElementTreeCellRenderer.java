package de.doerl.hqm.ui.tree;

import java.awt.Dimension;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

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
}
