package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
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
import de.doerl.hqm.base.dispatch.IndexOfQuest;
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.base.dispatch.SizeOfQuests;
import de.doerl.hqm.medium.IHqmWriter;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.TriggerType;

class Serializer extends AHQMWorker<Object, FileVersion> implements IHqmWriter {
//	private static final Logger LOGGER = Logger.getLogger( Serializer.class.getName());
	private QuestWorker mQuestWorker = new QuestWorker();
	private BitOutputStream mDst;

	public Serializer( OutputStream out) throws IOException {
		mDst = new BitOutputStream( out);
	}

	@Override
	protected Object doTask( AQuestTask task, FileVersion version) {
		mDst.writeData( task.getTaskTyp().ordinal(), DataBitHelper.TASK_TYPE, version);
		mDst.writeString( task.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeString( task.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		return null;
	}

	@Override
	protected Object doTaskItems( AQuestTaskItems task, FileVersion version) {
		doTask( task, version);
		mDst.writeData( SizeOf.getRequirements( task), DataBitHelper.TASK_ITEM_COUNT);
		task.forEachRequirement( this, version);
		return null;
	}

	public void flush() throws IOException {
		mDst.flush();
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, FileVersion version) {
		mDst.writeBoolean( false);
		mDst.writeFluidStack( fluid.mStack);
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, FileVersion version) {
		if (version.contains( FileVersion.BAG_LIMITS)) {
			mDst.writeData( IndexOf.getMember( grp), DataBitHelper.GROUP_COUNT);
		}
		mDst.writeString( grp.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeData( IndexOf.getMember( grp.mTier), DataBitHelper.TIER_COUNT);
		writeStacks( grp.mStacks, DataBitHelper.GROUP_ITEMS, version);
		if (version.contains( FileVersion.BAG_LIMITS)) {
			if (grp.mLimit == null) {
				mDst.writeBoolean( false);
			}
			else {
				mDst.writeBoolean( true);
				mDst.writeData( grp.mLimit, DataBitHelper.LIMIT);
			}
		}
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, FileVersion version) {
		mDst.writeString( tier.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeData( tier.mColorID, DataBitHelper.COLOR);
		int[] weights = tier.mWeights;
		for (int i = 0; i < weights.length; ++i) {
			mDst.writeData( weights[i], DataBitHelper.WEIGHT);
		}
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, FileVersion version) {
		mDst.writeBoolean( true);
		mDst.writeItemStack( item.mStack, version);
		mDst.writeData( item.mRequired, DataBitHelper.TASK_REQUIREMENT);
		mDst.writeData( item.mPrecision.ordinal(), DataBitHelper.ITEM_PRECISION);
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, FileVersion version) {
		mDst.writeIconIf( loc.mIcon, version);
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
	public Object forMarker( FMarker mark, FileVersion version) {
		mDst.writeString( mark.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeData( mark.mMark, DataBitHelper.REPUTATION_VALUE);
		return null;
	}

	@Override
	public Object forMob( FMob mob, FileVersion version) {
		mDst.writeIconIf( mob.mIcon, version);
		mDst.writeString( mob.mName, DataBitHelper.NAME_LENGTH);
		mDst.writeString( mob.mMob, DataBitHelper.MOB_ID_LENGTH);
		mDst.writeData( mob.mKills, DataBitHelper.KILL_COUNT);
		mDst.writeBoolean( mob.mExact);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, FileVersion version) {
		mDst.writeString( set.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeString( set.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, FileVersion version) {
		mDst.writeData( info.mType.ordinal(), DataBitHelper.REPEAT_TYPE);
		if (info.mType.isUseTime()) {
			mDst.writeData( info.mTotal, DataBitHelper.HOURS);
		}
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, FileVersion version) {
		mDst.writeData( IndexOf.getMember( rep), DataBitHelper.REPUTATION);
		mDst.writeString( rep.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeString( rep.mNeutral, DataBitHelper.QUEST_NAME_LENGTH);
		writeMarker( rep, version);
		return null;
	}

	@Override
	public Object forReward( FReward rr, FileVersion version) {
		mDst.writeData( IndexOf.getMember( rr.mRep), DataBitHelper.REPUTATION);
		mDst.writeData( rr.mValue, DataBitHelper.REPUTATION_VALUE);
		return null;
	}

	@Override
	public Object forSetting( FSetting set, FileVersion version) {
		mDst.writeData( IndexOf.getMember( set.mRep), DataBitHelper.REPUTATION);
		writeMarkerIf( set.mLower);
		writeMarkerIf( set.mUpper);
		mDst.writeBoolean( set.mInverted);
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, FileVersion version) {
		doTask( task, version);
		mDst.writeData( task.mDeaths, DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, FileVersion version) {
		doTask( task, version);
		mDst.writeData( SizeOf.getLocations( task), DataBitHelper.TASK_LOCATION_COUNT);
		task.forEachLocation( this, version);
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, FileVersion version) {
		doTask( task, version);
		mDst.writeData( SizeOf.getMobs( task), DataBitHelper.TASK_MOB_COUNT);
		task.forEachMob( this, version);
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, FileVersion version) {
		doTask( task, version);
		mDst.writeData( task.mKills, DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, FileVersion version) {
		doTask( task, version);
		mDst.writeData( SizeOf.getSettings( task), DataBitHelper.REPUTATION_SETTING);
		task.forEachSetting( this, version);
		return null;
	}

	private boolean isEmpty( Vector<?> lst) {
		int size = lst.size();
		for (int i = 0; i < size; ++i) {
			if (lst.get( i) != null) {
				return false;
			}
		}
		return true;
	}

	private int sizeOf( Vector<?> lst) {
		int res = 0;
		int size = lst.size();
		for (int i = 0; i < size; ++i) {
			if (lst.get( i) != null) {
				++res;
			}
		}
		return res;
	}

	@Override
	public void writeDst( FHqm hqm) {
		FileVersion version = hqm.getVersion();
		mDst.writeData( version.ordinal(), DataBitHelper.BYTE);
		if (version.contains( FileVersion.LOCK)) {
			mDst.writeString( hqm.mPassCode, DataBitHelper.PASS_CODE);
		}
		if (version.contains( FileVersion.LORE)) {
			mDst.writeString( hqm.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		}
		if (version.contains( FileVersion.SETS)) {
			writeQuestSetCat( hqm.mQuestSetCat, version);
		}
		if (version.contains( FileVersion.REPUTATION)) {
			writeReputations( hqm.mReputationCat, version);
		}
		writeQuests( hqm.mQuestSetCat, version);
		if (version.contains( FileVersion.BAGS)) {
			writeGroupTiers( hqm.mGroupTierCat, version);
			writeGroup( hqm.mGroupCat, version);
		}
	}

	private void writeGroup( FGroupCat cat, FileVersion version) {
		mDst.writeData( SizeOf.getMember( cat), DataBitHelper.GROUP_COUNT);
		cat.forEachMember( this, version);
	}

	private void writeGroupTiers( FGroupTierCat cat, FileVersion version) {
		mDst.writeData( SizeOf.getMember( cat), DataBitHelper.TIER_COUNT);
		cat.forEachMember( this, version);
	}

	private void writeIds( Vector<FQuest> lst, DataBitHelper bits, FileVersion version) {
		if (lst == null || isEmpty( lst)) {
			mDst.writeBoolean( false);
		}
		else {
			mDst.writeBoolean( true);
			mDst.writeData( sizeOf( lst), bits, version);
			int size = lst.size();
			for (int i = 0; i < size; ++i) {
				FQuest quest = lst.get( i);
				if (quest != null) {
					mDst.writeData( IndexOfQuest.get( quest), bits, version);
				}
			}
		}
	}

	private void writeMarker( FReputation rep, FileVersion version) {
		mDst.writeData( SizeOf.getMarker( rep), DataBitHelper.REPUTATION_MARKER);
		rep.forEachMarker( this, version);
	}

	private void writeMarkerIf( FMarker mark) {
		if (mark == null) {
			mDst.writeBoolean( false);
		}
		else {
			mDst.writeBoolean( true);
			mDst.writeData( IndexOf.getMarker( mark), DataBitHelper.REPUTATION_MARKER);
		}
	}

	private void writeQuest( FQuest quest, FileVersion version) {
		mDst.writeBoolean( true);
		mDst.writeString( quest.mName, DataBitHelper.QUEST_NAME_LENGTH);
		mDst.writeString( quest.mDescr, DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		mDst.writeData( quest.mX, DataBitHelper.QUEST_POS_X);
		mDst.writeData( quest.mY, DataBitHelper.QUEST_POS_Y);
		mDst.writeBoolean( quest.mBig);
		if (version.contains( FileVersion.SETS)) {
			mDst.writeData( IndexOf.getMember( quest.getParent()), DataBitHelper.QUEST_SETS);
			mDst.writeIconIf( quest.mIcon, version);
		}
		writeIds( quest.mRequirements, DataBitHelper.QUESTS, version);
		if (version.contains( FileVersion.OPTION_LINKS)) {
			writeIds( quest.mOptionLinks, DataBitHelper.QUESTS, version);
		}
		if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
			quest.mRepeatInfo.accept( this, version);
		}
		if (version.contains( FileVersion.TRIGGER_QUESTS)) {
			TriggerType type = quest.mTriggerType;
			mDst.writeData( type.ordinal(), DataBitHelper.TRIGGER_TYPE);
			if (type.isUseTaskCount()) {
				mDst.writeData( quest.mTriggerTasks, DataBitHelper.TASKS);
			}
		}
		if (version.contains( FileVersion.PARENT_COUNT)) {
			if (quest.mReqCount == null) {
				mDst.writeBoolean( false);
			}
			else {
				mDst.writeBoolean( true);
				mDst.writeData( quest.mReqCount, DataBitHelper.QUESTS, version);
			}
		}
		writeTasks( quest, version);
		writeStacksIf( quest.mRewards, version);
		writeStacksIf( quest.mChoices, version);
		if (version.contains( FileVersion.REPUTATION)) {
			writeRewards( quest, version);
		}
	}

	private void writeQuests( FQuestSetCat cat, FileVersion version) {
		mDst.writeData( SizeOfQuests.get( cat), DataBitHelper.QUESTS, version);
		cat.forEachMember( mQuestWorker, version);
	}

	private void writeQuestSetCat( FQuestSetCat cat, FileVersion version) {
		mDst.writeData( SizeOf.getMember( cat), DataBitHelper.QUEST_SETS);
		cat.forEachMember( this, version);
	}

	private void writeReputations( FReputationCat cat, FileVersion version) {
		mDst.writeData( SizeOf.getMember( cat), DataBitHelper.REPUTATION);
		cat.forEachMember( this, version);
	}

	private void writeRewards( FQuest quest, FileVersion version) {
		mDst.writeData( SizeOf.getReward( quest), DataBitHelper.REPUTATION_REWARD);
		quest.forEachReward( this, version);
	}

	private void writeStacks( Vector<FItemStack> lst, DataBitHelper bits, FileVersion version) {
		mDst.writeData( sizeOf( lst), bits);
		int size = lst.size();
		for (int i = 0; i < size; ++i) {
			FItemStack stk = lst.get( i);
			if (stk != null) {
				mDst.writeItemStackFix( stk, version);
			}
		}
	}

	private void writeStacksIf( Vector<FItemStack> lst, FileVersion version) {
		if (lst == null || isEmpty( lst)) {
			mDst.writeBoolean( false);
		}
		else {
			mDst.writeBoolean( true);
			writeStacks( lst, DataBitHelper.REWARDS, version);
		}
	}

	private void writeTasks( FQuest quest, FileVersion version) {
		mDst.writeData( SizeOf.getTasks( quest), DataBitHelper.TASKS);
		quest.forEachTask( this, version);
	}

	private final class QuestWorker extends AHQMWorker<Object, FileVersion> {
		@Override
		public Object forQuest( FQuest quest, FileVersion version) {
			writeQuest( quest, version);
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, FileVersion version) {
			set.forEachQuest( this, version);
			return null;
		}
	}
}
