package de.doerl.hqm.view;

import de.doerl.hqm.base.FQuest;

public class QuestEntity extends AEntity<FQuest> {
	private FQuest mQuest;

	public QuestEntity( EditView view, FQuest quest) {
		super( view);
		mQuest = quest;
	}

	@Override
	public FQuest getBase() {
		return mQuest;
	}

	@Override
	public void update() {
	}
}
