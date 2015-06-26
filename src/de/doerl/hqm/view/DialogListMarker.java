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
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.ui.ADialog;

class DialogListMarker extends ADialog {
	private static final long serialVersionUID = 8707497347819126949L;
	protected DefaultListModel<FMarker> mModel = new DefaultListModel<>();
	private JList<FMarker> mList;
	private JButton mBtnAdd = new JButton( "Add");
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnDelete = new JButton( "Delete");
	private ListCellRenderer<FMarker> mRenderer;
	private DialogMarker mEdit;

	public DialogListMarker( Window owner) {
		super( owner);
		mRenderer = new Renderer();
		mEdit = new DialogMarker( owner);
		setTheme( "edit.mob.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		updateSize( mBtnAdd);
		updateSize( mBtnChange);
		updateSize( mBtnDelete);
	}

	public static boolean update( FReputation rep, Window owner) {
		DialogListMarker dlg = new DialogListMarker( owner);
		dlg.createMain( rep);
		dlg.updateMain( rep);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( rep);
			return true;
		}
		else {
			return false;
		}
	}

	private Box createEdit() {
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.setMaximumSize( new Dimension( 50, Short.MAX_VALUE));
		result.add( mBtnAdd);
		result.add( mBtnChange);
		result.add( mBtnDelete);
		result.add( Box.createVerticalGlue());
		return result;
	}

	@Override
	protected final void createMain() {
		throw new UnsupportedOperationException( "use createMain(ICreator)");
	}

	protected void createMain( FReputation rep) {
		mMain.setPreferredSize( new Dimension( 450, 200));
		mList = new JList<FMarker>( mModel);
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
		mBtnAdd.addActionListener( new EntryAddHandler( rep));
		mBtnDelete.addActionListener( new EntryDeleteHandler());
		ActionListener edit = new EntryChangeHandler();
		mBtnChange.addActionListener( edit);
		dbl.addClickListener( edit);
	}

	private int getPosition( int mark) {
		for (int i = 0; i < mModel.size(); ++i) {
			FMarker mm = mModel.getElementAt( i);
			if (mm.mMark > mark) {
				return i;
			}
		}
		return 0;
	};

	protected void updateBtn() {
		int idx = mList.getSelectedIndex();
		mBtnDelete.setEnabled( idx >= 0);
		mBtnChange.setEnabled( idx >= 0);
	}

	private void updateMain( FReputation rep) {
		mModel.clear();
		for (FMarker mob : rep.mMarker) {
			mModel.addElement( mob);
		}
		updateBtn();
	}

	private void updateResult( FReputation rep) {
		rep.mMarker.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			FMarker e = mModel.get( i);
			rep.mMarker.add( e);
		}
		rep.sort();
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
		private FReputation mRep;

		private EntryAddHandler( FReputation rep) {
			mRep = rep;
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			FMarker entry = mEdit.addElement( mRep);
			if (entry != null) {
				int pos = getPosition( entry.mMark);
				mModel.insertElementAt( entry, pos);
				mList.setSelectedValue( entry, true);
				updateBtn();
			}
		}
	}

	private final class EntryChangeHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			FMarker old = mList.getSelectedValue();
			FMarker entry = mEdit.changeElement( old);
			if (entry != null) {
				int idx = mList.getSelectedIndex();
				mModel.remove( idx);
				int pos = getPosition( entry.mMark);
				mModel.insertElementAt( entry, pos);
			}
		}
	}

	private final class EntryDeleteHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			FMarker old = mList.getSelectedValue();
			if (old != null) {
				mModel.removeElement( old);
				updateBtn();
			}
		}
	}

	private static class Renderer extends AListCellRenderer<FMarker> {
		private static final long serialVersionUID = -430644712741965086L;
		private LeafLabel mMark = new LeafLabel( "");
		private LeafLabel mName = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mMark.setMinimumSize( new Dimension( 30, AEntity.FONT_NORMAL.getSize()));
			JComponent hori = AEntity.leafBoxHorizontal( AEntity.FONT_NORMAL.getSize());
			hori.add( mMark);
			hori.add( mName);
			add( hori);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FMarker> list, FMarker value, int index, boolean isSelected, boolean cellHasFocus) {
			mMark.setText( String.valueOf( value.mMark));
			mName.setText( value.mName);
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
