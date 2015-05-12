package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTiers;
import de.doerl.hqm.base.FGroups;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;
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
import de.doerl.hqm.base.FReputationMarker;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FReputationSetting;
import de.doerl.hqm.base.FReputations;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.QuestSetOfIdx;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IHqmReader;
import de.doerl.hqm.quest.BagTier;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.utils.Utils;

class Parser extends AHQMWorker<Object, Object> implements IHqmReader {
	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private BitInputStream mSrc;

	public Parser( InputStream is) throws IOException {
		mSrc = new BitInputStream( is);
	}

	@Override
	public void closeSrc() {
		try {
			if (mSrc != null) {
				mSrc.close();
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private void doItemTask( AQuestTaskItems task) {
		int count = mSrc.readData( DataBitHelper.TASK_ITEM_COUNT);
		for (int i = 0; i < count; ++i) {
			ARequirement req = task.createRequirement( mSrc.readBoolean());
			req.accept( this, null);
		}
	}

	private void doReputation( AQuestTaskReputation task) {
		int count = mSrc.readData( DataBitHelper.REPUTATION_SETTING);
		for (int i = 0; i < count; i++) {
			FReputationSetting res = task.createSetting();
			res.mRepID.mValue = mSrc.readData( DataBitHelper.REPUTATION);
			res.mLowerID.mValue = mSrc.readBoolean() ? mSrc.readData( DataBitHelper.REPUTATION_MARKER) : null;
			res.mUpperID.mValue = mSrc.readBoolean() ? mSrc.readData( DataBitHelper.REPUTATION_MARKER) : null;
			res.mInverted.mValue = mSrc.readBoolean();
		}
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement task, Object p) {
		task.getStack().mValue = mSrc.readFluidStack();
		task.mPrecision.mValue = ItemPrecision.PRECISE;
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement task, Object p) {
		task.getStack().mValue = mSrc.readItemStack();
		task.getRequired().mValue = mSrc.readData( DataBitHelper.TASK_REQUIREMENT);
		task.mPrecision.mValue = ItemPrecision.get( mSrc.readData( DataBitHelper.ITEM_PRECISION));
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, Object p) {
		task.mDeaths.mValue = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskItemsConsume( FQuestTaskItemsConsume task, Object p) {
		doItemTask( task);
		return null;
	}

	@Override
	public Object forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, Object p) {
		doItemTask( task);
		return null;
	}

	@Override
	public Object forTaskItemsCrafting( FQuestTaskItemsCrafting task, Object p) {
		doItemTask( task);
		return null;
	}

	@Override
	public Object forTaskItemsDetect( FQuestTaskItemsDetect task, Object p) {
		doItemTask( task);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, Object p) {
		int count = mSrc.readData( DataBitHelper.TASK_LOCATION_COUNT);
		for (int i = 0; i < count; i++) {
			FItemStack icon = mSrc.readIconIf();
			String name = mSrc.readString( DataBitHelper.NAME_LENGTH);
			FLocation loc = task.createLocation( icon, name);
			loc.mX.mValue = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mY.mValue = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mZ.mValue = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mRadius.mValue = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mVisibility.mValue = Visibility.get( mSrc.readData( DataBitHelper.LOCATION_VISIBILITY));
			loc.mDim.mValue = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
		}
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, Object p) {
		int count = mSrc.readData( DataBitHelper.TASK_MOB_COUNT);
		for (int i = 0; i < count; i++) {
			FItemStack icon = mSrc.readIconIf();
			String name = mSrc.readString( DataBitHelper.NAME_LENGTH);
			FMob res = task.createMob( icon, name);
			res.mMob.mValue = mSrc.readString( DataBitHelper.MOB_ID_LENGTH);
			res.mKills.mValue = mSrc.readData( DataBitHelper.KILL_COUNT);
			res.mExact.mValue = mSrc.readBoolean();
		}
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, Object p) {
		doReputation( task);
		task.mKills.mValue = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
		doReputation( task);
		return null;
	}

	private void readGroup( FGroups set) {
		int count = mSrc.readData( DataBitHelper.GROUP_COUNT);
		for (int i = 0; i < count; ++i) {
			int id = mSrc.contains( FileVersion.BAG_LIMITS) ? mSrc.readData( DataBitHelper.GROUP_COUNT) : i;
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			FGroup grp = set.createMember( name);
			grp.mID.mValue = id;
			grp.mTierID.mValue = mSrc.readData( DataBitHelper.TIER_COUNT);
			readGroupItems( grp);
			if (mSrc.contains( FileVersion.BAG_LIMITS) && mSrc.readBoolean()) {
				grp.mLimit.mValue = mSrc.readData( DataBitHelper.LIMIT);
			}
		}
	}

	private void readGroupItems( FGroup grp) {
		int count = mSrc.readData( DataBitHelper.GROUP_ITEMS);
		for (int i = 0; i < count; ++i) {
			AStack itemStack = mSrc.readFixedItemStack( true);
			if (itemStack != null) {
				grp.addItemStk( itemStack);
			}
		}
	}

	private void readGroupTiers( FGroupTiers set) {
		int count = mSrc.readData( DataBitHelper.TIER_COUNT);
		for (int i = 0; i < count; ++i) {
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			FGroupTier member = set.createMember( name);
			member.mColorID.mValue = mSrc.readData( DataBitHelper.COLOR);
			int[] weights = BagTier.newArray();
			for (int j = 0; j < weights.length; ++j) {
				weights[j] = mSrc.readData( DataBitHelper.WEIGHT);
			}
			member.mWeights.mValue = weights;
		}
	}

	private void readMarker( FReputation rep) {
		int count = mSrc.readData( DataBitHelper.REPUTATION_MARKER);
		for (int i = 0; i < count; ++i) {
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			FReputationMarker marker = rep.createMarker( name);
			marker.mValue.mValue = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
		}
		rep.sort();
	}

	private void readQuests( FQuestSets set) {
		int count = mSrc.readData( DataBitHelper.QUESTS);
		for (int i = 0; i < count; ++i) {
			if (mSrc.readBoolean()) {
				String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				String desc = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
				int x = mSrc.readData( DataBitHelper.QUEST_POS_X);
				int y = mSrc.readData( DataBitHelper.QUEST_POS_Y);
				boolean big = mSrc.readBoolean();
				FQuest member;
				if (mSrc.contains( FileVersion.SETS)) {
					int setID = mSrc.readData( DataBitHelper.QUEST_SETS);
					FQuestSet qs = QuestSetOfIdx.get( set, setID);
					if (qs == null) {
						qs = set.createMember( "--Missing--");
					}
					member = qs.createQuest( name);
					member.mIcon.mValue = mSrc.readIconIf();
				}
				else {
					FQuestSet qs = set.createMember( "--Default--");
					member = qs.createQuest( name);
				}
				member.mDesc.mValue = desc;
				member.mX.mValue = x;
				member.mY.mValue = y;
				member.mBig.mValue = big;
				if (mSrc.readBoolean()) {
					member.mRequirements.mValue = mSrc.readIds( DataBitHelper.QUESTS);
				}
				if (mSrc.contains( FileVersion.OPTION_LINKS) && mSrc.readBoolean()) {
					member.mOptionLinks.mValue = mSrc.readIds( DataBitHelper.QUESTS);
				}
				FRepeatInfo info = member.getRepeatInfo();
				if (mSrc.contains( FileVersion.REPEATABLE_QUESTS)) {
					RepeatType type = RepeatType.get( mSrc.readData( DataBitHelper.REPEAT_TYPE));
					info.mType.mValue = type;
					if (type.isUseTime()) {
						info.mTotal.mValue = mSrc.readData( DataBitHelper.HOURS);
					}
				}
				if (mSrc.contains( FileVersion.TRIGGER_QUESTS)) {
					TriggerType type = TriggerType.get( mSrc.readData( DataBitHelper.TRIGGER_TYPE));
					member.mTriggerType.mValue = type;
					if (type.isUseTaskCount()) {
						member.mTriggerTasks.mValue = mSrc.readData( DataBitHelper.TASKS);
					}
				}
				if (mSrc.contains( FileVersion.PARENT_COUNT) && mSrc.readBoolean()) {
					member.mReqUseModified.mValue = true;
					member.mReqCount.mValue = mSrc.readData( DataBitHelper.QUESTS);
				}
				else {
					member.mReqUseModified.mValue = false;
				}
				readTasks( member);
				member.setReward( mSrc.readRewardData());
				member.setRewardChoice( mSrc.readRewardData());
				readReputationReward( member);
			}
		}
	}

	private void readQuestSets( FQuestSets set) {
		if (mSrc.contains( FileVersion.SETS)) {
			int count = mSrc.readData( DataBitHelper.QUEST_SETS);
			for (int i = 0; i < count; i++) {
				String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				FQuestSet member = set.createMember( name);
				member.mDesc.mValue = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
			}
		}
		else {
			set.createMember( "Automatically generated");
		}
	}

	private void readReputationReward( FQuest quest) {
		if (mSrc.contains( FileVersion.REPUTATION)) {
			int count = mSrc.readData( DataBitHelper.REPUTATION_REWARD);
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					FReputationReward reward = quest.createReputationReward();
					reward.mRepID.mValue = mSrc.readData( DataBitHelper.REPUTATION);
					reward.mValue.mValue = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
				}
			}
		}
	}

	private void readReputations( FReputations set) {
		if (mSrc.contains( FileVersion.REPUTATION)) {
			int count = mSrc.readData( DataBitHelper.REPUTATION);
			for (int i = 0; i < count; ++i) {
				int id = mSrc.readData( DataBitHelper.REPUTATION);
				String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				FReputation member = set.createMember( name);
				member.mID.mValue = id;
				member.mNeutral.mValue = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				readMarker( member);
			}
		}
	}

	public void readSrc( FHqm hqm, ICallback cb) {
		hqm.setVersion( mSrc.mVersion);
		if (mSrc.contains( FileVersion.LOCK)) {
			hqm.mPassCode.mValue = mSrc.readString( DataBitHelper.PASS_CODE);
		}
		if (mSrc.contains( FileVersion.LORE)) {
			hqm.mDesc.mValue = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		}
		else {
			hqm.mDesc.mValue = "No description";
		}
		readQuestSets( hqm.mQuestSets);
		readReputations( hqm.mRepSets);
		readQuests( hqm.mQuestSets);
		if (mSrc.contains( FileVersion.BAGS)) {
			readGroupTiers( hqm.mGroupTiers);
			readGroup( hqm.mGroups);
		}
	}

	private void readTasks( FQuest quest) {
		int count = mSrc.readData( DataBitHelper.TASKS);
		for (int i = 0; i < count; ++i) {
			int type = mSrc.readData( DataBitHelper.TASK_TYPE);
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			AQuestTask task = quest.createQuestTask( type, name);
			task.mDesc.mValue = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
			task.accept( this, null);
//			if (result.size() > 0) {
//				task.addRequirement( result.get( result.size() - 1));
//			}
		}
	}
}
