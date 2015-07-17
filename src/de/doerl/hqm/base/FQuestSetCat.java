package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
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
	public FQuestSet createMember() {
		FQuestSet set = new FQuestSet( this);
		addMember( set);
		return set;
	}

	@Override
	public Vector<FQuestSet> getArr() {
		return mArr;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_SET_CAT;
	}

	@Override
	public String getNodeName() {
		return "Quest Sets";
	}
}
