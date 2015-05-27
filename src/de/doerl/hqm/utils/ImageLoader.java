package de.doerl.hqm.utils;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import de.doerl.hqm.utils.mods.DummyHandler;
import de.doerl.hqm.utils.mods.IHandler;

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

	public static Image getImage( String entry) {
		return sCache.get( entry);
	}

	public static Image getImage( String entry, int dmg, Runnable cb) {
		Image img = sCache.get( entry);
		if (img == null && cb != null) {
			SINGLETON.add( entry, dmg, cb);
		}
		return img;
	}

	private static void readImage( Request req) throws IOException {
		if (!sCache.containsKey( req.mEntry)) {
			int pos = req.mEntry.indexOf( ':');
			if (pos < 0) {
				Utils.log( LOGGER, Level.WARNING, "wrong stack name: {0}", req.mEntry);
			}
			else {
				String mod = req.mEntry.substring( 0, pos);
				String stk = req.mEntry.substring( pos + 1);
//				Utils.log( LOGGER, Level.FINEST, "load image {0}:{1}", mod, stk);
				IHandler hdl = sHandler.get( mod);
				if (hdl == null) {
					Utils.log( LOGGER, Level.WARNING, "missing handler for {0}", mod);
					hdl = new DummyHandler( mod);
					sHandler.put( mod, hdl);
				}
				Image img = hdl.load( stk, 0);
				if (img != null) {
					sCache.put( req.mEntry, img);
					if (req.mCallback != null) {
						SwingUtilities.invokeLater( req.mCallback);
					}
				}
				else {
					Utils.log( LOGGER, Level.WARNING, "missing image for {0}", req.mEntry);
				}
			}
		}
	}

	private synchronized void add( String entry, int dmg, Runnable cb) {
//		Utils.log( LOGGER, Level.FINEST, entry);
		mQueue.addLast( new Request( entry, dmg, cb));
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
		private String mEntry;
		private int mDmg;
		private Runnable mCallback;

		public Request( String entry, int dmg, Runnable cb) {
			mEntry = entry;
			mDmg = dmg;
			mCallback = cb;
		}
	}
}
