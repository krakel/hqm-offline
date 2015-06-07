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
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.ui.ADialog;

abstract class ADialogList extends ADialog {
	private static final long serialVersionUID = -3420390175859554647L;
	protected DefaultListModel<StackEntry> mModel = new DefaultListModel<>();
	private JList<StackEntry> mList;
	private JButton mBtnAdd = new JButton( "Add");
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnDelete = new JButton( "Delete");
	private JButton mBtnUp = new JButton( "Up");
	private JButton mBtnDown = new JButton( "Down");
	private boolean mModus;

	public ADialogList( Window owner, boolean modus) {
		super( owner);
		mModus = modus;
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		updateSize( mBtnAdd);
		updateSize( mBtnChange);
		updateSize( mBtnDelete);
		updateSize( mBtnUp);
		updateSize( mBtnDown);
	}

	private Box createEdit() {
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.setMaximumSize( new Dimension( 50, Short.MAX_VALUE));
		result.add( mBtnAdd);
		result.add( mBtnChange);
		result.add( mBtnDelete);
		result.add( Box.createVerticalStrut( 20));
		result.add( mBtnUp);
		result.add( mBtnDown);
		result.add( Box.createVerticalGlue());
		return result;
	}

	@Override
	protected void createMain() {
		mMain.setPreferredSize( new Dimension( 450, 200));
		mList = new JList<StackEntry>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setCellRenderer( new Renderer( mModus));
		mList.setVisibleRowCount( 5);
		JScrollPane scroll = new JScrollPane( mList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mMain.add( scroll);
		mMain.add( Box.createHorizontalStrut( GAP));
		mMain.add( createEdit());
		ClickHandler dbl = new ClickHandler();
		mList.addMouseListener( new ButtonUpdateHandler());
		mList.addMouseListener( dbl);
		mBtnUp.addActionListener( new EntryUpHandler());
		mBtnDown.addActionListener( new EntryDownHandler());
		mBtnAdd.addActionListener( new EntryAddHandler());
		mBtnDelete.addActionListener( new EntryDeleteHandler());
		ActionListener edit = new EntryChangeHandler();
		mBtnChange.addActionListener( edit);
		dbl.addClickListener( edit);
		updateBtn();
	}

	private void updateBtn() {
		int idx = mList.getSelectedIndex() + 1;
		mBtnDelete.setEnabled( idx > 0);
		mBtnChange.setEnabled( idx > 0);
		mBtnUp.setEnabled( idx > 1);
		mBtnDown.setEnabled( idx > 0 && idx < mModel.getSize());
	}

	private void updateSize( JComponent comp) {
		comp.setMaximumSize( new Dimension( Short.MAX_VALUE, comp.getMinimumSize().height));
	}

	private final class ButtonUpdateHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			updateBtn();
		}
	}

	private final class EntryAddHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			StackEntry entry = DialogStack.update( (Window) getParent(), new StackEntry(), mModus);
			if (entry != null) {
				mModel.addElement( entry);
				mList.setSelectedValue( entry, true);
				updateBtn();
			}
		}
	}

	private final class EntryChangeHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			StackEntry old = mList.getSelectedValue();
			StackEntry entry = DialogStack.update( (Window) getParent(), old, mModus);
			if (entry != null) {
				int idx = mList.getSelectedIndex();
				mModel.setElementAt( entry, idx);
			}
		}
	}

	private final class EntryDeleteHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			StackEntry old = mList.getSelectedValue();
			if (old != null) {
				mModel.removeElement( old);
				updateBtn();
			}
		}
	}

	private final class EntryDownHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent e) {
			int idx = mList.getSelectedIndex() + 1;
			if (idx > 0 && idx < mModel.getSize()) {
				StackEntry old = mModel.remove( idx - 1);
				mModel.insertElementAt( old, idx);
				mList.setSelectedIndex( idx);
				updateBtn();
			}
		}
	}

	private final class EntryUpHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent e) {
			int idx = mList.getSelectedIndex();
			if (idx > 0) {
				StackEntry old = mModel.remove( idx);
				mModel.insertElementAt( old, idx - 1);
				mList.setSelectedIndex( idx - 1);
				updateBtn();
			}
		}
	}

	private final class Renderer extends JPanel implements ListCellRenderer<StackEntry> {
		private static final long serialVersionUID = 5239073494468176719L;
		private LeafIcon mIcon = new LeafIcon( StackIcon.ICON_BACK);
		private LeafLabel mName = new LeafLabel( "Unknown");
		private boolean mModus;

		public Renderer( boolean modus) {
			mModus = modus;
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
			if (mModus) {
				mName.setText( String.format( "(%s%2d) %s", entry.getPrecision().getSymbol(), entry.mDamage, entry.getName()));
			}
			else {
				mName.setText( String.format( "(%2d) %s", entry.mDamage, entry.getName()));
			}
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
