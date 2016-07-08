package de.doerl.hqm.base;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.utils.Utils;

public abstract class AQuestTaskReputation extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( AQuestTaskReputation.class.getName());
	public final ArrayList<FSetting> mSettings = new ArrayList<>();

	public AQuestTaskReputation( FQuest parent) {
		super( parent);
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
}
