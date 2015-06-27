package de.doerl.hqm.view;

import java.awt.Window;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogCount extends ADialog {
	private static final long serialVersionUID = 7037803071354246523L;
	private JTextArea mDesc = new JTextArea();
	private JTextField mCount = new TextFieldInteger();

	private DialogCount( Window owner) {
		super( owner);
		setTheme( "edit.count.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		mDesc.setLineWrap( true);
		mDesc.setWrapStyleWord( true);
		mDesc.setOpaque( false);
		mDesc.setAlignmentX( LEFT_ALIGNMENT);
		mDesc.setText( "For this quest to unlock the player will have to complete a certain amount of parent quests.");
	}

	public static boolean update( FQuest quest, Window owner) {
		DialogCount dlg = new DialogCount( owner);
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
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Count", mCount));
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
		if (quest.mCount != null) {
			mCount.setText( String.valueOf( quest.mCount));
		}
		else {
			mCount.setText( "0");
		}
	}

	private void updateResult( FQuest quest) {
		int count = Utils.parseInteger( mCount.getText(), 0);
		quest.mCount = count > 0 ? Integer.valueOf( count) : null;
	}
}
