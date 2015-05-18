package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.utils.ResourceManager;

class EntityHQM extends AEntity<FHqm> implements ChangeListener {
	private static final long serialVersionUID = 4033642877403597083L;
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png").getSubimage( 0, 0, 280, 360);
	private FHqm mHQM;
	private LeafPanel mLeafLeft = new LeafPanel( true);
	private LeafPanel mLeafRight = new LeafPanel( false);
	private JLabel mLogo = leafImage( 280, 360, FRONT);
	private LeafTextBox mDesc = new LeafTextBox();

	public EntityHQM( EditView view, FHqm hqm) {
		super( view, new GridLayout( 1, 2));
		mHQM = hqm;
		mDesc.setText( mHQM.mDesc.mValue);
		createLeft( mLeafLeft);
		createRight( mLeafRight);
		add( mLeafLeft);
		add( mLeafRight);
		mDesc.getHandler().addActionListener( this);
	}

	private void createLeft( JPanel leaf) {
		leaf.add( mLogo);
		leaf.add( Box.createVerticalGlue());
	}

	private void createRight( JPanel leaf) {
		leaf.add( leafScoll( mDesc, 160));
//		leaf.add( Box.createVerticalGlue());
	}

	@Override
	public FHqm getBase() {
		return mHQM;
	}

	public void stateChanged( ChangeEvent evt) {
		DialogTextBox.update( mHQM.mDesc, mView);
		mDesc.setText( mHQM.mDesc.mValue);
	}
}
