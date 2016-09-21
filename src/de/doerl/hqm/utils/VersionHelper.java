package de.doerl.hqm.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class VersionHelper implements Runnable {
	private static final String RESOURCE = "de.doerl.hqm.hqm";
	private static final String MC_VERSION = "Minecraft 1.7.10";
	private static final Logger LOGGER = Logger.getLogger( VersionHelper.class.getName());
	public static final String VERSION = ResourceManager.getVersion( RESOURCE);
	private static final String MOD_NAME = ResourceManager.getBundleString( "build.title", RESOURCE);
	private static final String REMOTE_VERSION = "https://github.com/krakel/krakel.github.io/raw/master/hqm-offline/bin/version.xml";
	private static final String VERSION_INIT = "Initializing version check against remote authority file, located at {0}";
	private static final String VERSION_CURRENT = "Currently using the most up to date version ({2}) of {0} for {1}";
	private static final String VERSION_FINAL = "Version check stopping after three unsuccessful connection attempts";
	private static final String VERSION_ERROR = "Error while connecting to remote version authority file; trying again";
	private static final String VERSION_MC = "Unable to find a version of {0} for {1} in the remote authority file";
	private static final String VERSION_OUTDATED = "A new {0} version exists ({2}) for {1}. Get it here: {3}";
	private static final String VERSION_UNINITIALIZED = "Remote version check failed to initialize properly";
	private static final int VERSION_CHECK_ATTEMPTS = 3;
	private static final byte ID_UNINITIALIZED = 0;
	private static final byte ID_CURRENT = 1;
	private static final byte ID_OUTDATED = 2;
	private static final byte ID_ERROR = 3;
	private static final byte ID_FINAL = 4;
	private static final byte ID_MC_NOT_FOUND = 5;
	private static byte sResult = ID_UNINITIALIZED;
	private static String sLocation;
	private static String sVersion;

	private VersionHelper() {
	}

	private static void checkVersion() {
		sResult = ID_UNINITIALIZED;
		try {
			Properties props = readProperties();
			String version = props.getProperty( MC_VERSION);
			if (version == null) {
				sResult = ID_MC_NOT_FOUND;
				return;
			}
			String[] tokens = version.split( "\\|");
			if (tokens.length < 2) {
				sResult = ID_ERROR;
				return;
			}
			sVersion = tokens[0];
			sLocation = tokens[1];
			if (sVersion != null) {
				String current = VERSION.indexOf( '@') < 0 ? VERSION : "0.0"; // only for testing
				sResult = compareVersion( current, sVersion) < 0 ? ID_OUTDATED : ID_CURRENT;
			}
		}
		catch (Exception ex) {
			// ignore
		}
		finally {
			if (sResult == ID_UNINITIALIZED) {
				sResult = ID_ERROR;
			}
		}
	}

	private static int compareVersion( String s1, String s2) {
		String[] a1 = s1.split( "\\.");
		String[] a2 = s2.split( "\\.");
		int min = Math.min( a1.length, a2.length);
		int i = 0;
		while (i < min && a1[i].equals( a2[i])) {
			++i;
		}
		if (i < min) {
			return Integer.valueOf( a1[i]).compareTo( Integer.valueOf( a2[i]));
		}
		if (i < a1.length) {
			boolean zeros = true;
			do {
				zeros &= Integer.parseInt( a1[i++]) == 0;
			}
			while (zeros & i < a1.length);
			return zeros ? 0 : -1;
		}
		if (i < a2.length) {
			boolean zeros = true;
			do {
				zeros &= Integer.parseInt( a2[i++]) == 0;
			}
			while (zeros & i < a2.length);
			return zeros ? 0 : 1;
		}
		return 0;
	}

	public static void execute() {
		VersionHelper helper = new VersionHelper();
		new Thread( helper).start();
	}

	public static boolean isInitialized() {
		return sResult != ID_UNINITIALIZED && sResult != ID_FINAL;
	}

	public static boolean isOutdated() {
		return sResult == ID_OUTDATED;
	}

	private static void logResult() {
		switch (sResult) {
			case ID_CURRENT:
				Utils.log( LOGGER, Level.INFO, VERSION_CURRENT, MOD_NAME, MC_VERSION, sVersion);
				break;
			case ID_OUTDATED:
				Utils.log( LOGGER, Level.INFO, VERSION_OUTDATED, MOD_NAME, MC_VERSION, sVersion, sLocation);
				JOptionPane.showMessageDialog( null, Utils.format( VERSION_OUTDATED, MOD_NAME, MC_VERSION, sVersion, sLocation), "version  outdated", JOptionPane.WARNING_MESSAGE);
				break;
			case ID_MC_NOT_FOUND:
				Utils.log( LOGGER, Level.WARNING, VERSION_MC, MOD_NAME, MC_VERSION);
				JOptionPane.showMessageDialog( null, Utils.format( VERSION_MC, MOD_NAME, MC_VERSION), "minecaft version not found", JOptionPane.WARNING_MESSAGE);
				break;
			case ID_UNINITIALIZED:
				Utils.log( LOGGER, Level.WARNING, VERSION_UNINITIALIZED);
				break;
			default:
				sResult = ID_ERROR;
				Utils.log( LOGGER, Level.WARNING, VERSION_ERROR);
		}
	}

	private static Properties readProperties() {
		Properties props = new Properties();
		InputStream is = null;
		try {
			URL url = new URL( REMOTE_VERSION);
			is = url.openStream();
			props.loadFromXML( is);
		}
		catch (Exception ex) {
			// ignore
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
			}
			catch (Exception ex) {
				// ignore
			}
		}
		return props;
	}

	@Override
	public void run() {
		Utils.log( LOGGER, Level.INFO, VERSION_INIT, REMOTE_VERSION);
		try {
			for (int i = 0; i < VERSION_CHECK_ATTEMPTS; ++i) {
				checkVersion();
				logResult();
				if (sResult != ID_UNINITIALIZED && sResult != ID_ERROR) {
					break;
				}
				Thread.sleep( 10000);
			}
			if (sResult == ID_ERROR) {
				sResult = ID_FINAL;
				Utils.log( LOGGER, Level.WARNING, VERSION_FINAL);
			}
		}
		catch (InterruptedException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}
}
