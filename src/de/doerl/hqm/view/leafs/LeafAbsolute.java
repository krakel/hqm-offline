package de.doerl.hqm.view.leafs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.view.AEntity;
import de.doerl.hqm.view.ClickHandler;

public class LeafAbsolute extends JPanel {
	private static final long serialVersionUID = -7421492884184131122L;
	private static final Color LINE_COLOR = new Color( 192, 192, 192, 64);
	private static final int DX = ResourceManager.getW( false) / 3;
	private static final int DY = ResourceManager.getH( false) / 3;
	private static final int BORDER = 20;
	private static final int MAX_X = AEntity.VIEW_SIZE.width - BORDER - BORDER - ResourceManager.getW( false);
	private static final int MAX_Y = AEntity.VIEW_SIZE.height - BORDER - BORDER - ResourceManager.getH( false);
	private ClickHandler mHandler = new ClickHandler();
	private boolean mIsGrid = true;
	private BufferedImage mGrid = createGrid( AEntity.VIEW_SIZE);

	public LeafAbsolute() {
		super( null);
		setOpaque( false);
		setAlignmentX( LEFT_ALIGNMENT);
//		setBackground( Color.GRAY);
//		setBorder( BorderFactory.createEmptyBorder( 40, 40, 40, 40));
		addMouseListener( mHandler);
	}

	private static BufferedImage createGrid( Dimension size) {
		BufferedImage image = new BufferedImage( size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor( LINE_COLOR);
		int w = size.width - BORDER;
		int h = size.height - BORDER;
		for (int i = BORDER; i < w; i += DX) {
			g2.drawLine( i, BORDER, i, h);
		}
		for (int i = BORDER; i < h; i += DY) {
			g2.drawLine( BORDER, i, w, i);
		}
		g2.dispose();
		return image;
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	@Override
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor( getBackground());
		g2.fillRect( 0, 0, getWidth(), getHeight());
		AEntity.drawBackgroundHalf( g2, this, true);
		AEntity.drawBackgroundHalf( g2, this, false);
		if (mIsGrid) {
			AEntity.drawImage( g2, this, mGrid);
		}
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	public void setGrid( boolean value) {
		mIsGrid = value;
	}

	public int stepX( int x) {
		if (mIsGrid) {
			x -= BORDER;
			x /= DX;
			x *= DX;
			x += BORDER;
		}
		if (x < BORDER) {
			return BORDER;
		}
		if (x > MAX_X) {
			return MAX_X;
		}
		return x;
	}

	public int stepY( int y) {
		if (mIsGrid) {
			y -= BORDER;
			y /= DY;
			y *= DY;
			y += BORDER;
		}
		if (y < BORDER) {
			return BORDER;
		}
		if (y > MAX_Y) {
			return MAX_Y;
		}
		return y;
	}
}
