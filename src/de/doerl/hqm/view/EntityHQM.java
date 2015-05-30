package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.utils.ResourceManager;

class EntityHQM extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private FHqm mHQM;
	private JToolBar mTool = EditFrame.createToolBar();
	private ABundleAction mDescAction = new TextBoxAction();
	private JLabel mLogo = new LeafImage( ResourceManager.getImageUI( "hqm.default"));
	private LeafTextBox mDesc = new LeafTextBox();

	public EntityHQM( FHqm hqm, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
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
		mDesc.setText( mHQM.mDescr);
	}

	private final class TextBoxAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public TextBoxAction() {
			super( "entity.textbox");
			setEnabled( true);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextBox.update( mHQM.mDescr, mCtrl.getFrame());
			if (result != null) {
				mHQM.mDescr = result;
				mCtrl.fireChanged( mHQM);
			}
		}
	}
}
