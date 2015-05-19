package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.utils.Utils;

public final class FQuest extends ANamed {
	private static final Logger LOGGER = Logger.getLogger( FQuest.class.getName());
	public final FHqm mParentHQM;
	public final FParameterString mDesc = new FParameterString( this);
	public final FParameterInt mX = new FParameterInt( this);
	public final FParameterInt mY = new FParameterInt( this);
	public final FParameterBoolean mBig = new FParameterBoolean( this);
	public final FParameterStack mIcon = new FParameterStack( this);
	public final FParameterInt mTriggerTasks = new FParameterInt( this);
	public final FParameterEnum<TriggerType> mTriggerType = new FParameterEnum<TriggerType>( this);
	public final FParameterBoolean mReqUseModified = new FParameterBoolean( this);
	public final FParameterInt mReqCount = new FParameterInt( this);
	public final Vector<FParameterStack> mRewards = new Vector<FParameterStack>();
	public final Vector<FParameterStack> mChoices = new Vector<FParameterStack>();
	public final Vector<FQuest> mRequirements = new Vector<FQuest>();
	public final Vector<FQuest> mOptionLinks = new Vector<FQuest>();
	public FQuestSet mSet;
	private FRepeatInfo mRepeatInfo = new FRepeatInfo( this);
	private Vector<AQuestTask> mTasks = new Vector<AQuestTask>();
	private Vector<FReward> Reputation = new Vector<FReward>();
	private boolean mDeleted;

	public FQuest( FHqm parent, String name) {
		super( name);
		mParentHQM = parent;
		mTriggerType.mValue = TriggerType.NONE;
	}

	public FQuest( FHqm parent, String name, boolean deleted) {
		super( name);
		mParentHQM = parent;
		mTriggerType.mValue = TriggerType.NONE;
		mDeleted = deleted;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuest( this, p);
	}

	public AQuestTask createQuestTask( int type, String name) {
		AQuestTask task = null;
		switch (type) {
			case 0:
				task = new FQuestTaskItemsConsume( this, name);
				break;
			case 1:
				task = new FQuestTaskItemsCrafting( this, name);
				break;
			case 2:
				task = new FQuestTaskLocation( this, name);
				break;
			case 3:
				task = new FQuestTaskItemsConsumeQDS( this, name);
				break;
			case 4:
				task = new FQuestTaskItemsDetect( this, name);
				break;
			case 5:
				task = new FQuestTaskMob( this, name);
				break;
			case 6:
				task = new FQuestTaskDeath( this, name);
				break;
			case 7:
				task = new FQuestTaskReputationTarget( this, name);
				break;
			case 8:
				task = new FQuestTaskReputationKill( this, name);
				break;
		}
		mTasks.add( task);
		return task;
	}

	public FReward createReputationReward() {
		FReward reward = new FReward( this);
		Reputation.add( reward);
		return reward;
	}

	public <T, U> T forEachQuestTask( IHQMWorker<T, U> worker, U p) {
		for (AQuestTask disp : mTasks) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	public <T, U> T forEachReputationReward( IHQMWorker<T, U> worker, U p) {
		for (FReward disp : Reputation) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST;
	}

	@Override
	public FHqm getParent() {
		return mParentHQM;
	}

	public FRepeatInfo getRepeatInfo() {
		return mRepeatInfo;
	}

	public boolean isDeleted() {
		return mDeleted;
	}

	public void remove() {
		mParentHQM.removeQuest( this);
	}
}
