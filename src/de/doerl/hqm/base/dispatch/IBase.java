package de.doerl.hqm.base.dispatch;

public interface IBase extends IDispatcher {
	<T, U> T accept( IHQMWorker<T, U> w, U p);
}
