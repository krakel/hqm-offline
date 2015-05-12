package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.utils.Utils;

public abstract class AQuestTaskReputation extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( AQuestTaskReputation.class.getName());
	private Vector<FReputationSetting> mSettings = new Vector<FReputationSetting>();

	AQuestTaskReputation( FQuest parent, String name) {
		super( parent, name);
	}

	public FReputationSetting createSetting() {
		FReputationSetting res = new FReputationSetting( this);
		mSettings.add( res);
		return res;
	}

	public <T, U> T forEachSetting( IHQMWorker<T, U> worker, U p) {
		for (FReputationSetting disp : mSettings) {
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
}
