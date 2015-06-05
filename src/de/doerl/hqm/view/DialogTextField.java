package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JTextField;

import de.doerl.hqm.ui.ADialog;

class DialogTextField extends ADialog {
	private static final long serialVersionUID = -520943310358443074L;
	private JTextField mField = new JTextField();

	private DialogTextField( Window owner) {
		super( owner);
		setThema( "edit.textfield.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	public static String update( String value, Window owner) {
		DialogTextField dlg = new DialogTextField( owner);
		dlg.createMain();
		dlg.updateMain( value);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return dlg.getText();
		}
		else {
			return null;
		}
	}

	@Override
	protected void createMain() {
		mField.setFont( AEntity.FONT_NORMAL);
		mField.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mField.setAlignmentX( TOP_ALIGNMENT);
		mMain.add( mField);
	}

	private String getText() {
		return mField.getText();
	}

	private void updateMain( String value) {
		mField.setText( value != null ? value : "");
		mField.selectAll();
	}
}
