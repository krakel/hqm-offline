package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Window;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

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
			mModel.addElement( new StackEntry( isItem, stk.getName(), null, req.getCount(), stk.getDamage(), req.getPrecision()));
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
				req.mStack = new FItemStack( e.getName(), 1, e.mDmg);
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
		private JComboBox<String> mDmg = new JComboBox<>();
		private JComboBox<ItemPrecision> mPrec = new JComboBox<>( ItemPrecision.values());

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.require.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			for (int i = 0; i < 16; ++i) {
				mDmg.addItem( String.valueOf( i));
			}
			mItem.setOpaque( false);
			Insets in = mItem.getInsets();
			mItem.setBorder( BorderFactory.createEmptyBorder( in.top, 0, in.bottom, in.right));
			mCount.addKeyListener( new KeyAdaptorInterger());
			createMain();
		}

		@Override
		public StackEntry addElement( ICreator<StackEntry> creator) {
			mName.setText( "name");
			mCount.setText( "1");
			mDmg.setSelectedIndex( 0);
			mItem.setSelected( true);
			mPrec.setSelectedItem( ItemPrecision.PRECISE);
			return showEditor();
		}

		private ParallelGroup addLine( GroupLayout layout, ParallelGroup leftGrp, ParallelGroup rightGrp, String descr, JComponent comp) {
			JLabel lbl = new JLabel( descr);
			leftGrp.addComponent( lbl);
			rightGrp.addComponent( comp);
			return layout.createParallelGroup( Alignment.BASELINE).addComponent( lbl).addComponent( comp);
		}

		@Override
		public StackEntry changeElement( StackEntry entry) {
			mName.setText( entry.getName());
			mCount.setText( String.valueOf( entry.mCount));
			mDmg.setSelectedIndex( entry.mDmg);
			mItem.setSelected( entry.mItem);
			mPrec.setSelectedItem( entry.getPrecision());
			return showEditor();
		}

		@Override
		protected void createMain() {
			JPanel box = new JPanel();
			GroupLayout layout = new GroupLayout( box);
			box.setLayout( layout);
			box.setOpaque( false);
//			box.setBorder( BorderFactory.createLineBorder( Color.RED));
			layout.setAutoCreateGaps( true);
			SequentialGroup hori = layout.createSequentialGroup();
			SequentialGroup vert = layout.createSequentialGroup();
			ParallelGroup leftGrp = layout.createParallelGroup();
			ParallelGroup rightGrp = layout.createParallelGroup();
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Item", mItem));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Size", mCount));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Damage", mDmg));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Precition", mPrec));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
		}

		private StackEntry getResult() {
			int size = Utils.parseInteger( mCount.getText(), 1);
			ItemPrecision prec = ItemPrecision.get( mPrec.getSelectedIndex());
			return new StackEntry( mItem.isSelected(), mName.getText(), size, mDmg.getSelectedIndex(), prec);
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

	private static class Renderer extends JPanel implements ListCellRenderer<StackEntry> {
		private static final long serialVersionUID = 5239073494468176719L;
		private LeafIcon mIcon = new LeafIcon( StackIcon.ICON_SIZE);
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
			mIcon.setIcon( new StackIcon( null, 0.6));
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
			mIcon.setIcon( new StackIcon( null, 0.6, String.valueOf( value.mCount)));
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
