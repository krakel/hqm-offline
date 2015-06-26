package de.doerl.hqm.medium.json;

interface IToken {
	static final String HQM_VERSION = "version";
	static final String HQM_PASSCODE = "passcode";
	static final String HQM_DECRIPTION = "decription";
	static final String HQM_QUEST_SET_CAT = "questSetCatalog";
	static final String HQM_REPUTATION_CAT = "reputationCatalog";
	static final String HQM_QUESTS = "quests";
	static final String HQM_GROUP_TIER_CAT = "groupTierCatalog";
	static final String HQM_GROUP_CAT = "groupCatalog";
	//
	static final String QUEST_SET_NAME = "name";
	static final String QUEST_SET_DECR = "decription";
	//
	static final String REPUTATION_ID = "id";
	static final String REPUTATION_NAME = "name";
	static final String REPUTATION_NEUTRAL = "neutral";
	static final String REPUTATION_MARKERS = "markers";
	//
	static final String MARKER_NAME = "name";
	static final String MARKER_VALUE = "value";
	//
	static final String QUEST_ID = "id";
	static final String QUEST_NAME = "name";
	static final String QUEST_DESC = "description";
	static final String QUEST_X = "x";
	static final String QUEST_Y = "y";
	static final String QUEST_BIG = "big";
	static final String QUEST_SET = "setID";
	static final String QUEST_ICON = "icon";
	static final String QUEST_REQUIREMENTS = "requirements";
	static final String QUEST_OPTION_LINKS = "optionLinks";
	static final String QUEST_REPEAT_INFO = "repeatInfo";
	static final String QUEST_TRIGGER_TYPE = "triggerType";
	static final String QUEST_TRIGGER_TASKS = "triggerTasks";
	static final String QUEST_PARENT_REQUIREMENT_COUNT = "parentRequirementCount";
	static final String QUEST_TASKS = "tasks";
	static final String QUEST_REWARD = "reward";
	static final String QUEST_CHOICE = "choice";
	static final String QUEST_REP_REWRDS = "reputations";
	static final String QUEST_DELETED = "deleted";
	//
	static final String REPEAT_INFO_TYPE = "type";
	static final String REPEAT_INFO_TOTAL = "total";
	//
	static final String TASK_TYPE = "type";
	static final String TASK_NAME = "name";
	static final String TASK_DESC = "description";
	static final String TASK_REQUIREMENTS = "requirements";
	static final String TASK_DEATHS = "deaths";
	static final String TASK_LOCATIONS = "locations";
	static final String TASK_MOBS = "mobs";
	static final String TASK_KILLS = "kills";
	static final String TASK_SETTINGS = "settings";
	//
	static final String LOCATION_NAME = "name";
	static final String LOCATION_ICON = "icon";
	static final String LOCATION_X = "x";
	static final String LOCATION_Y = "y";
	static final String LOCATION_Z = "z";
	static final String LOCATION_RADIUS = "radius";
	static final String LOCATION_VISIBLE = "visible";
	static final String LOCATION_DIM = "dim";
	//
	static final String MOB_NAME = "name";
	static final String MOB_ICON = "icon";
	static final String MOB_MOB2 = "mob";
	static final String MOB_COUNT = "count";
	static final String MOB_EXACT = "exact";
	//
	static final String SETTING_REPUTATION = "reputationID";
	static final String SETTING_LOWER = "lowerID";
	static final String SETTING_UPPER = "upperID";
	static final String SETTING_INVERTED = "inverted";
	//
	static final String REQUIREMENT_REQUIRED = "required";
	static final String REQUIREMENT_PRECISION = "precision";
	//
	static final String ITEM_OBJECT = "item";
	static final String ITEM_NBT = "nbt";
	static final String FLUID_OBJECT = "fluid";
	//
	static final String REWARD_REPUTATION = "reputation";
	static final String REWARD_VALUE = "value";
	//
	static final String GROUP_TIER_ID = "id";
	static final String GROUP_TIER_NAME = "name";
	static final String GROUP_TIER_COLOR = "color";
	static final String GROUP_TIER_WEIGHTS = "weights";
	//
	static final String GROUP_ID = "id";
	static final String GROUP_NAME = "name";
	static final String GROUP_TIER = "tierID";
	static final String GROUP_LIMIT = "limit";
	static final String GROUP_STACKS = "stacks";
}
