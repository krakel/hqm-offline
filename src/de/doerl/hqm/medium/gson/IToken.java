package de.doerl.hqm.medium.gson;

interface IToken {
	// QUEST_SET_ADAPTER
	static final String QUEST_SET_NAME = "name";
	static final String QUEST_SET_DECR = "description";
	static final String QUEST_SET_QUESTS = "quests";
	static final String QUEST_SET_BARS = "reputationBar";
	static final String QUEST_SET_REPUTATION_BAR_OLD = "reputation";
	// REPUTATION_ADAPTER
	static final String REPUTATION_UUID = "id";
	static final String REPUTATION_NAME = "name";
	static final String REPUTATION_NEUTRAL = "neutral";
	static final String REPUTATION_MARKERS = "markers";
	// REPUTATION_MARKER_ADAPTER
	static final String MARKER_NAME = "name";
	static final String MARKER_VALUE = "value";
	// QUEST_ADAPTER
	static final String QUEST_UUID = "uuid";
	static final String QUEST_NAME = "name";
	static final String QUEST_DESC = "description";
	static final String QUEST_X = "x";
	static final String QUEST_Y = "y";
	static final String QUEST_ICON = "icon";
	static final String QUEST_BIG = "bigicon";
	static final String QUEST_REQUIREMENTS = "requirements";
	static final String QUEST_PREREQUISITES = "prerequisites";
//	static final String QUEST_OPTIONS = "options";
	static final String QUEST_OPTION_LINKS = "optionlinks";
	static final String QUEST_REPEAT_INFO = "repeat";
	static final String QUEST_TRIGGER_TYPE = "trigger";
	static final String QUEST_TRIGGER_TASKS = "triggertasks";
	static final String QUEST_PARENT_REQUIREMENT = "parentrequirement";
	static final String QUEST_TASKS = "tasks";
	static final String QUEST_REWARD = "reward";
	static final String QUEST_CHOICE = "rewardchoice";
	static final String QUEST_COMMANDS = "commandrewards";
	static final String QUEST_REP_REWRDS = "reputationrewards";
	// REPEAT_INFO_ADAPTER
	static final String REPEAT_INFO_TYPE = "type";
	static final String REPEAT_INFO_HOURS = "hours";
	static final String REPEAT_INFO_DAYS = "todaystal";
	// TASK_ADAPTER
	static final String TASK_TYPE = "type";
	static final String TASK_NAME = "description";
	static final String TASK_DESC = "longDescription";
	static final String TASK_REQUIREMENTS = "items";
	static final String TASK_REPUTATIONS = "reputation";
	static final String TASK_DEATHS = "deaths";
	static final String TASK_LOCATIONS = "locations";
	static final String TASK_MOBS = "mobs";
	static final String TASK_KILLS = "kills";
	// LOCATION_ADAPTER
	static final String LOCATION_X = "x";
	static final String LOCATION_Y = "y";
	static final String LOCATION_Z = "z";
	static final String LOCATION_DIM = "dim";
	static final String LOCATION_ICON = "icon";
	static final String LOCATION_RADIUS = "radius";
	static final String LOCATION_VISIBLE = "visible";
	static final String LOCATION_NAME = "name";
	// MOB_ADAPTER
	static final String MOB_COUNT = "kills";
	static final String MOB_EXACT = "exact";
	static final String MOB_OBJECT = "mob";
	static final String MOB_ICON = "icon";
	static final String MOB_NAME = "name";
	// REPUTATION_TASK_ADAPTER
	static final String SETTING_REPUTATION = "reputation";
	static final String SETTING_LOWER = "lower";
	static final String SETTING_UPPER = "upper";
	static final String SETTING_INVERTED = "inverted";
	// ITEM_REQUIREMENT_ADAPTER
	static final String REQUIREMENT_ITEM = "item";
	static final String REQUIREMENT_FLUID = "fluid";
	static final String REQUIREMENT_REQUIRED = "required";
	static final String REQUIREMENT_PRECISION = "precision";
	// FLUID, ITEM_STACK, NBT_TAG_COMPOUND
	static final String ITEM_NAME = "id";
	static final String ITEM_DAMAGE = "damage";
	static final String ITEM_SIZE = "amount";
	static final String ITEM_NBT = "nbt";
	// REPUTATION_BAR_ADAPTER
	static final String REPUTATION_BAR_REP = "reputationId";
	static final String REPUTATION_BAR_X = "x";
	static final String REPUTATION_BAR_Y = "y";
	// REPUTATION_REWARD_ADAPTER
	static final String REWARD_REPUTATION = "reputation";
	static final String REWARD_VALUE = "value";
	// GROUP_TIER_ADAPTER
	static final String GROUP_TIER_NAME = "name";
	static final String GROUP_TIER_COLOR = "colour";
	static final String GROUP_TIER_WEIGHTS = "weights";
	static final String GROUP_TIER_GROUPS = "groups";
	// GROUP_ADAPTER
	static final String GROUP_UUID = "id";
	static final String GROUP_NAME = "name";
	static final String GROUP_LIMIT = "limit";
	static final String GROUP_STACKS = "items";
	// DEATH_STATS_ADAPTER
	//
	static final String HQM_QUESTING = "questing";
	static final String HQM_HARDCORE = "hardcore";
	// TEAM_ADAPTER
	static final String TEAM_ID = "id";
	static final String TEAM_NAME = "name";
	static final String TEAM_LIFE_SETTING = "lifeSetting";
	static final String TEAM_REWARD_SETTING = "rewardSetting";
	static final String TEAM_PLAYERS = "players";
	static final String TEAM_REPUTATIONS = "reputations";
	static final String TEAM_REP_ID = "reputationId";
	static final String TEAM_REP_VAL = "reputationValue";
	static final String TEAM_QUEST_DATA_LIST = "questDataList";
	static final String TEAM_QUEST_ID = "questId";
	static final String TEAM_QUEST_DATA = "questData";
	static final String TEAM_INVITES = "invites";
	// QUESTING_DATA_ADAPTER
	static final String QUESTING_DATA_TEAM = "team";
	static final String QUESTING_DATA_LIVES = "lives";
	static final String QUESTING_DATA_UUID = "uuid";
	static final String QUESTING_DATA_NAME = "name";
	static final String QUESTING_DATA_GROUP_DATA = "groupData";
	static final String QUESTING_DATA_SELECTED_QUEST = "selectedQuest";
	static final String QUESTING_DATA_PLAYER_LORE = "playedLore";
	static final String QUESTING_DATA_RECEIVED_BOOK = "receivedBook";
	static final String QUESTING_DATA_DEATHS = "deaths";
	// QUEST_DATA_ADAPTER
	static final String QUEST_DATA_PLAYERS = "players";
	static final String QUEST_DATA_REWARDS = "rewards";
	static final String QUEST_DATA_COMPLETED = "completed";
	static final String QUEST_DATA_CLAIMED = "claimed";
	static final String QUEST_DATA_TASKS = "tasks";
	static final String QUEST_DATA_TASKS_SIZE = "tasksSize";
	static final String QUEST_DATA_AVAILABLE = "available";
	static final String QUEST_DATA_TIME = "time";
	// QUEST_DATA_TASK_ADAPTER
	static final String QUEST_DATA_TASK_TYPE = "type";
}
