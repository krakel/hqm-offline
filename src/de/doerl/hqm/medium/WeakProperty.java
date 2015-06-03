package de.doerl.hqm.medium;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

import de.doerl.hqm.base.FHqm;

public class WeakProperty {
	private static WeakProperty sManager = new WeakProperty();
	private ReferenceQueue<FHqm> sQueue = new ReferenceQueue<>();
	private Map<Object, WeakInfo> sValues = new HashMap<>();

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
			Object src = base.toString();
			wi = sValues.get( src);
			if (wi == null) {
				wi = new WeakInfo( src, base, sQueue);
				sValues.put( src, wi);
			}
		}
		return wi;
	}

	private void processQueue() {
		for (Reference<? extends FHqm> r = sQueue.poll(); r != null; r = sQueue.poll()) {
			WeakInfo ref = (WeakInfo) r;
			synchronized (sValues) {
				sValues.remove( ref.getSource());
			}
		}
	}

	protected void removeIntern( FHqm base) {
		processQueue();
		synchronized (sValues) {
			sValues.remove( base.toString());
		}
	}
}
