package de.doerl.hqm.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.utils.ResourceManager;

class HQMEntity extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png"); //.getSubimage( 20, 20, 260, 340);
	private FHqm mBase;

	public HQMEntity( EditView view, FHqm base) {
		super( view);
		mBase = base;
	}

	@Override
	public FHqm getBase() {
		return mBase;
	}

	@Override
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		EditView.drawBackground( g2, this);
		EditView.drawImage( g2, this, FRONT, false);
	}
}
