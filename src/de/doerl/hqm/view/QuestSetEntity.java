package de.doerl.hqm.view;

import java.awt.Color;

import de.doerl.hqm.base.FQuestSet;

public class QuestSetEntity extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private FQuestSet mQS;

	public QuestSetEntity( EditView view, FQuestSet qs) {
		super( view);
		mQS = qs;
		setBackground( Color.MAGENTA);
	}

	@Override
	public FQuestSet getBase() {
		return mQS;
	}
}
