package de.doerl.hqm.medium.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.IMediumWorker;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;

public class Medium implements IMedium {
	private static final Logger LOGGER = Logger.getLogger( Medium.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "JSON file", "json");
	public static final String MEDIUM = "json";
	public static final String JSON_PATH = "json_path";

	static File normalize( File choose) {
		return MediumUtils.normalize( choose, ".json");
	}

	static boolean readHqm( FHqm hqm, InputStream is, ICallback cb) {
		try {
			Parser parser = new Parser( is);
			try {
				parser.readSrc( hqm, cb);
				return true;
			}
			finally {
				parser.closeSrc();
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
		return false;
	}

	static boolean writeHQM( FHqm hqm, OutputStream os, ICallback cb) {
		try {
			MediaManager.setProperty( hqm, JSON_PATH, hqm.getURI());
			Serializer serializer = new Serializer( os);
			try {
				serializer.writeDst( hqm, cb);
				return true;
			}
			finally {
				serializer.closeDst();
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
		return false;
	}

	@Override
	public <T, U> T accept( IMediumWorker<T, U> w, U p) {
		return w.forMedium( this, p);
	}

	@Override
	public String getName() {
		return MEDIUM;
	}

	@Override
	public IRefreshListener getOpen( ICallback cb) {
		return new OpenJSON( cb);
	}

	@Override
	public IRefreshListener getSave( ICallback cb) {
		return new SaveJSON( cb);
	}

	@Override
	public IRefreshListener getSaveAs( ICallback cb) {
		return new SaveAsJSON( cb);
	}

	@Override
	public FHqm open( InputStream is, URI uri, ICallback cb) {
		try {
			FHqm def = new FHqm( uri);
			readHqm( def, is, cb);
			return def;
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
		return null;
	}

	@Override
	public void save( OutputStream os, FHqm hqm, ICallback cb) {
		try {
			writeHQM( hqm, os, cb);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
	}
}
