package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.Utils;

public class ImageLoader extends Thread {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	public static final ImageLoader SINGLETON = new ImageLoader();
	private static HashMap<String, IHandler> sHandler = new HashMap<>();
	private static HashMap<String, Image> sCache = new HashMap<>();
	private LinkedList<Request> mQueue = new LinkedList<>();

	private ImageLoader() {
		setName( "ImageLoader");
		setPriority( NORM_PRIORITY - 1);
		setDaemon( true);
	}

	public static void addHandler( IHandler hdl) {
		sHandler.put( hdl.getName(), hdl);
	}

	public static Image getImage( AStack stk, Runnable cb) {
		if (stk != null) {
			return getImage( stk.getKey(), cb);
		}
		else {
			return null;
		}
	}

	public static Image getImage( String key, Runnable cb) {
		Image img = sCache.get( key);
		if (img == null && key != null && cb != null) {
			if (key.indexOf( ':') < 0) {
				Utils.log( LOGGER, Level.WARNING, "wrong stack name: {0}", key);
			}
			else {
				SINGLETON.add( key, cb);
			}
		}
		return img;
	}

	public static void init() {
		addHandler( new UniversalHandler());
		SINGLETON.start();
	}

	private static void readImage( Request req) throws IOException {
		if (!sCache.containsKey( req.mStk)) {
//			Utils.log( LOGGER, Level.FINEST, "load image {0}:{1}", mod, stk);
			IHandler hdl = sHandler.get( req.mMod);
			if (hdl == null) {
//				Utils.log( LOGGER, Level.WARNING, "missing handler for {0}", req.mMod);
				hdl = new DummyHandler( req.mMod);
				sHandler.put( req.mMod, hdl);
			}
			Image img = hdl.load( req.mStk);
			if (img == null) {
				Utils.log( LOGGER, Level.WARNING, "missing image for {0}", req.mStk);
			}
			else {
				sCache.put( req.mStk, img);
				if (req.mCallback != null) {
					SwingUtilities.invokeLater( req.mCallback);
				}
			}
		}
	}

	private synchronized void add( String stk, Runnable cb) {
		mQueue.addLast( new Request( stk, cb));
		notifyAll();
	}

	private synchronized Request getNextEntry() throws InterruptedException {
		if (mQueue.isEmpty()) {
			wait( 0);
		}
		return mQueue.removeFirst();
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
		private String mMod = "NEI";
		private Runnable mCallback;

		public Request( String stk, Runnable cb) {
			mStk = stk;
			mCallback = cb;
		}
	}
}
