package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JTextField;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogIntegerField extends ADialog {
	private static final long serialVersionUID = -520943310358443074L;
	private JTextField mField = new TextFieldInteger();

	private DialogIntegerField( Window owner) {
		super( owner);
		setTheme( "edit.textfield.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	public static boolean update( FGroup group, Window owner) {
		DialogIntegerField dlg = new DialogIntegerField( owner);
		dlg.createMain();
		dlg.updateMain( String.valueOf( group.mLimit));
		if (dlg.showDialog() == DialogResult.APPROVE) {
			group.mLimit = Utils.parseInteger( dlg.getText(), 1);
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean update( FQuestTaskDeath task, Window owner) {
		DialogIntegerField dlg = new DialogIntegerField( owner);
		dlg.createMain();
		dlg.updateMain( String.valueOf( task.mDeaths));
		if (dlg.showDialog() == DialogResult.APPROVE) {
			task.mDeaths = Utils.parseInteger( dlg.getText(), 1);
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean update( FQuestTaskReputationKill task, Window owner) {
		DialogIntegerField dlg = new DialogIntegerField( owner);
		dlg.createMain();
		dlg.updateMain( String.valueOf( task.mKills));
		if (dlg.showDialog() == DialogResult.APPROVE) {
			task.mKills = Utils.parseInteger( dlg.getText(), 1);
			return true;
		}
		else {
			return false;
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
		mField.setText( value != null ? value : "???");
		mField.selectAll();
	}
}
