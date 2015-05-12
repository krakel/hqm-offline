package de.doerl.hqm.quest;

import java.awt.GraphicsEnvironment;

import javax.swing.Icon;
import javax.swing.UIManager;

import de.doerl.hqm.utils.ResourceManager;

public enum ElementTyp {
	HQM( "HQM", "hqm"),
	QUEST( "Quest", "quest"),
	QUEST_SET( "QuestSet", "questSet"),
	QUEST_SETS( "QuestSets", "questSets"),
	QUEST_TASK_DEATH( "TaskDeath", "taskDeath"),
	QUEST_TASK_ITEMS_CONSUME( "TaskItemsConsume", "taskItemConsume"),
	QUEST_TASK_ITEMS_CONSUME_QDS( "TaskItemsConsumeQDS", "taskItemConsumeQDS"),
	QUEST_TASK_ITEMS_CRAFTING( "TaskItemsCrafting", "taskItemCrafting"),
	QUEST_TASK_ITEMS_DETECT( "TaskItemsDetect", "taskItemDetect"),
	QUEST_TASK_LOCATION( "TaskLocation", "taskLocation"),
	QUEST_TASK_MOB( "TaskMob", "taskMob"),
	QUEST_TASK_REPUTATION_KILL( "TaskReputationKill", "taskRepKill"),
	QUEST_TASK_REPUTATION_TARGET( "TaskReputationTarget", "taskRepTarget"),
	REPUTATION( "Reputation", "reputation"),
	REPUTATIONS( "Reputations", "reputations"),
	REPUTATION_MARKER( "Marker", "repMarker"),
	REPUTATION_REWARD( "Reward", "repReward"),
	REPUTATION_SETTING( "Setting", "repSetting"),
	LOCATION( "Location", "location"),
	MOB( "Mob", "mob"),
	REPEAT_INFO( "RepeatInfo", "repeatInfo"),
	ITEM_REQUIREMENT( "ItemRequirement", "itemReq"),
	FLUID_REQUIREMENT( "FluidRequirement", "fluidReq"),
	GROUP( "Group", "grp"),
	GROUPS( "Groups", "grps"),
	GROUP_TIER( "GroupTier", "grpTier"),
	GROUP_TIERS( "GroupTiers", "grpTiers"),
	PARAMETER_SETS( "ParameterSets", "paramSets"),
	PARAMETER_BOOLEAN( "ParameterBoolean", "paramBool"),
	PARAMETER_INTEGER( "ParameterInteger", "paramInt"),
	PARAMETER_INT( "ParameterInt", "paramInt"),
	PARAMETER_INT_ARR( "ParameterIntArr", "paramIntArr"),
	PARAMETER_STACK( "ParameterStack", "paramStk"),
	PARAMETER_STRING( "ParameterString", "paramString");
	private String mToken;
	private String mIconName;

	private ElementTyp( String token, String key) {
		mToken = token;
		mIconName = "hqm.icon." + key;
		ResourceManager.makeIcon( mIconName, key + ".gif");
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

	public Icon getIcon() {
		if (GraphicsEnvironment.isHeadless()) {
			return null;
		}
		return UIManager.getIcon( mIconName);
	}

	public String getToken() {
		return mToken;
	}
}
