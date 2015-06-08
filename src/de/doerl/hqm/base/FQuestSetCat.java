package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.QuestSetOfName;
import de.doerl.hqm.quest.ElementTyp;

public final class FQuestSetCat extends ACategory<FQuestSet> {
	final FQuestSet mDelSet = new FQuestSet( this);

	FQuestSetCat( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuestSetCat( this, p);
	}

	public FQuest addDeletedQuest() {
		FQuest del = mDelSet.createQuest( "__DELETED__");
		mDelSet.mQuests.add( del);
		return del;
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
