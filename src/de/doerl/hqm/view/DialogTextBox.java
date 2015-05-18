package de.doerl.hqm.view;

import java.awt.Window;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import de.doerl.hqm.base.FParameterString;
import de.doerl.hqm.ui.ADialog;

class DialogTextBox extends ADialog {
	private static final long serialVersionUID = 5413493873409894323L;
	private JTextArea mArea = new JTextArea();

	public DialogTextBox( Window owner) {
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
		GroupLayout layout = mMain.getLayout();
		ParallelGroup hori = layout.createParallelGroup( GroupLayout.Alignment.CENTER);
		SequentialGroup vert = layout.createSequentialGroup();
		hori.addComponent( scroll, 400, 400, Short.MAX_VALUE);
		vert.addComponent( scroll, 200, 200, Short.MAX_VALUE);
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
	}

	private String getText() {
		return mArea.getText();
	}

	private void updateMain( String value) {
		mArea.setText( value != null ? value : "");
	}
}
