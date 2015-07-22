package de.doerl.hqm.view.dispatch;

import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FLanguage;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class LangDeleteWorker extends AHQMWorker<Object, FLanguage> {
	private static final LangDeleteWorker WORKER = new LangDeleteWorker();

	private LangDeleteWorker() {
	}

	public static void get( FHqm hqm, FLanguage lang) {
		hqm.mQuestSetCat.forEachMember( WORKER, lang);
		hqm.mReputationCat.forEachMember( WORKER, lang);
		hqm.mGroupTierCat.forEachMember( WORKER, lang);
		hqm.removeLanguage( lang);
	}

	public static void get( FHqm hqm, String text) {
		FLanguage lang = hqm.getLanguage( text);
		if (lang != null) {
			get( hqm, lang);
		}
	}

	@Override
	protected Object doNamed( ANamed named, FLanguage lang) {
		named.deleteLocale( lang);
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, FLanguage lang) {
		tier.deleteLocale( lang);
		tier.forEachGroup( this, lang);
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, FLanguage lang) {
		quest.deleteLocale( lang);
		quest.forEachTask( this, lang);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, FLanguage lang) {
		set.deleteLocale( lang);
		set.forEachQuest( this, lang);
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, FLanguage lang) {
		rep.deleteLocale( lang);
		rep.forEachMarker( this, lang);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, FLanguage lang) {
		task.deleteLocale( lang);
		task.forEachLocation( this, lang);
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, FLanguage lang) {
		task.deleteLocale( lang);
		task.forEachMob( this, lang);
		return null;
	}
}
