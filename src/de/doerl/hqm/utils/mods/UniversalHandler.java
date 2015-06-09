package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
				File[] arr = mBase.listFiles();
				for (File curr : arr) {
					if ("itempanel.csv".equals( curr.getName())) {
						parseCSVFile( curr);
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

	private void parseCSVFile( File csvFile) {
		BufferedReader src = null;
		try {
			HashMap<String, String> names = new HashMap<>();
			src = new BufferedReader( new FileReader( csvFile));
			src.readLine();
			for (String line = src.readLine(); line != null;) {
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
						match = isNBT ? new NBTMatcher() : new SimpleMatcher();
						sStackNames.put( stk, match);
					}
					match.addNBT( stk, file);
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

	private static interface IMatcher {
		void addNBT( String nbt, String file);

		String findFile( String nbt);
	}

	private static class NBTMatcher implements IMatcher {
		private HashMap<String, String> mMap = new HashMap<>();

		public NBTMatcher() {
		}

		@Override
		public void addNBT( String nbt, String file) {
			mMap.put( nbt, file);
		}

		@Override
		public String findFile( String nbt) {
			return mMap.get( nbt);
		}
	}

	private static class SimpleMatcher implements IMatcher {
		private String mFile;

		public SimpleMatcher() {
		}

		@Override
		public void addNBT( String nbt, String file) {
			mFile = file;
		}

		@Override
		public String findFile( String nbt) {
			return mFile;
		}
	}
}
