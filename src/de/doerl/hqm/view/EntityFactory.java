package de.doerl.hqm.view;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestSets;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class EntityFactory extends AHQMWorker<Object, EditView> {
//	private static final Logger LOGGER = Logger.getLogger( BaseFactory.class.getName());
	private static final EntityFactory WORKER = new EntityFactory();

	private EntityFactory() {
	}

	public static void get( ABase base, EditView view) {
		base.accept( WORKER, view);
	}

	@Override
	protected Object doBase( ABase base, EditView view) {
//		Utils.log( LOGGER, Level.WARNING, "Common.worker");
		return null;
	}

	@Override
	public Object forHQM( FHqm hqm, EditView view) {
		view.addBase( hqm, new HQMEntity( view, hqm));
		return null;
	}

	@Override
	public Object forQuestSets( FQuestSets set, EditView view) {
		view.addBase( set, new QuestSetEntity( view, set));
		return null;
	}
}
