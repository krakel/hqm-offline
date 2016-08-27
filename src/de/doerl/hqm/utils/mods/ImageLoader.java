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

import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.FCompound;

public class ImageLoader {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	private static final ReadImage THREAD = new ReadImage();
	private static HashMap<String, ArrayList<ItemNEI>> sStackes = new HashMap<>();
	private static File sBaseDir;
	private static File sImageDir;

	private ImageLoader() {
	}

	static void add( ItemNEI item) {
		ArrayList<ItemNEI> items = sStackes.get( item.mKey);
		if (items == null) {
			items = new ArrayList<ItemNEI>();
			sStackes.put( item.mKey, items);
		}
		items.add( item);
	}

	public static List<ItemNEI> find( String value, int max) {
		return find1( value.toLowerCase(), max);
	}

	private static List<ItemNEI> find1( String value, int max) {
		ArrayList<ItemNEI> res = new ArrayList<>();
		for (ArrayList<ItemNEI> arr : sStackes.values()) {
			for (ItemNEI item : arr) {
				item.findItem( res, value);
				if (res.size() > max) {
					break;
				}
			}
		}
		return res;
	}

	public static ItemNEI get( String key, FCompound nbt) {
		ArrayList<ItemNEI> items = sStackes.get( key);
		if (items == null) {
			items = new ArrayList<ItemNEI>();
			sStackes.put( key, items);
		}
		if (items.size() == 0) {
			ItemNEI item = new ItemNEI( key, nbt);
			items.add( item);
			return item;
		}
		if (items.size() == 1 || nbt == null) {
			return items.get( 0);
		}
		else {
			int max = Integer.MIN_VALUE;
			ItemNEI best = null;
			for (ItemNEI it : items) {
				int m = nbt.match( it.getNBT());
				if (m > max) {
					best = it;
					max = m;
				}
			}
			return best;
		}
	}

	public static Image getImage( Runnable cb, FItemStack stk) {
		if (stk != null) {
			return getImage( cb, stk.getKey(), stk.getNBT());
		}
		else {
			return getImage( cb, null, null);
		}
	}

	public static Image getImage( Runnable cb, String key, FCompound nbt) {
		if (key != null) {
			ItemNEI item = get( key, nbt);
			registerCB( cb, key, nbt);
			return item.getImage();
		}
		else {
			registerCB( cb, null, nbt);
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

	private static void registerCB( Runnable cb, String key, FCompound nbt) {
		if (cb != null) {
			THREAD.add( cb, key, nbt);
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

		public synchronized void add( Runnable cb, String key, FCompound nbt) {
			mQueue.addLast( new Request( cb, key, nbt));
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
				ItemNEI item = get( key, req.mNbt);
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
		private FCompound mNbt;

		public Request( Runnable cb, String key, FCompound nbt) {
			mCallback = cb;
			mKey = key;
			mNbt = nbt;
		}
	}
}
