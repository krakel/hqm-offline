package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.Box;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.mods.ImageLoader;

class ConfigDialog extends ADialog {
	private static final long serialVersionUID = 724962696459638751L;
	private ConfigBoolean mLanguage = new ConfigBoolean( BaseDefaults.LANGUAGE, "config.language");
	private ConfigString mLanguageMain = new ConfigString( BaseDefaults.LANGUAGE_MAIN, "config.language.main");
	private ConfigVersion mVersion = new ConfigVersion( BaseDefaults.FILE_VERSION, "config.version");

	private ConfigDialog( Window owner) {
		super( owner);
		setTheme( "config.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		mLanguage.registerChangeListener( mLanguageMain);
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
		mLanguage.initControl();
		mLanguageMain.initControl();
		mVersion.initControl();
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.add( mLanguage);
		result.add( mLanguageMain);
		result.add( Box.createVerticalStrut( 10));
		result.add( new SinkLine());
		result.add( Box.createVerticalStrut( 10));
		result.add( mVersion);
		mMain.add( result);
		mLanguage.fireAction();
	}

	private void saveResult() {
		mLanguage.applyChange();
		mLanguageMain.applyChange();
		mVersion.applyChange();
		ImageLoader.init();
	}
}
