package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.RepeatType;

public final class FRepeatInfo extends ABase {
	public final FQuest mParentQuest;
	public final FParameterEnum<RepeatType> mType = new FParameterEnum<RepeatType>( this, "Type");
	public final FParameterInt mTotal = new FParameterInt( this, "Total");

	public FRepeatInfo( FQuest parent) {
		mParentQuest = parent;
		mType.mValue = RepeatType.NONE;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forRepeatInfo( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPEAT_INFO;
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}
}
