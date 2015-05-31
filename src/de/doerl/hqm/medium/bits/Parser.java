package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
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
import de.doerl.hqm.base.dispatch.GroupTierOfIdx;
import de.doerl.hqm.base.dispatch.MarkerOfIdx;
import de.doerl.hqm.base.dispatch.QuestOfIdx;
import de.doerl.hqm.base.dispatch.QuestSetOfIdx;
import de.doerl.hqm.base.dispatch.ReputationOfIdx;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IHqmReader;
import de.doerl.hqm.quest.BagTier;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.utils.Utils;

class Parser extends AHQMWorker<Object, Object> implements IHqmReader {
	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private BitInputStream mSrc;
	private HashMap<FQuest, int[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, int[]> mOptionLinks = new HashMap<>();
	private HashMap<Integer, Vector<FQuest>> mPosts = new HashMap<>();
	private HashMap<Integer, FReputation> mReps = new HashMap<>();

	public Parser( InputStream is) throws IOException {
		mSrc = new BitInputStream( is);
	}

	private void addAllPosts( FQuest quest, int[] reqs) {
		for (int i : reqs) {
			addPost( quest, i);
		}
	}

	private void addPost( FQuest quest, Integer req) {
		Vector<FQuest> p = mPosts.get( req);
		if (p == null) {
			p = new Vector<FQuest>();
			mPosts.put( req, p);
		}
		p.add( quest);
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

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, Object p) {
		fluid.mStack = mSrc.readFluidStack();
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, Object p) {
		item.mStack = mSrc.readItemStack();
		item.mRequired = mSrc.readData( DataBitHelper.TASK_REQUIREMENT);
		item.mPrecision = ItemPrecision.get( mSrc.readData( DataBitHelper.ITEM_PRECISION));
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, Object p) {
		task.mDeaths = mSrc.readData( DataBitHelper.DEATHS);
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
			loc.mX = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mY = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mZ = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mRadius = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
			loc.mVisibility = Visibility.get( mSrc.readData( DataBitHelper.LOCATION_VISIBILITY));
			loc.mDim = mSrc.readData( DataBitHelper.WORLD_COORDINATE);
		}
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, Object p) {
		int count = mSrc.readData( DataBitHelper.TASK_MOB_COUNT);
		for (int i = 0; i < count; i++) {
			FItemStack icon = mSrc.readIconIf();
			String name = mSrc.readString( DataBitHelper.NAME_LENGTH);
			FMob mob = task.createMob( icon, name);
			mob.mMob = mSrc.readString( DataBitHelper.MOB_ID_LENGTH);
			mob.mKills = mSrc.readData( DataBitHelper.KILL_COUNT);
			mob.mExact = mSrc.readBoolean();
		}
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, Object p) {
		task.mKills = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
		int count = mSrc.readData( DataBitHelper.REPUTATION_SETTING);
		for (int i = 0; i < count; i++) {
			FSetting set = task.createSetting();
			set.mRep = ReputationOfIdx.get( task, mSrc.readData( DataBitHelper.REPUTATION));
			set.mLower = mSrc.readBoolean() ? MarkerOfIdx.get( set.mRep, mSrc.readData( DataBitHelper.REPUTATION_MARKER)) : null;
			set.mUpper = mSrc.readBoolean() ? MarkerOfIdx.get( set.mRep, mSrc.readData( DataBitHelper.REPUTATION_MARKER)) : null;
			set.mInverted = mSrc.readBoolean();
		}
		return null;
	}

	private void readGroup( FGroupCat cat) {
		int count = mSrc.readData( DataBitHelper.GROUP_COUNT);
		for (int i = 0; i < count; ++i) {
			if (mSrc.contains( FileVersion.BAG_LIMITS)) {
				mSrc.readData( DataBitHelper.GROUP_COUNT);
			}
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			FGroup grp = cat.createMember( name);
			grp.mTier = GroupTierOfIdx.get( cat.mParentHQM, mSrc.readData( DataBitHelper.TIER_COUNT));
			readStacks( grp.mStacks, DataBitHelper.GROUP_ITEMS);
			if (mSrc.contains( FileVersion.BAG_LIMITS) && mSrc.readBoolean()) {
				grp.mLimit = mSrc.readData( DataBitHelper.LIMIT);
			}
		}
	}

	private void readGroupTiers( FGroupTierCat cat) {
		int count = mSrc.readData( DataBitHelper.TIER_COUNT);
		for (int i = 0; i < count; ++i) {
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			FGroupTier tier = cat.createMember( name);
			tier.mColorID = mSrc.readData( DataBitHelper.COLOR);
			int[] weights = BagTier.newArray();
			for (int j = 0; j < weights.length; ++j) {
				weights[j] = mSrc.readData( DataBitHelper.WEIGHT);
			}
			tier.mWeights = weights;
		}
	}

	private void readMarker( FReputation rep) {
		int count = mSrc.readData( DataBitHelper.REPUTATION_MARKER);
		for (int i = 0; i < count; ++i) {
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			FMarker marker = rep.createMarker( name);
			marker.mMark = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
		}
		rep.sort();
	}

	private void readQuests( FHqm hqm) {
		int count = mSrc.readData( DataBitHelper.QUESTS);
		for (int i = 0; i < count; ++i) {
			if (mSrc.readBoolean()) {
				FQuest quest = hqm.createQuest( mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH));
				quest.mDescr = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
				quest.mX = mSrc.readData( DataBitHelper.QUEST_POS_X);
				quest.mY = mSrc.readData( DataBitHelper.QUEST_POS_Y);
				quest.mBig = mSrc.readBoolean();
				if (mSrc.contains( FileVersion.SETS)) {
					int setID = mSrc.readData( DataBitHelper.QUEST_SETS);
					FQuestSet qs = QuestSetOfIdx.get( hqm.mQuestSetCat, setID);
					if (qs == null) {
						qs = hqm.mQuestSetCat.createMember( "--Missing--");
					}
					quest.mIcon = mSrc.readIconIf();
					quest.mQuestSet = qs;
				}
				else {
					FQuestSet qs = hqm.mQuestSetCat.createMember( "--Default--");
					quest.mQuestSet = qs;
				}
				if (mSrc.readBoolean()) {
					int[] ids = mSrc.readIds( DataBitHelper.QUESTS);
					mRequirements.put( quest, ids);
					addAllPosts( quest, ids);
				}
				if (mSrc.contains( FileVersion.OPTION_LINKS) && mSrc.readBoolean()) {
					mOptionLinks.put( quest, mSrc.readIds( DataBitHelper.QUESTS));
				}
				FRepeatInfo info = quest.mRepeatInfo;
				if (mSrc.contains( FileVersion.REPEATABLE_QUESTS)) {
					RepeatType type = RepeatType.get( mSrc.readData( DataBitHelper.REPEAT_TYPE));
					info.mType = type;
					if (type.isUseTime()) {
						info.mTotal = mSrc.readData( DataBitHelper.HOURS);
					}
				}
				if (mSrc.contains( FileVersion.TRIGGER_QUESTS)) {
					TriggerType type = TriggerType.get( mSrc.readData( DataBitHelper.TRIGGER_TYPE));
					quest.mTriggerType = type;
					if (type.isUseTaskCount()) {
						quest.mTriggerTasks = mSrc.readData( DataBitHelper.TASKS);
					}
				}
				if (mSrc.contains( FileVersion.PARENT_COUNT) && mSrc.readBoolean()) {
					quest.mReqCount = mSrc.readData( DataBitHelper.QUESTS);
				}
				readTasks( quest);
				if (mSrc.readBoolean()) {
					readStacks( quest.mRewards, DataBitHelper.REWARDS);
				}
				if (mSrc.readBoolean()) {
					readStacks( quest.mChoices, DataBitHelper.REWARDS);
				}
				readRewards( quest);
			}
			else {
				hqm.addDeletedQuest();
			}
		}
	}

	private void readQuestSetCat( FQuestSetCat set) {
		if (mSrc.contains( FileVersion.SETS)) {
			int count = mSrc.readData( DataBitHelper.QUEST_SETS);
			for (int i = 0; i < count; i++) {
				String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				FQuestSet member = set.createMember( name);
				member.mDescr = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
			}
		}
		else {
			set.createMember( "Automatically generated");
		}
	}

	private void readReputations( FReputationCat set) {
		if (mSrc.contains( FileVersion.REPUTATION)) {
			int count = mSrc.readData( DataBitHelper.REPUTATION);
			for (int i = 0; i < count; ++i) {
				Integer id = mSrc.readData( DataBitHelper.REPUTATION);
				String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				FReputation member = set.createMember( name);
				member.mNeutral = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				readMarker( member);
				mReps.put( id, member);
			}
		}
	}

	private void readRewards( FQuest quest) {
		if (mSrc.contains( FileVersion.REPUTATION)) {
			int count = mSrc.readData( DataBitHelper.REPUTATION_REWARD);
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					FReward reward = quest.createReputationReward();
					reward.mRep = mReps.get( mSrc.readData( DataBitHelper.REPUTATION));
					reward.mValue = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
				}
			}
		}
	}

	public void readSrc( FHqm hqm, ICallback cb) {
		hqm.setVersion( mSrc.mVersion);
		if (mSrc.contains( FileVersion.LOCK)) {
			hqm.mPassCode = mSrc.readString( DataBitHelper.PASS_CODE);
		}
		if (mSrc.contains( FileVersion.LORE)) {
			hqm.mDescr = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		}
		else {
			hqm.mDescr = "No description";
		}
		readQuestSetCat( hqm.mQuestSetCat);
		readReputations( hqm.mReputationCat);
		readQuests( hqm);
		if (mSrc.contains( FileVersion.BAGS)) {
			readGroupTiers( hqm.mGroupTierCat);
			readGroup( hqm.mGroupCat);
		}
		updateRequirements( hqm);
		updateOptionLinks( hqm);
		updatePosts( hqm);
	}

	private void readStacks( Vector<AStack> param, DataBitHelper bitCount) {
		int count = mSrc.readData( bitCount);
		for (int i = 0; i < count; ++i) {
			AStack stk = mSrc.readFixedItemStack( true);
			if (stk != null) {
				param.add( stk);
			}
		}
	}

	private void readTasks( FQuest quest) {
		int count = mSrc.readData( DataBitHelper.TASKS);
		for (int i = 0; i < count; ++i) {
			int id = mSrc.readData( DataBitHelper.TASK_TYPE);
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			TaskTyp type = TaskTyp.get( id);
			AQuestTask task = quest.createQuestTask( type, name);
			task.mDescr = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
			task.accept( this, null);
//			if (result.size() > 0) {
//				task.addRequirement( result.get( result.size() - 1));
//			}
		}
	}

	private void updateOptionLinks( FHqm hqm) {
		for (Map.Entry<FQuest, int[]> e : mOptionLinks.entrySet()) {
			FQuest quest = e.getKey();
			int[] ids = e.getValue();
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest req = QuestOfIdx.get( hqm, id);
				if (req == null || req.isDeleted()) {
					Utils.log( LOGGER, Level.WARNING, "missing OptionLink [{0}] {1} for {2}", i, id, quest.mName);
				}
				else {
					quest.mOptionLinks.add( req);
				}
			}
		}
	}

	private void updatePosts( FHqm hqm) {
		for (Map.Entry<Integer, Vector<FQuest>> e : mPosts.entrySet()) {
			int id = e.getKey();
			Vector<FQuest> posts = e.getValue();
			FQuest quest = QuestOfIdx.get( hqm, id);
			if (quest == null || quest.isDeleted()) {
				Utils.log( LOGGER, Level.WARNING, "missing posts {0}", id);
			}
			else {
				quest.mPosts.addAll( posts);
			}
		}
	}

	private void updateRequirements( FHqm hqm) {
		for (Map.Entry<FQuest, int[]> e : mRequirements.entrySet()) {
			FQuest quest = e.getKey();
			int[] ids = e.getValue();
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest req = QuestOfIdx.get( hqm, id);
				if (req == null || req.isDeleted()) {
					Utils.log( LOGGER, Level.WARNING, "missing Requirement [{0}] {1} for {2}", i, id, quest.mName);
				}
				else {
					quest.mRequirements.add( req);
				}
			}
		}
	}
}
