package de.doerl.hqm;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.ImageLoader;
import de.doerl.hqm.utils.LoggingManager;
import de.doerl.hqm.utils.MinecraftHandler;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;

public class EditManager {
	private static final Logger LOGGER;
	static {
		Locale.setDefault( Locale.ENGLISH);
		LoggingManager.init();
		LoggingManager.setOut( "console", System.out, "hqm.console.logLevel");
		LOGGER = Logger.getLogger( EditManager.class.getName());
		PreferenceManager.init();
		ImageLoader.addHandler( new MinecraftHandler());
		ImageLoader.SINGLETON.start();
	}

	private EditManager() {
	}

	public static void closeEditor() {
	}

	public static void init() {
	}

	public static void logVersion() {
		String javaVersion = System.getProperty( "java.version");
		Utils.log( LOGGER, Level.INFO, "java.version", javaVersion);
	}
}
