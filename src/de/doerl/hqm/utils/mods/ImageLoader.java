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

	public static ItemNEI get( String key) {
		ItemNEI item = sStackes.get( key);
		if (item == null) {
			item = new ItemNEI( key, null);
			sStackes.put( key, item);
		}
		return item;
	}

	public static Image getImage( Runnable cb, AStack stk) {
		return getImage( cb, stk != null ? stk.getKey() : null);
	}

	public static Image getImage( Runnable cb, String key) {
		if (key != null) {
			ItemNEI item = get( key);
			registerCB( cb, key);
			return item.getImage();
		}
		else {
			registerCB( cb, null);
			return null;
		}
	}

	public static void init() {
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

	private static void registerCB( Runnable cb, String key) {
		if (cb != null) {
			THREAD.add( cb, key);
		}
	}

	private static class ReadImage extends Thread {
		private LinkedList<Request> mQueue = new LinkedList<>();
		private DummyHandler mDummy = new DummyHandler();

		public ReadImage() {
			setName( "ImageLoader");
			setPriority( NORM_PRIORITY - 1);
			setDaemon( true);
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

		public synchronized void add( Runnable cb, String key) {
			mQueue.addLast( new Request( cb, key));
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
			if (key != null) {
				ItemNEI item = get( key);
				if (sImageDir != null && item.getImage() == null) {
					File file = new File( sImageDir, item.getImageName());
					if (file.exists() && !file.isDirectory()) {
						item.setImage( readImage( file));
					}
				}
				if (item.getImage() == null) {
					item.setImage( mDummy.load( key));
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
		private Runnable mCallback;
		private String mKey;

		public Request( Runnable cb, String key) {
			mCallback = cb;
			mKey = key;
		}
	}
}
