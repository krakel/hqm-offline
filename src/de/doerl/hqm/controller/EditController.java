package de.doerl.hqm.controller;

import java.awt.Frame;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.TaskTyp;

public class EditController implements IModelListener {
//	private static final Logger LOGGER = Logger.getLogger( EditController.class.getName());
	private EditModel mModel;
	private Frame mFrame;
	private QuestRemoveDepent mRemoverDepent = new QuestRemoveDepent( this);
	private QuestDeleteFactory mRemoverQuest = new QuestDeleteFactory( this);

	public EditController( EditModel model, Frame frame) {
		mModel = model;
		mFrame = frame;
		model.addListener( this);
	}

	public void addListener( IModelListener l) {
		mModel.addListener( l);
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
	}

	@Override
	public void baseChanged( ModelEvent event) {
	}

	@Override
	public void baseRemoved( ModelEvent event) {
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

	public void fireRemoved( ABase base) {
		mModel.fireBaseRemoved( base);
//		mModel.fireBaseActivate( base.getParent());
	}

	public Frame getFrame() {
		return mFrame;
	}

	public FQuest questCreate( FQuestSet set, String name, int x, int y) {
		FQuest quest = set.mParentCategory.mParentHQM.createQuest( name);
		quest.mX = x;
		quest.mY = y;
		quest.mQuestSet = set;
		return quest;
	}

	public void questDelete( FQuest quest) {
		questDependentDelete( quest);
		quest.remove();
	}

	public void questDependentDelete( FQuest quest) {
		quest.mParentHQM.forEachQuest( mRemoverDepent, quest);
	}

	public FQuestSet questSetCreate( FQuestSetCat cat, String name) {
		return cat.createMember( name);
	}

	public void questSetDelete( FQuestSet qs) {
		qs.mParentCategory.mParentHQM.forEachQuest( mRemoverQuest, qs);
		qs.remove();
	}

	public AQuestTask questTaskCreate( FQuest quest, TaskTyp type, String name) {
		return quest.createQuestTask( type, name);
	}

	public void questTaskDelete( AQuestTask task) {
		task.remove();
	}

	public void removeListener( IModelListener l) {
		mModel.removeListener( l);
	}
}
