package de.doerl.hqm.controller;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;

public class EditController implements IModelListener {
//	private static final Logger LOGGER = Logger.getLogger( EditController.class.getName());
	private EditModel mModel;
	private QuestRemoveDepent mRemoverDepent = new QuestRemoveDepent( this);
	private QuestDeleteFactory mRemoverQuest = new QuestDeleteFactory( this);

	public EditController( EditModel model) {
		model.addListener( this);
		mModel = model;
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

	void fireAdded( ABase base) {
		mModel.fireBaseAdded( base);
	}

	public void fireChanged( ABase base) {
		mModel.fireBaseChanged( base);
	}

	void fireRemoved( ABase base) {
		mModel.fireBaseRemoved( base);
	}

	public void questCreate( FQuestSet set, String name, int x, int y) {
		FQuest quest = set.mParentCategory.mParentHQM.createQuest( name);
		quest.mX.mValue = x;
		quest.mY.mValue = y;
		quest.mQuestSet = set;
		fireAdded( quest);
	}

	public void questDelete( FQuest quest) {
		questDependentDelete( quest);
		quest.remove();
		fireRemoved( quest);
	}

	public void questDependentDelete( FQuest quest) {
		quest.mParentHQM.forEachQuest( mRemoverDepent, quest);
	}

	public void questSetCreate( FQuestSetCat cat, String name) {
		fireAdded( cat.createMember( name));
	}

	public void questSetDelete( FQuestSet qs) {
		qs.mParentCategory.mParentHQM.forEachQuest( mRemoverQuest, qs);
		qs.remove();
		fireRemoved( qs);
	}

	public void removeListener( IModelListener l) {
		mModel.removeListener( l);
	}
}
