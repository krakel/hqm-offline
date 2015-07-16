package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.ui.LinkType;
import de.doerl.hqm.utils.Utils;

public final class FQuestSet extends AMember {
	private static final Logger LOGGER = Logger.getLogger( FQuestSet.class.getName());
	private static final String BASE = "set";
	private LinkType mInformation = LinkType.NORM;
	final Vector<FQuest> mQuests = new Vector<>();
	public final FQuestSetCat mParentCategory;
	private int mID;
	public String mDescr;

	FQuestSet( FQuestSetCat parent, String name) {
		super( name);
		mParentCategory = parent;
		mID = MaxIdOf.getQuestSet( parent) + 1;
	}

	public static int fromIdent( String ident) {
		return fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuestSet( this, p);
	}

	void add( FQuest quest) {
		if (Utils.equals( quest.getParent(), this)) {
			mQuests.add( quest);
		}
		else {
			Utils.log( LOGGER, Level.WARNING, "wrong quest {0}", quest);
		}
	}

	public FQuest createQuest( String name) {
		FQuest quest = new FQuest( this, name);
		mQuests.add( quest);
		return quest;
	}

	public <T, U> T forEachQuest( IHQMWorker<T, U> worker, U p) {
		for (FQuest disp : mQuests) {
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
		return ElementTyp.QUEST_SET;
	}

	public int getID() {
		return mID;
	}

	@Override
	public LinkType getInformation() {
		return mInformation;
	}

	@Override
	public FQuestSetCat getParent() {
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

	public void setID( String ident) {
		if (ident != null) {
			int id = fromIdent( ident);
			if (id >= 0) {
				mID = id;
			}
		}
	}

	@Override
	public void setInformation( LinkType information) {
		mInformation = information;
	}

	public String toIdent() {
		return toIdent( BASE, mID);
	}
}
