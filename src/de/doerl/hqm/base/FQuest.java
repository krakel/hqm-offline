package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOfQuest;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.ui.LinkType;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

public final class FQuest extends AIdent implements IElement {
	private static final Logger LOGGER = Logger.getLogger( FQuest.class.getName());
	private static final String BASE = "quest";
	public int mX, mY;
	public boolean mBig;
	public Integer mCount;
	public FItemStack mIcon;
	public TriggerType mTriggerType = TriggerType.NONE;
	public int mTriggerTasks;
	public final Vector<FItemStack> mRewards = new Vector<>();
	public final Vector<FItemStack> mChoices = new Vector<>();
	public final Vector<FQuest> mRequirements = new Vector<>();
	public final Vector<FQuest> mOptionLinks = new Vector<>();
	public final Vector<FQuest> mPosts = new Vector<>();
	public final Vector<FReputationReward> mRepRewards = new Vector<>();
	public final FRepeatInfo mRepeatInfo = new FRepeatInfo( this);
	final Vector<AQuestTask> mTasks = new Vector<>();
	private FQuestSet mParentQuestSet;
	private LinkType mInformation = LinkType.NORM;

	FQuest( FQuestSet parent) {
		super( BASE, MaxIdOfQuest.get( parent) + 1);
		mParentQuestSet = parent;
	}

	FQuest( FQuestSet parent, int id) {
		super( BASE, id);
		mParentQuestSet = parent;
	}

	public static int fromIdent( String ident) {
		return AIdent.fromIdent( BASE, ident);
	}

	public static String toIdent( int idx) {
		return AIdent.toIdent( BASE, idx);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuest( this, p);
	}

	public boolean containExt() {
		for (FQuest prev : mRequirements) {
			if (Utils.different( prev.mParentQuestSet, mParentQuestSet)) {
				return true;
			}
		}
		for (FQuest post : mPosts) {
			if (Utils.different( post.mParentQuestSet, mParentQuestSet)) {
				return true;
			}
		}
		return false;
	}

	public AQuestTask createQuestTask( TaskTyp type) {
		AQuestTask task = null;
		switch (type) {
			case TASK_ITEMS_CONSUME:
				task = new FQuestTaskItemsConsume( this);
				break;
			case TASK_ITEMS_CRAFTING:
				task = new FQuestTaskItemsCrafting( this);
				break;
			case TASK_LOCATION:
				task = new FQuestTaskLocation( this);
				break;
			case TASK_ITEMS_CONSUME_QDS:
				task = new FQuestTaskItemsConsumeQDS( this);
				break;
			case TASK_ITEMS_DETECT:
				task = new FQuestTaskItemsDetect( this);
				break;
			case TASK_MOB:
				task = new FQuestTaskMob( this);
				break;
			case TASK_DEATH:
				task = new FQuestTaskDeath( this);
				break;
			case TASK_REPUTATION_TARGET:
				task = new FQuestTaskReputationTarget( this);
				break;
			case TASK_REPUTATION_KILL:
				task = new FQuestTaskReputationKill( this);
				break;
		}
		mTasks.add( task);
		return task;
	}

	public FReputationReward createRepReward() {
		FReputationReward reward = new FReputationReward( this);
		mRepRewards.add( reward);
		return reward;
	}

	public <T, U> T forEachReward( IHQMWorker<T, U> worker, U p) {
		for (FReputationReward disp : mRepRewards) {
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
	public LinkType getInformation() {
		return mInformation;
	}

	@Override
	public FQuestSet getParent() {
		return mParentQuestSet;
	}

	public boolean isEnabled() {
		switch (mTriggerType) {
			case ANTI_TRIGGER:
				return false;
			case NONE:
				int allowed = mCount != null ? mRequirements.size() - mCount.intValue() : 0;
				if (allowed < 0) {
					return false;
				}
				for (FQuest req : mRequirements) {
					if (!req.isEnabled() && --allowed < 0) {
						return false;
					}
				}
			case QUEST_TRIGGER:
			case TASK_TRIGGER:
		}
		return true;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentQuestSet.mQuests, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentQuestSet.mQuests, this);
	}

	@Override
	void localeDefault( LocaleInfo info) {
		info.mInfo1 = "Unnamed Quest";
		info.mInfo2 = "No description";
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

	@Override
	public void setInformation( LinkType information) {
		mInformation = information;
	}
}
