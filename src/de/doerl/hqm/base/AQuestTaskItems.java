package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.utils.Utils;

public abstract class AQuestTaskItems extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( AQuestTaskItems.class.getName());
	public Vector<ARequirement> mRequirements = new Vector<>();

	AQuestTaskItems( FQuest parent, String name) {
		super( parent, name);
	}

	public ARequirement createRequirement( boolean type) {
		ARequirement req = null;
		if (type) {
			req = new FItemRequirement( this);
		}
		else {
			req = new FFluidRequirement( this);
		}
		mRequirements.add( req);
		return req;
	}

	public <T, U> T forEachRequirement( IHQMWorker<T, U> worker, U p) {
		for (ARequirement disp : mRequirements) {
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
