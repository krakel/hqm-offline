package de.doerl.hqm.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class MinecraftHandler implements IHandler {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	private static FileFilter JAR_FILTER = new FileFilter() {
		@Override
		public boolean accept( File src) {
			return src.isFile() && src.getName().endsWith( ".jar");
		}
	};
	private static final String ITEMS = "assets/minecraft/textures/items";
	private static final String BLOCKS = "assets/minecraft/textures/blocks";
	private HashMap<String, Image> mCache = new HashMap<>();
	private File mJarFile;

	public MinecraftHandler() {
	}

	private String createKey( String path) {
		int p1 = path.lastIndexOf( '/');
		int p2 = path.lastIndexOf( '.');
		if (p1 < 0 || p2 <= p1) {
			return null;
		}
		return path.substring( p1 + 1, p2);
	}

	private File getJarFile() {
		String path = PreferenceManager.getString( BaseDefaults.MINECRAFT_DIR);
		if (path != null) {
			File src = new File( path);
			if (src.isDirectory()) {
				File[] arr = src.listFiles();
				for (File curr : arr) {
					if (JAR_FILTER.accept( curr)) {
						return curr;
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "minecraft";
	}

	@Override
	public Image load( String stk) {
		if (mJarFile == null) {
			mJarFile = getJarFile();
			if (mJarFile != null) {
				parseFile();
			}
		}
		return mCache.get( stk);
	}

	private void parseFile() {
		JarFile jar = null;
		try {
			jar = new JarFile( mJarFile);
			for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				String name = entry.getName();
				if (name.endsWith( ".mcmeta")) {
				}
				else if (name.startsWith( ITEMS)) {
					putIf( jar, entry, name, true);
				}
				else if (name.startsWith( BLOCKS)) {
					putIf( jar, entry, name, false);
				}
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			try {
				if (jar != null) {
					jar.close();
				}
			}
			catch (IOException ex) {
			}
		}
	}

	private void putIf( JarFile jar, JarEntry entry, String name, boolean overwrite) throws IOException {
		String key = createKey( name);
		if (Utils.validString( key) && (overwrite || !mCache.containsKey( key))) {
			Image img = readImage( jar, entry);
			if (img != null) {
				mCache.put( key, img);
				Utils.log( LOGGER, Level.WARNING, "found minecraft:{0}", key);
				int pos = key.lastIndexOf( '_');
				if (pos > 0) {
					mCache.put( key.substring( pos + 1) + '_' + key.substring( 0, pos), img);
					mCache.put( key.substring( 0, pos), img);
				}
			}
		}
	}

	private Image readImage( JarFile jar, JarEntry entry) throws IOException {
		BufferedImage img = null;
		InputStream is = null;
		try {
			is = jar.getInputStream( entry);
			img = ImageIO.read( is);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
			}
			catch (Exception ex) {
			}
		}
		return img;
	}
}
