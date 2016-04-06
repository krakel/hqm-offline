package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.data.AQuestTaskData;
import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FPlayer;
import de.doerl.hqm.base.data.FPlayerStats;
import de.doerl.hqm.base.data.FQuestData;
import de.doerl.hqm.base.data.FQuestTaskDataDeath;
import de.doerl.hqm.base.data.FQuestTaskDataItems;
import de.doerl.hqm.base.data.FQuestTaskDataLocation;
import de.doerl.hqm.base.data.FQuestTaskDataMob;
import de.doerl.hqm.base.data.FQuestTaskDataReputationKill;
import de.doerl.hqm.base.data.FQuestTaskDataReputationTarget;
import de.doerl.hqm.base.data.FTeamData;
import de.doerl.hqm.base.data.FTeamStats;
import de.doerl.hqm.base.data.GroupData;
import de.doerl.hqm.base.dispatch.ADataWorker;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.ReputationOfID;
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.base.dispatch.TaskOfID;
import de.doerl.hqm.base.dispatch.TeamOfID;
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

	@Override
	public Object forDataTaskDeath( FQuestTaskDataDeath taskData, FileVersion version) {
		taskData.mCompleted = mSrc.readBoolean();
		taskData.mDeaths = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forDataTaskItems( FQuestTaskDataItems taskData, FileVersion version) {
		taskData.mCompleted = mSrc.readBoolean();
		Vector<ARequirement> requirements = taskData.mTask.mRequirements;
		int count = mSrc.readData( DataBitHelper.TASK_ITEM_COUNT);
		for (int i = 0; i < count; ++i) {
			int progress = mSrc.readData( DataBitHelper.ITEM_PROGRESS);
			if (i < requirements.size()) {
				taskData.mProgress.add( Math.min( requirements.get( i).getCount(), Math.max( 0, progress)));
			}
		}
		return null;
	}

	@Override
	public Object forDataTaskLocation( FQuestTaskDataLocation taskData, FileVersion version) {
		taskData.mCompleted = mSrc.readBoolean();
		int count = mSrc.readData( DataBitHelper.TASK_LOCATION_COUNT);
		Vector<Boolean> visited = taskData.mVisited;
		for (int i = 0; i < count; ++i) {
			boolean val = mSrc.readBoolean();
			if (i < visited.size()) {
				visited.add( Boolean.valueOf( val));
			}
		}
		return null;
	}

	@Override
	public Object forDataTaskMob( FQuestTaskDataMob taskData, FileVersion version) {
		taskData.mCompleted = mSrc.readBoolean();
		Vector<FMob> mobs = taskData.mTask.mMobs;
		int count = mSrc.readData( DataBitHelper.TASK_MOB_COUNT);
		for (int i = 0; i < count; i++) {
			int val = mSrc.readData( DataBitHelper.KILL_COUNT);
			if (i < mobs.size()) {
				taskData.mKilled.add( Math.min( mobs.get( i).mKills, Math.max( 0, val)));
			}
		}
		return null;
	}

	@Override
	public Object forDataTaskReputationKill( FQuestTaskDataReputationKill taskData, FileVersion version) {
		taskData.mCompleted = mSrc.readBoolean();
		taskData.mKills = mSrc.readData( DataBitHelper.DEATHS);
		return null;
	}

	@Override
	public Object forDataTaskReputationTarget( FQuestTaskDataReputationTarget taskData, FileVersion p) {
		taskData.mCompleted = mSrc.readBoolean();
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
		int count = mSrc.readData( DataBitHelper.PLAYERS, version);
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
					player.mSelectedQuest = mSrc.readData( DataBitHelper.QUESTS, version);
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
					player.mTeam = TeamOfID.get( data, mSrc.readData( DataBitHelper.TEAMS));
				}
				else {
					FTeamData teamData = data.createTeam();
					teamData.setName( player.mName);
					teamData.setSingle( true);
					readTeamData( teamData, version);
					player.mTeam = teamData;
				}
				if (complex && version.contains( FileVersion.BAG_LIMITS)) {
					readGroups( player);
				}
			}
			if (version.contains( FileVersion.DEATHS)) {
				readPlayerStats( player, complex, version);
			}
			if (!complex) {
				readTeamStats( data, version);
			}
		}
	}

	private void readPlayerStats( FPlayer player, boolean complex, FileVersion version) {
		if (complex) {
			FPlayerStats stats = player.createStats();
			readDeath( stats);
		}
		else {
			int count = mSrc.readData( DataBitHelper.PLAYERS, version);
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
			for (int i = 0; i < questData.mParentTeamData.getPlayerCount(); ++i) {
				if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
					questData.mReward.add( mSrc.readBoolean());
				}
				else {
					questData.mReward.add( !mSrc.readBoolean() && questData.mCompleted);
				}
			}
		}
		int size = SizeOf.getTasks( quest);
		int count = mSrc.readData( DataBitHelper.TASKS);
		for (int id = 0; id < count; id++) {
			TaskTyp type = TaskTyp.get( mSrc.readData( DataBitHelper.TASK_TYPE, version));
			if (id < size) {
				AQuestTask task = TaskOfID.get( quest, id);
				if (Utils.different( type, task.getTaskTyp())) {
					Utils.log( LOGGER, Level.WARNING, "different TaskTyp for id {0}, need {1}, got {2}", id, type, task.getTaskTyp());
				}
				AQuestTaskData taskData = questData.createTaskData( task);
				taskData.accept( this, version);
			}
			else {
				AQuestTask task = quest.createQuestTask( type);
				AQuestTaskData taskData = questData.createTaskData( task);
				taskData.accept( this, version);
				taskData.remove();
				task.remove();
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

	private void readTeamData( FTeamData teamData, FileVersion version) {
		FQuestSetCat questSetCat = teamData.mParentData.mHqm.mQuestSetCat;
		int count = mSrc.readData( DataBitHelper.QUESTS, version);
		for (int i = 0; i < count; ++i) {
			int id = mSrc.readData( DataBitHelper.QUESTS, version);
			FQuest quest = QuestOfID.get( questSetCat, id);
			int bits = version.contains( FileVersion.REMOVED_QUESTS) ? mSrc.readData( DataBitHelper.INT) : 0;
			if (quest != null) {
				FQuestData questData = teamData.createQuestData( quest);
				readQuestData( questData, quest, version);
			}
			else if (version.contains( FileVersion.REMOVED_QUESTS)) {
				mSrc.readData( bits);
			}
		}
		if (version.contains( FileVersion.REPUTATION)) {
			readTeamReputations( teamData);
		}
	}

	private void readTeamPlayer( FTeamData teamData, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.PLAYERS, version);
		for (int i = 0; i < count; ++i) {
			String name = mSrc.readString( DataBitHelper.NAME_LENGTH);
			if (name == null) {
				name = "Unknown";
			}
			boolean inTeam = mSrc.readBoolean();
			boolean owner = inTeam && mSrc.readBoolean();
			teamData.createPlayer( name, inTeam, owner);
		}
	}

	private void readTeamReputations( FTeamData teamData) {
		int cpunt = mSrc.readData( DataBitHelper.REPUTATION);
		for (int i = 0; i < cpunt; ++i) {
			int id = mSrc.readData( DataBitHelper.REPUTATION);
			int value = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
			if (ReputationOfID.get( teamData.getData().mHqm, id) != null) {
				teamData.setReputation( id, Integer.valueOf( value));
			}
		}
	}

	private void readTeams( FData data, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.TEAMS);
		for (int i = 0; i < count; ++i) {
			String name = mSrc.readString( DataBitHelper.NAME_LENGTH);
			FTeamData teamData = data.createTeam( mSrc.readData( DataBitHelper.TEAMS));
			teamData.setName( name);
			teamData.setSingle( false);
			if (version.contains( FileVersion.TEAM_SETTINGS)) {
				teamData.mLifeSetting = LifeSetting.get( mSrc.readData( DataBitHelper.TEAM_LIVES_SETTING));
				teamData.mRewardSetting = RewardSetting.get( mSrc.readData( DataBitHelper.TEAM_REWARD_SETTING));
				if (teamData.mRewardSetting == RewardSetting.ALL) {
					teamData.mRewardSetting = RewardSetting.getDefault();
				}
			}
			readTeamPlayer( teamData, version);
			readTeamData( teamData, version);
		}
	}

	private void readTeamStats( FData data, FileVersion version) {
		int count = mSrc.readData( DataBitHelper.TEAMS);
		for (int i = 0; i < count; ++i) {
			FTeamData team = TeamOfIdx.get( data, i);
			FTeamStats stata = team.mStats;
			stata.mName = mSrc.readString( DataBitHelper.NAME_LENGTH);
			stata.mPlayers = mSrc.readData( DataBitHelper.PLAYERS, version);
			stata.mLives = mSrc.readData( DataBitHelper.TEAM_LIVES, version);
			stata.mProgress = mSrc.readData( DataBitHelper.TEAM_PROGRESS);
		}
	}
}
