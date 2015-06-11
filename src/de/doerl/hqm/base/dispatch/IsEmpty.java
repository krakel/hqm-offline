package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;

public class IsEmpty extends AHQMWorker<Boolean, Object> {
	private static final IsEmpty WORKER = new IsEmpty();

	private IsEmpty() {
	}

	public static boolean getRequirement( AQuestTaskItems task) {
		return task.forEachRequirement( WORKER, null) == null;
	}

	@Override
	protected Boolean doRequirement( ARequirement req, Object p) {
		return Boolean.TRUE;
	}
}
