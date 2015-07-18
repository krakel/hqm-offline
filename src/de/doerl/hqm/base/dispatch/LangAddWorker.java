package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FReputation;

public class LangAddWorker extends AHQMWorker<Object, String> {
	private static final LangAddWorker WORKER = new LangAddWorker();

	private LangAddWorker() {
	}

	public static void get( FHqm hqm, String lang) {
		hqm.mQuestSetCat.forEachMember( WORKER, lang);
		hqm.mReputationCat.forEachMember( WORKER, lang);
		hqm.mGroupTierCat.forEachMember( WORKER, lang);
		if (!hqm.mLanguages.contains( lang)) {
			hqm.mLanguages.add( lang);
		}
	}

	@Override
	protected Object doNamed( ANamed named, String lang) {
		named.addLang( lang);
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, String lang) {
		tier.addLang( lang);
		tier.forEachGroup( this, lang);
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, String lang) {
		quest.addLang( lang);
		quest.forEachTask( this, lang);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, String lang) {
		set.addLang( lang);
		set.forEachQuest( this, lang);
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, String lang) {
		rep.addLang( lang);
		rep.forEachMarker( this, lang);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, String lang) {
		task.addLang( lang);
		task.forEachLocation( this, lang);
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, String lang) {
		task.addLang( lang);
		task.forEachMob( this, lang);
		return null;
	}
}
