package de.doerl.hqm.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class LoggingManager {
	private static final String BUNDLE_HQM = "de.doerl.hqm.resources.message";
	public static final Logger ROOT;
	public static final String ROOT_LOGGER = "de.doerl.hqm";
	private static Filter sFilter = null;
	private static Map<Object, Handler> sHandler = new HashMap<>();
	static {
		ROOT = createRootLogger( ROOT_LOGGER, BUNDLE_HQM);
		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override
			public void run() {
				ROOT.setLevel( Level.OFF);
				synchronized (sHandler) {
					for (Handler hdl : sHandler.values()) {
						if (hdl != null) {
							ROOT.removeHandler( hdl);
							hdl.setFilter( null);
							hdl.close();
						}
					}
				}
			}
		});
	}

	private static Logger createRootLogger( String name, String bundle) {
		Logger result = null;
		try {
			result = Logger.getLogger( name, bundle);
		}
		catch (Exception ex) {
			System.err.println( "Error LoggingManager: " + ex.getMessage());
		}
		if (result == null) {
			result = Logger.getLogger( name);
		}
		result.setLevel( Level.ALL);
		result.setUseParentHandlers( false);
		return result;
	}

	public static void init() {
	}

	public static void setDebug( String dbgProp, String lvlProp) {
		if (dbgProp != null && Boolean.getBoolean( dbgProp)) {
			try {
				String level = null;
				if (lvlProp != null) {
					level = System.getProperty( lvlProp);
				}
				if (level != null) {
					ROOT.setLevel( Level.parse( level.toUpperCase()));
				}
				else {
					ROOT.setLevel( Level.ALL);
				}
			}
			catch (Exception ex) {
				System.err.println( "Error LoggingManager: " + ex.getMessage());
				ROOT.setLevel( Level.OFF);
			}
		}
		else {
			ROOT.setLevel( Level.OFF);
		}
	}

	public static void setFilter( Filter filter) {
		synchronized (sHandler) {
			for (Handler hdl : sHandler.values()) {
				if (hdl != null) {
					hdl.setFilter( filter);
				}
			}
		}
		sFilter = filter;
	}

	public static void setOut( Object key, File dst, String lvlProp) {
		try {
			File parrent = dst.getParentFile();
			if (parrent != null && !parrent.exists()) {
				parrent.mkdir();
			}
			setOut( key, new FileOutputStream( dst), lvlProp);
		}
		catch (Exception ex) {
			System.err.println( "error LoggingManager: " + ex.getMessage());
		}
	}

	public static void setOut( Object key, OutputStream out, String lvlProp) {
		Handler hdl;
		synchronized (sHandler) {
			hdl = sHandler.remove( key);
		}
		if (hdl != null) {
			ROOT.removeHandler( hdl);
			hdl.setFilter( null);
			hdl.close();
		}
		if (out != null) {
			hdl = new LogHandler( out, new SingleLineFormatter());
			hdl.setFilter( sFilter);
			try {
				String level = null;
				if (lvlProp != null) {
					level = System.getProperty( lvlProp);
				}
				if (level != null) {
					hdl.setLevel( Level.parse( level));
				}
				else {
					hdl.setLevel( Level.ALL);
				}
			}
			catch (Exception ex) {
				System.err.println( "Error LoggingManager: " + ex.getMessage());
				hdl.setLevel( Level.OFF);
			}
			sHandler.put( key, hdl);
			ROOT.addHandler( hdl);
		}
	}

	private static class LogHandler extends StreamHandler {
		public LogHandler( OutputStream out, Formatter formatter) {
			super( out, formatter);
		}

		@Override
		public boolean isLoggable( LogRecord record) {
			int levelValue = getLevel().intValue();
			if (record.getLevel().intValue() < levelValue || levelValue == Level.OFF.intValue()) {
				return false;
			}
			Filter filter = getFilter();
			if (filter == null) {
				return true;
			}
			return filter.isLoggable( record);
		}

		@Override
		public synchronized void publish( LogRecord record) {
			super.publish( record);
			flush();
		}
	}
}
