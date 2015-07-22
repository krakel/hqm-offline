package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FQuest;

public class TaskOfID extends AHQMWorker<AQuestTask, Integer> {
	private static final TaskOfID WORKER = new TaskOfID();

	private TaskOfID() {
	}

	public static AQuestTask get( FQuest quest, int id) {
		return quest.forEachTask( WORKER, id);
	}

	public static AQuestTask get( FQuest quest, String ident) {
		return quest.forEachTask( WORKER, AQuestTask.fromIdent( ident));
	}

	@Override
	protected AQuestTask doTask( AQuestTask task, Integer id) {
		return task.getID() == id.intValue() ? task : null;
	}
}
