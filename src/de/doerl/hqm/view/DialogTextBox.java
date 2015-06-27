package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ADialog;

class DialogTextBox extends ADialog {
	private static final long serialVersionUID = 5413493873409894323L;
	private JTextArea mArea = new TextAreaAscii();

	private DialogTextBox( Window owner) {
		super( owner);
		setTheme( "edit.textbox.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	public static String update( String value, Window owner, DataBitHelper bits) {
		DialogTextBox dlg = new DialogTextBox( owner);
		dlg.createMain();
		dlg.updateMain( value);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return dlg.getText( bits);
		}
		else {
			return null;
		}
	}

	@Override
	protected void createMain() {
		mArea.setFont( ADialog.FONT_NORMAL);
		mArea.setLineWrap( true);
		mArea.setWrapStyleWord( true);
		JScrollPane scroll = new JScrollPane( mArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize( new Dimension( 400, 200));
		scroll.setAlignmentX( TOP_ALIGNMENT);
		mMain.add( scroll);
	}

	private String getText( DataBitHelper bits) {
		return bits.truncate( mArea.getText());
	}

	private void updateMain( String value) {
		mArea.setText( value != null ? value : "");
	}
}
