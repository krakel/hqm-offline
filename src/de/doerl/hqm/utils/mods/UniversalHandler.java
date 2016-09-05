package de.doerl.hqm.utils.mods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.ANbt;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FList;
import de.doerl.hqm.utils.nbt.FLong;
import de.doerl.hqm.utils.nbt.ParserAtBits;

class UniversalHandler {
	private static Logger LOGGER = Logger.getLogger( UniversalHandler.class.getName());

	private UniversalHandler() {
	}

	public static void init( File baseDir) {
		ArrayList<ItemNEI> items = new ArrayList<>();
		parseDumpFile( items, baseDir);
		parseNbtFile( items, baseDir);
		loadAllImages( items);
	}

	private static void loadAllImages( ArrayList<ItemNEI> items) {
		for (ItemNEI item : items) {
			if (item.mBase.length() == 0) {
				Utils.log( LOGGER, Level.WARNING, "missing package: {0}", item);
			}
			else {
				ImageLoader.add( item);
			}
		}
	}

	static void parseDumpFile( ArrayList<ItemNEI> items, File baseDir) {
		File csvFile = new File( baseDir, BaseDefaults.ITEMPANEL_CSV);
		if (csvFile.isFile()) {
			NameCache cache = new NameCache();
			BufferedReader src = null;
			try {
				src = new BufferedReader( new InputStreamReader( new FileInputStream( csvFile), "ISO-8859-1"));
				src.readLine(); //               Item Name,        Item ID, Item meta, Has NBT, Display Name
				String line = src.readLine(); // minecraft:planks, 5,       5,         false,   Dark Oak Wood Planks
				while (line != null) {
					ItemNEI item = new ItemNEI( line);
					item.findImage( cache);
					items.add( item);
					line = src.readLine();
				}
			}
			catch (IOException ex) {
				Utils.logThrows( Selector.LOGGER, Level.WARNING, ex);
			}
			finally {
				Utils.closeIgnore( src);
			}
		}
	}

	static void parseNbtFile( ArrayList<ItemNEI> items, File baseDir) {
		File nbtFile = new File( baseDir, BaseDefaults.ITEMPANEL_NBT);
		if (nbtFile.isFile()) {
			FileInputStream src = null;
			try {
				src = new FileInputStream( nbtFile);
				FCompound res = ParserAtBits.readAsCompound( src);
				FList lst = (FList) res.get( "list");
				if (lst.getElement() != ANbt.ID_COMPOUND) {
					Utils.log( Selector.LOGGER, Level.WARNING, "wrong nbt list type  {0}", lst.getElement());
				}
				else if (lst.size() != items.size()) {
					Utils.log( Selector.LOGGER, Level.WARNING, "wrong size between item and nbt panel  {0} != {1}", items.size(), lst.size());
				}
				else {
					for (int i = 0, m = items.size(); i < m; ++i) {
						ItemNEI item = items.get( i);
						try {
							FCompound cmp = (FCompound) lst.get( i);
							FLong idNbt = (FLong) cmp.get( "id");
							if (idNbt == null) {
								Utils.log( Selector.LOGGER, Level.WARNING, "missing id nbt for {0}", item.mID);
							}
							else if (Integer.parseInt( item.mID) != idNbt.asInt()) {
								Utils.log( Selector.LOGGER, Level.WARNING, "wrong ids item and nbt panel  {0} != {1}", item.mID, idNbt);
							}
							FCompound tag = (FCompound) cmp.get( "tag");
							if (tag != null) {
								tag.clearName();
								item.setNBT( tag);
							}
						}
						catch (Exception ex) {
							Utils.logThrows( Selector.LOGGER, Level.WARNING, ex);
						}
					}
				}
			}
			catch (IOException ex) {
				Utils.logThrows( Selector.LOGGER, Level.WARNING, ex);
			}
			finally {
				Utils.closeIgnore( src);
			}
		}
	}
}
