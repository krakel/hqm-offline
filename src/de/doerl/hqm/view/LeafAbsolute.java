package de.doerl.hqm.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

class LeafAbsolute extends JPanel {
	private static final long serialVersionUID = -7421492884184131122L;
	private ClickHandler mHandler = new ClickHandler();

	public LeafAbsolute() {
		super( null);
		setOpaque( false);
		setAlignmentX( LEFT_ALIGNMENT);
//		setBackground( Color.GRAY);
		setBorder( BorderFactory.createEmptyBorder( 40, 40, 40, 40));
		addMouseListener( mHandler);
	}

	public void addClickListener( IClickListener l) {
		mHandler.addClickListener( l);
	}

	@Override
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor( getBackground());
		g2.fillRect( 0, 0, getWidth(), getHeight());
		AEntity.drawBackgroundHalf( g2, this, AEntity.BACKGROUND, true);
		AEntity.drawBackgroundHalf( g2, this, AEntity.BACKGROUND, false);
	}

	public void removeClickListener( IClickListener l) {
		mHandler.removeClickListener( l);
	}
}
