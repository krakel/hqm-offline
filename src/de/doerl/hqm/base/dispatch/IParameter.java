package de.doerl.hqm.base.dispatch;

public interface IParameter extends IDispatcher {
	<T, U> T accept( IParameterWorker<T, U> w, U p);
}
