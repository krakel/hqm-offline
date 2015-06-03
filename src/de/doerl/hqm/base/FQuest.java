package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

public final class FQuest extends ANamed {
	private static final Logger LOGGER = Logger.getLogger( FQuest.class.getName());
	public final FHqm mParentHQM;
	public String mDescr;
	public int mX, mY;
	public boolean mBig;
	public AStack mIcon;
	public int mTriggerTasks;
	public TriggerType mTriggerType = TriggerType.NONE;
	public Integer mReqCount;
	public Vector<FItemStack> mRewards = new Vector<>();
	public Vector<FItemStack> mChoices = new Vector<>();
	public Vector<FQuest> mRequirements = new Vector<>();
	public Vector<FQuest> mOptionLinks = new Vector<>();
	public Vector<FQuest> mPosts = new Vector<>();
	public Vector<FReward> Reputation = new Vector<>();
	public FQuestSet mQuestSet;
	public FRepeatInfo mRepeatInfo = new FRepeatInfo( this);
	private Vector<AQuestTask> mTasks = new Vector<>();
	private boolean mDeleted;

	public FQuest( FHqm parent, String name) {
		super( name);
		mParentHQM = parent;
	}

	public FQuest( FHqm parent, String name, boolean deleted) {
		super( name);
		mParentHQM = parent;
		mDeleted = deleted;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuest( this, p);
	}

	public AQuestTask createQuestTask( TaskTyp type, String name) {
		AQuestTask task = null;
		switch (type) {
			case TASK_ITEMS_CONSUME:
				task = new FQuestTaskItemsConsume( this, name);
				break;
			case TASK_ITEMS_CRAFTING:
				task = new FQuestTaskItemsCrafting( this, name);
				break;
			case TASK_LOCATION:
				task = new FQuestTaskLocation( this, name);
				break;
			case TASK_ITEMS_CONSUME_QDS:
				task = new FQuestTaskItemsConsumeQDS( this, name);
				break;
			case TASK_ITEMS_DETECT:
				task = new FQuestTaskItemsDetect( this, name);
				break;
			case TASK_MOB:
				task = new FQuestTaskMob( this, name);
				break;
			case TASK_DEATH:
				task = new FQuestTaskDeath( this, name);
				break;
			case TASK_REPUTATION_TARGET:
				task = new FQuestTaskReputationTarget( this, name);
				break;
			case TASK_REPUTATION_KILL:
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

	public <T, U> T forEachReward( IHQMWorker<T, U> worker, U p) {
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

	public <T, U> T forEachTask( IHQMWorker<T, U> worker, U p) {
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

	public int getCenterX() {
		return getX() + ResourceManager.getW( mBig) / 2;
	}

	public int getCenterY() {
		return getY() + ResourceManager.getH( mBig) / 2;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST;
	}

	@Override
	public FQuestSet getHierarchy() {
		return mQuestSet;
	}

	@Override
	public FHqm getHqm() {
		return mParentHQM;
	}

	@Override
	public FHqm getParent() {
		return mParentHQM;
	}

	public int getX() {
		if (mBig) {
			return mX;
		}
		else {
			return mX + 1;
		}
	}

	public int getY() {
		if (mBig) {
			return mY;
		}
		else {
			return mY + 1;
		}
	}

	public boolean isDeleted() {
		return mDeleted;
	}

	public boolean isFree() {
		return mRequirements.isEmpty();
	}

	public void remove() {
		mParentHQM.removeQuest( this);
	}

	public void removeTask( AQuestTask task) {
		int pos = mTasks.indexOf( task);
		if (pos >= 0) {
			mTasks.setElementAt( null, pos);
		}
	}
}
