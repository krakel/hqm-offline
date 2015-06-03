package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.RepeatType;

public final class FRepeatInfo extends ABase {
	public final FQuest mParentQuest;
	public RepeatType mType = RepeatType.NONE;
	public int mTotal;

	public FRepeatInfo( FQuest parent) {
		mParentQuest = parent;
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
	public FQuest getHierarchy() {
		return mParentQuest;
	}

	@Override
	public FHqm getHqm() {
		return mParentQuest.getHqm();
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}
}
