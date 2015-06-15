package de.doerl.hqm.view;

import java.awt.Window;
import java.util.Vector;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.ItemPrecision;

class DialogListItems extends ADialogStacks {
	private static final long serialVersionUID = 7121230392342882985L;

	private DialogListItems( Window owner) {
		super( owner, false);
		setTheme( "edit.item.theme");
	}

	public static boolean update( Vector<FItemStack> values, Window owner) {
		DialogListItems dlg = new DialogListItems( owner);
		dlg.createMain( new Creator());
		dlg.updateMain( values);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( values);
			return true;
		}
		else {
			return false;
		}
	}

	private void updateMain( Vector<FItemStack> value) {
		mModel.clear();
		for (AStack stk : value) {
			mModel.addElement( new StackEntry( true, stk, stk.getCount(), ItemPrecision.PRECISE));
		}
	}

	private void updateResult( Vector<FItemStack> values) {
		values.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			values.add( new FItemStack( e.getName(), e.mDmg, e.mCount));
		}
	}
}
