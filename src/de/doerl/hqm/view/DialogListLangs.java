package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

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

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FLanguage;
import de.doerl.hqm.base.dispatch.LangAddWorker;
import de.doerl.hqm.base.dispatch.LangDeleteWorker;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogListLangs extends ADialog {
	private static final long serialVersionUID = 7673867993395512621L;
	protected DefaultListModel<FLanguage> mModel = new DefaultListModel<>();
	private JList<FLanguage> mList;
	private JButton mBtnAdd = new JButton( "Add");
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnDefault = new JButton( "Default");
	private JButton mBtnDelete = new JButton( "Delete");
	private ListCellRenderer<FLanguage> mRenderer;
	private FLanguage mDefault;
	private DialogTextField mEdit;
	private FHqm mHqm;

	public DialogListLangs( Window owner, FHqm hqm) {
		super( owner);
		mHqm = hqm;
		mEdit = new DialogTextField( owner);
		mRenderer = new Renderer();
		setTheme( "edit.mob.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		updateSize( mBtnAdd);
		updateSize( mBtnChange);
		updateSize( mBtnDefault);
		updateSize( mBtnDelete);
	}

	public static boolean update( FHqm hqm, Window owner) {
		DialogListLangs dlg = new DialogListLangs( owner, hqm);
		dlg.createMain();
		dlg.updateMain();
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult();
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
		result.add( mBtnDefault);
		result.add( Box.createVerticalStrut( 10));
		result.add( mBtnDelete);
		result.add( Box.createVerticalGlue());
		return result;
	}

	@Override
	protected void createMain() {
		mMain.setPreferredSize( new Dimension( 450, 200));
		mList = new JList<FLanguage>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setCellRenderer( mRenderer);
		mList.setVisibleRowCount( 5);
		JScrollPane scroll = new JScrollPane( mList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mMain.add( scroll);
		mMain.add( Box.createHorizontalStrut( GAP));
		mMain.add( createEdit());
		mList.addMouseListener( new ButtonUpdateHandler());
		mBtnAdd.addActionListener( new EntryAddHandler());
		mBtnChange.addActionListener( new EntryChangeHandler());
		mBtnDefault.addActionListener( new EntryDefaultHandler());
		mBtnDelete.addActionListener( new EntryDeleteHandler());
	}

	protected void updateBtn() {
		FLanguage value = mList.getSelectedValue();
		mBtnDefault.setEnabled( value != null && !value.equals( mDefault));
		mBtnDelete.setEnabled( value != null);
	}

	private void updateMain() {
		mModel.clear();
		for (FLanguage lang : mHqm.mLanguages) {
			mModel.addElement( lang);
		}
		mDefault = mHqm.mMain;
		updateBtn();
	}

	private void updateResult() {
		Vector<FLanguage> found = new Vector<>();
		for (int i = 0; i < mModel.size(); ++i) {
			FLanguage e = mModel.get( i);
			found.add( e);
			LangAddWorker.get( mHqm, e);
		}
		for (int i = mHqm.mLanguages.size() - 1; i >= 0; --i) {
			FLanguage e = mHqm.mLanguages.get( i);
			if (!found.contains( e)) {
				LangDeleteWorker.get( mHqm, e);
			}
		}
		if (mHqm.mLanguages.size() == 0) {
			LangAddWorker.get( mHqm, FHqm.LANG_EN_US);
		}
		if (mHqm.mLanguages.contains( mDefault)) {
			mHqm.mMain = mDefault;
		}
		else if (!mHqm.mLanguages.contains( mHqm.mMain)) {
			mHqm.mMain = mHqm.mLanguages.firstElement();
		}
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
			String value = mEdit.change( "xxXX");
			if (value != null) {
				mModel.addElement( mHqm.createLanguage( value));
				updateBtn();
			}
		}
	}

	private final class EntryChangeHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			FLanguage entry = mList.getSelectedValue();
			String value = mEdit.change( entry.mLocale);
			if (value != null) {
				entry.mLocale = value;
				mList.repaint();
				updateBtn();
			}
		}
	}

	private final class EntryDefaultHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			FLanguage entry = mList.getSelectedValue();
			if (entry != null) {
				mDefault = entry;
				mList.repaint();
				updateBtn();
			}
		}
	}

	private final class EntryDeleteHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			FLanguage entry = mList.getSelectedValue();
			if (entry != null) {
				if (Utils.equals( entry, mDefault)) {
					mDefault = null;
				}
				mModel.removeElement( entry);
				updateBtn();
			}
		}
	}

	private class Renderer extends AListCellRenderer<FLanguage> {
		private static final long serialVersionUID = -1157136740559242098L;
		private LeafLabel mName = new LeafLabel( "xxXX");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			AEntity.setSizes( this, ADialog.FONT_NORMAL.getSize());
			mName.setFont( ADialog.FONT_NORMAL);
			mName.setAlignmentX( LEFT_ALIGNMENT);
			add( mName);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FLanguage> list, FLanguage value, int index, boolean isSelected, boolean cellHasFocus) {
			mName.setText( value.mLocale);
			if (Utils.equals( value, mDefault)) {
				mName.setFont( ADialog.FONT_STACK);
			}
			else {
				mName.setFont( ADialog.FONT_NORMAL);
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
