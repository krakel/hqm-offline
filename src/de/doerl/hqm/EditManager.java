package de.doerl.hqm;

import java.io.File;
import java.util.Locale;

import de.doerl.hqm.utils.LoggingManager;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;

public class EditManager {
	static {
		Locale.setDefault( Locale.ENGLISH);
		PreferenceManager.init();
		LoggingManager.init();
		LoggingManager.setOut( "file", getLogFile(), "hqm.file.logLevel");
//		LoggingManager.setOut( "console", System.out, "hqm.console.logLevel");
		ImageLoader.init();
	}
//	private static final Logger LOGGER = Logger.getLogger( EditManager.class.getName());

	private EditManager() {
	}

	public static File getLogFile() {
		return new File( System.getProperty( "hqm.pref.dir"), "hqm" + Utils.getDateString() + ".log");
	}

	public static void init() {
	}

	public static void logVersion() {
//		Utils.log( LOGGER, Level.INFO, "java.version", System.getProperty( "java.version"));
	}
}
