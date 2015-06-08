package de.doerl.hqm.medium.json;

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
import de.doerl.hqm.base.FQuestTaskItemsConsume;
import de.doerl.hqm.base.FQuestTaskItemsConsumeQDS;
import de.doerl.hqm.base.FQuestTaskItemsCrafting;
import de.doerl.hqm.base.FQuestTaskItemsDetect;
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
import de.doerl.hqm.medium.IHqmWriter;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.json.JsonWriter;

class Serializer extends AHQMWorker<Object, FileVersion> implements IHqmWriter, IToken {
	private QuestWorker mQuestWorker = new QuestWorker();
	private JsonWriter mDst;

	public Serializer( OutputStream os) throws IOException {
		mDst = new JsonWriter( os);
	}

	private void doTask( AQuestTask task) {
		mDst.print( TASK_TYPE, task.getTaskTyp());
		mDst.print( TASK_NAME, task.mName);
		mDst.print( TASK_DESC, task.mDescr);
	}

	private void doTaskItem( AQuestTaskItems task, FileVersion version) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_REQUIREMENTS);
		task.forEachRequirement( this, version);
		mDst.endArray();
		mDst.endObject();
	}

	public void flushDst() {
		mDst.flush();
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, FileVersion version) {
		mDst.beginObject();
		mDst.print( REQUIREMENT_FLUID, fluid.getStack());
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, FileVersion version) {
		mDst.beginObject();
		mDst.print( GROUP_ID, IndexOf.getMember( grp));
		mDst.print( GROUP_NAME, grp.mName);
		mDst.print( GROUP_TIER, toID( IndexOf.getMember( grp.mTier), grp.mTier.mName));
		if (version.contains( FileVersion.BAG_LIMITS)) {
			mDst.print( GROUP_LIMIT, grp.mLimit);
		}
		writeStackArr( grp.mStacks, GROUP_STACKS);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, FileVersion version) {
		mDst.beginObject();
		mDst.print( GROUP_TIER_ID, IndexOf.getMember( tier));
		mDst.print( GROUP_TIER_NAME, tier.mName);
		mDst.print( GROUP_TIER_COLOR, tier.mColorID);
		mDst.printArr( GROUP_TIER_WEIGHTS, tier.mWeights);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, FileVersion version) {
		mDst.beginObject();
		mDst.print( REQUIREMENT_ITEM, item.getStack());
		mDst.print( REQUIREMENT_REQUIRED, item.mRequired);
		mDst.print( REQUIREMENT_PRECISION, item.mPrecision);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, FileVersion version) {
		mDst.beginObject();
		mDst.print( LOCATION_NAME, loc.mName);
		mDst.print( LOCATION_ICON, loc.mIcon);
		mDst.print( LOCATION_X, loc.mX);
		mDst.print( LOCATION_Y, loc.mY);
		mDst.print( LOCATION_Z, loc.mZ);
		mDst.print( LOCATION_RADIUS, loc.mRadius);
		mDst.print( LOCATION_VISIBLE, loc.mVisibility);
		mDst.print( LOCATION_DIM, loc.mDim);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMarker( FMarker mark, FileVersion version) {
		mDst.beginObject();
		mDst.print( MARKER_NAME, mark.mName);
		mDst.print( MARKER_VALUE, mark.mMark);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMob( FMob mob, FileVersion version) {
		mDst.beginObject();
		mDst.print( MOB_NAME, mob.mName);
		mDst.print( MOB_ICON, mob.mIcon);
		mDst.print( MOB_MOB2, mob.mMob);
		mDst.print( MOB_COUNT, mob.mKills);
		mDst.print( MOB_EXACT, mob.mExact);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, FileVersion version) {
		mDst.beginObject();
		mDst.print( QUEST_SET_NAME, set.mName);
		mDst.print( QUEST_SET_DECR, set.mDescr);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, FileVersion version) {
		mDst.beginObject( QUEST_REPEAT_INFO);
		mDst.print( REPEAT_INFO_TYPE, info.mType);
		if (info.mType.isUseTime()) {
			mDst.print( REPEAT_INFO_TOTAL, info.mTotal);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, FileVersion version) {
		mDst.beginObject();
		mDst.print( REPUTATION_NAME, rep.mName);
		mDst.print( REPUTATION_NEUTRAL, rep.mNeutral);
		writeMarkers( rep, version);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReward( FReward rr, FileVersion version) {
		mDst.beginObject();
		mDst.print( REWARD_REPUTATION, toID( IndexOf.getMember( rr.mRep), rr.mRep.mName));
		mDst.print( REWARD_VALUE, rr.mValue);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forSetting( FSetting rs, FileVersion version) {
		mDst.beginObject();
		mDst.print( SETTING_REPUTATION, toID( IndexOf.getMember( rs.mRep), rs.mRep.mName));
		if (rs.mLower != null) {
			mDst.print( SETTING_LOWER, toID( IndexOf.getMarker( rs.mLower), rs.mLower.mName));
		}
		if (rs.mUpper != null) {
			mDst.print( SETTING_UPPER, toID( IndexOf.getMarker( rs.mUpper), rs.mUpper.mName));
		}
		mDst.print( SETTING_INVERTED, rs.mInverted);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, FileVersion version) {
		mDst.beginObject();
		doTask( task);
		mDst.print( TASK_DEATHS, task.mDeaths);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskItemsConsume( FQuestTaskItemsConsume task, FileVersion version) {
		doTaskItem( task, version);
		return null;
	}

	@Override
	public Object forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, FileVersion version) {
		doTaskItem( task, version);
		return null;
	}

	@Override
	public Object forTaskItemsCrafting( FQuestTaskItemsCrafting task, FileVersion version) {
		doTaskItem( task, version);
		return null;
	}

	@Override
	public Object forTaskItemsDetect( FQuestTaskItemsDetect task, FileVersion version) {
		doTaskItem( task, version);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, FileVersion version) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_LOCATIONS);
		task.forEachLocation( this, version);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, FileVersion version) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_MOBS);
		task.forEachMob( this, version);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, FileVersion version) {
		mDst.beginObject();
		doTask( task);
		mDst.print( TASK_KILLS, task.mKills);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, FileVersion version) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_SETTINGS);
		task.forEachSetting( this, version);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	private String toID( int idx, String name) {
		return String.format( "%03d - %s", idx, name);
	}

	@Override
	public void writeDst( FHqm hqm) {
		FileVersion version = hqm.getVersion();
		mDst.beginObject();
		mDst.print( HQM_VERSION, version);
		if (version.contains( FileVersion.LOCK)) {
			mDst.printIf( HQM_PASSCODE, hqm.mPassCode);
		}
		if (version.contains( FileVersion.LORE)) {
			mDst.print( HQM_DECRIPTION, hqm.mDescr);
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
			writeGroups( hqm.mGroupCat, version);
		}
		mDst.endObject();
	}

	private void writeGroups( FGroupCat set, FileVersion version) {
		mDst.beginArray( HQM_GROUP_CAT);
		set.forEachMember( this, version);
		mDst.endArray();
	}

	private void writeGroupTiers( FGroupTierCat set, FileVersion version) {
		mDst.beginArray( HQM_GROUP_TIER_CAT);
		set.forEachMember( this, version);
		mDst.endArray();
	}

	private void writeMarkers( FReputation rep, FileVersion version) {
		mDst.beginArray( REPUTATION_MARKERS);
		rep.forEachMarker( this, version);
		mDst.endArray();
	}

	private void writeQuest( FQuest quest, FileVersion version) {
		mDst.beginObject();
		mDst.print( QUEST_ID, IndexOfQuest.get( quest));
		mDst.print( QUEST_NAME, quest.mName);
		mDst.print( QUEST_DESC, quest.mDescr);
		mDst.print( QUEST_X, quest.mX);
		mDst.print( QUEST_Y, quest.mY);
		mDst.print( QUEST_BIG, quest.mBig);
		if (version.contains( FileVersion.SETS)) {
			mDst.print( QUEST_SET, toID( IndexOf.getMember( quest.getParent()), quest.getParent().mName));
		}
		mDst.print( QUEST_ICON, quest.mIcon);
		writeQuestArr( quest.mRequirements, QUEST_REQUIREMENTS);
		if (version.contains( FileVersion.OPTION_LINKS)) {
			writeQuestArr( quest.mOptionLinks, QUEST_OPTION_LINKS);
		}
		if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
			quest.mRepeatInfo.accept( this, version);
		}
		if (version.contains( FileVersion.TRIGGER_QUESTS)) {
			mDst.print( QUEST_TRIGGER_TYPE, quest.mTriggerType);
			if (quest.mTriggerType.isUseTaskCount()) {
				mDst.print( QUEST_TRIGGER_TASKS, quest.mTriggerTasks);
			}
		}
		if (version.contains( FileVersion.PARENT_COUNT)) {
			mDst.print( QUEST_PARENT_REQUIREMENT_COUNT, quest.mReqCount);
		}
		writeTasks( quest, version);
		writeStackArr( quest.mRewards, QUEST_REWARD);
		writeStackArr( quest.mChoices, QUEST_CHOICE);
		if (version.contains( FileVersion.REPUTATION)) {
			writeRewards( quest, version);
		}
		mDst.endObject();
	}

	private void writeQuestArr( Vector<FQuest> arr, String key) {
		if (arr != null && !arr.isEmpty()) {
			mDst.beginArray( key);
			for (FQuest quest : arr) {
				if (quest != null) {
					mDst.printValue( toID( IndexOfQuest.get( quest), quest.mName));
				}
			}
			mDst.endArray();
		}
	}

	private void writeQuests( FQuestSetCat cat, FileVersion version) {
		mDst.beginArray( HQM_QUESTS);
		cat.forEachMember( mQuestWorker, version);
		mDst.endArray();
	}

	private void writeQuestSetCat( FQuestSetCat set, FileVersion version) {
		mDst.beginArray( HQM_QUEST_SET_CAT);
		set.forEachMember( this, version);
		mDst.endArray();
	}

	private void writeReputations( FReputationCat set, FileVersion version) {
		mDst.beginArray( HQM_REPUTATION_CAT);
		set.forEachMember( this, version);
		mDst.endArray();
	}

	private void writeRewards( FQuest quest, FileVersion version) {
		mDst.beginArray( QUEST_REPUTATIONS);
		quest.forEachReward( this, version);
		mDst.endArray();
	}

	private void writeStackArr( Vector<FItemStack> arr, String key) {
		if (arr != null && !arr.isEmpty()) {
			mDst.beginArray( key);
			for (AStack stk : arr) {
				mDst.beginObject();
				mDst.print( ITEM_NAME, stk);
				mDst.printIf( ITEM_NBT, stk.getNBT());
				mDst.endObject();
			}
			mDst.endArray();
		}
	}

	private void writeTasks( FQuest quest, FileVersion version) {
		mDst.beginArray( QUEST_TASKS);
		quest.forEachTask( this, version);
		mDst.endArray();
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
