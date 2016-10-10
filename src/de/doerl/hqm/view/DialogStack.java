package de.doerl.hqm.view;

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
import de.doerl.hqm.utils.nbt.ParserAtNEI;
import de.doerl.hqm.utils.nbt.SerializerAtNEI;
import de.doerl.hqm.view.ADialogList.ICreator;
import de.doerl.hqm.view.leafs.LeafSearch;
import de.doerl.hqm.view.leafs.LeafSearch.ISearchListener;
import de.doerl.hqm.view.leafs.LeafSearch.SearchEvent;

class DialogStack extends ADialogEdit<StackEntry> {
	private static final long serialVersionUID = 651515231606809783L;
	private JCheckBox mItem = new JCheckBox();
	private JTextField mDisplay = new TextFieldAscii();
	private JTextField mName = new TextFieldAscii();
	private JTextField mCount = new TextFieldInteger();
	private JTextField mDmg = new TextFieldInteger();
	private JTextArea mArea = new TextAreaAscii();
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
		mArea.setLineWrap( true);
		mArea.setWrapStyleWord( true);
		createMain();
		mSearch.addSearchListener( new ISearchListener() {
			@Override
			public void doAction( SearchEvent event) {
				ItemNEI item = event.getItem();
				if (item != null) {
					mDisplay.setText( item.getDisplay());
					mName.setText( item.mName);
					mDmg.setText( String.valueOf( item.mDamage));
					mCount.setText( String.valueOf( 1));
					mItem.setSelected( true);
					mPrec.setSelectedItem( ItemPrecision.PRECISE);
					mArea.setText( SerializerAtNEI.write( item.getNBT(), true));
				}
			}
		});
//		mSearch.setMinimumSize( mSearch.getPreferredSize());
//		mDisplay.setPreferredSize( new Dimension( 200, mDisplay.getPreferredSize().height));
//		mName.setPreferredSize( new Dimension( 200, mName.getPreferredSize().height));
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
		mDisplay.setText( "display");
		mName.setText( "name");
		mDmg.setText( "0");
		mCount.setText( "1");
		mItem.setSelected( true);
		mPrec.setSelectedItem( ItemPrecision.PRECISE);
		mArea.setText( "");
		return showEditor();
	}

	@Override
	public StackEntry changeElement( StackEntry entry) {
		mDisplay.setText( entry.getDisplay());
		mName.setText( entry.getName());
		mDmg.setText( String.valueOf( entry.mDmg));
		mCount.setText( String.valueOf( entry.mCount));
		mItem.setSelected( entry.mIsItem);
		mPrec.setSelectedItem( entry.getPrecision());
		mArea.setText( entry.getNbtStr());
		return showEditor();
	}

	private Group createLeft( GroupLayout layout, Group hori) {
		Group vert = layout.createSequentialGroup();
		Group leftGrp = layout.createParallelGroup();
		Group rightGrp = layout.createParallelGroup();
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Item", mItem));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Display", mDisplay));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Damage", mDmg));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Size", mCount));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "Precition", mPrec));
		vert.addGroup( addLine( layout, leftGrp, rightGrp, "NBT", mArea));
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
		vert.addComponent( mSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
		hori.addComponent( mSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
		mMain.add( box);
		mSearch.doSearch();
	}

	private StackEntry getResult() {
		int size = Utils.parseInteger( mCount.getText(), 1);
		ItemPrecision prec = ItemPrecision.get( mPrec.getSelectedIndex());
		return new StackEntry( mItem.isSelected(), mName.getText(), Utils.parseInteger( mDmg.getText(), 0), mArea.getText(), size, prec);
	}

	private FItemStack getResultStk() {
		String name = mName.getText();
		if (Utils.validString( name)) {
			return new FItemStack( name, Utils.parseInteger( mDmg.getText(), 0), 1, ParserAtNEI.parse( mArea.getText()));
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
			mDisplay.setText( stk.getDisplay());
			mName.setText( stk.getName());
			mDmg.setText( String.valueOf( stk.getDamage()));
			mCount.setText( "1");
			mCount.setEnabled( false);
			mItem.setSelected( true);
			mPrec.setSelectedItem( ItemPrecision.PRECISE);
			mArea.setText( SerializerAtNEI.write( stk.getNBT(), true));
		}
		else {
			mDisplay.setText( "");
			mName.setText( "unknown");
			mDmg.setText( "0");
			mCount.setText( "1");
			mCount.setEnabled( false);
			mItem.setSelected( true);
			mPrec.setSelectedItem( ItemPrecision.PRECISE);
			mArea.setText( "");
		}
	}
}
