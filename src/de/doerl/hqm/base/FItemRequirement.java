package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.nbt.FCompound;

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
		return mStack.getNBT();
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

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "stack", mStack);
		sb.appendMsg( "amount", mAmount);
		sb.appendMsg( "precision", mPrecision);
		return sb.toString();
	}
}
