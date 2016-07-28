package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ItemNEI;
import de.doerl.hqm.view.ADialogList.ICreator;
import de.doerl.hqm.view.leafs.LeafSearch;
import de.doerl.hqm.view.leafs.LeafSearch.ISearchListener;
import de.doerl.hqm.view.leafs.LeafSearch.SearchEvent;

class DialogStack extends ADialogEdit<StackEntry> {
	private static final long serialVersionUID = 651515231606809783L;
	private JCheckBox mItem = new JCheckBox();
	private JTextField mName = new TextFieldAscii();
	private JTextField mCount = new TextFieldInteger();
	private JTextField mDmg = new TextFieldInteger();
	private JTextArea mNBT = new TextAreaAscii();
	private JComboBox<ItemPrecision> mPrec = new JComboBox<>( ItemPrecision.values());
	private LeafSearch mSearch = new LeafSearch();

	public DialogStack( Window owner, boolean require) {
		super( owner);
		setTheme( "edit.stack.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		mItem.setOpaque( false);
		Insets in = mItem.getInsets();
		mItem.setBorder( BorderFactory.createEmptyBorder( in.top, 0, in.bottom, in.right));
		createMain();
		mSearch.addSearchListener( new ISearchListener() {
			@Override
			public void doAction( SearchEvent event) {
				ItemNEI item = event.getItem();
				if (item != null) {
					mName.setText( item.mName);
					mDmg.setText( String.valueOf( item.mDamage));
					mCount.setText( String.valueOf( 1));
					mItem.setSelected( true);
					mPrec.setSelectedItem( ItemPrecision.PRECISE);
					mNBT.setText( "");
				}
			}
		});
		mName.setPreferredSize( new Dimension( 200, mName.getPreferredSize().height));
		mItem.setEnabled( require);
		mPrec.setEnabled( require);
	}

	public static FItemStack update( FItemStack stk, Window owner) {
		DialogStack dlg = new DialogStack( owner, false);
		dlg.updateMainStk( stk);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return dlg.getResultStk();
		}
		else {
			return null;
		}
	}

	@Override
	public StackEntry addElement( ICreator<StackEntry> creator) {
		mName.setText( "name");
		mDmg.setText( "0");
		mCount.setText( "1");
		mItem.setSelected( true);
		mPrec.setSelectedItem( ItemPrecision.PRECISE);
		mNBT.setText( "");
		return showEditor();
	}

	@Override
	public StackEntry changeElement( StackEntry entry) {
		mName.setText( entry.getName());
		mDmg.setText( String.valueOf( entry.mDmg));
		mCount.setText( String.valueOf( entry.mCount));
		mItem.setSelected( entry.mItem);
		mPrec.setSelectedItem( entry.getPrecision());
		mNBT.setText( entry.mNbt);
		return showEditor();
	}

	private Group createLeft( GroupLayout layout, Group hori) {
		Group vert = layout.createSequentialGroup();
		Group leftGrp = layout.createParallelGroup();
		Group rightGrp = layout.createParallelGroup();
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Item", mItem));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Damage", mDmg));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Size", mCount));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Precition", mPrec));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "NBT", mNBT));
		hori.addGroup( leftGrp);
		hori.addGroup( rightGrp);
		return vert;
	}

	@Override
	protected void createMain() {
		JPanel box = new JPanel();
		GroupLayout layout = new GroupLayout( box);
		box.setLayout( layout);
		box.setOpaque( false);
//		box.setBorder( BorderFactory.createLineBorder( Color.RED));
		layout.setAutoCreateGaps( true);
		Group hori = layout.createSequentialGroup();
		Group vert = layout.createParallelGroup();
		vert.addGroup( createLeft( layout, hori));
		vert.addComponent( mSearch);
		hori.addComponent( mSearch);
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
		mMain.add( box);
		mSearch.doSearch();
	}

	private StackEntry getResult() {
		int size = Utils.parseInteger( mCount.getText(), 1);
		ItemPrecision prec = ItemPrecision.get( mPrec.getSelectedIndex());
		return new StackEntry( mItem.isSelected(), mName.getText(), Utils.parseInteger( mDmg.getText(), 0), mNBT.getText(), size, prec);
	}

	private FItemStack getResultStk() {
		String name = mName.getText();
		if (Utils.validString( name)) {
			return new FItemStack( mNBT.getText(), name, Utils.parseInteger( mDmg.getText(), 0), 1);
		}
		else {
			return null;
		}
	}

	private StackEntry showEditor() {
		if (showDialog() == DialogResult.APPROVE) {
			return getResult();
		}
		else {
			return null;
		}
	}

	private void updateMainStk( FItemStack stk) {
		if (stk != null) {
			mName.setText( stk.getName());
			mDmg.setText( String.valueOf( stk.getDamage()));
			mCount.setText( "1");
			mCount.setEnabled( false);
			mItem.setSelected( true);
			mPrec.setSelectedItem( ItemPrecision.PRECISE);
			mNBT.setText( stk.getNBT());
		}
		else {
			mName.setText( "unknown");
			mDmg.setText( "0");
			mCount.setText( "1");
			mCount.setEnabled( false);
			mItem.setSelected( true);
			mPrec.setSelectedItem( ItemPrecision.PRECISE);
		}
	}
}
