package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogMarker extends ADialog {
	private static final long serialVersionUID = 7720930197206098500L;
	private JTextField mName = new TextFieldAscii();
	private JTextField mMark = new TextFieldInteger();

	public DialogMarker( Window owner) {
		super( owner);
		setTheme( "edit.mark.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		createMain();
		mName.setPreferredSize( new Dimension( 200, mName.getPreferredSize().height));
	}

	public FMarker addElement( FReputation rep) {
		mName.setText( "unknown");
		mMark.setText( "0");
		if (showDialog() == DialogResult.APPROVE) {
			FMarker entry = rep.createMarker( "unknown");
			updateResult( entry);
			return entry;
		}
		else {
			return null;
		}
	}

	public FMarker changeElement( FMarker entry) {
		mName.setText( entry.mName);
		mMark.setText( String.valueOf( entry.mMark));
		if (showDialog() == DialogResult.APPROVE) {
			updateResult( entry);
			return entry;
		}
		else {
			return null;
		}
	}

	@Override
	protected void createMain() {
		JPanel box = new JPanel();
		GroupLayout layout = new GroupLayout( box);
		box.setLayout( layout);
		box.setOpaque( false);
//			box.setBorder( BorderFactory.createLineBorder( Color.RED));
		layout.setAutoCreateGaps( true);
		Group hori = layout.createSequentialGroup();
		Group vert = layout.createSequentialGroup();
		Group leftGrp = layout.createParallelGroup();
		Group rightGrp = layout.createParallelGroup();
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Mark", mMark));
		hori.addGroup( leftGrp);
		hori.addGroup( rightGrp);
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
		mMain.add( box);
	}

	private void updateResult( FMarker entry) {
		entry.mName = DataBitHelper.NAME_LENGTH.truncate( mName.getText());
		entry.mMark = Utils.parseInteger( mMark.getText(), 0);
	}
}
