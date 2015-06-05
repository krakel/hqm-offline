package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IDispatcher;
import de.doerl.hqm.base.dispatch.IHQMWorker;

public interface IBase extends IDispatcher {
	<T, U> T accept( IHQMWorker<T, U> w, U p);

	FHqm getHqm();
}
