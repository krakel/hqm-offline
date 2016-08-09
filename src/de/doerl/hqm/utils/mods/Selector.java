package de.doerl.hqm.utils.mods;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.EditManager;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.LoggingManager;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.ANbt;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FList;
import de.doerl.hqm.utils.nbt.NbtReader;

@SuppressWarnings( "unused")
public class Selector {
	private static final Logger LOGGER;
	static {
		Locale.setDefault( Locale.ENGLISH);
		PreferenceManager.init();
		LoggingManager.init();
		LoggingManager.setOut( "console", System.out, "hqm.console.logLevel");
		LOGGER = Logger.getLogger( EditManager.class.getName());
	}

	private static void checkPack( File modDir) {
		File[] arr = modDir.listFiles();
		for (File curr : arr) {
			if (BaseDefaults.ITEMPANEL_CSV.equals( curr.getName())) {
				parsePackFile( curr);
				break;
			}
		}
	}

	private static void checkPackages() {
		File csvFile = new File( PreferenceManager.getString( BaseDefaults.PKG_DIR));
		for (File curr : csvFile.listFiles()) {
			if (curr.isDirectory()) {
				checkPack( curr);
			}
		}
	}

	private static void copyImages( ArrayList<ItemNEI> arr, File dir) {
		File srcDir = new File( PreferenceManager.getString( BaseDefaults.DUMP_DIR), BaseDefaults.ITEMPANEL_ICONS);
		for (ItemNEI item : arr) {
			String img = item.getImageName();
			File src = new File( srcDir, img);
			if (src.exists()) {
				File dstDir = new File( PreferenceManager.getString( BaseDefaults.PKG_DIR), item.getPkg());
				File dst = new File( dstDir, img);
				try {
					Files.copy( src.toPath(), dst.toPath());
				}
				catch (IOException ex) {
					Utils.log( LOGGER, Level.WARNING, "can not copy file: {0}, {1}, {2}", item.getPkg(), src, ex.getMessage());
				}
			}
			else {
				Utils.log( LOGGER, Level.WARNING, "missing image: {0}, {1}", item.getPkg(), src);
			}
		}
	}

	private static void deleteImages( File dir) {
		File[] arr = dir.listFiles();
		for (File curr : arr) {
			if (curr.getName().endsWith( ".png")) {
				curr.delete();
			}
		}
	}

	private static void isolatePackages() {
		ArrayList<ItemNEI> items = new ArrayList<>();
		parseDumpFile( items);
		parseNbtFile( items);
		writeFiles( items);
	}

	public static void main( String[] args) {
		isolatePackages();
//		checkPackages();
	}

	private static void parseDumpFile( ArrayList<ItemNEI> items) {
		NameCache cache = new NameCache();
		BufferedReader src = null;
		try {
			int index = 2;
			File csvFile = new File( PreferenceManager.getString( BaseDefaults.DUMP_DIR), BaseDefaults.ITEMPANEL_CSV);
			src = new BufferedReader( new InputStreamReader( new FileInputStream( csvFile), "ISO-8859-1"));
			src.readLine(); //               Item Name,        Item ID, Item meta, Has NBT, Display Name
			String line = src.readLine(); // minecraft:planks, 5,       5,         false,   Dark Oak Wood Planks
			while (line != null) {
				ItemNEI item = new ItemNEI( line);
				item.findImage( cache);
				items.add( item);
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

	private static void parseNbtFile( ArrayList<ItemNEI> items) {
		FileInputStream src = null;
		try {
			File csvFile = new File( PreferenceManager.getString( BaseDefaults.DUMP_DIR), BaseDefaults.ITEMPANEL_NBT);
			src = new FileInputStream( csvFile);
			FCompound res = NbtReader.readAsCompound( src);
			FList lst = (FList) res.get( "list");
			if (lst.getElement() != FCompound.ID) {
				Utils.log( LOGGER, Level.WARNING, "wrong nbt list type  {0}", lst.getElement());
			}
			else if (lst.size() != items.size()) {
				Utils.log( LOGGER, Level.WARNING, "wrong size between item and nbt panel  {0} != {1}", items.size(), lst.size());
			}
			else {
				for (int i = 0, m = items.size(); i < m; ++i) {
					ItemNEI item = items.get( i);
					FCompound cmp = (FCompound) lst.get( i);
					ANbt idNbt = cmp.get( "id");
					if (idNbt == null) {
						Utils.log( LOGGER, Level.WARNING, "missing id nbt for {0}", item.mID);
					}
					else if (Utils.different( item.mID, idNbt.toString())) {
						Utils.log( LOGGER, Level.WARNING, "wrong ids item and nbt panel  {0} != {1}", item.mID, idNbt);
					}
					FCompound tag = (FCompound) cmp.get( "tag");
					if (tag != null) {
						tag.clearName();
						item.setNBT( tag);
					}
				}
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( src);
		}
	}

	private static void parsePackFile( File csvFile) {
		NameCache cache = new NameCache();
		BufferedReader src = null;
		try {
			int index = 2;
			src = new BufferedReader( new InputStreamReader( new FileInputStream( csvFile), "UTF-8"));
			src.readLine(); //               Item Name,        Item ID, Item meta, Has NBT, Display Name
			String line = src.readLine(); // minecraft:planks, 5,       5,         false,   Dark Oak Wood Planks
			while (line != null) {
				ItemNEI item = new ItemNEI( line);
				if (item.mBase.length() == 0) {
					Utils.log( LOGGER, Level.WARNING, "missing base {0}: {1}", index, line);
				}
				if (cache.checkImage( item.mBase)) {
					Utils.log( LOGGER, Level.WARNING, "double entry {0}: {1}", index, line);
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

	private static void writeFiles( ArrayList<ItemNEI> items) {
		Map<String, ArrayList<ItemNEI>> cache = new HashMap<>();
		for (ItemNEI item : items) {
			String pkg = item.getPkg();
			if (pkg == null) {
				Utils.log( LOGGER, Level.WARNING, "missing item: {0}", item);
			}
			else {
				ArrayList<ItemNEI> arr = cache.get( pkg);
				if (arr == null) {
					arr = new ArrayList<>();
					cache.put( pkg, arr);
				}
				if (pkg == null) {
					Utils.log( LOGGER, Level.WARNING, "missing item: {0}", item);
				}
				else {
					arr.add( item);
				}
			}
		}
		File pkgDir = new File( PreferenceManager.getString( BaseDefaults.PKG_DIR));
		for (Map.Entry<String, ArrayList<ItemNEI>> entry : cache.entrySet()) {
			String pkg = entry.getKey();
			System.out.println( pkg);
			File dir = new File( pkgDir, pkg);
			if (dir.exists() && dir.isFile()) {
				dir.delete();
			}
			if (!dir.exists()) {
				dir.mkdir();
			}
			ArrayList<ItemNEI> arr = entry.getValue();
//			writePanelFile( arr, dir);
			writeItemsFile( arr, dir);
			deleteImages( dir);
			copyImages( arr, dir);
		}
	}

	private static void writeItemsFile( ArrayList<ItemNEI> arr, File dir) {
		File file = new File( dir, BaseDefaults.ITEMS);
		if (file.exists()) {
			file.delete();
		}
		PrintWriter dst = null;
		try {
			dst = new PrintWriter( new BufferedWriter( new FileWriter( file)));
			dst.println( "Item Name,Item ID,Item meta,Has NBT,Display Name,NBT");
			for (ItemNEI item : arr) {
				dst.print( item.mName);
				dst.print( ',');
				dst.print( item.mID);
				dst.print( ',');
				dst.print( item.mDamage);
				dst.print( ',');
				dst.print( item.mHasNBT);
				dst.print( ',');
				dst.print( item.getImageName());
				dst.print( ',');
				dst.println( item.getNBT() != null ? item.getNBT().toString() : "");
			}
			dst.flush();
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( dst);
		}
	}

	private static void writePanelFile( ArrayList<ItemNEI> arr, File dir) {
		File file = new File( dir, BaseDefaults.ITEMPANEL_CSV);
		if (file.exists()) {
			file.delete();
		}
		PrintWriter dst = null;
		try {
			dst = new PrintWriter( new BufferedWriter( new FileWriter( file)));
			dst.println( "Item Name,Item ID,Item meta,Has NBT,Display Name");
			for (ItemNEI item : arr) {
				dst.print( item.mName);
				dst.print( ',');
				dst.print( item.mID);
				dst.print( ',');
				dst.print( item.mDamage);
				dst.print( ',');
				dst.print( item.mHasNBT);
				dst.print( ',');
				dst.println( item.mBase);
			}
			dst.flush();
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( dst);
		}
	}
}
