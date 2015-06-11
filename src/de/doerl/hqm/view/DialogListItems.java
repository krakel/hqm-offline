package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.Matcher;
import de.doerl.hqm.view.LeafSearch.ISearchListener;
import de.doerl.hqm.view.LeafSearch.SearchEvent;

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
			mModel.addElement( new StackEntry( true, stk, stk.getCount(), null));
		}
	}

	private void updateResult( Vector<FItemStack> values) {
		values.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			values.add( new FItemStack( e.getName(), e.mDmg, e.mCount));
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
		private LeafSearch mSearch = new LeafSearch();
		private JTextField mDmg = new JTextField();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
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
				}
			});
			mName.setPreferredSize( new Dimension( 200, mName.getPreferredSize().height));
		}

		@Override
		public StackEntry addElement( ICreator<StackEntry> creator) {
			mName.setText( "name");
			mDmg.setText( "0");
			mCount.setText( "1");
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
			mDmg.setText( String.valueOf( entry.mDmg));
			mCount.setText( String.valueOf( entry.mCount));
			return showEditor();
		}

		private Group createLeft( GroupLayout layout, Group hori) {
			SequentialGroup vert = layout.createSequentialGroup();
			ParallelGroup leftGrp = layout.createParallelGroup();
			ParallelGroup rightGrp = layout.createParallelGroup();
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Damage", mDmg));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Size", mCount));
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

		private StackEntry showEditor() {
			if (showDialog() == DialogResult.APPROVE) {
				int size = Utils.parseInteger( mCount.getText(), 1);
				return new StackEntry( true, mName.getText(), Utils.parseInteger( mDmg.getText(), 0), size, ItemPrecision.PRECISE);
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
