package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FQuestTaskReputationTarget extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( FQuestTaskReputationTarget.class.getName());
	private Vector<FSetting> mSettings = new Vector<FSetting>();

	public FQuestTaskReputationTarget( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskReputationTarget( this, p);
	}

	public FSetting createSetting() {
		FSetting res = new FSetting( this);
		mSettings.add( res);
		return res;
	}

	public <T, U> T forEachSetting( IHQMWorker<T, U> worker, U p) {
		for (FSetting disp : mSettings) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK_REPUTATION_TARGET;
	}
}
