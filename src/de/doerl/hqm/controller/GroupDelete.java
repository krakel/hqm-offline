package de.doerl.hqm.controller;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class GroupDelete extends AHQMWorker<Object, Object> {
//	private FGroup mGrp;
//	private EditController mCtrl;
	private GroupDelete( FGroup grp, EditController ctrl) {
//		mGrp = grp;
//		mCtrl = ctrl;
	}

	public static void get( FGroup grp, EditController ctrl) {
//		GroupDelete worker = new GroupDelete( grp, ctrl);
//		grp.mParentCategory.mParentHQM.mQuestSetCat.forEachMember( worker, null);
		grp.remove();
	}
}
