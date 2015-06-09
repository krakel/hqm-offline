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
	private static final ImageLoader SINGLETON = new ImageLoader();
	private HashMap<String, Image> mCache = new HashMap<>();
	private IHandler mDummy = new DummyHandler();
	private IHandler mUni = new UniversalHandler();
	private LinkedList<Request> mQueue = new LinkedList<>();

	private ImageLoader() {
		setName( "ImageLoader");
		setPriority( NORM_PRIORITY - 1);
		setDaemon( true);
	}

	public static Image getImage( AStack stk, Runnable cb) {
		if (stk != null) {
			return SINGLETON.getImage( stk.getKey(), stk.getNBT(), cb);
		}
		else {
			return null;
		}
	}

	public static void init() {
		SINGLETON.start();
	}

	private synchronized void add( String stk, String nbt, Runnable cb) {
		mQueue.addLast( new Request( stk, nbt, cb));
		notifyAll();
	}

	private Image getImage( String key, String nbt, Runnable cb) {
		Image img = mCache.get( key);
		if (img == null && key != null && cb != null) {
			if (key.indexOf( ':') < 0) {
				Utils.log( LOGGER, Level.WARNING, "wrong stack name: {0}", key);
			}
			else {
				add( key, nbt, cb);
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
			Image img = mUni.load( req.mStk, req.mNbt);
			if (img == null) {
				img = mDummy.load( req.mStk, req.mNbt);
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
		private String mNbt;
		private Runnable mCallback;

		public Request( String stk, String nbt, Runnable cb) {
			mStk = stk;
			mNbt = nbt;
			mCallback = cb;
		}
	}
}
