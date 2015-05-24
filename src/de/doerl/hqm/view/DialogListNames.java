package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogListNames extends ADialog {
	private static final long serialVersionUID = -2711755204151080619L;
	private DefaultAction mOk = new DefaultAction( BTN_OK, DialogResult.APPROVE);
	private DefaultListModel<String> mModel = new DefaultListModel<>();
	private JList<String> mList;
	private String mIgnore;

	private DialogListNames( Window owner, String ignore) {
		super( owner);
		mIgnore = ignore;
		mOk.setEnabled( false);
		setThema( "edit.list.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( mOk);
		addEscapeAction();
	}

	public static String update( Vector<String> vals, String ignore, Window owner) {
		if (vals != null) {
			DialogListNames dlg = new DialogListNames( owner, ignore);
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
		mList = new JList<String>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mList.setCellRenderer( new Renderer());
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				String current = mList.getSelectedValue();
				mOk.setEnabled( current != null && Utils.different( current, mIgnore));
			}
		});
		mList.setPreferredSize( new Dimension( 400, 200));
		mList.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		mMain.add( mList);
	}

	private String getSelected() {
		return mList.getSelectedValue();
	}

	private void updateMain( Vector<String> vals) {
		mModel.clear();
		for (String s : vals) {
			mModel.addElement( s);
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
