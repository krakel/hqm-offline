package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import de.doerl.hqm.Tuple2;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogTaskField extends ADialog {
	private static final long serialVersionUID = 3815258461920509252L;
	private JComboBox<TaskTyp> mTypes = new JComboBox<>( TaskTyp.values());
	private JTextField mField = new JTextField();

	private DialogTaskField( Window owner) {
		super( owner);
		setThema( "edit.taskfield.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	public static Tuple2<TaskTyp, String> update( TaskTyp type, String value, Window owner) {
		DialogTaskField dlg = new DialogTaskField( owner);
		dlg.createMain();
		dlg.updateMain( type, value);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return Tuple2.apply( dlg.getTaskType(), dlg.getText());
		}
		else {
			return null;
		}
	}

	@Override
	protected void createMain() {
		mTypes.setAlignmentX( LEFT_ALIGNMENT);
		mField.setFont( AEntity.FONT_NORMAL);
		mField.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mField.setAlignmentX( LEFT_ALIGNMENT);
		Box box = Box.createVerticalBox();
		box.setAlignmentX( TOP_ALIGNMENT);
		box.add( mTypes);
		box.add( Box.createVerticalStrut( GAP));
		box.add( mField);
		mMain.add( box);
	}

	private TaskTyp getTaskType() {
		return (TaskTyp) mTypes.getSelectedItem();
	}

	private String getText() {
		String text = mField.getText();
		return Utils.validString( text) ? text : getTaskType().getTitle();
	}

	private void updateMain( TaskTyp type, String value) {
		if (type != null) {
			mTypes.setSelectedItem( type);
		}
		else {
			mTypes.setSelectedIndex( 0);
		}
		mField.setText( value != null ? value : "");
	}
}
