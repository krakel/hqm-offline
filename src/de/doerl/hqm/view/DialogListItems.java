package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Window;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;

class DialogListItems extends ADialogList<StackEntry> {
	private static final long serialVersionUID = 7121230392342882985L;

	private DialogListItems( Window owner) {
		super( owner, new Renderer(), new Editor( owner));
		setTheme( "edit.item.theme");
	}

	public static boolean update( Vector<FItemStack> values, Window owner) {
		DialogListItems dlg = new DialogListItems( owner);
		dlg.createMain( new Creator());
		dlg.updateMain( values);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( values);
			return true;
		}
		else {
			return false;
		}
	}

	private void updateMain( Vector<FItemStack> value) {
		mModel.clear();
		for (AStack stk : value) {
			mModel.addElement( new StackEntry( true, stk.getName(), null, stk.getCount(), 0, null));
		}
	}

	private void updateResult( Vector<FItemStack> values) {
		values.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			values.add( new FItemStack( e.getName(), e.mCount, e.mDmg));
		}
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
		private JTextField mName = new JTextField();
		private JTextField mCount = new JTextField();
		private JComboBox<String> mDmg = new JComboBox<>();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			for (int i = 0; i < 16; ++i) {
				mDmg.addItem( String.valueOf( i));
			}
			mCount.addKeyListener( new KeyAdaptorInterger());
			createMain();
		}

		@Override
		public StackEntry addElement( ICreator<StackEntry> creator) {
			mName.setText( "name");
			mCount.setText( "1");
			mDmg.setSelectedIndex( 0);
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
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Size", mCount));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Damage", mDmg));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
		}

		private StackEntry showEditor() {
			if (showDialog() == DialogResult.APPROVE) {
				int size = Utils.parseInteger( mCount.getText(), 1);
				return new StackEntry( true, mName.getText(), size, mDmg.getSelectedIndex(), ItemPrecision.PRECISE);
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
			mInfo.setText( String.format( "dmg %2d, count %d", value.mDmg, value.mCount));
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
