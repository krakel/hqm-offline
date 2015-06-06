package de.doerl.hqm.view;

import java.awt.Window;
import java.util.Vector;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;

class DialogListStacks extends ADialogList {
	private static final long serialVersionUID = 7121230392342882985L;

	private DialogListStacks( Window owner) {
		super( owner);
		setThema( "edit.stack.thema");
	}

	public static boolean update( Vector<FItemStack> values, Window owner) {
		DialogListStacks dlg = new DialogListStacks( owner);
		dlg.createMain();
		dlg.updateMain( values);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.getResult( values);
			return true;
		}
		else {
			return false;
		}
	}

	private void getResult( Vector<FItemStack> values) {
		values.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			values.add( new FItemStack( e.getName(), e.mCount, e.mDamage));
		}
	}

	private void updateMain( Vector<FItemStack> value) {
		mModel.clear();
		for (AStack stk : value) {
			mModel.addElement( new StackEntry( true, stk.getName(), null, stk.getCount(), 0, null));
		}
	}
}
