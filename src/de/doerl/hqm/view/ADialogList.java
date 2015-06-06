package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.ui.ADialog;

abstract class ADialogList extends ADialog {
	private static final long serialVersionUID = -3420390175859554647L;
	protected DefaultListModel<StackEntry> mModel = new DefaultListModel<>();
	private JList<StackEntry> mList;
	private JButton mBtnAdd = new JButton( "Add");
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnDelete = new JButton( "Delete");

	public ADialogList( Window owner) {
		super( owner);
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		mBtnAdd.setMaximumSize( null);
		mBtnChange.setMaximumSize( null);
		mBtnDelete.setMaximumSize( null);
	}

	private Box createEdit() {
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
//		result.setPreferredSize( new Dimension( 100, 100));
//		result.setMaximumSize( new Dimension( 100, 100));
//		result.setMinimumSize( new Dimension( 100, 100));
		result.add( mBtnAdd);
		result.add( mBtnChange);
		result.add( mBtnDelete);
		result.add( Box.createVerticalGlue());
		return result;
	}

	@Override
	protected void createMain() {
		mMain.setPreferredSize( new Dimension( 450, 200));
		mList = new JList<StackEntry>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mList.setCellRenderer( new Renderer());
		mList.setMinimumSize( new Dimension( 100, 20));
		mList.setPreferredSize( new Dimension( 200, 200));
		mList.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		mMain.add( mList);
		mMain.add( Box.createHorizontalStrut( GAP));
		mMain.add( createEdit());
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				StackEntry old = mList.getSelectedValue();
				if (old != null) {
					updateBtn( true);
				}
			}
		});
		mBtnDelete.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent evt) {
				StackEntry old = mList.getSelectedValue();
				if (old != null) {
					mModel.removeElement( old);
					updateBtn( false);
				}
			}
		});
		mBtnChange.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent evt) {
				StackEntry old = mList.getSelectedValue();
				StackEntry e = DialogStack.update( old, (Window) getParent());
				if (e != null) {
					int idx = mList.getSelectedIndex();
					mModel.setElementAt( e, idx);
				}
			}
		});
		mBtnAdd.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent evt) {
				StackEntry e = DialogStack.update( new StackEntry(), (Window) getParent());
				if (e != null) {
					mModel.addElement( e);
				}
			}
		});
	}

	private void updateBtn( boolean enabled) {
		mBtnDelete.setEnabled( enabled);
		mBtnChange.setEnabled( enabled);
	}

	private final class Renderer extends JPanel implements ListCellRenderer<StackEntry> {
		private static final long serialVersionUID = 5239073494468176719L;
		private LeafIcon mIcon = new LeafIcon( StackIcon.ICON_BACK);
		private LeafLabel mName = new LeafLabel( "Unknown");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
			mIcon.setIcon( new StackIcon( null, 0.6));
			add( mIcon);
			add( Box.createHorizontalStrut( 5));
			add( mName);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends StackEntry> list, StackEntry entry, int index, boolean isSelected, boolean cellHasFocus) {
			mIcon.setIcon( new StackIcon( null, 0.6, String.valueOf( entry.mCount)));
			mName.setText( entry.mName);
			if (isSelected) {
				setBackground( list.getSelectionBackground());
			}
			else {
				setBackground( list.getBackground());
			}
			return this;
		}
	}

	static class StackEntry {
		public boolean mItem;
		private String mName;
		public String mIcon;
		public int mCount;
		public int mDamage;
		public ItemPrecision mPrecision;

		public StackEntry() {
			mItem = true;
			setName( "name");
			mCount = 1;
			mDamage = 0;
			mPrecision = ItemPrecision.PRECISE;
		}

		public StackEntry( boolean item, String name, int count, int dmg, ItemPrecision precition) {
			mItem = item;
			setName( name);
			mCount = count;
			mDamage = dmg;
			mPrecision = precition;
		}

		public StackEntry( boolean item, String name, String icon, int count, int dmg, ItemPrecision precition) {
			mItem = item;
			setName( name);
			mIcon = icon;
			mCount = count;
			mDamage = dmg;
			mPrecision = precition;
		}

		public String getName() {
			return mName;
		}

		public void setName( String name) {
			if (name.indexOf( ':') < 0) {
				mName = "unknown:" + name;
			}
			else {
				mName = name;
			}
		}

		@Override
		public String toString() {
			return mName;
		}
	}
}
