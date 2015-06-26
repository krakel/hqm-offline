package de.doerl.hqm.controller;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

class ReputationDelete extends AHQMWorker<Object, Object> {
	private FReputation mRep;
	private EditController mCtrl;

	private ReputationDelete( FReputation rep, EditController ctrl) {
		mRep = rep;
		mCtrl = ctrl;
	}

	public static void get( FReputation rep, EditController ctrl) {
		ReputationDelete worker = new ReputationDelete( rep, ctrl);
		rep.mParentCategory.mParentHQM.mQuestSetCat.forEachMember( worker, null);
		rep.remove();
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		quest.forEachReward( this, p);
		mCtrl.fireChanged( quest);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		set.forEachQuest( this, p);
		return null;
	}

	@Override
	public Object forReward( FReward rr, Object p) {
		if (Utils.equals( rr.mRep, mRep)) {
			rr.remove();
		}
		return null;
	}
}
