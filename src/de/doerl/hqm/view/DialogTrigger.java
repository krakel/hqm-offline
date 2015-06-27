package de.doerl.hqm.view;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogTrigger extends ADialog {
	private static final long serialVersionUID = -7234317010798818603L;
	private JTextArea mDesc = new JTextArea();
	private JComboBox<TriggerType> mTrigger = new JComboBox<>( TriggerType.values());
	private JTextField mTasks = new TextFieldInteger();

	private DialogTrigger( Window owner) {
		super( owner);
		setTheme( "edit.textfield.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		mDesc.setLineWrap( true);
		mDesc.setWrapStyleWord( true);
		mDesc.setOpaque( false);
		mDesc.setAlignmentX( LEFT_ALIGNMENT);
		mTrigger.addActionListener( new TriggerAction());
	}

	public static boolean update( FQuest quest, Window owner) {
		DialogTrigger dlg = new DialogTrigger( owner);
		dlg.createMain();
		dlg.updateMain( quest);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( quest);
			return true;
		}
		else {
			return false;
		}
	}

	private Group createBottom( GroupLayout layout, Group vert) {
		Group hori = layout.createSequentialGroup();
		ParallelGroup leftGrp = layout.createParallelGroup();
		ParallelGroup rightGrp = layout.createParallelGroup();
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Trigger", mTrigger));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Tasks", mTasks));
		hori.addGroup( leftGrp);
		hori.addGroup( rightGrp);
		return hori;
	}

	@Override
	protected void createMain() {
		JPanel box = new JPanel();
		GroupLayout layout = new GroupLayout( box);
		box.setLayout( layout);
		box.setOpaque( false);
		AEntity.setSizes( box, 160);
		layout.setAutoCreateGaps( true);
		Group hori = layout.createParallelGroup();
		Group vert = layout.createSequentialGroup();
		hori.addGroup( createBottom( layout, vert));
		hori.addComponent( mDesc);
		vert.addComponent( mDesc);
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
		mMain.add( box);
	}

	private void updateMain( FQuest quest) {
		mTrigger.setSelectedItem( quest.mTriggerType);
		mTasks.setText( String.valueOf( quest.mTriggerTasks));
	}

	private void updateResult( FQuest quest) {
		try {
			TriggerType type = (TriggerType) mTrigger.getSelectedItem();
			if (type != null) {
				quest.mTriggerType = type;
				if (type.isUseTaskCount()) {
					quest.mTriggerTasks = Utils.parseInteger( mTasks.getText(), 1);
				}
				else {
					quest.mTriggerTasks = 1;
				}
			}
		}
		catch (ClassCastException ex) {
		}
	}

	private final class TriggerAction implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			try {
				TriggerType type = (TriggerType) mTrigger.getSelectedItem();
				mTasks.setEditable( type != null && type.isUseTaskCount());
				mDesc.setText( type != null ? type.getDescription() : null);
			}
			catch (ClassCastException ex) {
			}
		}
	}
}
