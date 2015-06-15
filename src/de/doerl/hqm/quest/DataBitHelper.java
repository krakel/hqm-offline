package de.doerl.hqm.quest;

public enum DataBitHelper {
	BYTE( 8),
	SHORT( 16),
	INT( 32),
	BOOLEAN( 1),
	EMPTY( 0),
	NBT_LENGTH( 15),
	PACKET_ID( 5),
	NAME_LENGTH( 5),
	PLAYERS( 16) {
		@Override
		public int getBitCount( FileVersion version) {
			return version.contains( FileVersion.REPEATABLE_QUESTS) ? getCount() : 10;
		}
	},
	QUESTS( 10) {
		@Override
		public int getBitCount( FileVersion version) {
			return version.contains( FileVersion.SETS) ? getCount() : 7;
		}
	},
	TASKS( 4),
	REWARDS( 3),
	QUEST_SETS( 5),
	ITEM_PROGRESS( 30),
	QUEST_NAME_LENGTH( 5),
	QUEST_DESCRIPTION_LENGTH( 16),
	QUEST_POS_X( 9),
	QUEST_POS_Y( 8),
	TASK_TYPE( 4) {
		@Override
		public int getBitCount( FileVersion version) {
			return version.contains( FileVersion.REPUTATION_KILL) ? getCount() : 3;
		}
	},
	TASK_ITEM_COUNT( 6, 35),
	TASK_REQUIREMENT( 32),
	QUEST_REWARD( 3),
	ITEM_PRECISION( 2),
	GROUP_ITEMS( 6),
	GROUP_COUNT( 10),
	TIER_COUNT( 7),
	WEIGHT( 19),
	COLOR( 4),
	PASS_CODE( 7),
	LIMIT( 10),
	TEAMS( 10),
	TEAM_ACTION_ID( 4),
	LIVES( 8),
	TEAM_LIVES( 0) {
		@Override
		public int getBitCount( FileVersion version) {
			return PLAYERS.getBitCount( version) + LIVES.getBitCount( version);
		}
	},
	TEAM_ERROR( 2),
	TEAM_REWARD_SETTING( 2),
	TEAM_LIVES_SETTING( 1),
	OP_ACTION( 3),
	BAG_TIER( 3, 4),
	DEATHS( 12),
	TEAM_PROGRESS( 7),
	TASK_LOCATION_COUNT( 3, 4),
	WORLD_COORDINATE( 32),
	LOCATION_VISIBILITY( 2),
	TICKS( 10),
	HOURS( 32),
	REPEAT_TYPE( 2),
	TRIGGER_TYPE( 2),
	TASK_MOB_COUNT( 3, 4),
	KILL_COUNT( 16),
	MOB_ID_LENGTH( 10),
	TRACKER_TYPE( 2),
	PORTAL_TYPE( 2),
	REPUTATION( 8),
	REPUTATION_VALUE( 32),
	REPUTATION_REWARD( 3),
	REPUTATION_SETTING( 3, 4),
	REPUTATION_MARKER( 5, 30);
	private int mCount;
	private boolean mMaximum;
	private int mCached;

	private DataBitHelper( int bitCount) {
		mCount = bitCount;
	}

	private DataBitHelper( int bitCount, int maximum) {
		this( bitCount);
		int calculatedMax = getMaximum();
		if (maximum < calculatedMax) {
			mCached = maximum;
		}
	}

	public int getBitCount( FileVersion version) {
		return mCount;
	}

	public final int getCount() {
		return mCount;
	}

	public int getMaximum() {
		if (!mMaximum) {
			mMaximum = true;
			mCached = (1 << mCount) - 1;
		}
		return mCached;
	}

	public String truncate( String txt) {
		int max = getMaximum();
		if (max < txt.length()) {
			return txt.substring( 0, max);
		}
		else {
			return txt;
		}
	}
}
