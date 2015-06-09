package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.Box;

import de.doerl.hqm.utils.BaseDefaults;

class ConfigDialog extends ADialog {
	private static final long serialVersionUID = 724962696459638751L;
	private ConfigPath mMineDir = new ConfigPath( BaseDefaults.MINECRAFT_DIR, "config.mine.dir");
//	private ConfigPath mModDir = new ConfigPath( BaseDefaults.MODULE_DIR, "config.mod.dir");
	private ConfigPath mDumpDir = new ConfigPath( BaseDefaults.DUMP_DIR, "config.dump.dir");
	private ConfigVersion mVersion = new ConfigVersion();

	private ConfigDialog( Window owner) {
		super( owner);
		setTheme( "config.theme");
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
//		mModDir.initControl();
		mDumpDir.initControl();
		mVersion.initControl();
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.add( mMineDir);
//		result.add( mModDir);
		result.add( mDumpDir);
		result.add( Box.createVerticalStrut( 10));
		result.add( mVersion);
		mMain.add( result);
	}

	private void saveResult() {
		mMineDir.applyChange();
//		mModDir.applyChange();
		mDumpDir.applyChange();
		mVersion.applyChange();
	}
}
