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

public final class FQuest extends ANamed implements IElement {
	private static final Logger LOGGER = Logger.getLogger( FQuest.class.getName());
	private FQuestSet mParentQuestSet;
	public String mDescr;
	public int mX, mY;
	public boolean mBig;
	public FItemStack mIcon;
	public int mTriggerTasks;
	public TriggerType mTriggerType = TriggerType.NONE;
	public Integer mReqCount;
	public final Vector<FItemStack> mRewards = new Vector<>();
	public final Vector<FItemStack> mChoices = new Vector<>();
	public final Vector<FQuest> mRequirements = new Vector<>();
	public final Vector<FQuest> mOptionLinks = new Vector<>();
	public final Vector<FQuest> mPosts = new Vector<>();
	public final Vector<FReward> Reputation = new Vector<>();
	public FRepeatInfo mRepeatInfo = new FRepeatInfo( this);
	final Vector<AQuestTask> mTasks = new Vector<>();

	public FQuest( FQuestSet parent, String name) {
		super( name);
		mParentQuestSet = parent;
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

	public void delete() {
		moveTo( mParentQuestSet.mParentCategory.mDelSet);
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
		return mX + ResourceManager.getW5( mBig);
	}

	public int getCenterY() {
		return mY + ResourceManager.getH5( mBig);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST;
	}

	@Override
	public FHqm getHqm() {
		return mParentQuestSet.getHqm();
	}

	@Override
	public FQuestSet getParent() {
		return mParentQuestSet;
	}

	public boolean isDeleted() {
		return mParentQuestSet.isDeleted();
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentQuestSet.mQuests, this);
	}

	public boolean isFree() {
		return mRequirements.isEmpty();
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentQuestSet.mQuests, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentQuestSet.mQuests, this);
	}

	public void moveTo( FQuestSet set) {
		remove();
		mParentQuestSet = set;
		set.add( this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentQuestSet.mQuests, this);
	}

	public void remove() {
		ABase.remove( mParentQuestSet.mQuests, this);
	}
}
