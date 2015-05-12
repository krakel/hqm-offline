package de.doerl.hqm.medium;

import de.doerl.hqm.base.dispatch.IWorker;

public interface IMediumWorker<T, U> extends IWorker {
	T forMedium( IMedium m, U p);
}
