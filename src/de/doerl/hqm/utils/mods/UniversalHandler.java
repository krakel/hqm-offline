package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;

class UniversalHandler implements IHandler {
	private static Logger LOGGER = Logger.getLogger( UniversalHandler.class.getName());
	private static HashMap<String, IMatcher> sStackNames = new HashMap<>();
	private File mBase;
	private File mImage;

	public UniversalHandler() {
	}

	public List<SimpleMatcher> find( String value, int max) {
		ArrayList<SimpleMatcher> arr = new ArrayList<>();
		for (IMatcher mm : sStackNames.values()) {
			mm.findMatch( arr, value);
			if (arr.size() > max) {
				break;
			}
		}
		return arr;
	}

	private File getBaseDir() {
		String name = PreferenceManager.getString( BaseDefaults.DUMP_DIR);
		if (name != null) {
			File path = new File( name);
			if (path.isDirectory()) {
				return path;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "NEI";
	}

	public Image load( String stk, String nbt) {
		if (mBase == null) {
			mBase = getBaseDir();
			if (mBase == null) {
				mBase = new File( "."); // only one try
			}
			else {
				ArrayList<String> json = new ArrayList<>();
				File[] arr = mBase.listFiles();
				for (File curr : arr) {
					if ("itempanel.json".equals( curr.getName())) {
						parseJsonPlain( curr, json);
						break;
					}
				}
				for (File curr : arr) {
					if ("itempanel.csv".equals( curr.getName())) {
						parseCSVFile( curr, json);
						break;
					}
				}
				File imgBase = new File( mBase, "itempanel_icons");
				if (imgBase.exists() && imgBase.isDirectory()) {
					mImage = imgBase;
				}
			}
		}
		if (mImage != null) {
			IMatcher match = sStackNames.get( stk);
			if (match != null) {
				File file = new File( mImage, match.findFile( nbt) + ".png");
				if (file.exists() && !file.isDirectory()) {
					return readImage( file);
				}
			}
		}
		return null;
	}

	private void parseCSVFile( File csvFile, ArrayList<String> json) {
		BufferedReader src = null;
		try {
			HashMap<String, String> names = new HashMap<>();
			src = new BufferedReader( new FileReader( csvFile));
			src.readLine();
			int idx = 0;
			String line = src.readLine();
			while (line != null) {
				int p1 = line.indexOf( ',');
				int p2 = line.indexOf( ',', p1 + 1);
				int p3 = line.indexOf( ',', p2 + 1);
				int p4 = line.indexOf( ',', p3 + 1);
				int p5 = line.indexOf( ',', p4 + 1);
				if (p4 < 0 || p5 >= 0) {
					Utils.log( LOGGER, Level.WARNING, "wrong cvs line: {0}", line);
				}
				else {
					String name = line.substring( 0, p1);
//					String id = line.substring( p1 + 1, p2);
					String meta = line.substring( p2 + 1, p3);
					boolean isNBT = Utils.parseBoolean( line.substring( p3 + 1, p4), false);
					String base = line.substring( p4 + 1).replace( ':', '_');
					String stk = name + '%' + meta;
					int i = 1;
					String file = base;
					while (names.containsKey( file)) {
						file = base + '_' + ++i;
					}
					names.put( file, stk);
					IMatcher match = sStackNames.get( stk);
					if (match == null) {
						match = isNBT ? new NBTMatcher( stk) : new SimpleMatcher( stk);
						sStackNames.put( stk, match);
					}
					String nbt = json.get( idx);
					if (nbt != null) {
						match.addNBT( nbt, file);
					}
					else {
						match.addNBT( file, file);
					}
				}
				line = src.readLine();
				++idx;
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( src);
		}
	}

//	private FArray parseJsonFile( File jsonFile) {
//		FArray arr = new FArray();
//		try {
//			BufferedReader src = null;
//			try {
//				src = new BufferedReader( new FileReader( jsonFile));
//				for (String line = src.readLine(); line != null;) {
//					NEIReader rdr = new NEIReader( line);
//					arr.add( rdr.doLine());
//					line = src.readLine();
//				}
//			}
//			finally {
//				Utils.closeIgnore( src);
//			}
//		}
//		catch (IOException ex) {
//			Utils.logThrows( LOGGER, Level.WARNING, ex);
//		}
//		return arr;
//	}
//
	private void parseJsonPlain( File jsonFile, ArrayList<String> arr) {
		try {
			BufferedReader src = null;
			try {
				src = new BufferedReader( new FileReader( jsonFile));
				for (String line = src.readLine(); line != null;) {
					arr.add( line);
					line = src.readLine();
				}
			}
			finally {
				Utils.closeIgnore( src);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private Image readImage( File file) {
		BufferedImage img = null;
		InputStream is = null;
		try {
			is = new FileInputStream( file);
			img = ImageIO.read( is);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( is);
		}
		return img;
	}
}
