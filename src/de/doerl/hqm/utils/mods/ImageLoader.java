package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;

public class ImageLoader {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	private static final ReadImage THREAD = new ReadImage();
	private static HashMap<String, ItemNEI> sStackes = new HashMap<>();
	private static HashMap<String, Image> sCache = new HashMap<>();
	private static File sBaseDir;
	private static File sImageDir;

	private ImageLoader() {
	}

	static void add( ItemNEI item) {
		if (!sStackes.containsKey( item.mKey)) {
			sStackes.put( item.mKey, item);
		}
	}

	public static List<ItemNEI> find( String value, int max) {
		return find1( value.toLowerCase(), max);
	}

	private static List<ItemNEI> find1( String value, int max) {
		ArrayList<ItemNEI> arr = new ArrayList<>();
		for (ItemNEI item : sStackes.values()) {
			item.findItem( arr, value);
			if (arr.size() > max) {
				break;
			}
		}
		return arr;
	}

	public static Image getImage( AStack stk, Runnable cb) {
		if (stk != null) {
			return getImage0( stk.getKey(), cb);
		}
		else {
			return getImage0( null, cb);
		}
	}

	public static Image getImage( String key, Runnable cb) {
		return getImage0( key, cb);
	}

	private static Image getImage0( String key, Runnable cb) {
		if (cb != null) {
			THREAD.add( key, cb);
		}
		if (key == null) {
			return null;
		}
		if (key.indexOf( ':') < 0) {
			Utils.log( LOGGER, Level.WARNING, "wrong stack name: {0}", key);
		}
		return sCache.get( key);
	}

	public static void init() {
		sCache.clear();
		sStackes.clear();
		initDirectories();
		UniversalHandler.init( sBaseDir);
		if (!THREAD.isAlive()) {
			THREAD.start();
		}
	}

	private static void initDirectories() {
		String name = PreferenceManager.getString( BaseDefaults.DUMP_DIR);
		File path = null;
		if (name != null) {
			path = new File( name);
		}
		if (path != null && path.isDirectory()) {
			sBaseDir = path;
		}
		else {
			sBaseDir = new File( "."); // only one try
		}
		File imageDir = new File( sBaseDir, BaseDefaults.ITEMPANEL_ICONS);
		if (imageDir.exists() && imageDir.isDirectory()) {
			sImageDir = imageDir;
		}
	}

	private static Image load( String key) {
		if (sImageDir != null) {
			ItemNEI item = sStackes.get( key);
			if (item != null) {
				File file = new File( sImageDir, item.getImageName());
				if (file.exists() && !file.isDirectory()) {
					return readImage( file);
				}
			}
		}
		return null;
	}

	private static Image readImage( File file) {
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

	private static class ReadImage extends Thread {
		private LinkedList<Request> mQueue = new LinkedList<>();
		private DummyHandler mDummy = new DummyHandler();

		public ReadImage() {
			setName( "ImageLoader");
			setPriority( NORM_PRIORITY - 1);
			setDaemon( true);
		}

		public synchronized void add( String key, Runnable cb) {
			mQueue.addLast( new Request( key, cb));
			notifyAll();
		}

		private synchronized Request getNextEntry() throws InterruptedException {
			if (mQueue.isEmpty()) {
				wait( 0);
			}
			return mQueue.removeFirst();
		}

		private void readImage( Request req) throws IOException {
			String key = req.mKey;
			if (key != null && !sCache.containsKey( key)) {
				Image img = ImageLoader.load( key);
				if (img == null) {
					img = mDummy.load( key);
				}
				if (img != null) {
					sCache.put( key, img);
				}
			}
			if (req.mCallback != null) {
				SwingUtilities.invokeLater( req.mCallback);
			}
		}

		@Override
		public void run() {
			try {
				while (!interrupted()) {
					sleep( 10);
					Request entry = getNextEntry();
					if (entry != null) {
						try {
							readImage( entry);
						}
						catch (IOException ex) {
						}
						catch (Exception ex) {
							Utils.logThrows( LOGGER, Level.WARNING, ex);
						}
					}
				}
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	private static class Request {
		private String mKey;
		private Runnable mCallback;

		public Request( String key, Runnable cb) {
			mKey = key;
			mCallback = cb;
		}
	}
}
