package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FGroup;
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
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.base.dispatch.IndexOfGroup;
import de.doerl.hqm.base.dispatch.ReindexOfQuests;
import de.doerl.hqm.medium.IHqmWriter;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.JsonWriter;

class Serializer extends AHQMWorker<Object, Object> implements IHqmWriter, IToken {
	private QuestWorker mQuestWorker = new QuestWorker();
	private GroupWorker mGroupWorker = new GroupWorker();
	private JsonWriter mDst;

	public Serializer( OutputStream os) throws IOException {
		mDst = new JsonWriter( os);
	}

	private void doTask( AQuestTask task) {
		mDst.print( TASK_ID, task.toIdent());
		mDst.print( TASK_TYPE, task.getTaskTyp());
		mDst.print( TASK_NAME, task.mName);
		mDst.print( TASK_DESC, task.mDescr);
	}

	private void doTaskItem( AQuestTaskItems task) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_REQUIREMENTS);
		task.forEachRequirement( this, null);
		mDst.endArray();
		mDst.endObject();
	}

	public void flushDst() {
		mDst.flush();
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, Object p) {
		mDst.beginObject();
		mDst.print( FLUID_OBJECT, fluid.getStack());
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, Object p) {
		mDst.beginObject();
		mDst.print( GROUP_TIER_ID, tier.toIdent());
		mDst.print( GROUP_TIER_NAME, tier.mName);
		mDst.print( GROUP_TIER_COLOR, tier.mColorID);
		mDst.printArr( GROUP_TIER_WEIGHTS, tier.mWeights);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, Object p) {
		mDst.beginObject();
		mDst.print( ITEM_OBJECT, item.getStack());
		String nbt = item.getStack().getNBT();
		if (Utils.validString( nbt)) {
			mDst.print( ITEM_NBT, nbt);
		}
		mDst.print( REQUIREMENT_REQUIRED, item.mRequired);
		mDst.print( REQUIREMENT_PRECISION, item.mPrecision);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, Object p) {
		mDst.beginObject();
		mDst.print( LOCATION_NAME, loc.mName);
		writeIcon( LOCATION_ICON, loc.mIcon);
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
	public Object forMarker( FMarker mark, Object p) {
		mDst.beginObject();
		mDst.print( MARKER_NAME, mark.mName);
		mDst.print( MARKER_VALUE, mark.mMark);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMob( FMob mob, Object p) {
		mDst.beginObject();
		mDst.print( MOB_NAME, mob.mName);
		writeIcon( MOB_ICON, mob.mIcon);
		mDst.print( MOB_MOB2, mob.mMob);
		mDst.print( MOB_COUNT, mob.mKills);
		mDst.print( MOB_EXACT, mob.mExact);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		mDst.beginObject();
		mDst.print( QUEST_SET_ID, set.toIdent());
		mDst.print( QUEST_SET_NAME, set.mName);
		mDst.print( QUEST_SET_DECR, set.mDescr);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, Object p) {
		mDst.beginObject( QUEST_REPEAT_INFO);
		mDst.print( REPEAT_INFO_TYPE, info.mType);
		if (info.mType.isUseTime()) {
			mDst.print( REPEAT_INFO_TOTAL, info.mTotal);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, Object p) {
		mDst.beginObject();
		mDst.print( REPUTATION_ID, rep.toIdent());
		mDst.print( REPUTATION_NAME, rep.mName);
		mDst.print( REPUTATION_NEUTRAL, rep.mNeutral);
		writeMarkers( rep);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputationReward( FReputationReward rr, Object p) {
		mDst.beginObject();
		mDst.print( REWARD_REPUTATION, rr.mRep.toIdent());
		mDst.print( REWARD_VALUE, rr.mValue);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forSetting( FSetting rs, Object p) {
		mDst.beginObject();
		mDst.print( SETTING_REPUTATION, rs.mRep.toIdent());
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
	public Object forTaskDeath( FQuestTaskDeath task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.print( TASK_DEATHS, task.mDeaths);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskItemsConsume( FQuestTaskItemsConsume task, Object p) {
		doTaskItem( task);
		return null;
	}

	@Override
	public Object forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, Object p) {
		doTaskItem( task);
		return null;
	}

	@Override
	public Object forTaskItemsCrafting( FQuestTaskItemsCrafting task, Object p) {
		doTaskItem( task);
		return null;
	}

	@Override
	public Object forTaskItemsDetect( FQuestTaskItemsDetect task, Object p) {
		doTaskItem( task);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_LOCATIONS);
		task.forEachLocation( this, p);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_MOBS);
		task.forEachMob( this, p);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.print( TASK_KILLS, task.mKills);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_SETTINGS);
		task.forEachSetting( this, p);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	private String toID( int idx, String name) {
		return String.format( "%03d - %s", idx, name);
	}

	@Override
	public void writeDst( FHqm hqm) {
		mDst.beginObject();
		mDst.print( HQM_VERSION, hqm.getVersion());
		mDst.printIf( HQM_PASSCODE, hqm.mPassCode);
		mDst.print( HQM_DECRIPTION, hqm.mDescr);
		writeQuestSetCat( hqm.mQuestSetCat);
		writeReputations( hqm.mReputationCat);
		writeQuests( hqm.mQuestSetCat);
		writeGroupTiers( hqm.mGroupTierCat);
		writeGroups( hqm.mGroupTierCat);
		mDst.endObject();
	}

	private void writeGroup( FGroup grp) {
		mDst.beginObject();
		mDst.print( GROUP_ID, IndexOfGroup.get( grp));
		mDst.print( GROUP_NAME, grp.mName);
		mDst.print( GROUP_TIER, toID( IndexOf.getMember( grp.mParentTier), grp.mParentTier.mName));
		mDst.print( GROUP_LIMIT, grp.mLimit);
		writeStackArr( GROUP_STACKS, grp.mStacks);
		mDst.endObject();
	}

	private void writeGroups( FGroupTierCat cat) {
		mDst.beginArray( HQM_GROUP_CAT);
		cat.forEachMember( mGroupWorker, null);
		mDst.endArray();
	}

	private void writeGroupTiers( FGroupTierCat cat) {
		mDst.beginArray( HQM_GROUP_TIER_CAT);
		cat.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeIcon( String key, FItemStack icon) {
		if (icon == null) {
			mDst.print( key, null);
		}
		else {
			String nbt = icon.getNBT();
			if (Utils.validString( nbt)) {
				mDst.beginObject( key);
				mDst.print( ITEM_OBJECT, icon);
				mDst.print( ITEM_NBT, nbt);
				mDst.endObject();
			}
			else {
				mDst.print( key, icon);
			}
		}
	}

	private void writeMarkers( FReputation rep) {
		mDst.beginArray( REPUTATION_MARKERS);
		rep.forEachMarker( this, null);
		mDst.endArray();
	}

	private void writeQuest( FQuest quest) {
		mDst.beginObject();
		mDst.print( QUEST_ID, quest.toIdent());
		mDst.print( QUEST_NAME, quest.mName);
		mDst.print( QUEST_DESC, quest.mDescr);
		mDst.print( QUEST_X, quest.mX);
		mDst.print( QUEST_Y, quest.mY);
		mDst.print( QUEST_BIG, quest.mBig);
		mDst.print( QUEST_SET, quest.getParent().toIdent());
		writeIcon( QUEST_ICON, quest.mIcon);
		writeQuestArr( QUEST_REQUIREMENTS, quest.mRequirements);
		writeQuestArr( QUEST_OPTION_LINKS, quest.mOptionLinks);
		quest.mRepeatInfo.accept( this, null);
		mDst.print( QUEST_TRIGGER_TYPE, quest.mTriggerType);
		mDst.print( QUEST_TRIGGER_TASKS, quest.mTriggerTasks);
		mDst.print( QUEST_PARENT_REQUIREMENT_COUNT, quest.mCount);
		writeTasks( quest);
		writeStackArr( QUEST_REWARD, quest.mRewards);
		writeStackArr( QUEST_CHOICE, quest.mChoices);
		writeRewards( quest);
		mDst.endObject();
	}

	private void writeQuestArr( String key, Vector<FQuest> arr) {
		if (arr != null && !arr.isEmpty()) {
			mDst.beginArray( key);
			for (FQuest quest : arr) {
				if (quest != null) {
					mDst.printValue( quest.toIdent());
				}
			}
			mDst.endArray();
		}
	}

	private void writeQuests( FQuestSetCat cat) {
		ReindexOfQuests.get( cat);
		mDst.beginArray( HQM_QUESTS);
		cat.forEachMember( mQuestWorker, null);
		mDst.endArray();
	}

	private void writeQuestSetCat( FQuestSetCat set) {
		mDst.beginArray( HQM_QUEST_SET_CAT);
		set.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeReputations( FReputationCat set) {
		mDst.beginArray( HQM_REPUTATION_CAT);
		set.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeRewards( FQuest quest) {
		mDst.beginArray( QUEST_REP_REWRDS);
		quest.forEachReward( this, null);
		mDst.endArray();
	}

	private void writeStackArr( String key, Vector<FItemStack> arr) {
		if (arr != null && !arr.isEmpty()) {
			mDst.beginArray( key);
			for (AStack stk : arr) {
				String nbt = stk.getNBT();
				if (Utils.validString( nbt)) {
					mDst.beginObject();
					mDst.print( ITEM_OBJECT, stk);
					mDst.printIf( ITEM_NBT, nbt);
					mDst.endObject();
				}
				else {
					mDst.printValue( stk);
				}
			}
			mDst.endArray();
		}
	}

	private void writeTasks( FQuest quest) {
		mDst.beginArray( QUEST_TASKS);
		quest.forEachTask( this, null);
		mDst.endArray();
	}

	private final class GroupWorker extends AHQMWorker<Object, Object> {
		@Override
		public Object forGroup( FGroup grp, Object p) {
			writeGroup( grp);
			return null;
		}

		@Override
		public Object forGroupTier( FGroupTier tier, Object p) {
			tier.forEachGroup( this, p);
			return null;
		}
	}

	private final class QuestWorker extends AHQMWorker<Object, Object> {
		@Override
		public Object forQuest( FQuest quest, Object p) {
			writeQuest( quest);
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, Object p) {
			set.forEachQuest( this, p);
			return null;
		}
	}
}
