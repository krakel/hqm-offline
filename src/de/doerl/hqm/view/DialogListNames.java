package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogListNames<E> extends ADialog {
	private static final long serialVersionUID = -2711755204151080619L;
	private DefaultAction mOk = new DefaultAction( BTN_OK, DialogResult.APPROVE);
	private DefaultListModel<E> mModel = new DefaultListModel<>();
	private JList<E> mList;
	private E mIgnore;

	private DialogListNames( Window owner, E ignore) {
		super( owner);
		mIgnore = ignore;
		mOk.setEnabled( false);
		setTheme( "edit.list.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( mOk);
		addEscapeAction();
	}

	public static <E> E update( ArrayList<E> vals, E ignore, Window owner) {
		if (vals != null) {
			DialogListNames<E> dlg = new DialogListNames<>( owner, ignore);
			dlg.createMain();
			dlg.updateMain( vals);
			if (dlg.showDialog() == DialogResult.APPROVE) {
				return dlg.getSelected();
			}
		}
		return null;
	}

	public static <E> E update( E[] vals, E ignore, Window owner) {
		if (vals != null) {
			DialogListNames<E> dlg = new DialogListNames<>( owner, ignore);
			dlg.createMain();
			dlg.updateMain( vals);
			if (dlg.showDialog() == DialogResult.APPROVE) {
				return dlg.getSelected();
			}
		}
		return null;
	}

	@Override
	protected void createMain() {
		mList = new JList<>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setCellRenderer( new Renderer());
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				E current = mList.getSelectedValue();
				mOk.setEnabled( current != null && Utils.different( current, mIgnore));
			}
		});
		JScrollPane scroll = new JScrollPane( mList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		scroll.setPreferredSize( new Dimension( 400, 200));
		scroll.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		mMain.add( scroll);
	}

	private E getSelected() {
		return mList.getSelectedValue();
	}

	private void updateMain( ArrayList<E> vals) {
		mModel.clear();
		for (int i = 0; i < vals.size(); ++i) {
			mModel.addElement( vals.get( i));
		}
		if (mIgnore != null) {
			mList.setSelectedValue( mIgnore, true);
		}
	}

	private void updateMain( E[] vals) {
		mModel.clear();
		for (int i = 0; i < vals.length; ++i) {
			mModel.addElement( vals[i]);
		}
		if (mIgnore != null) {
			mList.setSelectedValue( mIgnore, true);
		}
	}

	private class Renderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = -534075821364492652L;

		@Override
		public Component getListCellRendererComponent( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus);
			setEnabled( Utils.different( value, mIgnore));
			return this;
		}
	}
}
