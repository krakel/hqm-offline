package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import de.doerl.hqm.base.FParameterString;
import de.doerl.hqm.ui.ADialog;

class DialogTextBox extends ADialog {
	private static final long serialVersionUID = 5413493873409894323L;
	private JTextArea mArea = new JTextArea();

	private DialogTextBox( Window owner) {
		super( owner);
		mArea.setFont( AEntity.FONT_NORMAL);
		mArea.setLineWrap( true);
		mArea.setWrapStyleWord( true);
		setThema( "edit.textbox.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		createMain();
	}

	public static void update( FParameterString param, EditView view) {
		if (param != null) {
			DialogTextBox dlg = new DialogTextBox( ADialog.getParentFrame( view));
			dlg.updateMain( param.mValue);
			if (dlg.showDialog() == DialogResult.APPROVE) {
				param.mValue = dlg.getText();
			}
		}
	}

	@Override
	protected void createMain() {
		JScrollPane scroll = new JScrollPane( mArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize( new Dimension( 400, 200));
		scroll.setAlignmentX( TOP_ALIGNMENT);
		mMain.add( scroll);
	}

	private String getText() {
		return mArea.getText();
	}

	private void updateMain( String value) {
		mArea.setText( value != null ? value : "");
	}
}
