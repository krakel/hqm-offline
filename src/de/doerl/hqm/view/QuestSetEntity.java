package de.doerl.hqm.view;

import de.doerl.hqm.base.FQuestSets;

class QuestSetEntity extends AEntity<FQuestSets> {
	private FQuestSets mSet;

	public QuestSetEntity( EditView view, FQuestSets set) {
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
