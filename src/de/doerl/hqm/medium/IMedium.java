package de.doerl.hqm.medium;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.dispatch.IDispatcher;

public interface IMedium extends IDispatcher {
	public static final String ACTIV_MEDIUM = "activ_medium";

	<T, U> T accept( IMediumWorker<T, U> w, U p);

	String getName();

	IRefreshListener getOpen( ICallback cb);

	IRefreshListener getSave( ICallback cb);

	IRefreshListener getSaveAs( ICallback cb);

	FHqm open( InputStream is, URI uri, ICallback cb);

	void save( OutputStream os, FHqm hqm, ICallback cb);
}
