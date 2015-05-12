package de.doerl.hqm.medium;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.doerl.hqm.base.FHqm;

public class WeakProperty {
	private static WeakProperty sManager = new WeakProperty();
	private ReferenceQueue<FHqm> sQueue = new ReferenceQueue<FHqm>();
	private Map<URI, WeakInfo> sValues = new HashMap<URI, WeakInfo>();

	private WeakProperty() {
	}

	public static WeakInfo get( FHqm base) {
		return sManager.getIntern( base);
	}

	public static void remove( FHqm base) {
		sManager.removeIntern( base);
	}

	protected WeakInfo getIntern( FHqm base) {
		processQueue();
		WeakInfo wi;
		synchronized (sValues) {
			URI uri = base.getURI();
			wi = sValues.get( uri);
			if (wi == null) {
				wi = new WeakInfo( uri, base, sQueue);
				sValues.put( uri, wi);
			}
		}
		return wi;
	}

	private void processQueue() {
		for (Reference<? extends FHqm> r = sQueue.poll(); r != null; r = sQueue.poll()) {
			WeakInfo ref = (WeakInfo) r;
			synchronized (sValues) {
				sValues.remove( ref.getURI());
			}
		}
	}

	protected void removeIntern( FHqm base) {
		processQueue();
		synchronized (sValues) {
			sValues.remove( base.getURI());
		}
	}
}
