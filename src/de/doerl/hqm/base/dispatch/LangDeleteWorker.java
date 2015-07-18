package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.utils.Utils;

public class LangDeleteWorker extends AHQMWorker<Object, String> {
	private static final LangDeleteWorker WORKER = new LangDeleteWorker();

	private LangDeleteWorker() {
	}

	public static void get( FHqm hqm, String lang) {
		hqm.mQuestSetCat.forEachMember( WORKER, lang);
		hqm.mReputationCat.forEachMember( WORKER, lang);
		hqm.mGroupTierCat.forEachMember( WORKER, lang);
		hqm.mLanguages.remove( lang);
		if (Utils.equals( hqm.mLang, lang)) {
			if (hqm.mLanguages.size() > 0) {
				hqm.mLang = hqm.mLanguages.firstElement();
			}
			else {
				hqm.mLang = FHqm.LANG_EN_US;
			}
		}
	}

	@Override
	protected Object doNamed( ANamed named, String lang) {
		named.deleteLang( lang);
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, String lang) {
		tier.deleteLang( lang);
		tier.forEachGroup( this, lang);
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, String lang) {
		quest.deleteLang( lang);
		quest.forEachTask( this, lang);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, String lang) {
		set.deleteLang( lang);
		set.forEachQuest( this, lang);
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, String lang) {
		rep.deleteLang( lang);
		rep.forEachMarker( this, lang);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, String lang) {
		task.deleteLang( lang);
		task.forEachLocation( this, lang);
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, String lang) {
		task.deleteLang( lang);
		task.forEachMob( this, lang);
		return null;
	}
}
