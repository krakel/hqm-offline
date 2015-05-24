package de.doerl.hqm.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageLoader extends Thread {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	public static final ImageLoader SINGLETON = new ImageLoader();
	private HashMap<String, BufferedImage> mCache = new HashMap<>();
	private LinkedList<String> mQueue = new LinkedList<>();

	private ImageLoader() {
		setName( "ImageLoader");
		setPriority( NORM_PRIORITY - 1);
		setDaemon( true);
	}

	public synchronized void add( String name) {
		Utils.log( LOGGER, Level.FINEST, "ImageLoader.add");
		mQueue.addLast( name);
		notifyAll();
	}

	private synchronized String getNextEntry() throws InterruptedException {
		if (mQueue.isEmpty()) {
			wait( 0);
		}
		return mQueue.removeFirst();
	}

	private void readImage( String entry) throws IOException {
		if (mCache.containsKey( entry)) {
		}
	}

	@Override
	public void run() {
		try {
			while (!interrupted()) {
				sleep( 100);
				String entry = getNextEntry();
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
