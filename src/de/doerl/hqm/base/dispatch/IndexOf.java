package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.utils.Utils;

public class IndexOf extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private ABase mBase;

	private IndexOf( ABase set) {
		mBase = set;
	}

	public static int getGroup( FGroup grp) {
		IndexOf worker = new IndexOf( grp);
		grp.mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	public static int getMarker( FMarker mark) {
		IndexOf worker = new IndexOf( mark);
		mark.getParent().forEachMarker( worker, null);
		return worker.mResult;
	}

	public static int getMember( AMember<?> member) {
		IndexOf worker = new IndexOf( member);
		member.getParent().forEachMember( worker, null);
		return worker.mResult;
	}

	public static int getQuest( FQuest quest) {
		IndexOf worker = new IndexOf( quest);
		quest.mParentHQM.forEachQuest( worker, null);
		return worker.mResult;
	}

	public static int getReputation( FReputation rep) {
		IndexOf worker = new IndexOf( rep);
		rep.mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	protected Boolean doMember( AMember<? extends ANamed> member, Object p) {
		++mResult;
		return Utils.equals( member, mBase) ? Boolean.TRUE : null;
	}

	@Override
	public Boolean forGroup( FGroup grp, Object p) {
		++mResult;
		return Utils.equals( grp, mBase) ? Boolean.TRUE : null;
	}

	@Override
	public Boolean forMarker( FMarker mark, Object p) {
		++mResult;
		return Utils.equals( mark, mBase) ? Boolean.TRUE : null;
	}

	@Override
	public Boolean forQuest( FQuest quest, Object p) {
		++mResult;
		return Utils.equals( quest, mBase) ? Boolean.TRUE : null;
	}

	@Override
	public Boolean forReputation( FReputation rep, Object p) {
		++mResult;
		return Utils.equals( rep, mBase) ? Boolean.TRUE : null;
	}
}
