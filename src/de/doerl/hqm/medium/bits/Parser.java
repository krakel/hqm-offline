package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.ARequirement;
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
import de.doerl.hqm.base.FReputationBar;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.GroupTierOfID;
import de.doerl.hqm.base.dispatch.MarkerOfID;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.QuestSetOfID;
import de.doerl.hqm.base.dispatch.ReputationOfID;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.utils.Utils;

class Parser extends AHQMWorker<Object, FileVersion> {
	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private BitInputStream mSrc;
	private HashMap<FQuest, int[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, int[]> mOptionLinks = new HashMap<>();
	private HashMap<Integer, Vector<FQuest>> mPosts = new HashMap<>();

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

	private void doItemTask( AQuestTaskItems task, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.TASK_ITEM_COUNT);
		for (int i = 0; i < count; ++i) {
			ARequirement req = task.createRequirement( mSrc.readBoolean());
			req.accept( this, version);
		}
	}

	private void doReputationTask( AQuestTaskReputation task) {
		int count = mSrc.readData( DataBitHelper.REPUTATION_SETTING);
		for (int id = 0; id < count; id++) { // TODO should also be AIdent
			FSetting set = task.createSetting();
			set.mRep = ReputationOfID.get( task, mSrc.readData( DataBitHelper.REPUTATION));
			set.mLower = mSrc.readBoolean() ? MarkerOfID.get( set.mRep, mSrc.readData( DataBitHelper.REPUTATION_MARKER)) : null;
			set.mUpper = mSrc.readBoolean() ? MarkerOfID.get( set.mRep, mSrc.readData( DataBitHelper.REPUTATION_MARKER)) : null;
			set.mInverted = mSrc.readBoolean();
		}
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, FileVersion version) {
		fluid.mStack = mSrc.readFluidStack();
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, FileVersion version) {
		item.mStack = mSrc.readItemStack( version);
		item.mRequired = mSrc.readData( DataBitHelper.TASK_REQUIREMENT);
		if (version.contains( FileVersion.CUSTOM_PRECISION_TYPES)) {
			item.mPrecision = ItemPrecision.parse( mSrc.readString( DataBitHelper.ITEM_PRECISION, version));
		}
		else {
			item.mPrecision = ItemPrecision.get( mSrc.readData( DataBitHelper.ITEM_PRECISION, version));
		}
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, FileVersion version) {
		RepeatType type = RepeatType.get( mSrc.readData( DataBitHelper.REPEAT_TYPE));
		info.mType = type;
		if (type.isUseTime()) {
			info.mTotal = mSrc.readData( DataBitHelper.HOURS);
		}
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, FileVersion version) {
		task.mDeaths = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskItemsConsume( FQuestTaskItemsConsume task, FileVersion version) {
		doItemTask( task, version);
		return null;
	}

	@Override
	public Object forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, FileVersion version) {
		doItemTask( task, version);
		return null;
	}

	@Override
	public Object forTaskItemsCrafting( FQuestTaskItemsCrafting task, FileVersion version) {
		doItemTask( task, version);
		return null;
	}

	@Override
	public Object forTaskItemsDetect( FQuestTaskItemsDetect task, FileVersion version) {
		doItemTask( task, version);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.TASK_LOCATION_COUNT);
		for (int id = 0; id < count; id++) {
			FLocation loc = task.createLocation( id);
			loc.mIcon = mSrc.readIconIf( version);
			loc.setName( mSrc.readString( DataBitHelper.NAME_LENGTH));
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
	public Object forTaskMob( FQuestTaskMob task, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.TASK_MOB_COUNT);
		for (int id = 0; id < count; id++) {
			FMob mob = task.createMob( id);
			mob.mIcon = mSrc.readIconIf( version);
			mob.setName( mSrc.readString( DataBitHelper.NAME_LENGTH));
			mob.mMob = mSrc.readString( DataBitHelper.MOB_ID_LENGTH);
			mob.mKills = mSrc.readData( DataBitHelper.KILL_COUNT);
			mob.mExact = mSrc.readBoolean();
		}
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, FileVersion version) {
		doReputationTask( task);
		task.mKills = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, FileVersion version) {
		doReputationTask( task);
		return null;
	}

	private void readCommands( FQuest quest) {
		if (mSrc.readBoolean()) {
			int count = mSrc.readData( DataBitHelper.REWARDS);
			for (int i = 0; i < count; ++i) {
				quest.mCommands.add( mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH));
			}
		}
	}

	private void readGroup( FGroupTierCat cat, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.GROUP_COUNT);
		for (int i = 0; i < count; ++i) {
			int id;
			if (version.contains( FileVersion.BAG_LIMITS)) {
				id = mSrc.readData( DataBitHelper.GROUP_COUNT);
			}
			else {
				id = i;
			}
			String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
			FGroupTier tier = GroupTierOfID.get( cat, mSrc.readData( DataBitHelper.TIER_COUNT));
			if (tier == null) {
				Utils.log( LOGGER, Level.WARNING, "missing tier of group {0}", name);
				tier = cat.createMember();
				tier.setName( "__Missing__");
			}
			FGroup grp = tier.createGroup( id);
			grp.setName( name);
			readStacks( grp.mStacks, DataBitHelper.GROUP_ITEMS, version);
			if (version.contains( FileVersion.BAG_LIMITS) && mSrc.readBoolean()) {
				grp.mLimit = mSrc.readData( DataBitHelper.LIMIT);
			}
		}
	}

	private void readGroupTiers( FGroupTierCat cat) {
		int count = mSrc.readData( DataBitHelper.TIER_COUNT);
		for (int id = 0; id < count; ++id) {
			FGroupTier tier = cat.createGroupTier( id);
			tier.setName( mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH));
			tier.mColorID = mSrc.readData( DataBitHelper.COLOR);
			for (int j = 0; j < tier.mWeights.length; ++j) {
				tier.mWeights[j] = mSrc.readData( DataBitHelper.WEIGHT);
			}
		}
	}

	private void readMarker( FReputation rep) {
		int count = mSrc.readData( DataBitHelper.REPUTATION_MARKER);
		for (int id = 0; id < count; ++id) {
			FMarker marker = rep.createMarker( id);
			marker.setName( mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH));
			marker.mMark = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
		}
		rep.sort();
	}

	private void readQuests( FQuestSetCat cat, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.QUESTS, version);
		for (int id = 0; id < count; ++id) {
			if (mSrc.readBoolean()) {
				String name = mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH);
				String descr = mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
				int x = mSrc.readData( DataBitHelper.QUEST_POS_X);
				int y = mSrc.readData( DataBitHelper.QUEST_POS_Y);
				boolean big = mSrc.readBoolean();
				int setID;
				FItemStack icon;
				if (version.contains( FileVersion.SETS)) {
					setID = mSrc.readData( DataBitHelper.QUEST_SETS);
					icon = mSrc.readIconIf( version);
				}
				else {
					setID = 0;
					icon = null;
				}
				FQuestSet set = QuestSetOfID.get( cat, setID);
				if (set == null) {
					Utils.log( LOGGER, Level.WARNING, "missing set of quest {0}", name);
					set = cat.createMember();
					set.setName( "__Missing__");
				}
				FQuest quest = set.createQuest( id);
				quest.setName( name);
				quest.setDescr( descr);
				quest.mX = x;
				quest.mY = y;
				quest.mBig = big;
				quest.mIcon = icon;
				if (mSrc.readBoolean()) {
					int[] ids = mSrc.readIds( DataBitHelper.QUESTS, version);
					mRequirements.put( quest, ids);
					addAllPosts( quest, ids);
				}
				if (version.contains( FileVersion.OPTION_LINKS) && mSrc.readBoolean()) {
					int[] ids = mSrc.readIds( DataBitHelper.QUESTS, version);
					mOptionLinks.put( quest, ids);
				}
				if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
					quest.mRepeatInfo.accept( this, version);
				}
				if (version.contains( FileVersion.TRIGGER_QUESTS)) {
					TriggerType type = TriggerType.get( mSrc.readData( DataBitHelper.TRIGGER_TYPE));
					quest.mTriggerType = type;
					if (type.isUseTaskCount()) {
						quest.mTriggerTasks = mSrc.readData( DataBitHelper.TASKS);
					}
				}
				if (version.contains( FileVersion.PARENT_COUNT) && mSrc.readBoolean()) {
					quest.mCount = mSrc.readData( DataBitHelper.QUESTS, version);
				}
				readTasks( quest, version);
				readStacksIf( quest.mRewards, DataBitHelper.REWARDS, version);
				readStacksIf( quest.mChoices, DataBitHelper.REWARDS, version);
				if (version.contains( FileVersion.COMMAND_REWARDS)) {
					readCommands( quest);
				}
				if (version.contains( FileVersion.REPUTATION)) {
					readRewards( quest);
				}
			}
		}
	}

	private void readQuestSetCat( FQuestSetCat cat, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.QUEST_SETS);
		for (int id = 0; id < count; id++) {
			FQuestSet set = cat.createQuestSet( id);
			set.setName( mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH));
			set.setDescr( mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH));
			if (version.contains( FileVersion.REPUTATION_BARS)) {
				readReputationBars( set, version);
			}
		}
	}

	private void readReputationBars( FQuestSet set, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.BYTE);
		for (int i = 0; i < count; ++i) {
			FReputationBar bar = set.createReputationBar();
			bar.mValue = mSrc.readData( DataBitHelper.INT);
		}
	}

	private void readReputations( FReputationCat cat) {
		int count = mSrc.readData( DataBitHelper.REPUTATION);
		for (int i = 0; i < count; ++i) {
			FReputation rep = cat.createReputation( mSrc.readData( DataBitHelper.REPUTATION));
			rep.setName( mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH));
			rep.setDescr( mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH));
			readMarker( rep);
		}
	}

	private void readRewards( FQuest quest) {
		int count = mSrc.readData( DataBitHelper.REPUTATION_REWARD);
		for (int i = 0; i < count; i++) {
			FReputationReward reward = quest.createRepReward();
			reward.mRep = ReputationOfID.get( quest.getHqm(), mSrc.readData( DataBitHelper.REPUTATION));
			reward.mValue = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
		}
	}

	void readSrc( FHqm hqm) {
		FileVersion version = FileVersion.get( mSrc.readByte());
		hqm.setVersion( version);
		hqm.setMain( FHqm.LANG_EN_US);
		if (version.contains( FileVersion.LOCK)) {
			hqm.mPassCode = mSrc.readString( DataBitHelper.PASS_CODE);
		}
		if (version.contains( FileVersion.LORE)) {
			hqm.setDescr( mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH));
		}
		else {
			hqm.setDescr( "No description");
		}
		if (version.contains( FileVersion.SETS)) {
			readQuestSetCat( hqm.mQuestSetCat, version);
		}
		else {
			FQuestSet set = hqm.mQuestSetCat.createMember();
			set.setName( "__Default__");
		}
		if (version.contains( FileVersion.REPUTATION)) {
			readReputations( hqm.mReputationCat);
		}
		readQuests( hqm.mQuestSetCat, version);
		if (version.contains( FileVersion.BAGS)) {
			readGroupTiers( hqm.mGroupTierCat);
			readGroup( hqm.mGroupTierCat, version);
		}
		updateRequirements( hqm);
		updateOptionLinks( hqm);
		updatePosts( hqm);
	}

	private void readStacks( Vector<FItemStack> param, DataBitHelper bits, FileVersion version) {
		int count = mSrc.readData( bits);
		for (int i = 0; i < count; ++i) {
			FItemStack stk = mSrc.readItemStackFix( version);
			if (stk != null) {
				param.add( stk);
			}
		}
	}

	private void readStacksIf( Vector<FItemStack> param, DataBitHelper bits, FileVersion version) {
		if (mSrc.readBoolean()) {
			readStacks( param, bits, version);
		}
	}

	private void readTasks( FQuest quest, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.TASKS);
		for (int i = 0; i < count; ++i) {
			TaskTyp type = TaskTyp.get( mSrc.readData( DataBitHelper.TASK_TYPE, version));
			AQuestTask task = quest.createQuestTask( type);
			task.setName( mSrc.readString( DataBitHelper.QUEST_NAME_LENGTH));
			task.setDescr( mSrc.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH));
			task.accept( this, version);
		}
	}

	private void updateOptionLinks( FHqm hqm) {
		for (FQuest quest : mOptionLinks.keySet()) {
			int[] ids = mOptionLinks.get( quest);
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest other = QuestOfID.get( hqm, id);
				if (other == null) {
					Utils.log( LOGGER, Level.WARNING, "missing OptionLink [{0}] {1} for {2}", i, id, quest.getName());
				}
				else {
					quest.mOptionLinks.add( other);
				}
			}
		}
	}

	private void updatePosts( FHqm hqm) {
		for (Integer id : mPosts.keySet()) {
			FQuest other = QuestOfID.get( hqm, id);
			if (other == null) {
				Utils.log( LOGGER, Level.WARNING, "missing posts {0}", id);
			}
			else {
				other.mPosts.addAll( mPosts.get( id));
			}
		}
	}

	private void updateRequirements( FHqm hqm) {
		for (FQuest quest : mRequirements.keySet()) {
			int[] ids = mRequirements.get( quest);
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest other = QuestOfID.get( hqm, id);
				if (other == null) {
					Utils.log( LOGGER, Level.WARNING, "missing Requirement [{0}] {1} for {2}", i, id, quest.getName());
				}
				else {
					quest.mRequirements.add( other);
				}
			}
		}
	}
}
