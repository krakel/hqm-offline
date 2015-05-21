package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.utils.ResourceManager;

class EntityHQM extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png").getSubimage( 0, 0, 280, 360);
	private FHqm mHQM;
	private JToolBar mTool = EditFrame.createToolBar();
	private ABundleAction mDescAction = new TextBoxAction();
	private JLabel mLogo = leafImage( 280, 360, FRONT);
	private LeafTextBox mDesc = new LeafTextBox();

	public EntityHQM( EditView view, FHqm hqm) {
		super( view, new GridLayout( 1, 2));
		mHQM = hqm;
		createLeafs();
		update();
		mDesc.addClickListener( mDescAction);
		mTool.add( mDescAction);
		mTool.addSeparator();
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
	}

	@Override
	public void baseChanged( ModelEvent event) {
		if (mHQM.equals( event.mBase)) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					update();
				}
			});
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
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

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}

	public void update() {
		mDesc.setText( mHQM.mDesc.mValue);
	}

	private final class TextBoxAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public TextBoxAction() {
			super( "entity.textbox");
			setEnabled( true);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextBox.update( mHQM.mDesc.mValue, mView);
			if (result != null) {
				mHQM.mDesc.mValue = result;
				mView.getController().fireChanged( mHQM);
			}
		}
	}
}
