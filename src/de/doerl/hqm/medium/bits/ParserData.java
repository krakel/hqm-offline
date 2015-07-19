package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FPlayer;
import de.doerl.hqm.base.data.FPlayerStats;
import de.doerl.hqm.base.data.FTeam;
import de.doerl.hqm.base.data.FTeamStats;
import de.doerl.hqm.base.data.GroupData;
import de.doerl.hqm.base.data.PlayerEntry;
import de.doerl.hqm.base.data.QuestData;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.ReputationOfIdx;
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.base.dispatch.SizeOfQuests;
import de.doerl.hqm.base.dispatch.TeamOfIdx;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.LifeSetting;
import de.doerl.hqm.quest.RewardSetting;

class ParserData {
//	private static final Logger LOGGER = Logger.getLogger( ParserData.class.getName());
	private BitInputStream mSrc;

	public ParserData( InputStream is) throws IOException {
		mSrc = new BitInputStream( is);
	}

	private void loadData( FTeam team, FileVersion version, boolean light) {
		readTeamData( team, version, light);
//		int playerCount = team.getPlayerCount();
		if (light) {
			for (int i = 0; i < team.mQuestData.size(); ++i) {
				QuestData questData = team.mQuestData.get( i);
				if (questData != null) {
					FQuest quest = QuestOfID.get( (FQuestSetCat) null, i);
//					quest.preRead( playerCount, questData);
					readQuestData( questData, quest, version, true);
				}
			}
			for (int i = 0; i < SizeOf.getMember( (FReputationCat) null); i++) {
				if (ReputationOfIdx.get( (FReputationCat) null, i) != null) {
					team.setReputation( i, Integer.valueOf( mSrc.readData( DataBitHelper.REPUTATION_VALUE)));
				}
			}
		}
		else {
			for (int i = 0; i < SizeOfQuests.get( (FQuestSetCat) null); i++) {
				FQuest quest = QuestOfID.get( (FQuestSetCat) null, i);
				QuestData data = team.mQuestData.get( i);
				if (quest != null && data != null) {
//					quest.preRead( playerCount, data);
				}
			}
			int count = mSrc.readData( DataBitHelper.QUESTS);
			for (int i = 0; i < count; i++) {
				int id = mSrc.readData( DataBitHelper.QUESTS);
				FQuest quest = QuestOfID.get( (FQuestSetCat) null, id);
				int bits = -1;
				if (version.contains( FileVersion.REMOVED_QUESTS)) {
					bits = mSrc.readData( DataBitHelper.INT);
				}
				if (quest != null && team.mQuestData.get( id) != null) {
					readQuestData( team.mQuestData.get( id), quest, version, false);
					continue;
				}
				if (version.contains( FileVersion.REMOVED_QUESTS)) {
					mSrc.readData( bits);
				}
			}
			team.createReputation();
			if (version.contains( FileVersion.REPUTATION)) {
				int reputationCount = mSrc.readData( DataBitHelper.REPUTATION);
				for (int i = 0; i < reputationCount; i++) {
					int id = mSrc.readData( DataBitHelper.REPUTATION);
					int value = mSrc.readData( DataBitHelper.REPUTATION_VALUE);
					if (ReputationOfIdx.get( (FReputationCat) null, id) != null) {
						team.setReputation( id, Integer.valueOf( value));
					}
				}
			}
		}
	}

	private void loadTeamData( FTeam team, FileVersion version, boolean light) {
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
		for (int i = 0; i < count; i++) {
			String name = mSrc.readString( DataBitHelper.NAME_LENGTH);
			if (name == null) {
				name = "Unknown";
			}
			boolean inTeam = mSrc.readBoolean();
			boolean owner = inTeam && mSrc.readBoolean();
			team.mPlayers.add( new PlayerEntry( name, inTeam, owner));
		}
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
					loadData( player.mTeam, version, !complex);
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

	private void readQuestData( QuestData questData, FQuest quest, FileVersion version, boolean light) {
		questData.mCompleted = mSrc.readBoolean();
		questData.mClaimed = version.contains( FileVersion.REPUTATION) && mSrc.readBoolean();
		if (version.contains( FileVersion.REPEATABLE_QUESTS)) {
			questData.mAvailable = mSrc.readBoolean();
			questData.mTime = mSrc.readData( DataBitHelper.HOURS);
//			if (questData.mCompleted && repeatInfo.getType() == RepeatType.NONE) {
//				questData.mAvailable = false;
//			}
		}
//		else if (repeatInfo.getType() != RepeatType.INSTANT) {
//			questData.mAvailable = !questData.mCompleted;
//		}
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
		if (light) {
//			for (int i = 0; i < tasks.size(); ++i) {
//				QuestTask task = tasks.get( i);
//				questData.tasks[i] = task.validateData( questData.tasks[i]);
//				task.read( mSrc, questData.tasks[i], version, true);
//			}
		}
		else {
			int count = mSrc.readData( DataBitHelper.TASKS);
			for (int i = 0; i < count; i++) {
//				int type = mSrc.readData( DataBitHelper.TASK_TYPE);
//				if (i >= tasks.size()) {
//					try {
//						Class clazz = taskTypes[type].clazz;
//						Constructor constructor = clazz.getConstructor( new Class[] {
//							hardcorequesting.quests.Quest.class, java.lang.String.class, java.lang.String.class
//						});
//						Object obj = constructor.newInstance( new Object[] {
//							this, "Fake", "Fake"
//						});
//						QuestTask task = (QuestTask) obj;
//						--nextTaskId;
//						Constructor constructor2 = task.getDataType().getConstructor( new Class[] {
//							hardcorequesting.quests.QuestTask.class
//						});
//						Object obj2 = constructor2.newInstance( new Object[] {
//							task
//						});
//						task.read( mSrc, (QuestDataTask) obj2, version, false);
//						task.onDelete();
//					}
//					catch (Exception ex) {
//						ex.printStackTrace();
//					}
//				}
//				else {
//					QuestTask task = tasks.get( i);
//					questData.tasks[i] = task.validateData( questData.tasks[i]);
//					task.read( mSrc, questData.tasks[i], version, false);
//				}
			}
		}
	}

	void readSrc( FData data) {
		FileVersion version = FileVersion.get( mSrc.readByte());
		data.setVersion( version);
		if (!version.contains( FileVersion.SETS)) {
			data.mHardcore = mSrc.readBoolean();
			data.mQuest = mSrc.readBoolean();
		}
		if (version.contains( FileVersion.QUESTS)) {
			data.mHardcore = mSrc.readBoolean();
			data.mQuest = mSrc.readBoolean();
			if (version.contains( FileVersion.TEAMS)) {
				readTeams( data, version, false);
			}
			readPlayers( data, version);
		}
	}

	private void readTeamData( FTeam team, FileVersion version, boolean light) {
		if (light) {
			team.setId( mSrc.readBoolean() ? -1 : 0);
			team.mReloadedInvites = true;
			if (team.isSingle()) {
				int count = mSrc.readData( DataBitHelper.TEAMS);
				if (count != 0) {
					for (int i = 0; i < count; ++i) {
						FTeam tt = new FTeam( null);
						loadTeamData( tt, version, true);
						team.mInvites.add( tt);
					}
				}
			}
		}
		if (version.contains( FileVersion.TEAMS) && !team.isSingle()) {
			loadTeamData( team, version, light);
		}
		if (light && !team.isSingle() && team.isSharingLives()) {
			team.mClientTeamLives = mSrc.readData( DataBitHelper.TEAM_LIVES);
		}
	}

	private void readTeams( FData data, FileVersion version, boolean light) {
		int count = mSrc.readData( DataBitHelper.TEAMS);
		for (int i = 0; i < count; ++i) {
			FTeam team = data.createTeam();
//			team.loadData( version, mSrc, false);
			loadData( team, version, light);
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
