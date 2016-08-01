/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
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
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.json.JsonReader;

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
		Map<String, ArrayList<ItemNEI>> cache = new HashMap<>();
		parseDumpFile( cache);
//		parseNbtFile( cache);
		writeFiles( cache);
	}

	public static void main( String[] args) {
		isolatePackages();
//		checkPackages();
	}

	private static void parseDumpFile( Map<String, ArrayList<ItemNEI>> map) {
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
				if (item.getPkg() == null) {
					Utils.log( LOGGER, Level.WARNING, "missing package: {0}", line);
				}
				else {
					ArrayList<ItemNEI> arr = map.get( item.getPkg());
					if (arr == null) {
						arr = new ArrayList<>();
						map.put( item.getPkg(), arr);
					}
					arr.add( item);
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

	private static void parseNbtFile( Map<String, ArrayList<ItemNEI>> cache) {
		FileInputStream src = null;
		try {
			File csvFile = new File( PreferenceManager.getString( BaseDefaults.DUMP_DIR), BaseDefaults.ITEMPANEL_NBT);
			src = new FileInputStream( csvFile);
			JsonReader rdr = new JsonReader( src);
			IJson all = rdr.doAll();
			FObject obj = FObject.to( all);
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

	private static void writeFiles( Map<String, ArrayList<ItemNEI>> cache) {
		File pkgDir = new File( PreferenceManager.getString( BaseDefaults.PKG_DIR));
		for (Map.Entry<String, ArrayList<ItemNEI>> entry : cache.entrySet()) {
			String pkg = entry.getKey();
			ArrayList<ItemNEI> arr = entry.getValue();
			System.out.println( pkg);
			File dir = new File( pkgDir, pkg);
			if (dir.exists() && dir.isFile()) {
				dir.delete();
			}
			if (!dir.exists()) {
				dir.mkdir();
			}
			writePanelFile( arr, dir);
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
				dst.println( item.getImageName());
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
