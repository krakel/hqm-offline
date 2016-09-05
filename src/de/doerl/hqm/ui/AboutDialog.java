package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.VersionHelper;

class AboutDialog extends ADialog {
	private static final long serialVersionUID = 7261363764353127252L;

	private AboutDialog( Window owner) {
		super( owner);
		setTheme( "about.theme");
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	public static void update( Window owner) {
		AboutDialog dlg = new AboutDialog( owner);
		dlg.createMain();
		dlg.showDialog();
	}

	private Box createDescription() {
		Box hori = Box.createHorizontalBox();
		hori.setAlignmentX( LEFT_ALIGNMENT);
		JLabel logo = new JLabel( ResourceManager.getIcon( "Krakel1.png"));
		logo.setAlignmentX( TOP_ALIGNMENT);
		hori.add( logo);
		hori.add( Box.createHorizontalStrut( 50));
		JTextArea txt = new JTextArea( ResourceManager.getString( "config.about") + VersionHelper.VERSION);
//		txt.setPreferredSize( new Dimension( 200, 100));
		txt.setAlignmentX( TOP_ALIGNMENT);
		txt.setEditable( false);
		txt.setWrapStyleWord( true);
		txt.setBackground( getBackground());
		hori.add( txt);
		return hori;
	}

	@Override
	protected void createMain() {
		mMain.add( createDescription());
		mMain.add( Box.createVerticalGlue());
	}
}
