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
	public final FParameterString mDesc = new FParameterString( this, "Description");
	public final FParameterInt mX = new FParameterInt( this, "X");
	public final FParameterInt mY = new FParameterInt( this, "Y");
	public final FParameterBoolean mBig = new FParameterBoolean( this, "Big");
	public final FParameterStack mIcon = new FParameterStack( this, "Icon");
	public final Vector<FQuest> mRequirements = new Vector<FQuest>();
	public final FParameterIntegerArr mOptionLinks = new FParameterIntegerArr( this, "OptionLinks");
	public final FParameterInt mTriggerTasks = new FParameterInt( this, "Trigger");
	public final FParameterEnum<TriggerType> mTriggerType = new FParameterEnum<TriggerType>( this, "TriggerType");
	public final FParameterBoolean mReqUseModified = new FParameterBoolean( this, "RequirementUseModified");
	public final FParameterInt mReqCount = new FParameterInt( this, "RequirementCount");
	public final Vector<FParameterStack> mRewards = new Vector<FParameterStack>();
	public final Vector<FParameterStack> mChoices = new Vector<FParameterStack>();
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

	public int getCenterX() {
		return mX.mValue + getW() / 2;
	}

	public int getCenterY() {
		return mY.mValue + getH() / 2;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST;
	}

	public int getH() {
		return mBig.mValue ? 37 : 30;
	}

	@Override
	public FHqm getParent() {
		return mParentHQM;
	}

	public FRepeatInfo getRepeatInfo() {
		return mRepeatInfo;
	}

	public int getW() {
		return mBig.mValue ? 31 : 25;
	}

	public boolean isDeleted() {
		return mDeleted;
	}
}
