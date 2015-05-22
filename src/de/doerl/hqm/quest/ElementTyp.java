package de.doerl.hqm.quest;


public enum ElementTyp {
	HQM( "HQM"),
	QUEST( "Quest"),
	QUEST_SET( "QuestSet"),
	QUEST_SET_CAT( "QuestSetCat"),
	QUEST_TASK_DEATH( "TaskDeath"),
	QUEST_TASK_ITEMS_CONSUME( "TaskItemsConsume"),
	QUEST_TASK_ITEMS_CONSUME_QDS( "TaskItemsConsumeQDS"),
	QUEST_TASK_ITEMS_CRAFTING( "TaskItemsCrafting"),
	QUEST_TASK_ITEMS_DETECT( "TaskItemsDetect"),
	QUEST_TASK_LOCATION( "TaskLocation"),
	QUEST_TASK_MOB( "TaskMob"),
	QUEST_TASK_REPUTATION_KILL( "TaskReputationKill"),
	QUEST_TASK_REPUTATION_TARGET( "TaskReputationTarget"),
	REPUTATION( "Reputation"),
	REPUTATION_CAT( "ReputationCat"),
	REPUTATION_MARKER( "Marker"),
	REPUTATION_REWARD( "Reward"),
	REPUTATION_SETTING( "Setting"),
	LOCATION( "Location"),
	MOB( "Mob"),
	REPEAT_INFO( "RepeatInfo"),
	ITEM_REQUIREMENT( "ItemRequirement"),
	FLUID_REQUIREMENT( "FluidRequirement"),
	GROUP( "Group"),
	GROUP_CAT( "GroupCat"),
	GROUP_TIER( "GroupTier"),
	GROUP_TIER_CAT( "GroupTierCat");
	private String mToken;

	private ElementTyp( String token) {
		mToken = token;
	}

	public static ElementTyp get( int idx) {
		ElementTyp[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public String getToken() {
		return mToken;
	}
}
