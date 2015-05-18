package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.utils.ResourceManager;

class EntityHQM extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png").getSubimage( 0, 0, 280, 360);
	private FHqm mHQM;
	private JLabel mLogo = leafImage( 280, 360, FRONT);
	private LeafTextBox mDesc = new LeafTextBox();

	public EntityHQM( EditView view, FHqm hqm) {
		super( view, new GridLayout( 1, 2));
		mHQM = hqm;
		createLeafs();
		mDesc.setText( mHQM.mDesc.mValue);
		mDesc.getHandler().addClickListener( new IClickListener() {
			@Override
			public void onDoubleClick( MouseEvent evt) {
				updateDesc();
			}

			@Override
			public void onSingleClick( MouseEvent evt) {
			}
		});
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( mLogo);
		leaf.add( Box.createVerticalGlue());
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( leafScoll( mDesc, 160));
//		leaf.add( Box.createVerticalGlue());
	}

	@Override
	public FHqm getBase() {
		return mHQM;
	}

	private void updateDesc() {
		DialogTextBox.update( mHQM.mDesc, mView);
		mDesc.setText( mHQM.mDesc.mValue);
	}
}
