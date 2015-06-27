package de.doerl.hqm.controller;

import java.awt.Frame;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.quest.TaskTyp;

public class EditController {
	//	private static final Logger LOGGER = Logger.getLogger( EditController.class.getName());
	private EditModel mModel;
	private Frame mFrame;

	public EditController( EditModel model, Frame frame) {
		mModel = model;
		mFrame = frame;
	}

	public void addListener( IModelListener l) {
		mModel.addListener( l);
	}

	public void fireActive( ABase base) {
		mModel.fireBaseActivate( base);
	}

	public void fireAdded( ABase base) {
		base.getHqm().setModified( true);
		mModel.fireBaseAdded( base);
	}

	public void fireChanged( ABase base) {
		base.getHqm().setModified( true);
		mModel.fireBaseChanged( base);
	}

	public void fireModified( ABase base) {
		mModel.fireBaseModified( base);
	}

	public void fireRemoved( ABase base) {
		mModel.fireBaseRemoved( base);
	}

	public Frame getFrame() {
		return mFrame;
	}

	public void groupCreate( FGroupTier tier, String name) {
		FGroup grp = tier.createGroup( name);
		fireAdded( grp);
	}

	public void groupDelete( FGroup grp) {
		GroupDelete.get( grp, this);
		fireRemoved( grp);
	}

	public void grpTierDelete( FGroupTier tier) {
		tier.remove();
		fireRemoved( tier);
	}

	public <E extends AMember> void memberCreate( ACategory<E> cat, String name) {
		E member = cat.createMember( name);
		fireAdded( member);
	}

	public FQuest questCreate( FQuestSet set, String name, int x, int y) {
		FQuest quest = set.createQuest( name);
		quest.mX = x;
		quest.mY = y;
		return quest;
	}

	public void questDelete( FQuest quest) {
		QuestRemoveDepent.get( quest, this);
		fireRemoved( quest);
		quest.remove();
	}

	public void questMoveTo( FQuest quest, FQuestSet set) {
		fireRemoved( quest);
		quest.moveTo( set);
		fireAdded( quest);
	}

	public void questSetDelete( FQuestSet set) {
		QuestSetDelete.get( set, this);
		fireRemoved( set);
	}

	public void questTaskCreate( FQuest quest, TaskTyp type, String name) {
		AQuestTask task = quest.createQuestTask( type, name);
		fireAdded( task);
	}

	public void questTaskDelete( AQuestTask task) {
		task.remove();
		fireRemoved( task);
	}

	public void removeListener( IModelListener l) {
		mModel.removeListener( l);
	}

	public void reputationDelete( FReputation rep) {
		ReputationDelete.get( rep, this);
		fireRemoved( rep);
	}
}
