package de.doerl.hqm.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FReputation extends AMember {
	private static final Logger LOGGER = Logger.getLogger( FReputation.class.getName());
	private static final String BASE = "rep";
	public final FReputationCat mParentCategory;
	public final Vector<FMarker> mMarker = new Vector<>();
	private HashMap<String, LangInfo> mInfo = new HashMap<>();

	FReputation( FReputationCat parent) {
		super( BASE, MaxIdOf.getReputation( parent) + 1);
		mParentCategory = parent;
	}

	FReputation( FReputationCat parent, int id) {
		super( BASE, id);
		mParentCategory = parent;
	}

	public static int fromIdent( String ident) {
		return AIdented.fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReputation( this, p);
	}

	public FMarker createMarker() {
		FMarker marker = new FMarker( this);
		mMarker.add( marker);
		return marker;
	}

	public <T, U> T forEachMarker( IHQMWorker<T, U> worker, U p) {
		for (FMarker disp : mMarker) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION;
	}

	private LangInfo getInfo() {
		String lang = getHqm().mLang;
		LangInfo info = mInfo.get( lang);
		if (info == null) {
			info = new LangInfo();
			mInfo.put( lang, info);
		}
		return info;
	}

	@Override
	public String getName() {
		return getInfo().mName;
	}

	public String getNeutral() {
		return getInfo().mNeutral;
	}

	@Override
	public FReputationCat getParent() {
		return mParentCategory;
	}

	public boolean isFirst() {
		return ABase.isFirst( mParentCategory.mArr, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentCategory.mArr, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentCategory.mArr, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentCategory.mArr, this);
	}

	public void remove() {
		ABase.remove( mParentCategory.mArr, this);
	}

	@Override
	public void setName( String name) {
		getInfo().mName = name;
	}

	public void setNeutral( String neutral) {
		getInfo().mNeutral = neutral;
	}

	public void sort() {
		Collections.sort( mMarker);
	}

	@Override
	public String toString() {
		LangInfo info = getInfo();
		return String.format( "%s [%s]", info.mName, info.mNeutral);
	}

	private static class LangInfo {
		public String mName;
		public String mNeutral;
	}
}
