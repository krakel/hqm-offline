package de.doerl.hqm.medium.gson;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;

class Parser extends AHQMWorker<Object, FObject> implements IToken {
	private void loadState( FHqm hqm) {
		FObject obj = Medium.redJson( Medium.STATE_FILE);
		if (obj != null) {
			hqm.mQuesting = FValue.toBoolean( obj.get( IToken.HQM_QUESTING));
			hqm.mHadrcore = FValue.toBoolean( obj.get( IToken.HQM_HARDCORE));
		}
	}

	void readSrc( FHqm hqm, FObject obj) {
		loadState( hqm);
//        QuestLine.loadDescription();
//        DeathStats.loadAll(isClient);
//        Reputation.loadAll();
//        GroupTier.loadAll();
//        Team.loadAll(isClient);
//        QuestSet.loadAll();
//        QuestingData.loadQuestingData();
//        SaveHelper.onLoad();
	}
}
