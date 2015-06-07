package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.ui.ADialog;

abstract class ADialogList<E> extends ADialog {
	private static final long serialVersionUID = -3420390175859554647L;
	protected DefaultListModel<E> mModel = new DefaultListModel<>();
	private JList<E> mList;
	private JButton mBtnAdd = new JButton( "Add");
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnDelete = new JButton( "Delete");
	private JButton mBtnUp = new JButton( "Up");
	private JButton mBtnDown = new JButton( "Down");
	private ListCellRenderer<E> mRenderer;
	private ADialogEdit<E> mEdit;

	public ADialogList( Window owner, ListCellRenderer<E> renderer, ADialogEdit<E> edit) {
		super( owner);
		mRenderer = renderer;
		mEdit = edit;
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
	protected final void createMain() {
		throw new UnsupportedOperationException( "use createMain(ICreator)");
	}

	protected void createMain( ICreator<E> creator) {
		mMain.setPreferredSize( new Dimension( 450, 200));
		mList = new JList<E>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setCellRenderer( mRenderer);
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
		mBtnAdd.addActionListener( new EntryAddHandler( creator));
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
	};

	private final class ButtonUpdateHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			updateBtn();
		}
	}

	private final class EntryAddHandler implements ActionListener {
		private ICreator<E> mCreator;

		private EntryAddHandler( ICreator<E> creator) {
			mCreator = creator;
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			E entry = mEdit.addElement( mCreator);
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
			E old = mList.getSelectedValue();
			E entry = mEdit.changeElement( old);
			if (entry != null) {
				int idx = mList.getSelectedIndex();
				mModel.setElementAt( entry, idx);
			}
		}
	}

	private final class EntryDeleteHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			E old = mList.getSelectedValue();
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
				E old = mModel.remove( idx - 1);
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
				E old = mModel.remove( idx);
				mModel.insertElementAt( old, idx - 1);
				mList.setSelectedIndex( idx - 1);
				updateBtn();
			}
		}
	}

	protected static interface ICreator<E> {
		E addElement();
	}
}
