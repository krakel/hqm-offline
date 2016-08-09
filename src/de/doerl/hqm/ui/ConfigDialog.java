package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.Box;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.mods.ImageLoader;

class ConfigDialog extends ADialog {
	private static final long serialVersionUID = 724962696459638751L;
	private ConfigBoolean mLanguge = new ConfigBoolean( BaseDefaults.LANGUAGE, "config.language");
	private ConfigPath mDumpDir = new ConfigPath( BaseDefaults.DUMP_DIR, "config.dump.dir");
	private ConfigPath mPkgDir = new ConfigPath( BaseDefaults.PKG_DIR, "config.pkg.dir");
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
		mLanguge.initControl();
		mDumpDir.initControl();
		mPkgDir.initControl();
		mVersion.initControl();
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.add( mLanguge);
		result.add( mDumpDir);
		result.add( mPkgDir);
		result.add( Box.createVerticalStrut( 10));
		result.add( mVersion);
		mMain.add( result);
	}

	private void saveResult() {
		mLanguge.applyChange();
		mDumpDir.applyChange();
		mPkgDir.applyChange();
		mVersion.applyChange();
		ImageLoader.init();
	}
}
