package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;

public final class FItemRequirement extends ARequirement {
	public FParameterStack mStack = new FParameterStack( this);
	public FParameterInt mRequired = new FParameterInt( this);
	public final FParameterEnum<ItemPrecision> mPrecision = new FParameterEnum<ItemPrecision>( this);

	public FItemRequirement( AQuestTaskItems parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forItemRequirement( this, p);
	}

	@Override
	public int getCount() {
		return mRequired.mValue;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.ITEM_REQUIREMENT;
	}

	@Override
	public ItemPrecision getPrecision() {
		return mPrecision.mValue;
	}

	@Override
	public FParameterStack getStack() {
		return mStack;
	}
}
