package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class SizeOfQuests extends AHQMWorker<Object, Object> {
	private int mResult = 0;

	private SizeOfQuests() {
	}

	public static Object get( FQuestSet set) {
		SizeOfQuests size = new SizeOfQuests();
		set.forEachQuest( size, null);
		return size.mResult;
	}

	public static int get( FQuestSetCat cat) {
		SizeOfQuests size = new SizeOfQuests();
		cat.forEachMember( size, null);
		return size.mResult;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		set.forEachQuest( this, p);
		return null;
	}
}
