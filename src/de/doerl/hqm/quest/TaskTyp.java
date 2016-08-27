package de.doerl.hqm.quest;

public enum TaskTyp {
	CONSUME( "Consume Task", "taskConsume.gif", "A task where the player can hand in items or fluids. One can also use the Quest Delivery System to submit items and fluids."),
	CRAFTING( "Crafting Task", "taskCrafting.gif", "A task where the player has to craft specific items."),
	LOCATION( "Location Task", "taskLocation.gif", "A task where the player has to reach one or more locations."),
	CONSUME_QDS( "QDS Task", "taskQDS.gif", "A task where the player can hand in items or fluids. This is a normal consume task where manual submit has been disabled to teach the player about the QDS"),
	DETECT( "Detection Task", "taskDetect.gif", "A task where the player needs specific items. These do not have to be handed in, having them in one's inventory is enough."),
	KILL( "Killing Task", "taskMob.gif", "A task where the player has to kill certain monsters."),
	DEATH( "Death Task", "taskDeath.gif", "A task where the player has to die a certain amount of times."),
	REPUTATION( "Reputation Task", "taskTarget.gif", "A task where the player has to reach a certain reputation."),
	REPUTATION_KILL( "Rep Kill Task", "taskKill.gif", "A task where the player has to kill other players with certain reputations.");
	private String mTitle;
	private String mIcon;
	private String mDescr;

	private TaskTyp( String title, String icon, String desc) {
		mTitle = title;
		mIcon = icon;
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

	public static TaskTyp parse( String name) {
		try {
			return valueOf( name);
		}
		catch (Exception ex) {
			return CONSUME;
		}
	}

	public String getDescr() {
		return mDescr;
	}

	public String getIcon() {
		return mIcon;
	}

	public String getTitle() {
		return mTitle;
	}
}
