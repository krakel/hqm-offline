package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

class LeafPanel extends JPanel {
	private static final long serialVersionUID = -3474402067796441059L;
	private static final Border LEFT_BORDER = BorderFactory.createEmptyBorder( 40, 40, 40, 10);
	private static final Border RIGHT_BORDER = BorderFactory.createEmptyBorder( 40, 10, 40, 40);
	private boolean mLeft;

	public LeafPanel( boolean left) {
		mLeft = left;
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		setOpaque( false);
		setAlignmentX( LEFT_ALIGNMENT);
		if (left) {
			setBorder( LEFT_BORDER);
		}
		else {
			setBorder( RIGHT_BORDER);
		}
	}

	public LeafPanel( boolean left, Color color) {
		mLeft = left;
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		setOpaque( false);
		setAlignmentX( LEFT_ALIGNMENT);
		if (left) {
			setBorder( BorderFactory.createCompoundBorder( LEFT_BORDER, BorderFactory.createLineBorder( color)));
		}
		else {
			setBorder( BorderFactory.createCompoundBorder( RIGHT_BORDER, BorderFactory.createLineBorder( color)));
		}
	}

	@Override
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor( getBackground());
		g2.fillRect( 0, 0, getWidth(), getHeight());
		AEntity.drawBackground( g2, this, AEntity.BACKGROUND, mLeft);
	}
}
