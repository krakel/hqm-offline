package de.doerl.hqm.view;

import java.awt.Window;
import java.util.Vector;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.ElementTyp;

class DialogListRequirements extends ADialogStacks {
	private static final long serialVersionUID = 8338191508190847304L;

	private DialogListRequirements( Window owner) {
		super( owner, true);
		setTheme( "edit.require.theme");
	}

	public static boolean update( AQuestTaskItems task, Window owner) { //  Vector<ARequirement> value
		DialogListRequirements dlg = new DialogListRequirements( owner);
		dlg.createMain( new Creator());
		dlg.updateMain( task.mRequirements);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( task);
			return true;
		}
		else {
			return false;
		}
	}

	private void updateMain( Vector<ARequirement> value) {
		mModel.clear();
		for (ARequirement req : value) {
			AStack stk = req.getStack();
			boolean isItem = req.getElementTyp() == ElementTyp.ITEM_REQUIREMENT;
			mModel.addElement( new StackEntry( isItem, stk, req.getCount(), req.getPrecision()));
		}
	}

	private Vector<ARequirement> updateResult( AQuestTaskItems task) {
		Vector<ARequirement> require = task.mRequirements;
		require.clear();
		Vector<ARequirement> result = new Vector<>();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			if (e.mItem) {
				FItemRequirement req = task.createItemRequirement();
				req.mStack = new FItemStack( e.mNbt, e.getName(), e.mDmg, 1);
				req.mRequired = e.mCount;
				req.mPrecision = e.getPrecision();
			}
			else {
				FFluidRequirement req = task.createFluidRequirement();
				req.mStack = new FFluidStack( e.getName(), e.mCount);
			}
		}
		return result;
	}
}
