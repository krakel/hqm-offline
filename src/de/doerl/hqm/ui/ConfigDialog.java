package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.Box;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.mods.ImageLoader;

class ConfigDialog extends ADialog {
	private static final long serialVersionUID = 724962696459638751L;
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
		mDumpDir.initControl();
		mVersion.initControl();
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.add( mDumpDir);
		result.add( Box.createVerticalStrut( 10));
		result.add( mVersion);
		mMain.add( result);
	}

	private void saveResult() {
		mDumpDir.applyChange();
		mVersion.applyChange();
		ImageLoader.init();
	}
}
