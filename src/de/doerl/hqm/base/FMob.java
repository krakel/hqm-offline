package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FMob extends ANamed {
	public final FQuestTaskMob mParentTask;
	public FItemStack mIcon;
	public String mMob;
	public int mKills;
	public boolean mExact;

	public FMob( FQuestTaskMob parent, FItemStack icon, String name) {
		super( name);
		mParentTask = parent;
		mIcon = icon;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forMob( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.MOB;
	}

	@Override
	public FQuestTaskMob getHierarchy() {
		return mParentTask;
	}

	@Override
	public FQuestTaskMob getParent() {
		return mParentTask;
	}
}
