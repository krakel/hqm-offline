package de.doerl.hqm.view;

import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.ADialogList.StackEntry;

class DialogStack extends ADialog {
	private static final long serialVersionUID = 651515231606809783L;
	private JCheckBox mItem = new JCheckBox();
	private JTextField mName = new JTextField();
	private JTextField mCount = new JTextField();
	private JComboBox<String> mDmg = new JComboBox<>();
	private JComboBox<ItemPrecision> mPrec = new JComboBox<>( ItemPrecision.values());

	public DialogStack( Window owner) {
		super( owner);
		setThema( "edit.stack.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		for (int i = 0; i < 16; ++i) {
			mDmg.addItem( String.valueOf( i));
		}
		mItem.setOpaque( false);
		Insets in = mItem.getInsets();
		mItem.setBorder( BorderFactory.createEmptyBorder( in.top, 0, in.bottom, in.right));
	}

	public static StackEntry update( boolean item, String name, int size, int dmg, ItemPrecision pre, Window owner) {
		DialogStack dlg = new DialogStack( owner);
		dlg.createMain();
		dlg.updateMain( item, name, size, dmg, pre);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return dlg.getResult();
		}
		else {
			return null;
		}
	}

	public static StackEntry update( StackEntry entry, Window owner) {
		DialogStack dlg = new DialogStack( owner);
		dlg.createMain();
		dlg.updateMain( entry);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return dlg.getResult();
		}
		else {
			return null;
		}
	}

	private ParallelGroup addLine( GroupLayout layout, ParallelGroup leftGrp, ParallelGroup rightGrp, String descr, JComponent comp) {
		JLabel lbl = new JLabel( descr);
		leftGrp.addComponent( lbl);
		rightGrp.addComponent( comp);
		return layout.createParallelGroup( Alignment.BASELINE).addComponent( lbl).addComponent( comp);
	}

	@Override
	protected void createMain() {
		JPanel box = new JPanel();
		GroupLayout layout = new GroupLayout( box);
		box.setLayout( layout);
		box.setOpaque( false);
//		box.setBorder( BorderFactory.createLineBorder( Color.RED));
		layout.setAutoCreateGaps( true);
		SequentialGroup hori = layout.createSequentialGroup();
		SequentialGroup vert = layout.createSequentialGroup();
		//
		ParallelGroup leftGrp = layout.createParallelGroup();
		ParallelGroup rightGrp = layout.createParallelGroup();
		//
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Item", mItem));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Size", mCount));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Damage", mDmg));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Precition", mPrec));
		hori.addGroup( leftGrp);
		hori.addGroup( rightGrp);
		//
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
		mMain.add( box);
	}

	private StackEntry getResult() {
		int size = Utils.parseInteger( mCount.getText(), 1);
		ItemPrecision prec = ItemPrecision.get( mPrec.getSelectedIndex());
		return new StackEntry( mItem.isSelected(), mName.getText(), size, mDmg.getSelectedIndex(), prec);
	}

	private void updateMain( boolean item, String name, int size, int dmg, ItemPrecision pre) {
		mItem.setSelected( item);
		mName.setText( name);
		mCount.setText( String.valueOf( size));
		mDmg.setSelectedIndex( dmg);
		mPrec.setSelectedItem( pre);
	}

	private void updateMain( StackEntry entry) {
		mItem.setSelected( entry.mItem);
		mName.setText( entry.getName());
		mCount.setText( String.valueOf( entry.mCount));
		mDmg.setSelectedIndex( entry.mDamage);
		mPrec.setSelectedItem( entry.mPrecision);
	}
}
