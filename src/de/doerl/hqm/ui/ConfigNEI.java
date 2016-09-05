package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.mods.ImageLoader;

public class ConfigNEI extends ADialog {
	private static final long serialVersionUID = 5263466939159690365L;
	private JTextArea mLabel = new JTextArea( ResourceManager.getString( "config.nei.info"));
	private JLabel mGrafig = new JLabel( ResourceManager.getIcon( "Buttons.jpg"));
	private ConfigPath mDumpDir = new ConfigPath( BaseDefaults.DUMP_DIR, "config.dump.dir");
	private ConfigPath mPkgDir = new ConfigPath( BaseDefaults.PKG_DIR, "config.pkg.dir");

	private ConfigNEI( Window owner) {
		super( owner);
		setTheme( "config.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		mLabel.setEditable( false);
		mLabel.setLineWrap( true);
		mLabel.setWrapStyleWord( true);
		mLabel.setBackground( getBackground());
	}

	public static void update( Window owner) {
		ConfigNEI dlg = new ConfigNEI( owner);
		dlg.createMain();
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.saveResult();
		}
	}

	private Box createDescription() {
		Box hori = Box.createHorizontalBox();
		hori.setAlignmentX( LEFT_ALIGNMENT);
		hori.add( mLabel);
		hori.add( Box.createHorizontalStrut( 10));
		hori.add( mGrafig);
		return hori;
	}

	@Override
	protected void createMain() {
		mDumpDir.initControl();
		mPkgDir.initControl();
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.add( createDescription());
		result.add( Box.createVerticalStrut( 10));
		result.add( new SinkLine());
		result.add( Box.createVerticalStrut( 10));
		result.add( mDumpDir);
		result.add( mPkgDir);
		mMain.add( result);
	}

	private void saveResult() {
		mDumpDir.applyChange();
		mPkgDir.applyChange();
		ImageLoader.init();
	}
}
