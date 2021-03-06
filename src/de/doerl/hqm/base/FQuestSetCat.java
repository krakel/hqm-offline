package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.ANamed.LocaleInfo;
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
	public Vector<FQuestSet> asVector() {
		return new Vector<>( mArr);
	}

	@Override
	public FQuestSet createMember() {
		FQuestSet set = new FQuestSet( this);
		addMember( set);
		return set;
	}

	public FQuestSet createQuestSet( int id) {
		FQuestSet set = new FQuestSet( this, id);
		addMember( set);
		return set;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_SET_CAT;
	}

	@Override
	public String getNodeName() {
		return "Quest Sets";
	}

	void localeDefault( LocaleInfo info) {
	}
}
