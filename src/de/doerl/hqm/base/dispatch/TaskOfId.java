package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FQuest;

public class TaskOfId extends AHQMWorker<AQuestTask, Integer> {
	private static final TaskOfId WORKER = new TaskOfId();

	private TaskOfId() {
	}

	public static AQuestTask get( FQuest quest, int id) {
		return quest.forEachTask( WORKER, id);
	}

	@Override
	protected AQuestTask doTask( AQuestTask task, Integer id) {
		return task.getID() == id.intValue() ? task : null;
	}
}
