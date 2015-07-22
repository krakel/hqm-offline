package de.doerl.hqm.base.data;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class TaskDataOfTask extends AHQMWorker<AQuestTaskData, FQuestData> {
	private static final TaskDataOfTask WORKER = new TaskDataOfTask();

	private TaskDataOfTask() {
	}

	public static AQuestTaskData get( FQuestData questData, AQuestTask task) {
		return task.accept( WORKER, questData);
	}

	@Override
	protected AQuestTaskData doTaskItems( AQuestTaskItems task, FQuestData questData) {
		return new FQuestTaskDataItems( questData, task);
	}

	@Override
	public AQuestTaskData forTaskDeath( FQuestTaskDeath task, FQuestData questData) {
		return new FQuestTaskDataDeath( questData, task);
	}

	@Override
	public AQuestTaskData forTaskLocation( FQuestTaskLocation task, FQuestData questData) {
		return new FQuestTaskDataLocation( questData, task);
	}

	@Override
	public AQuestTaskData forTaskMob( FQuestTaskMob task, FQuestData questData) {
		return new FQuestTaskDataMob( questData, task);
	}

	@Override
	public AQuestTaskData forTaskReputationKill( FQuestTaskReputationKill task, FQuestData questData) {
		return new FQuestTaskDataReputationKill( questData, task);
	}

	@Override
	public AQuestTaskData forTaskReputationTarget( FQuestTaskReputationTarget task, FQuestData questData) {
		return new FQuestTaskDataReputationTarget( questData, task);
	}
}
