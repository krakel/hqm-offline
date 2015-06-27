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

import de.doerl.hqm.base.FRepeatInfo;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogRepeat extends ADialog {
	private static final long serialVersionUID = -7234317010798818603L;
	private JTextArea mDesc = new JTextArea();
	private JComboBox<RepeatType> mType = new JComboBox<>( RepeatType.values());
	private JTextField mDays = new TextFieldInteger();
	private JTextField mHours = new TextFieldInteger();

	private DialogRepeat( Window owner) {
		super( owner);
		setTheme( "edit.repeat.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		mDesc.setLineWrap( true);
		mDesc.setWrapStyleWord( true);
		mDesc.setOpaque( false);
		mDesc.setAlignmentX( LEFT_ALIGNMENT);
		mType.addActionListener( new TypeAction());
	}

	public static boolean update( FRepeatInfo info, Window owner) {
		DialogRepeat dlg = new DialogRepeat( owner);
		dlg.createMain();
		dlg.updateMain( info);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( info);
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
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Type", mType));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Days", mDays));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Hours", mHours));
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

	private void updateMain( FRepeatInfo info) {
		mType.setSelectedItem( info.mType);
		mDays.setText( String.valueOf( info.mTotal / 24));
		mHours.setText( String.valueOf( info.mTotal % 24));
	}

	private void updateResult( FRepeatInfo info) {
		try {
			RepeatType type = (RepeatType) mType.getSelectedItem();
			if (type != null) {
				info.mType = type;
				if (type.isUseTime()) {
					int days = Utils.parseInteger( mDays.getText(), 0);
					int hours = Utils.parseInteger( mHours.getText(), 0);
					info.mTotal = Math.min( DataBitHelper.HOURS.getMaximum(), days * 24 + hours);
				}
				else {
					info.mTotal = 0;
				}
			}
		}
		catch (ClassCastException ex) {
		}
	}

	private final class TypeAction implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			try {
				RepeatType type = (RepeatType) mType.getSelectedItem();
				if (type != null) {
					mDays.setEditable( type.isUseTime());
					mHours.setEditable( type.isUseTime());
					mDesc.setText( type.getDescription());
				}
				else {
					mDays.setEditable( false);
					mHours.setEditable( false);
					mDesc.setText( null);
				}
			}
			catch (ClassCastException ex) {
			}
		}
	}
}
