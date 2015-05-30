package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;

public final class FItemRequirement extends ARequirement {
	public FItemStack mStack;
	public int mRequired;
	public ItemPrecision mPrecision;

	public FItemRequirement( AQuestTaskItems parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forItemRequirement( this, p);
	}

	@Override
	public int getCount() {
		return mRequired;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.ITEM_REQUIREMENT;
	}

	@Override
	public ItemPrecision getPrecision() {
		return mPrecision;
	}

	@Override
	public AStack getStack() {
		return mStack;
	}
}
