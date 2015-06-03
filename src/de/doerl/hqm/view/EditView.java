package de.doerl.hqm.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupCat;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;

public class EditView extends JPanel implements IModelListener {
	private static final long serialVersionUID = -15489231166915296L;
	private static final Logger LOGGER = Logger.getLogger( EditView.class.getName());
	private static final GridBagConstraints GBC = createConstraints();
	private HashMap<ABase, AEntity<?>> mContent = new HashMap<>();
	private EditController mCtrl;
	private AEntity<?> mEmpty;

	public EditView( EditController ctrl) {
		setLayout( new GridBagLayout());
		mCtrl = ctrl;
		mEmpty = new EntityEmpty( ctrl);
		setOpaque( false);
//		setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		setCenter( mEmpty);
	}

	private static GridBagConstraints createConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		return gbc;
	}

	@Override
	public void baseActivate( ModelEvent event) {
		ABase base = event.mBase;
		if (base != null) {
			AEntity<?> ent = EntityFactory.get( base, this);
			if (ent != null) {
				SwingUtilities.invokeLater( new EntityActivate( ent));
			}
			else {
				Utils.log( LOGGER, Level.WARNING, "missing AEntity for {0}", base);
			}
		}
	}

	@Override
	public void baseAdded( ModelEvent event) {
		EntityFactory.get( event.mBase, this);
	}

	@Override
	public void baseChanged( ModelEvent event) {
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		AEntity<?> ent = mContent.remove( base);
		if (ent != null) {
			mCtrl.removeListener( ent);
			SwingUtilities.invokeLater( new EntityRemove( ent));
		}
	}

	public EditController getController() {
		return mCtrl;
	}

	public JToolBar getToolBar( ABase base) {
		AEntity<?> ent = EntityFactory.get( base, this);
		if (ent != null) {
			return ent.getToolBar();
		}
		return null;
	}

	private void setCenter( AEntity<?> ent) {
		removeAll();
		add( ent, GBC);
		validate();
		repaint();
	}

	private final class EntityActivate implements Runnable {
		private AEntity<?> mEnt;

		public EntityActivate( AEntity<?> ent) {
			mEnt = ent;
		}

		@Override
		public void run() {
			setCenter( mEnt);
		}
	}

	private static class EntityFactory extends AHQMWorker<AEntity<? extends ABase>, EditView> {
//		private static final Logger LOGGER = Logger.getLogger( EntityFactory.class.getName());
		private static final EntityFactory WORKER = new EntityFactory();

		private EntityFactory() {
		}

		public static AEntity<? extends ABase> get( ABase base, EditView view) {
			return base.accept( WORKER, view);
		}

		@Override
		protected AEntity<? extends ABase> doTask( AQuestTask task, EditView p) {
			return null;
		}

		@Override
		public AEntity<? extends ABase> forGroup( FGroup grp, EditView view) {
			AEntity<?> ent = view.mContent.get( grp);
			if (ent == null) {
				ent = new EntityGroup( grp, view.getController());
				view.mContent.put( grp, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forGroupCat( FGroupCat cat, EditView view) {
			AEntity<?> ent = view.mContent.get( cat);
			if (ent == null) {
				ent = new EntityGroupCat( cat, view.getController());
				view.mContent.put( cat, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forGroupTier( FGroupTier tier, EditView view) {
			AEntity<?> ent = view.mContent.get( tier);
			if (ent == null) {
				ent = new EntityGroupTier( tier, view.getController());
				view.mContent.put( tier, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forGroupTierCat( FGroupTierCat cat, EditView view) {
			AEntity<?> ent = view.mContent.get( cat);
			if (ent == null) {
				ent = new EntityGroupTierCat( cat, view.getController());
				view.mContent.put( cat, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forHQM( FHqm hqm, EditView view) {
			AEntity<?> ent = view.mContent.get( hqm);
			if (ent == null) {
				ent = new EntityHQM( hqm, view.getController());
				view.mContent.put( hqm, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forQuest( FQuest quest, EditView view) {
			if (quest.isDeleted()) {
				return null;
			}
			AEntity<?> ent = view.mContent.get( quest);
			if (ent == null) {
				ent = new EntityQuest( quest, view.getController());
				view.mContent.put( quest, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forQuestSet( FQuestSet set, EditView view) {
			AEntity<?> ent = view.mContent.get( set);
			if (ent == null) {
				ent = new EntityQuestSet( set, view.getController());
				view.mContent.put( set, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forQuestSetCat( FQuestSetCat cat, EditView view) {
			AEntity<?> ent = view.mContent.get( cat);
			if (ent == null) {
				ent = new EntityQuestSetCat( cat, view.getController());
				view.mContent.put( cat, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forReputation( FReputation rep, EditView view) {
			AEntity<?> ent = view.mContent.get( rep);
			if (ent == null) {
				ent = new EntityReputation( rep, view.getController());
				view.mContent.put( rep, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}

		@Override
		public AEntity<? extends ABase> forReputationCat( FReputationCat cat, EditView view) {
			AEntity<?> ent = view.mContent.get( cat);
			if (ent == null) {
				ent = new EntityReputationCat( cat, view.getController());
				view.mContent.put( cat, ent);
				view.mCtrl.addListener( ent);
			}
			return ent;
		}
	}

	private final class EntityRemove implements Runnable {
		private AEntity<?> mEnt;

		public EntityRemove( AEntity<?> ent) {
			mEnt = ent;
		}

		@Override
		public void run() {
			mEnt.setVisible( false);
			setCenter( mEmpty);
		}
	}
}
