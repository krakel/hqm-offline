package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FMob extends ANamed {
	public final FQuestTaskMob mParentTask;
	public final FParameterStack mIcon = new FParameterStack( this);
	public final FParameterString mMob = new FParameterString( this);
	public final FParameterInt mKills = new FParameterInt( this);
	public final FParameterBoolean mExact = new FParameterBoolean( this);

	public FMob( FQuestTaskMob parent, FItemStack icon, String name) {
		super( name);
		mParentTask = parent;
		mIcon.mValue = icon;
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
	public FQuestTaskMob getParent() {
		return mParentTask;
	}
}
