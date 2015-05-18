package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
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
		GroupLayout layout = mMain.getLayout();
		ParallelGroup hori = layout.createParallelGroup();
		SequentialGroup vert = layout.createSequentialGroup();
		addComponent( hori, vert, new JLabel( "Krakel"));
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
	}
}
