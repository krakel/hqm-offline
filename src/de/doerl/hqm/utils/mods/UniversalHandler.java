package de.doerl.hqm.utils.mods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.Utils;

class UniversalHandler {
	private static Logger LOGGER = Logger.getLogger( UniversalHandler.class.getName());

	private UniversalHandler() {
	}

	public static void init( File baseDir) {
		File[] arr = baseDir.listFiles();
		for (File curr : arr) {
			if (BaseDefaults.ITEMPANEL.equals( curr.getName())) {
				parseCSVFile( curr);
				break;
			}
		}
	}

	private static void parseCSVFile( File csvFile) {
		NameCache cache = new NameCache();
		BufferedReader src = null;
		try {
			src = new BufferedReader( new InputStreamReader( new FileInputStream( csvFile), "ISO-8859-1"));
			src.readLine(); //               Item Name,        Item ID, Item meta, Has NBT, Display Name
			String line = src.readLine(); // minecraft:planks, 5,       5,         false,   Dark Oak Wood Planks
			while (line != null) {
				ItemNEI item = new ItemNEI( line);
				item.findImage( cache);
				if (item.mBase.length() == 0) {
					Utils.log( LOGGER, Level.WARNING, "missing package: {0}", line);
				}
				else if (ImageLoader.get( item) == null) {
					ImageLoader.put( item);
				}
				line = src.readLine();
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
