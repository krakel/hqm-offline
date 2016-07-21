package de.doerl.hqm.utils.mods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

class UniversalHandler {
	private static Logger LOGGER = Logger.getLogger( UniversalHandler.class.getName());
	private static final String ITEMPANEL_CSV = "itempanel.csv";

	private UniversalHandler() {
	}

	public static void init( File baseDir) {
		File[] arr = baseDir.listFiles();
		for (File curr : arr) {
			if (ITEMPANEL_CSV.equals( curr.getName())) {
				parseCSVFile( curr);
				break;
			}
		}
	}

	private static void parseCSVFile( File csvFile) {
		BufferedReader src = null;
		try {
			NameCache cache = new NameCache();
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
//					String id = line.substring( p1 + 1, p2);
					String meta = line.substring( p2 + 1, p3);
//					Utils.parseBoolean( line.substring( p3 + 1, p4), false);
					String base = line.substring( p4 + 1).replace( ':', '_');
					if (base.length() > 0 && base.charAt( 0) == '"') {
						base = base.substring( 1, base.length() - 1);
					}
					String stk = name + '%' + meta;
					Matcher match = ImageLoader.get( stk);
					if (match == null) {
						String image = cache.next( base);
						match = ImageLoader.put( stk, image);
					}
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

	private static class NameCache {
		private HashSet<String> mNames = new HashSet<>();

		public String next( String base) {
			String file = base;
			int i = 1;
			while (mNames.contains( file)) {
				file = base + '_' + ++i;
			}
			mNames.add( file);
			return file;
		}
	}
}
