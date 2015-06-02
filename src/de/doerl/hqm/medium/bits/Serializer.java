package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupCat;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FRepeatInfo;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IHqmWriter;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.TriggerType;

class Serializer extends AHQMWorker<Object, Object> implements IHqmWriter {
	private BitOutputStream mDst;

	public Serializer( OutputStream out) throws IOException {
		mDst = new BitOutputStream( out);
	}

	@Override
	public void closeDst() {
	}

	protected Object doTask( AQuestTask task) {
		mDst.writeData( task.getTaskTyp().ordinal(), DataBitHelper.TASK_TYPE);
		mDst.writeString( task.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeString( task.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		return null;
	}

	@Override
	protected Object doTaskItems( AQuestTaskItems task, Object p) {
		doTask( task);
		mDst.writeData( SizeOf.getRequirements( task), DataBitHelper.TASK_ITEM_COUNT);
		task.forEachRequirement( this, null);
		return null;
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, Object p) {
		mDst.writeBoolean( false);
		mDst.writeFluidStack( fluid.mStack);
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, Object p) {
		if (mDst.contains( FileVersion.BAG_LIMITS)) {
			mDst.writeData( IndexOf.getGroup( grp), DataBitHelper.GROUP_COUNT);
		}
		mDst.writeString( grp.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeData( IndexOf.getMember( grp.mTier), DataBitHelper.TIER_COUNT);
		writeStacks( grp.mStacks, DataBitHelper.GROUP_ITEMS);
		if (mDst.contains( FileVersion.BAG_LIMITS)) {
			if (grp.mLimit != null) {
				mDst.writeBoolean( true);
				mDst.writeData( grp.mLimit, DataBitHelper.LIMIT);
			}
			else {
				mDst.writeBoolean( false);
			}
		}
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, Object p) {
		mDst.writeString( tier.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeData( tier.mColorID, DataBitHelper.COLOR);
		int[] weights = tier.mWeights;
		for (int i = 0; i < weights.length; ++i) {
			mDst.writeData( weights[i], DataBitHelper.WEIGHT);
		}
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, Object p) {
		mDst.writeBoolean( true);
		mDst.writeItemStack( item.mStack);
		mDst.writeData( item.mRequired, DataBitHelper.TASK_REQUIREMENT);
		mDst.writeData( item.mPrecision.ordinal(), DataBitHelper.ITEM_PRECISION);
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, Object p) {
		mDst.writeIconIf( loc.mIcon);
		mDst.writeString( loc.mName, DataBitHelper.NAME_LENGTH);
		mDst.writeData( loc.mX, DataBitHelper.WORLD_COORDINATE);
		mDst.writeData( loc.mY, DataBitHelper.WORLD_COORDINATE);
		mDst.writeData( loc.mZ, DataBitHelper.WORLD_COORDINATE);
		mDst.writeData( loc.mRadius, DataBitHelper.WORLD_COORDINATE);
		mDst.writeData( loc.mVisibility.ordinal(), DataBitHelper.LOCATION_VISIBILITY);
		mDst.writeData( loc.mDim, DataBitHelper.WORLD_COORDINATE);
		return null;
	}

	@Override
	public Object forMarker( FMarker mark, Object p) {
		mDst.writeString( mark.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeData( mark.mMark, DataBitHelper.REPUTATION_VALUE);
		return null;
	}

	@Override
	public Object forMob( FMob mob, Object p) {
		mDst.writeIconIf( mob.mIcon);
		mDst.writeString( mob.mName, DataBitHelper.NAME_LENGTH);
		mDst.writeString( mob.mMob, DataBitHelper.MOB_ID_LENGTH);
		mDst.writeData( mob.mKills, DataBitHelper.KILL_COUNT);
		mDst.writeBoolean( mob.mExact);
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		if (quest.isDeleted()) {
			mDst.writeBoolean( false);
		}
		else {
			mDst.writeBoolean( true);
			mDst.writeString( quest.mName, DataBitHelper.QUEST_NAME_LENGTH);
			mDst.writeString( quest.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
			mDst.writeData( quest.mX, DataBitHelper.QUEST_POS_X);
			mDst.writeData( quest.mY, DataBitHelper.QUEST_POS_Y);
			mDst.writeBoolean( quest.mBig);
			if (mDst.contains( FileVersion.SETS)) {
				mDst.writeData( IndexOf.getMember( quest.mQuestSet), DataBitHelper.QUEST_SETS);
				mDst.writeIconIf( quest.mIcon);
			}
			writeIds( quest.mRequirements, DataBitHelper.QUESTS);
			if (mDst.contains( FileVersion.OPTION_LINKS)) {
				writeIds( quest.mOptionLinks, DataBitHelper.QUESTS);
			}
			if (mDst.contains( FileVersion.REPEATABLE_QUESTS)) {
				quest.mRepeatInfo.accept( this, null);
			}
			if (mDst.contains( FileVersion.TRIGGER_QUESTS)) {
				TriggerType type = quest.mTriggerType;
				mDst.writeData( type.ordinal(), DataBitHelper.TRIGGER_TYPE);
				if (type.isUseTaskCount()) {
					mDst.writeData( quest.mTriggerTasks, DataBitHelper.TASKS);
				}
			}
			if (mDst.contains( FileVersion.PARENT_COUNT)) {
				if (quest.mReqCount != null) {
					mDst.writeBoolean( true);
					mDst.writeData( quest.mReqCount, DataBitHelper.QUESTS);
				}
				else {
					mDst.writeBoolean( false);
				}
			}
			writeTasks( quest);
			writeStacksIf( quest.mRewards, DataBitHelper.REWARDS);
			writeStacksIf( quest.mChoices, DataBitHelper.REWARDS);
			writeRewards( quest);
		}
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		mDst.writeString( set.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeString( set.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, Object p) {
		mDst.writeData( info.mType.ordinal(), DataBitHelper.REPEAT_TYPE);
		if (info.mType.isUseTime()) {
			mDst.writeData( info.mTotal, DataBitHelper.HOURS);
		}
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, Object p) {
		mDst.writeData( IndexOf.getReputation( rep), DataBitHelper.REPUTATION);
		mDst.writeString( rep.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeString( rep.mNeutral, DataBitHelper.QUEST_NAME_LENGTH);
		writeMarker( rep);
		return null;
	}

	@Override
	public Object forReward( FReward rr, Object p) {
		mDst.writeData( IndexOf.getReputation( rr.mRep), DataBitHelper.REPUTATION);
		mDst.writeData( rr.mValue, DataBitHelper.REPUTATION_VALUE);
		return null;
	}

	@Override
	public Object forSetting( FSetting set, Object p) {
		mDst.writeData( IndexOf.getReputation( set.mRep), DataBitHelper.REPUTATION);
		writeMarkerIf( set.mLower);
		writeMarkerIf( set.mUpper);
		mDst.writeBoolean( set.mInverted);
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, Object p) {
		doTask( task);
		mDst.writeData( task.mDeaths, DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, Object p) {
		doTask( task);
		mDst.writeData( SizeOf.getLocations( task), DataBitHelper.TASK_LOCATION_COUNT);
		task.forEachLocation( this, null);
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, Object p) {
		doTask( task);
		mDst.writeData( SizeOf.getMobs( task), DataBitHelper.TASK_MOB_COUNT);
		task.forEachMob( this, null);
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, Object p) {
		doTask( task);
		mDst.writeData( task.mKills, DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
		doTask( task);
		mDst.writeData( SizeOf.getSettings( task), DataBitHelper.REPUTATION_SETTING);
		task.forEachSetting( this, null);
		return null;
	}

	@Override
	public void writeDst( FHqm hqm, ICallback cb) {
		mDst.setVersion( hqm.getVersion());
		if (mDst.contains( FileVersion.LOCK)) {
			mDst.writeString( hqm.mPassCode, DataBitHelper.PASS_CODE);
		}
		if (mDst.contains( FileVersion.LORE)) {
			mDst.writeString( hqm.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		}
		writeQuestSetCat( hqm.mQuestSetCat);
		writeReputations( hqm.mReputationCat);
		writeQuests( hqm);
		writeGroupTiers( hqm.mGroupTierCat);
		writeGroup( hqm.mGroupCat);
	}

	private void writeGroup( FGroupCat cat) {
		if (mDst.contains( FileVersion.BAGS)) {
			mDst.writeData( SizeOf.getCategories( cat), DataBitHelper.GROUP_COUNT);
			cat.forEachMember( this, null);
		}
	}

	private void writeGroupTiers( FGroupTierCat cat) {
		if (mDst.contains( FileVersion.BAGS)) {
			mDst.writeData( SizeOf.getCategories( cat), DataBitHelper.TIER_COUNT);
			cat.forEachMember( this, null);
		}
	}

	private void writeIds( Vector<FQuest> lst, DataBitHelper bits) {
		if (lst != null) {
			mDst.writeBoolean( true);
			mDst.writeData( lst.size(), bits);
			for (FQuest quest : lst) {
				if (quest != null) {
					mDst.writeData( IndexOf.getQuest( quest), bits);
				}
			}
		}
		else {
			mDst.writeBoolean( false);
		}
	}

	private void writeMarker( FReputation rep) {
		mDst.writeData( SizeOf.getMarker( rep), DataBitHelper.REPUTATION_MARKER);
		rep.forEachMarker( this, null);
	}

	private void writeMarkerIf( FMarker mark) {
		if (mark != null) {
			mDst.writeBoolean( true);
			mDst.writeData( IndexOf.getMarker( mark), DataBitHelper.REPUTATION_MARKER);
		}
		else {
			mDst.writeBoolean( false);
		}
	}

	private void writeQuests( FHqm hqm) {
		mDst.writeData( SizeOf.getQuests( hqm), DataBitHelper.QUESTS);
		hqm.forEachQuest( this, null);
	}

	private void writeQuestSetCat( FQuestSetCat cat) {
		if (mDst.contains( FileVersion.SETS)) {
			mDst.writeData( SizeOf.getCategories( cat), DataBitHelper.QUEST_SETS);
			cat.forEachMember( this, null);
		}
	}

	private void writeReputations( FReputationCat cat) {
		if (mDst.contains( FileVersion.REPUTATION)) {
			mDst.writeData( SizeOf.getCategories( cat), DataBitHelper.REPUTATION);
			cat.forEachMember( this, null);
		}
	}

	private void writeRewards( FQuest quest) {
		if (mDst.contains( FileVersion.REPUTATION)) {
			mDst.writeData( SizeOf.getReward( quest), DataBitHelper.REPUTATION_REWARD);
			quest.forEachReward( this, null);
		}
	}

	private void writeStacks( Vector<FItemStack> lst, DataBitHelper bits) {
		mDst.writeData( lst.size(), bits);
		for (AStack stk : lst) {
			mDst.writeItemStackFix( stk);
		}
	}

	private void writeStacksIf( Vector<FItemStack> lst, DataBitHelper bits) {
		if (lst != null) {
			mDst.writeBoolean( true);
			writeStacks( lst, DataBitHelper.REWARDS);
		}
		else {
			mDst.writeBoolean( false);
		}
	}

	private void writeTasks( FQuest quest) {
		mDst.writeData( SizeOf.getTasks( quest), DataBitHelper.TASKS);
		quest.forEachTask( this, null);
	}
}
