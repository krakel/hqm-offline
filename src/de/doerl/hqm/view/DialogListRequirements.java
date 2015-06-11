package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.Matcher;
import de.doerl.hqm.view.LeafSearch.ISearchListener;
import de.doerl.hqm.view.LeafSearch.SearchEvent;

class DialogListRequirements extends ADialogList<StackEntry> {
	private static final long serialVersionUID = 8338191508190847304L;

	private DialogListRequirements( Window owner) {
		super( owner, new Renderer(), new Editor( owner));
		setTheme( "edit.require.theme");
	}

	public static boolean update( AQuestTaskItems task, Window owner) { //  Vector<ARequirement> value
		DialogListRequirements dlg = new DialogListRequirements( owner);
		dlg.createMain( new Creator());
		dlg.updateMain( task.mRequirements);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( task);
			return true;
		}
		else {
			return false;
		}
	}

	private void updateMain( Vector<ARequirement> value) {
		mModel.clear();
		for (ARequirement req : value) {
			AStack stk = req.getStack();
			boolean isItem = req.getElementTyp() == ElementTyp.ITEM_REQUIREMENT;
			mModel.addElement( new StackEntry( isItem, stk, req.getCount(), req.getPrecision()));
		}
	}

	private Vector<ARequirement> updateResult( AQuestTaskItems task) {
		Vector<ARequirement> require = task.mRequirements;
		require.clear();
		Vector<ARequirement> result = new Vector<>();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			if (e.mItem) {
				FItemRequirement req = task.createItemRequirement();
				req.mStack = new FItemStack( e.getName(), e.mDmg, 1);
				req.mRequired = e.mCount;
				req.mPrecision = e.getPrecision();
			}
			else {
				FFluidRequirement req = task.createFluidRequirement();
				req.mStack = new FFluidStack( e.getName(), e.mCount);
			}
		}
		return result;
	}

	private static class Creator implements ICreator<StackEntry> {
		@Override
		public StackEntry addElement() {
			return new StackEntry();
		}

		public ABase getBase() {
			return null;
		}
	}

	private static class Editor extends ADialogEdit<StackEntry> {
		private static final long serialVersionUID = 651515231606809783L;
		private JCheckBox mItem = new JCheckBox();
		private JTextField mName = new JTextField();
		private JTextField mCount = new JTextField();
		private LeafSearch mSearch = new LeafSearch();
		private JTextField mDmg = new JTextField();
		private JComboBox<ItemPrecision> mPrec = new JComboBox<>( ItemPrecision.values());

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.require.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			mItem.setOpaque( false);
			Insets in = mItem.getInsets();
			mItem.setBorder( BorderFactory.createEmptyBorder( in.top, 0, in.bottom, in.right));
			mDmg.addKeyListener( new KeyAdaptorInterger());
			mCount.addKeyListener( new KeyAdaptorInterger());
			createMain();
			mSearch.addSearchListener( new ISearchListener() {
				@Override
				public void doAction( SearchEvent event) {
					Matcher match = event.getMatch();
					mName.setText( match.getName());
					mDmg.setText( String.valueOf( match.getDamage()));
					mCount.setText( String.valueOf( 1));
					mItem.setSelected( true);
					mPrec.setSelectedItem( ItemPrecision.PRECISE);
				}
			});
			mName.setPreferredSize( new Dimension( 200, mName.getPreferredSize().height));
		}

		@Override
		public StackEntry addElement( ICreator<StackEntry> creator) {
			mName.setText( "name");
			mDmg.setText( "0");
			mCount.setText( "1");
			mItem.setSelected( true);
			mPrec.setSelectedItem( ItemPrecision.PRECISE);
			return showEditor();
		}

		private Group addLine( GroupLayout layout, Group left, Group right, String descr, JComponent comp) {
			JLabel lbl = new JLabel( descr);
			left.addComponent( lbl);
			right.addComponent( comp);
			return layout.createParallelGroup( Alignment.BASELINE).addComponent( lbl).addComponent( comp);
		}

		@Override
		public StackEntry changeElement( StackEntry entry) {
			mName.setText( entry.getName());
			mDmg.setText( String.valueOf( entry.mDmg));
			mCount.setText( String.valueOf( entry.mCount));
			mItem.setSelected( entry.mItem);
			mPrec.setSelectedItem( entry.getPrecision());
			return showEditor();
		}

		private Group createLeft( GroupLayout layout, Group hori) {
			Group vert = layout.createSequentialGroup();
			Group left = layout.createParallelGroup();
			Group right = layout.createParallelGroup();
			vert.addGroup( addLine( layout, left, right, "Item", mItem));
			vert.addGroup( addLine( layout, left, right, "Name", mName));
			vert.addGroup( addLine( layout, left, right, "Damage", mDmg));
			vert.addGroup( addLine( layout, left, right, "Size", mCount));
			vert.addGroup( addLine( layout, left, right, "Precition", mPrec));
			hori.addGroup( left);
			hori.addGroup( right);
			return vert;
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
			return new StackEntry( mItem.isSelected(), mName.getText(), Utils.parseInteger( mDmg.getText(), 0), size, prec);
		}

		private StackEntry showEditor() {
			if (showDialog() == DialogResult.APPROVE) {
				return getResult();
			}
			else {
				return null;
			}
		}
	}

	private static class Renderer extends AListCellRenderer<StackEntry> {
		private static final long serialVersionUID = 5239073494468176719L;
		private LeafIcon mIcon = new LeafIcon();
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
			mIcon.setIcon( new StackIcon());
			add( mIcon);
			add( Box.createHorizontalStrut( 5));
			add( createBox());
		}

		private Box createBox() {
			mName.setFont( AEntity.FONT_STACK);
			mInfo.setFont( AEntity.FONT_SMALL);
			mName.setAlignmentX( LEFT_ALIGNMENT);
			mInfo.setAlignmentX( LEFT_ALIGNMENT);
			Box box = Box.createVerticalBox();
			box.setAlignmentY( CENTER_ALIGNMENT);
			box.add( mName);
			box.add( mInfo);
			return box;
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends StackEntry> list, StackEntry value, int index, boolean isSelected, boolean cellHasFocus) {
			Image img = ImageLoader.getImage( value.getKey(), createUpdater( list));
			mIcon.setIcon( new StackIcon( img, String.valueOf( value.mCount)));
			mName.setText( value.getName());
			mInfo.setText( String.format( "%s, dmg %2d, count %d", value.getPrecision(), value.mDmg, value.mCount));
			if (isSelected) {
				setBackground( list.getSelectionBackground());
			}
			else {
				setBackground( list.getBackground());
			}
			return this;
		}
	}
}
