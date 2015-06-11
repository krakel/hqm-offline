package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

abstract class AHandler implements IHandler {
	private static Logger LOGGER = Logger.getLogger( AHandler.class.getName());
	protected File mJarFile;
	protected HashMap<String, Image> mCache = new HashMap<>();

	protected String createFileName( String path) {
		int p1 = path.lastIndexOf( '/');
		int p2 = path.lastIndexOf( '.');
		if (p1 < 0 || p2 <= p1) {
			return null;
		}
		return path.substring( p1 + 1, p2);
	}

	private File getJarFile() {
		String path = getPath();
		if (path != null) {
			File src = new File( path);
			if (src.isDirectory()) {
				File[] arr = src.listFiles();
				for (File curr : arr) {
					if (JarFilter.isJar( curr) && isModFile( curr.getName())) {
						return curr;
					}
				}
			}
		}
		return null;
	}

	abstract String getKey( String fileName);

	abstract String getPath();

	abstract String getToken();

	abstract boolean isBlock( String name);

	abstract boolean isItem( String name);

	abstract boolean isModFile( String name);

	public Image load( String key) {
		if (mJarFile == null) {
			mJarFile = getJarFile();
			if (mJarFile == null) {
				mJarFile = new File( "."); // only one try
			}
			else {
				parseFile();
			}
		}
		Image img = mCache.get( key);
		if (img == null) {
			img = ResourceManager.stringImage( getToken() + mCache.size());
			mCache.put( key, img);
		}
		return img;
	}

	protected void parseFile() {
		JarFile jar = null;
		try {
			jar = new JarFile( mJarFile);
			for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				String name = entry.getName();
				if (name.endsWith( ".mcmeta")) {
				}
				else if (isItem( name)) {
					putIf( jar, entry, name, false);
				}
				else if (isBlock( name)) {
					putIf( jar, entry, name, true);
				}
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( jar);
		}
	}

	private void putIf( JarFile jar, JarEntry entry, String name, boolean ignore) throws IOException {
		String fileName = createFileName( name);
		if (Utils.validString( fileName)) {
			String key = getKey( fileName);
			if (ignore && mCache.containsKey( key)) {
				//				Utils.log( LOGGER, Level.WARNING, "found double item name: {0}", key);
			}
			else {
				Image img = readImage( jar, entry);
				if (img != null) {
					mCache.put( key, img);
					//					Utils.log( LOGGER, Level.WARNING, "found {0}", key);
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
			Utils.closeIgnore( is);
		}
		return img;
	}
}
