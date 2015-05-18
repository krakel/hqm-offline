package de.doerl.hqm.ui;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JLabel;

class AboutDialog extends ADialog {
	private static final long serialVersionUID = 7261363764353127252L;

	public AboutDialog( Window owner) {
		super( owner);
		setThema( "about.thema");
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		createMain();
	}

	@Override
	protected void createMain() {
		JLabel lbl = new JLabel( "Krakel");
		lbl.setPreferredSize( new Dimension( 400, 200));
		lbl.setAlignmentX( TOP_ALIGNMENT);
		mMain.add( lbl);
	}
}
