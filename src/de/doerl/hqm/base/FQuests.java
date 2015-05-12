package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FQuests extends ASet<FQuest> {
	FQuests( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuests( this, p);
	}

	@Override
	public FQuest createMember( String name) {
		FQuest reward = new FQuest( this, name);
		addMember( reward);
		return reward;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUESTS;
	}
}
