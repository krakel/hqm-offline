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
		return SINGLETON.mUni.find( value, max);
	}

	public static Image getImage( AStack stk, Runnable cb) {
		if (stk != null) {
			return SINGLETON.getImage0( stk.getKey(), cb);
		}
		else {
			return null;
		}
	}

	public static Image getImage( Matcher sm, Runnable cb) {
		if (sm != null) {
			return SINGLETON.getImage0( sm.mKey, cb);
		}
		else {
			return null;
		}
	}

	public static Image getImage( String key, Runnable cb) {
		if (key != null) {
			return SINGLETON.getImage0( key, cb);
		}
		else {
			return null;
		}
	}

	public static void init() {
		SINGLETON.start();
	}

	private synchronized void add( String stk, Runnable cb) {
		mQueue.addLast( new Request( stk, cb));
		notifyAll();
	}

	private Image getImage0( String key, Runnable cb) {
		Image img = mCache.get( key);
		if (img == null && key != null && cb != null) {
			if (key.indexOf( ':') < 0) {
				Utils.log( LOGGER, Level.WARNING, "wrong stack name: {0}", key);
			}
			else {
				add( key, cb);
			}
		}
		return img;
	}

	private synchronized Request getNextEntry() throws InterruptedException {
		if (mQueue.isEmpty()) {
			wait( 0);
		}
		return mQueue.removeFirst();
	}

	private void readImage( Request req) throws IOException {
		if (!mCache.containsKey( req.mStk)) {
			Image img = mUni.load( req.mStk);
			if (img == null) {
				img = mDummy.load( req.mStk);
			}
			if (img != null) {
				mCache.put( req.mStk, img);
				if (req.mCallback != null) {
					SwingUtilities.invokeLater( req.mCallback);
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				sleep( 100);
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
		private String mStk;
		private Runnable mCallback;

		public Request( String stk, Runnable cb) {
			mStk = stk;
			mCallback = cb;
		}
	}
}
