package de.doerl.hqm;

import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.LoggingManager;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;

public class EditManager {
	private static final Logger LOGGER;
	static {
		Locale.setDefault( Locale.ENGLISH);
		PreferenceManager.init();
		LoggingManager.init();
		LoggingManager.setOut( "file", getLogFile(), "hqm.file.logLevel");
		LoggingManager.setOut( "console", System.out, "hqm.console.logLevel");
		LOGGER = Logger.getLogger( EditManager.class.getName());
		ImageLoader.init();
	}

	private EditManager() {
	}

	public static void closeEditor() {
	}

	public static File getLogFile() {
		return new File( System.getProperty( "hqm.pref.dir"), "hqm" + Utils.getDateString() + ".log");
	}

	public static void init() {
	}

	public static void logVersion() {
		String javaVersion = System.getProperty( "java.version");
		Utils.log( LOGGER, Level.INFO, "java.version", javaVersion);
	}
}
