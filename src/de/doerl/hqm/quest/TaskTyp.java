package de.doerl.hqm.quest;

public enum TaskTyp {
	TASK_ITEMS_CONSUME( "Consume Task", "A task where the player can hand in items or fluids. One can also use the Quest Delivery System to submit items and fluids."),
	TASK_ITEMS_CRAFTING( "Crafting Task", "A task where the player has to craft specific items."),
	TASK_LOCATION( "Location Task", "A task where the player has to reach one or more locations."),
	TASK_ITEMS_CONSUME_QDS( "QDS Task", "A task where the player can hand in items or fluids. This is a normal consume task where manual submit has been disabled to teach the player about the QDS"),
	TASK_ITEMS_DETECT( "Detection Task", "A task where the player needs specific items. These do not have to be handed in, having them in one's inventory is enough."),
	TASK_MOB( "Killing Task", "A task where the player has to kill certain monsters."),
	TASK_DEATH( "Death Task", "A task where the player has to die a certain amount of times."),
	TASK_REPUTATION_TARGET( "Reputation Task", "A task where the player has to reach a certain reputation."),
	TASK_REPUTATION_KILL( "Rep Kill Task", "A task where the player has to kill other players with certain reputations.");
	private String mTitle;
	private String mDescr;

	private TaskTyp( String title, String desc) {
		mTitle = title;
		mDescr = desc;
	}

	public static TaskTyp get( int idx) {
		TaskTyp[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public String getDescr() {
		return mDescr;
	}

	public String getTitle() {
		return mTitle;
	}

	@Override
	public String toString() {
		return mTitle;
	}
}
