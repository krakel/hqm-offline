package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FQuestSet extends AMember {
//	private static final Logger LOGGER = Logger.getLogger( FQuestSet.class.getName());
	public final FQuestSetCat mParentCategory;
	public String mDescr;

	public FQuestSet( FQuestSetCat parent, String name) {
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
	public ACategory<FQuestSet> getHierarchy() {
		return mParentCategory;
	}

	@Override
	public FQuestSetCat getParent() {
		return mParentCategory;
	}

	public boolean isFirst() {
		return ABase.isFirst( mParentCategory.mArr, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentCategory.mArr, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentCategory.mArr, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentCategory.mArr, this);
	}

	public void remove() {
		ABase.remove( mParentCategory.mArr, this);
	}
}
