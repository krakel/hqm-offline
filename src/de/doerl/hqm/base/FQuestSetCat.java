package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.QuestSetOfName;
import de.doerl.hqm.quest.ElementTyp;

public final class FQuestSetCat extends ACategory<FQuestSet> {
	FQuestSetCat( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuestSetCat( this, p);
	}

	@Override
	public FQuestSet createMember( String name) {
		FQuestSet result = QuestSetOfName.get( this, name);
		if (result == null) {
			result = new FQuestSet( this, name);
			addMember( result);
		}
		return result;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_SET_CAT;
	}
}