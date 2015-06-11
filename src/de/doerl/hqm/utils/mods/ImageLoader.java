package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.Utils;

public class ImageLoader extends Thread {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	private static final ImageLoader SINGLETON = new ImageLoader();
	private HashMap<String, Image> mCache = new HashMap<>();
	private DummyHandler mDummy = new DummyHandler();
	private UniversalHandler mUni = new UniversalHandler();
	private LinkedList<Request> mQueue = new LinkedList<>();

	private ImageLoader() {
		setName( "ImageLoader");
		setPriority( NORM_PRIORITY - 1);
		setDaemon( true);
	}

	public static List<Matcher> find( String value, int max) {
		return SINGLETON.mUni.find( value.toLowerCase(), max);
	}

	public static Image getImage( AStack stk, Runnable cb) {
		if (stk != null) {
			return SINGLETON.getImage0( stk.getKey(), cb);
		}
		else {
			return SINGLETON.getImage0( null, cb);
		}
	}

	public static Image getImage( String key, Runnable cb) {
		return SINGLETON.getImage0( key, cb);
	}

	public static void init() {
		SINGLETON.mCache.clear();
		SINGLETON.mUni.init();
		if (!SINGLETON.isAlive()) {
			SINGLETON.start();
		}
	}

	private synchronized void add( String key, Runnable cb) {
		mQueue.addLast( new Request( key, cb));
		notifyAll();
	}

	private Image getImage0( String key, Runnable cb) {
		if (cb != null) {
			add( key, cb);
		}
		if (key != null) {
			if (key.indexOf( ':') < 0) {
				Utils.log( LOGGER, Level.WARNING, "wrong stack name: {0}", key);
			}
			return mCache.get( key);
		}
		else {
			return null;
		}
	}

	private synchronized Request getNextEntry() throws InterruptedException {
		if (mQueue.isEmpty()) {
			wait( 0);
		}
		return mQueue.removeFirst();
	}

	private void readImage( Request req) throws IOException {
		String key = req.mKey;
		if (key != null && !mCache.containsKey( key)) {
			Image img = mUni.load( key);
			if (img == null) {
				img = mDummy.load( key);
			}
			if (img != null) {
				mCache.put( key, img);
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

	private static class Request {
		private String mKey;
		private Runnable mCallback;

		public Request( String key, Runnable cb) {
			mKey = key;
			mCallback = cb;
		}
	}
}
