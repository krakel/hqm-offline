package de.doerl.hqm.view;

import java.awt.Window;
import java.util.ArrayList;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
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

	public static boolean update( AQuestTaskItems task, Window owner) {
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

	private void updateMain( ArrayList<ARequirement> value) {
		mModel.clear();
		for (ARequirement req : value) {
			boolean isItem = req.getElementTyp() == ElementTyp.ITEM_REQUIREMENT;
			mModel.addElement( new StackEntry( isItem, req.getNBT(), req.getStack(), req.mAmount, req.getPrecision()));
		}
		updateBtn();
	}

	private ArrayList<ARequirement> updateResult( AQuestTaskItems task) {
		ArrayList<ARequirement> require = task.mRequirements;
		require.clear();
		ArrayList<ARequirement> result = new ArrayList<>();
		for (int i = 0; i < mModel.size(); ++i) {
			StackEntry e = mModel.get( i);
			if (e.mItem) {
				FItemRequirement req = task.createItemRequirement();
				req.setStack( new FItemStack( e.getName(), e.mDmg, 1, e.mNbt));
				req.mAmount = e.mCount;
				req.mPrecision = e.getPrecision();
			}
			else {
				FFluidRequirement req = task.createFluidRequirement();
				req.setStack( new FFluidStack( e.getName()));
				req.mAmount = e.mCount;
			}
		}
		return result;
	}
}
