package de.doerl.hqm.view;

import java.awt.Color;

import de.doerl.hqm.base.FQuest;

public class QuestEntity extends AEntity<FQuest> {
	private static final long serialVersionUID = -5707664232506407627L;
	private FQuest mQuest;

	public QuestEntity( EditView view, FQuest quest) {
		super( view);
		mQuest = quest;
		setBackground( Color.ORANGE);
	}

	@Override
	public FQuest getBase() {
		return mQuest;
	}
}
