package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FLong;

public final class FItemRequirement extends ARequirement {
	private FItemStack mStack;
	public ItemPrecision mPrecision;

	public FItemRequirement( AQuestTaskItems parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forItemRequirement( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.ITEM_REQUIREMENT;
	}

	@Override
	public FCompound getNBT() {
		FCompound mNBT = mStack.getNBT();
		if (mNBT != null) {
			return mNBT;
		}
		else if (mStack.isOldItem()) {
			int id = Utils.parseInteger( mStack.getName(), 0);
			return FCompound.create( FLong.createShort( "id", id), FLong.createShort( "Damage", mStack.getDamage()), FLong.createByte( "Amount", mAmount));
		}
		else if (mStack.getName() != null) {
			return null;
		}
		else {
			return FCompound.create();
		}
	}

	@Override
	public ItemPrecision getPrecision() {
		return mPrecision;
	}

	@Override
	public FItemStack getStack() {
		return mStack;
	}

	public void setStack( FItemStack stack) {
		mStack = stack;
	}
}
