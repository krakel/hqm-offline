package de.doerl.hqm.medium;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.dispatch.IDispatcher;

public interface IMedium extends IDispatcher {
	<T, U> T accept( IMediumWorker<T, U> w, U p);

	String getIcon();

	String getName();

	IRefreshListener getOpen( ICallback cb);

	IRefreshListener getSave( ICallback cb);

	IRefreshListener getSaveAs( ICallback cb);

	FHqm openHqm( File file);

	IMedium parse( String file);

	void testLoad( FHqm hqm, InputStream is) throws IOException;

	void testSave( FHqm hqm, OutputStream os) throws IOException;
}
