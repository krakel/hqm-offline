package de.doerl.hqm.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import de.doerl.hqm.base.FQuestSets;

class QuestSetsEntity extends AEntity<FQuestSets> {
	private static final long serialVersionUID = -5930552368392528379L;
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
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		EditView.drawBackground( g2, this);
	}
}
