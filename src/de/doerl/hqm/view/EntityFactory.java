package de.doerl.hqm.view;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.ASet;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class EntityFactory extends AHQMWorker<AEntity<? extends ABase>, EditView> {
//	private static final Logger LOGGER = Logger.getLogger( EntityFactory.class.getName());
	private static final EntityFactory WORKER = new EntityFactory();

	private EntityFactory() {
	}

	public static AEntity<? extends ABase> get( ABase base, EditView view) {
		return base.accept( WORKER, view);
	}

	@Override
	protected AEntity<? extends ABase> doSet( ASet<? extends ANamed> set, EditView view) {
		return null;
	}

	@Override
	public AEntity<? extends ABase> forHQM( FHqm hqm, EditView view) {
		return new HQMEntity( view, hqm);
	}

	@Override
	public AEntity<? extends ABase> forQuest( FQuest quest, EditView view) {
		return new QuestEntity( view, quest);
	}

	@Override
	public AEntity<? extends ABase> forQuestSet( FQuestSet qs, EditView view) {
		return new QuestSetEntity( view, qs);
	}

	@Override
	public AEntity<? extends ABase> forQuestSets( FQuestSets set, EditView view) {
		return new QuestSetsEntity( view, set);
	}
}
