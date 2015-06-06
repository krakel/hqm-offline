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
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

abstract class ADialogList extends ADialog {
	private static final long serialVersionUID = -3420390175859554647L;
	protected DefaultListModel<StackEntry> mModel = new DefaultListModel<>();
	private JList<StackEntry> mList;
	private JTextField mField = new JTextField();
	private JButton mBtnDelete = new JButton( "Delete");
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnAdd = new JButton( "Add");

	public ADialogList( Window owner) {
		super( owner);
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	private Box createButtons() {
		updateBtn( false);
		Box result = Box.createHorizontalBox();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.add( mBtnDelete);
		result.add( Box.createHorizontalGlue());
		result.add( mBtnChange);
		result.add( Box.createHorizontalGlue());
		result.add( mBtnAdd);
		return result;
	}

	private Box createEdit() {
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.setPreferredSize( new Dimension( 230, 100));
		result.setMaximumSize( new Dimension( 230, 100));
		result.setMinimumSize( new Dimension( 230, 100));
		mField.setFont( AEntity.FONT_NORMAL);
		mField.setPreferredSize( new Dimension( 230, 2 * getFont().getSize()));
		mField.setMaximumSize( new Dimension( 230, 2 * getFont().getSize()));
		mField.setAlignmentX( LEFT_ALIGNMENT);
		result.add( mField);
		result.add( Box.createVerticalStrut( GAP));
		result.add( createButtons());
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
					mField.setText( old.mName);
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
			public void actionPerformed( ActionEvent e) {
				StackEntry old = mList.getSelectedValue();
				String text = mField.getText();
				if (old != null && Utils.validString( text)) {
					int idx = mList.getSelectedIndex();
					mModel.setElementAt( new StackEntry( true, text, old.mIcon, old.mCount, old.mDamage, old.mPrecision), idx);
				}
			}
		});
		mBtnAdd.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e) {
				String text = mField.getText();
				if (Utils.validString( text)) {
					mModel.addElement( new StackEntry( true, text, null, 1, 0, null));
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

	protected static class StackEntry {
		public boolean mItem;
		public String mName;
		public String mIcon;
		public int mCount;
		public int mDamage;
		public ItemPrecision mPrecision;

		public StackEntry( boolean item, String name, String icon, int count, int dmg, ItemPrecision precition) {
			mItem = item;
			mName = name;
			mIcon = icon;
			mCount = count;
			mDamage = dmg;
			mPrecision = precition;
		}

		@Override
		public String toString() {
			return mName;
		}
	}
}
