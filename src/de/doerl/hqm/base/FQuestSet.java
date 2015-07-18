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

	FQuestSet( FQuestSetCat parent) {
		super( BASE, MaxIdOf.getQuestSet( parent) + 1);
		mParentCategory = parent;
	}

	public static int fromIdent( String ident) {
		return AIdent.fromIdent( BASE, ident);
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

	public FQuest createQuest() {
		FQuest quest = new FQuest( this);
		mQuests.add( quest);
		return quest;
	}

	public FQuest createQuest( int id) {
		FQuest quest = new FQuest( this, id);
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

	public String getDescr() {
		return getInfo().mInfo2;
	}

	public String getDescr( String lang) {
		return getInfo( lang).mInfo2;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_SET;
	}

	@Override
	public LinkType getInformation() {
		return mInformation;
	}

	@Override
	public String getName() {
		return getInfo().mInfo1;
	}

	public String getName( String lang) {
		return getInfo( lang).mInfo1;
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

	public void setDescr( String descr) {
		getInfo().mInfo2 = descr;
	}

	public void setDescr( String lang, String descr) {
		getInfo( lang).mInfo2 = descr;
	}

	@Override
	public void setInformation( LinkType information) {
		mInformation = information;
	}

	@Override
	public void setName( String name) {
		getInfo().mInfo1 = name;
	}

	public void setName( String lang, String name) {
		getInfo( lang).mInfo1 = name;
	}
}
