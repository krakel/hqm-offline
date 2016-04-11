package de.doerl.hqm.ui;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.Box;
import javax.swing.JLabel;

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

	@Override
	protected void createMain() {
		JLabel lbl = new JLabel( "Krakel HQM offline editor version " + VersionHelper.VERSION);
		lbl.setPreferredSize( new Dimension( 400, 200));
		lbl.setAlignmentX( TOP_ALIGNMENT);
		mMain.add( lbl);
		mMain.add( Box.createVerticalGlue());
	}
}
