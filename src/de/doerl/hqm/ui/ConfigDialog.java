package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.Box;

import de.doerl.hqm.utils.BaseDefaults;

class ConfigDialog extends ADialog {
	private static final long serialVersionUID = 724962696459638751L;
	private ConfigPath mMineDir = new ConfigPath( BaseDefaults.MINECRAFT_DIR, "config.mine.dir");
	private ConfigPath mModDir = new ConfigPath( BaseDefaults.MODULE_DIR, "config.mod.dir");
	private ConfigVersion mVersion = new ConfigVersion();

	private ConfigDialog( Window owner) {
		super( owner);
		setThema( "config.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	public static void update( Window owner) {
		ConfigDialog dlg = new ConfigDialog( owner);
		dlg.createMain();
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.saveResult();
		}
	}

	@Override
	protected void createMain() {
		mMineDir.initControl();
		mModDir.initControl();
		mVersion.initControl();
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.add( mMineDir);
		result.add( mModDir);
		result.add( Box.createVerticalStrut( 10));
		result.add( mVersion);
		mMain.add( result);
	}

	private void saveResult() {
		mMineDir.applyChange();
		mModDir.applyChange();
		mVersion.applyChange();
	}
}
