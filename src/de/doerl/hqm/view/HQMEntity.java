package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.utils.ResourceManager;

class HQMEntity extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png").getSubimage( 0, 0, 280, 360);
	private FHqm mBase;
	private JPanel mLeafLeft = leafPanel( true);
	private JPanel mLeafRight = leafPanel( false);
	private JLabel mLogo = leafImage( 280, 360, FRONT);

	public HQMEntity( EditView view, FHqm base) {
		super( view, new GridLayout( 1, 2));
		mBase = base;
		createLeft( mLeafLeft);
		createRight( mLeafRight);
		add( mLeafLeft);
		add( mLeafRight);
	}

	private void createLeft( JPanel leaf) {
		leaf.add( mLogo);
		leaf.add( Box.createVerticalGlue());
	}

	private void createRight( JPanel leaf) {
//		leaf.add( Box.createVerticalGlue());
	}

	@Override
	public FHqm getBase() {
		return mBase;
	}
}
