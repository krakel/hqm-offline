package de.doerl.hqm.base;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FGroupTier extends AMember {
	private static final Logger LOGGER = Logger.getLogger( FGroupTier.class.getName());
	private static final String BASE = "tier";
	public final FGroupTierCat mParentCategory;
	final Vector<FGroup> mGroups = new Vector<>();
	public int mColorID;
	public int[] mWeights;
	private HashMap<String, String> mInfo = new HashMap<>();

	FGroupTier( FGroupTierCat parent) {
		super( BASE, MaxIdOf.getTier( parent) + 1);
		mParentCategory = parent;
	}

	FGroupTier( FGroupTierCat parent, int id) {
		super( BASE, id);
		mParentCategory = parent;
	}

	public static int fromIdent( String ident) {
		return AIdented.fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroupTier( this, p);
	}

	public FGroup createGroup() {
		FGroup grp = new FGroup( this);
		mGroups.add( grp);
		return grp;
	}

	public FGroup createGroup( int id) {
		FGroup grp = new FGroup( this, id);
		mGroups.add( grp);
		return grp;
	}

	public <T, U> T forEachGroup( IHQMWorker<T, U> worker, U p) {
		for (FGroup disp : mGroups) {
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
		return ElementTyp.GROUP_TIER;
	}

	@Override
	public String getName() {
		return mInfo.get( getHqm().mLang);
	}

	@Override
	public FGroupTierCat getParent() {
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
		mInfo.put( getHqm().mLang, name);
	}
}
