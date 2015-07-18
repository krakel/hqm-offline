package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JTextField;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ADialog;

class DialogTextField extends ADialog {
	private static final long serialVersionUID = -520943310358443074L;
	private JTextField mField = new TextFieldAscii();

	public DialogTextField( Window owner) {
		super( owner);
		setTheme( "edit.textfield.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		createMain();
		addEscapeAction();
	}

	public String change( String value) {
		updateMain( value);
		if (showDialog() == DialogResult.APPROVE) {
			return mField.getText();
		}
		else {
			return null;
		}
	}

	public String change( String value, DataBitHelper bits) {
		updateMain( value);
		if (showDialog() == DialogResult.APPROVE) {
			return getText( bits);
		}
		else {
			return null;
		}
	}

	@Override
	protected void createMain() {
		mField.setFont( ADialog.FONT_NORMAL);
		mField.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mField.setAlignmentX( TOP_ALIGNMENT);
		mMain.add( mField);
	}

	private String getText( DataBitHelper bits) {
		return bits.truncate( mField.getText());
	}

	private void updateMain( String value) {
		mField.setText( value != null ? value : "???");
		mField.selectAll();
	}
}
