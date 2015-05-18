package de.doerl.hqm.view;

import java.awt.Window;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JList;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.ui.ADialog;

class DialogList extends ADialog {
	private static final long serialVersionUID = -2711755204151080619L;
	private DefaultListModel<String> mModel = new DefaultListModel<String>();
	private JList<String> mList;

	private DialogList( Window owner) {
		super( owner);
		mList = new JList<String>( mModel);
		setThema( "edit.list.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		createMain();
	}

	public static <E extends ANamed> void update( ACategory<E> param, EditView view) {
		if (param != null) {
			DialogList dlg = new DialogList( ADialog.getParentFrame( view));
			MemberFactory.get( param, dlg.mModel);
			if (dlg.showDialog() == DialogResult.APPROVE) {
//				param.mValue = dlg.getText();
			}
		}
	}

	@Override
	protected void createMain() {
		GroupLayout layout = mMain.getLayout();
		ParallelGroup hori = layout.createParallelGroup( GroupLayout.Alignment.CENTER);
		SequentialGroup vert = layout.createSequentialGroup();
		hori.addComponent( mList, 400, 400, Short.MAX_VALUE);
		vert.addComponent( mList, 200, 200, Short.MAX_VALUE);
		layout.setHorizontalGroup( hori);
		layout.setVerticalGroup( vert);
	}

	private static class MemberFactory extends AHQMWorker<Object, DefaultListModel<String>> {
		private static final MemberFactory WORKER = new MemberFactory();

		private MemberFactory() {
		}

		public static void get( ACategory<?> set, DefaultListModel<String> model) {
			model.clear();
			set.forEachMember( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, DefaultListModel<String> model) {
			return null;
		}

		@Override
		protected Object doMember( AMember<? extends ANamed> member, DefaultListModel<String> model) {
			model.addElement( member.getName());
			return null;
		}
	}
}
