package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.data.AQuestDataTask;
import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FPlayer;
import de.doerl.hqm.base.data.FPlayerStats;
import de.doerl.hqm.base.data.FQuestData;
import de.doerl.hqm.base.data.FQuestDataTaskDeath;
import de.doerl.hqm.base.data.FQuestDataTaskItems;
import de.doerl.hqm.base.data.FQuestDataTaskLocation;
import de.doerl.hqm.base.data.FQuestDataTaskMob;
import de.doerl.hqm.base.data.FQuestDataTaskReputationKill;
import de.doerl.hqm.base.data.FQuestDataTaskReputationTarget;
import de.doerl.hqm.base.data.FTeam;
import de.doerl.hqm.base.data.FTeamStats;
import de.doerl.hqm.base.data.GroupData;
import de.doerl.hqm.base.data.PlayerEntry;
import de.doerl.hqm.base.dispatch.ADataWorker;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.ReputationOfID;
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.base.dispatch.SizeOfQuests;
import de.doerl.hqm.base.dispatch.TaskOfID;
import de.doerl.hqm.base.dispatch.TeamOfIdx;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.LifeSetting;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.RewardSetting;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.utils.Utils;

class ParserData extends ADataWorker<Object, FileVersion> {
	private static final Logger LOGGER = Logger.getLogger( ParserData.class.getName());
	private BitInputStream mSrc;

	public ParserData( InputStream is) throws IOException {
		mSrc = new BitInputStream( is);
	}

	private void doTask( AQuestDataTask task) {
		task.mCompleted = mSrc.readBoolean();
	}

	@Override
	public Object forDataTaskDeath( FQuestDataTaskDeath task, FileVersion version) {
		doTask( task);
		task.mDeaths = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forDataTaskItems( FQuestDataTaskItems task, FileVersion version) {
		doTask( task);
		int count = mSrc.readData( DataBitHelper.TASK_ITEM_COUNT);
		for (int i = 0; i < count; ++i) {
			int progress = mSrc.readData( DataBitHelper.ITEM_PROGRESS);
			if (i < task.mProgress.length) {
//				task.mProgress[i] = Math.min( items[i].required, Math.max( 0, progress));
			}
		}
		return null;
	}

	@Override
	public Object forDataTaskLocation( FQuestDataTaskLocation task, FileVersion version) {
		doTask( task);
		int count = mSrc.readData( DataBitHelper.TASK_LOCATION_COUNT);
		boolean visited[] = task.mVisited;
		for (int i = 0; i < count; ++i) {
			boolean val = mSrc.readBoolean();
			if (i < visited.length) {
				visited[i] = val;
			}
		}
		return null;
	}

	@Override
	public Object forDataTaskMob( FQuestDataTaskMob task, FileVersion version) {
		doTask( task);
		int count = mSrc.readData( DataBitHelper.TASK_MOB_COUNT);
		int killed[] = task.mKilled;
		for (int i = 0; i < count; i++) {
			int val = mSrc.readData( DataBitHelper.KILL_COUNT);
			if (i < killed.length) {
//				killed[i] = Math.min( mobs[i].count, Math.max( 0, val));
			}
		}
		return null;
	}

	@Override
	public Object forDataTaskReputationKill( FQuestDataTaskReputationKill task, FileVersion version) {
		doTask( task);
		task.mKills = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forDataTaskReputationTarget( FQuestDataTaskReputationTarget task, FileVersion p) {
		doTask( task);
		return null;
	}

	private void readDeath( FPlayerStats stats) {
		for (int i = 0; i < stats.mDeaths.length; ++i) {
			stats.mDeaths[i] = mSrc.readData( DataBitHelper.DEATHS);
		}
	}

	private void readGroups( FPlayer player) {
		int count = mSrc.readData( DataBitHelper.GROUP_COUNT);
		for (int i = 0; i < count; ++i) {
			int id = mSrc.readData( DataBitHelper.GROUP_COUNT);
			GroupData groupData = player.getGroupData( id);
			if (groupData != null) {
				groupData.mRetrieved = mSrc.readData( DataBitHelper.LIMIT);
			}
		}
	}

	private void readPlayers( FData data, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.PLAYERS);
		for (int i = 0; i < count; ++i) {
			FPlayer player = data.createPlayer();
			player.setName( mSrc.readString( version.contains( FileVersion.QUESTS) ? DataBitHelper.NAME_LENGTH : DataBitHelper.BYTE));
			if (data.mHardcore) {
				player.mLives = mSrc.readData( DataBitHelper.LIVES);
			}
			boolean complex = version.contains( FileVersion.SETS);
			if (data.mQuest && version.contains( FileVersion.QUESTS)) {
				if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
					data.mServer = complex;
					data.mTicks = mSrc.readData( DataBitHelper.TICKS);
					data.mHours = mSrc.readData( DataBitHelper.HOURS);
				}
				if (mSrc.readBoolean()) {
					player.mSelectedQuest = mSrc.readData( DataBitHelper.QUESTS);
					player.mSelectedTask = mSrc.readData( DataBitHelper.TASKS);
				}
				else {
					player.mSelectedQuest = -1;
					player.mSelectedTask = -1;
				}
				if (version.contains( FileVersion.LORE_AUDIO)) {
					player.mPlayedLore = mSrc.readBoolean();
					player.mReceivedBook = mSrc.readBoolean();
				}
				if (version.contains( FileVersion.TEAMS) && !mSrc.readBoolean()) {
					player.mTeam = TeamOfIdx.get( data, mSrc.readData( DataBitHelper.TEAMS));
				}
				else {
					player.mTeam = data.createTeam();
					player.mTeam.mName = player.mName;
					player.mTeam.createQuestData();
					player.mTeam.createReputation();
					readTeam( player.mTeam, version);
				}
				if (complex && version.contains( FileVersion.BAG_LIMITS)) {
					readGroups( player);
				}
			}
			if (version.contains( FileVersion.DEATHS)) {
				readPlayerStats( player, complex);
			}
			if (!complex) {
				readTeamStats( data);
			}
		}
	}

	private void readPlayerStats( FPlayer player, boolean complex) {
		if (complex) {
			FPlayerStats stats = player.createStats();
			readDeath( stats);
		}
		else {
			int count = mSrc.readData( DataBitHelper.PLAYERS);
			for (int i = 0; i < count; ++i) {
				FPlayerStats stats = player.createStats();
				stats.mName = mSrc.readString( DataBitHelper.NAME_LENGTH);
				readDeath( stats);
			}
		}
	}

	private void readQuestData( FQuestData questData, FQuest quest, FileVersion version) {
		questData.mCompleted = mSrc.readBoolean();
		questData.mClaimed = version.contains( FileVersion.REPUTATION) && mSrc.readBoolean();
		if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
			questData.mAvailable = mSrc.readBoolean();
			questData.mTime = mSrc.readData( DataBitHelper.HOURS);
			if (questData.mCompleted && quest.mRepeatInfo.mType == RepeatType.NONE) {
				questData.mAvailable = false;
			}
		}
		else if (quest.mRepeatInfo.mType != RepeatType.INSTANT) {
			questData.mAvailable = !questData.mCompleted;
		}
		if (questData.mCompleted) {
			for (int i = 0; i < questData.mReward.length; ++i) {
				if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
					questData.mReward[i] = mSrc.readBoolean();
				}
				else {
					questData.mReward[i] = !mSrc.readBoolean() && questData.mCompleted;
				}
			}
		}
		int size = SizeOf.getTasks( quest);
		int count = mSrc.readData( DataBitHelper.TASKS);
		for (int id = 0; id < count; id++) {
			int idx = mSrc.readData( DataBitHelper.TASK_TYPE);
			if (id < size) {
				AQuestTask task = TaskOfID.get( quest, id);
//				questData.mTasks[id] = task.validateData( questData.mTasks[id]);
//				task.read( mSrc, questData.mTasks[id], version, false);
			}
			else {
				try {
					TaskTyp type = TaskTyp.get( idx);
					AQuestTask task = quest.createQuestTask( type);
//					--nextTaskId;
//					Constructor<AQuestTask> constructor2 = task.getDataType().getConstructor( new Class[] {
//						hardcorequesting.quests.QuestTask.class
//					});
//					AQuestTask obj2 = constructor2.newInstance( new Object[] {
//						task
//					});
//					questData.mTasks[id] = mSrc.readBoolean();
//					task.read( mSrc, (QuestDataTask) obj2, version, false);
//					task.onDelete();
				}
				catch (Exception ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}
	}

	void readSrc( FData data) {
		FileVersion version = FileVersion.get( mSrc.readByte());
		data.setVersion( version);
		data.mHardcore = mSrc.readBoolean();
		data.mQuest = mSrc.readBoolean();
		if (version.contains( FileVersion.TEAMS)) {
			readTeams( data, version);
		}
		readPlayers( data, version);
	}

	private void readTeam( FTeam team, FileVersion version) {
		if (version.contains( FileVersion.TEAMS) && !team.isSingle()) {
			readTeamData( team, version);
		}
		int playerCount = team.getPlayerCount();
		FQuestSetCat questSetCat = team.mParentData.mHqm.mQuestSetCat;
		for (int id = 0; id < SizeOfQuests.get( questSetCat); ++id) {
			FQuest quest = QuestOfID.get( questSetCat, id);
			FQuestData data = team.mQuestData.get( id);
			if (quest != null && data != null) {
				data.preRead( playerCount);
			}
		}
		int count = mSrc.readData( DataBitHelper.QUESTS);
		for (int i = 0; i < count; ++i) {
			int id = mSrc.readData( DataBitHelper.QUESTS);
			FQuest quest = QuestOfID.get( questSetCat, id);
			int bits = -1;
			if (version.contains( FileVersion.REMOVED_QUESTS)) {
				bits = mSrc.readData( DataBitHelper.INT);
			}
			FQuestData questData = team.mQuestData.get( id);
			if (quest != null && questData != null) {
				readQuestData( questData, quest, version);
			}
			else if (version.contains( FileVersion.REMOVED_QUESTS)) {
				mSrc.readData( bits);
			}
		}
		team.createReputation();
		if (version.contains( FileVersion.REPUTATION)) {
			int reputationCount = mSrc.readData( DataBitHelper.REPUTATION);
			for (int i = 0; i < reputationCount; i++) {
				int id = mSrc.readData( DataBitHelper.REPUTATION);
				int value = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
				if (ReputationOfID.get( (FReputationCat) null, id) != null) {
					team.setReputation( id, Integer.valueOf( value));
				}
			}
		}
	}

	private void readTeamData( FTeam team, FileVersion version) {
		team.mName = mSrc.readString( DataBitHelper.NAME_LENGTH);
		team.mID = mSrc.readData( DataBitHelper.TEAMS);
		if (version.contains( FileVersion.TEAM_SETTINGS)) {
			team.mLifeSetting = LifeSetting.get( mSrc.readData( DataBitHelper.TEAM_LIVES_SETTING));
			team.mRewardSetting = RewardSetting.get( mSrc.readData( DataBitHelper.TEAM_REWARD_SETTING));
			if (team.mRewardSetting == RewardSetting.ALL) {
				team.mRewardSetting = RewardSetting.getDefault();
			}
		}
		team.mPlayers.clear();
		int count = mSrc.readData( DataBitHelper.PLAYERS);
		for (int i = 0; i < count; ++i) {
			String name = mSrc.readString( DataBitHelper.NAME_LENGTH);
			if (name == null) {
				name = "Unknown";
			}
			boolean inTeam = mSrc.readBoolean();
			boolean owner = inTeam && mSrc.readBoolean();
			team.mPlayers.add( new PlayerEntry( name, inTeam, owner));
		}
	}

	private void readTeams( FData data, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.TEAMS);
		for (int i = 0; i < count; ++i) {
			FTeam team = data.createTeam();
			team.createQuestData();
			team.createReputation();
			readTeam( team, version);
		}
	}

	private void readTeamStats( FData data) {
		int count = mSrc.readData( DataBitHelper.TEAMS);
		for (int i = 0; i < count; ++i) {
			FTeam team = TeamOfIdx.get( data, i);
			FTeamStats stata = team.mStats;
			stata.mName = mSrc.readString( DataBitHelper.NAME_LENGTH);
			stata.mPlayers = mSrc.readData( DataBitHelper.PLAYERS);
			stata.mLives = mSrc.readData( DataBitHelper.TEAM_LIVES);
			stata.mProgress = mSrc.readData( DataBitHelper.TEAM_PROGRESS);
		}
	}
}
