package de.doerl.hqm.model;

import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.ASet;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class LoaderFactory extends AHQMWorker<Object, EditModel> {
	private static final LoaderFactory WORKER = new LoaderFactory();

	private LoaderFactory() {
	}

	public static void create( FHqm hqm, EditModel model) {
		hqm.accept( WORKER, model);
	}

	@Override
	protected Object doMember( AMember<? extends ANamed> set, EditModel model) {
		model.fireBaseAdded( set);
		return null;
	}

	@Override
	protected Object doSet( ASet<? extends ANamed> set, EditModel model) {
		model.fireBaseAdded( set);
		set.forEachMember( this, model);
		return null;
	}

	@Override
	public Object forHQM( FHqm hqm, EditModel model) {
		model.fireBaseAdded( hqm);
		hqm.mQuestSets.accept( this, model);
		hqm.mRepSets.accept( this, model);
		hqm.mQuests.accept( this, model);
		hqm.mGroupTiers.accept( this, model);
		hqm.mGroups.accept( this, model);
		return null;
	}
}
