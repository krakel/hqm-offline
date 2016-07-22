/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.utils.mods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.EditManager;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.LoggingManager;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;

public class Selector {
	private static final Logger LOGGER;
	static {
		Locale.setDefault( Locale.ENGLISH);
		PreferenceManager.init();
		LoggingManager.init();
		LoggingManager.setOut( "console", System.out, "hqm.console.logLevel");
		LOGGER = Logger.getLogger( EditManager.class.getName());
	}

	private static void checkMod( File modDir) {
		File[] arr = modDir.listFiles();
		for (File curr : arr) {
			if (BaseDefaults.ITEMPANEL.equals( curr.getName())) {
				parseCSVFile( curr);
				break;
			}
		}
	}

	private static void checkPackages( File pkgDir) {
		File[] arr = pkgDir.listFiles();
		for (File curr : arr) {
			if (curr.isDirectory()) {
				checkMod( curr);
			}
		}
	}

	public static void main( String[] args) {
//		String dumpDir = PreferenceManager.getString( BaseDefaults.DUMP_DIR);
		String pkgDir = PreferenceManager.getString( BaseDefaults.PKG_DIR);
		checkPackages( new File( pkgDir));
	}

	@SuppressWarnings( "unused")
	private static void parseCSVFile( File csvFile) {
		BufferedReader src = null;
		try {
			Set<String> cache = new HashSet<>();
			int index = 2;
			src = new BufferedReader( new FileReader( csvFile));
			src.readLine(); //               Item Name,        Item ID, Item meta, Has NBT, Display Name
			String line = src.readLine(); // minecraft:planks, 5,       5,         false,   Dark Oak Wood Planks
			while (line != null) {
				int p1 = line.indexOf( ',');
				int p2 = line.indexOf( ',', p1 + 1);
				int p3 = line.indexOf( ',', p2 + 1);
				int p4 = line.indexOf( ',', p3 + 1);
				if (p4 < 0) {
					Utils.log( LOGGER, Level.WARNING, "wrong cvs line: {0}", line);
				}
				else {
					String name = line.substring( 0, p1);
					String id = line.substring( p1 + 1, p2);
					String meta = line.substring( p2 + 1, p3);
					Utils.parseBoolean( line.substring( p3 + 1, p4), false);
					String base = line.substring( p4 + 1);
					if (base.length() == 0) {
						Utils.log( LOGGER, Level.WARNING, "missing base {0}: {1}", index, line);
					}
					if (base.charAt( 0) == '"') {
						Utils.log( LOGGER, Level.WARNING, "wrong base {0}: {1}", index, line);
					}
					if (base.indexOf( ':') >= 0) {
						Utils.log( LOGGER, Level.WARNING, "wrong base {0}: {1}", index, line);
					}
					if (cache.contains( base)) {
						Utils.log( LOGGER, Level.WARNING, "double entry {0}: {1}", index, line);
					}
					cache.add( base);
				}
				line = src.readLine();
				++index;
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( src);
		}
	}
}
