package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.view.leafs.LeafImage;
import de.doerl.hqm.view.leafs.LeafTextBox;

class EntityHQM extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private FHqm mHQM;
	private ABundleAction mDescAction = new TextBoxAction();
	private ABundleAction mVersionAction = new VersionAction();
	private ABundleAction mLangAction = new LangAction();
	private JLabel mLogo = new LeafImage( ResourceManager.getImageUI( "hqm.default"));
	private LeafTextBox mDesc = new LeafTextBox();

	public EntityHQM( FHqm hqm, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mHQM = hqm;
		createLeafs();
		update();
		mDesc.addClickListener( mDescAction);
		mTool.add( mDescAction);
		mTool.add( mVersionAction);
		mTool.add( mLangAction);
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
	public void baseTreeChange( ModelEvent event) {
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

	public void update() {
		mDesc.setText( mHQM.getDescr());
	}

	private final class LangAction extends ABundleAction {
		private static final long serialVersionUID = -1837921138808186880L;

		public LangAction() {
			super( "entity.hqm.lang");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (DialogListLangs.update( mHQM, mCtrl.getFrame())) {
				mCtrl.fireChanged( mHQM);
			}
		}
	}

	private final class TextBoxAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public TextBoxAction() {
			super( "entity.hqm.desc");
			setEnabled( true);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextBox.update( mHQM.getDescr(), mCtrl.getFrame(), DataBitHelper.QUEST_DESCRIPTION_LENGTH);
			if (result != null) {
				mHQM.setDescr( result);
				mCtrl.fireChanged( mHQM);
			}
		}
	}

	private final class VersionAction extends ABundleAction {
		private static final long serialVersionUID = -2650359366196052333L;

		public VersionAction() {
			super( "entity.hqm.version");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			FileVersion[] names = FileVersion.values();
			FileVersion result = DialogListNames.update( names, mHQM.getVersion(), mCtrl.getFrame());
			if (result != null) {
				mHQM.setVersion( result);
			}
		}
	}
}
