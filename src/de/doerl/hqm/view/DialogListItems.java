package de.doerl.hqm.view;

import java.awt.Window;
import java.util.ArrayList;

import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.ItemPrecision;

class DialogListItems extends ADialogStacks {
	private static final long serialVersionUID = 7121230392342882985L;

	private DialogListItems( Window owner) {
		super( owner, false);
		setTheme( "edit.item.theme");
	}

	public static boolean update( ArrayList<FItemStack> values, Window owner) {
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

	private void updateMain( ArrayList<FItemStack> value) {
		mModel.clear();
		for (FItemStack stk : value) {
			mModel.addElement( new StackEntry( true, stk.getNBT(), stk, stk.getStackSize(), ItemPrecision.PRECISE));
		}
		updateBtn();
	}

	private void updateResult( ArrayList<FItemStack> values) {
		values.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			values.add( new FItemStack( e.getName(), e.mDmg, e.mCount, e.mNbt));
		}
	}
}
