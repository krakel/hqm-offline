package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;

public interface IBase extends IDispatcher {
	<T, U> T accept( IHQMWorker<T, U> w, U p);

	FHqm getHqm();
}
