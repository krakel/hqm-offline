package de.doerl.hqm.view;

import de.doerl.hqm.base.FQuestSets;

class QuestSetsEntity extends AEntity<FQuestSets> {
	private FQuestSets mSet;

	public QuestSetsEntity( EditView view, FQuestSets set) {
		super( view);
		mSet = set;
	}

	@Override
	public FQuestSets getBase() {
		return mSet;
	}

	@Override
	public void update() {
	}
}
