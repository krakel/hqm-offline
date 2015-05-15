package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FQuestSet extends AMember<FQuestSet> {
//	private static final Logger LOGGER = Logger.getLogger( FQuestSet.class.getName());
	public final FQuestSets mParentCategory;
	public final FParameterString mDesc = new FParameterString( this, "Description");

	public FQuestSet( FQuestSets parent, String name) {
		super( name);
		mParentCategory = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuestSet( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_SET;
	}

	@Override
	public ACategory<FQuestSet> getParent() {
		return mParentCategory;
	}
}
