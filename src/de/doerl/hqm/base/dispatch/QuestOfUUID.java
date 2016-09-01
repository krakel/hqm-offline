/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.utils.Utils;

public class QuestOfUUID extends AHQMWorker<FQuest, String> {
	private static final QuestOfUUID WORKER = new QuestOfUUID();

	private QuestOfUUID() {
	}

	public static FQuest get( FHqm hqm, String uuid) {
		return get( hqm.mQuestSetCat, uuid);
	}

	public static FQuest get( FQuestSet set, String uuid) {
		return get( set.mParentCategory, uuid);
	}

	public static FQuest get( FQuestSetCat cat, String uuid) {
		return cat.forEachMember( WORKER, uuid);
	}

	@Override
	public FQuest forQuest( FQuest quest, String uuid) {
		return Utils.equals( quest.getUUID(), uuid) ? quest : null;
	}

	@Override
	public FQuest forQuestSet( FQuestSet set, String uuid) {
		return set.forEachQuest( this, uuid);
	}
}
