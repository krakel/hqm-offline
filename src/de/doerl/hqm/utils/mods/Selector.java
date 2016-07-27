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
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.EditManager;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.LoggingManager;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;

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
			if (BaseDefaults.ITEMPANEL.equals( curr.getName())) {
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

	private static void copyImages( ArrayList<Item> arr, File dir) {
		File srcDir = new File( PreferenceManager.getString( BaseDefaults.DUMP_DIR), BaseDefaults.ITEMPANEL_ICONS);
		for (Item item : arr) {
			String img = item.mImage + ".png";
			File src = new File( srcDir, img);
			if (src.exists()) {
				File dstDir = new File( PreferenceManager.getString( BaseDefaults.PKG_DIR), item.mPkg);
				File dst = new File( dstDir, img);
				try {
					Files.copy( src.toPath(), dst.toPath());
				}
				catch (IOException ex) {
					Utils.log( LOGGER, Level.WARNING, "can not copy file: {0}, {1}, {2}", item.mPkg, src, ex.getMessage());
				}
			}
			else {
				Utils.log( LOGGER, Level.WARNING, "missing image: {0}, {1}", item.mPkg, src);
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
		Map<String, ArrayList<Item>> cache = new HashMap<>();
		parseDumpFile( cache);
		writeFiles( cache);
	}

	public static void main( String[] args) {
		isolatePackages();
//		checkPackages();
	}

	private static void parseDumpFile( Map<String, ArrayList<Item>> map) {
		BufferedReader src = null;
		try {
			int index = 2;
			Set<String> cache = new HashSet<>();
			File csvFile = new File( PreferenceManager.getString( BaseDefaults.DUMP_DIR), BaseDefaults.ITEMPANEL);
			src = new BufferedReader( new InputStreamReader( new FileInputStream( csvFile), "ISO-8859-1"));
			src.readLine(); //               Item Name,        Item ID, Item meta, Has NBT, Display Name
			String line = src.readLine(); // minecraft:planks, 5,       5,         false,   Dark Oak Wood Planks
			while (line != null) {
				Item item = new Item( line);
				item.cacheImage( cache);
				if (item.mPkg == null) {
					Utils.log( LOGGER, Level.WARNING, "missing package: {0}", line);
				}
				else {
					ArrayList<Item> arr = map.get( item.mPkg);
					if (arr == null) {
						arr = new ArrayList<>();
						map.put( item.mPkg, arr);
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

	private static void parsePackFile( File csvFile) {
		BufferedReader src = null;
		try {
			Set<String> cache = new HashSet<>();
			int index = 2;
			src = new BufferedReader( new InputStreamReader( new FileInputStream( csvFile), "UTF-8"));
			src.readLine(); //               Item Name,        Item ID, Item meta, Has NBT, Display Name
			String line = src.readLine(); // minecraft:planks, 5,       5,         false,   Dark Oak Wood Planks
			while (line != null) {
				Item item = new Item( line);
				if (item.mBase.length() > 0) {
					if (item.mBase.length() == 0) {
						Utils.log( LOGGER, Level.WARNING, "missing base {0}: {1}", index, line);
					}
					if (item.mBase.indexOf( ':') >= 0) {
						Utils.log( LOGGER, Level.WARNING, "wrong base {0}: {1}", index, line);
					}
					if (cache.contains( item.mBase)) {
						Utils.log( LOGGER, Level.WARNING, "double entry {0}: {1}", index, line);
					}
					cache.add( item.mBase);
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

	private static void writeFiles( Map<String, ArrayList<Item>> cache) {
		File pkgDir = new File( PreferenceManager.getString( BaseDefaults.PKG_DIR));
		for (Map.Entry<String, ArrayList<Item>> entry : cache.entrySet()) {
			String pkg = entry.getKey();
			ArrayList<Item> arr = entry.getValue();
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

	private static void writeItemsFile( ArrayList<Item> arr, File dir) {
		File file = new File( dir, BaseDefaults.ITEMS);
		if (file.exists()) {
			file.delete();
		}
		PrintWriter dst = null;
		try {
			dst = new PrintWriter( new BufferedWriter( new FileWriter( file)));
			dst.println( "Item Name,Item ID,Item meta,Has NBT,Display Name");
			for (Item item : arr) {
				dst.print( item.mName);
				dst.print( ',');
				dst.print( item.mID);
				dst.print( ',');
				dst.print( item.mMeta);
				dst.print( ',');
				dst.print( item.mHasNBT);
				dst.print( ',');
				dst.println( item.mImage);
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

	private static void writePanelFile( ArrayList<Item> arr, File dir) {
		File file = new File( dir, BaseDefaults.ITEMPANEL);
		if (file.exists()) {
			file.delete();
		}
		PrintWriter dst = null;
		try {
			dst = new PrintWriter( new BufferedWriter( new FileWriter( file)));
			dst.println( "Item Name,Item ID,Item meta,Has NBT,Display Name");
			for (Item item : arr) {
				dst.print( item.mName);
				dst.print( ',');
				dst.print( item.mID);
				dst.print( ',');
				dst.print( item.mMeta);
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

	private static class Item {
		public String mPkg;
		public String mName;
		public String mID;
		public String mMeta;
		public boolean mHasNBT;
		public String mBase;
		public String mImage;

		public Item( String line) {
			int p1 = line.indexOf( ',');
			int p2 = line.indexOf( ',', p1 + 1);
			int p3 = line.indexOf( ',', p2 + 1);
			int p4 = line.indexOf( ',', p3 + 1);
			mName = line.substring( 0, p1);
			mID = line.substring( p1 + 1, p2);
			mMeta = line.substring( p2 + 1, p3);
			mHasNBT = Utils.parseBoolean( line.substring( p3 + 1, p4), false);
			mBase = Utils.toWindowsName( line.substring( p4 + 1));
			if (mBase.length() > 0 && mBase.charAt( 0) == '"') {
				mBase = mBase.substring( 1, mBase.length() - 1);
			}
			mImage = mBase;
			int p5 = mName.indexOf( ':');
			if (p5 > 0) {
				int p6 = mName.indexOf( '|');
				mPkg = mName.substring( 0, p6 < 0 ? p5 : p6);
			}
		}

		private static String findImage( Set<String> cache, String base) {
			String image = base;
			int i = 1;
			while (cache.contains( image)) {
				image = base + '_' + ++i;
			}
			return image;
		}

		public void cacheImage( Set<String> cache) {
			mImage = findImage( cache, mBase);
			cache.add( mImage);
		}
	}
}
