package de.doerl.hqm.view;

import de.doerl.hqm.base.FQuestSet;

public class QuestSetEntity extends AEntity<FQuestSet> {
	private FQuestSet mQS;

	public QuestSetEntity( EditView view, FQuestSet qs) {
		super( view);
		mQS = qs;
	}

	@Override
	public FQuestSet getBase() {
		return mQS;
	}

	@Override
	public void update() {
	}
}
