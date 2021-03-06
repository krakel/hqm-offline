package de.doerl.hqm.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.LinkType;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.leafs.LeafQuest;

public class EditView extends JPanel implements IModelListener {
	private static final long serialVersionUID = -15489231166915296L;
	private static final Logger LOGGER = Logger.getLogger( EditView.class.getName());
	private static final GridBagConstraints GBC = createConstraints();
	private HashMap<ABase, AEntity<?>> mContent = new HashMap<>();
	private EditController mCtrl;
	private AEntity<?> mEmpty;
	private volatile AEntity<?> mActive;
	private volatile EntityQuestSet mActiveSet;

	public EditView( EditController ctrl) {
		setLayout( new GridBagLayout());
		mCtrl = ctrl;
		mEmpty = new EntityEmpty( ctrl);
		mActive = mEmpty;
		setOpaque( false);
//		setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		setCenter( mEmpty);
		ToolTipManager.sharedInstance().setDismissDelay( Integer.MAX_VALUE);
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
			if (event.mCtrlKey) {
				AEntity<?> ent = EntityFactory.get( base.getParent(), this);
				if (ent != null) {
					SwingUtilities.invokeLater( new EntityActivate( ent));
					if (base instanceof FQuest) {
						EntityQuestSet es = (EntityQuestSet) ent;
						es.activeLeaf( (FQuest) base);
					}
				}
				else {
					Utils.log( LOGGER, Level.WARNING, "missing AEntity for {0}", base.getParent());
				}
			}
			else {
				AEntity<?> ent = EntityFactory.get( base, this);
				if (ent != null) {
					SwingUtilities.invokeLater( new EntityActivate( ent));
				}
				else {
					Utils.log( LOGGER, Level.WARNING, "missing AEntity for {0}", base);
				}
			}
		}
		else {
			SwingUtilities.invokeLater( new EntityActivate( mEmpty));
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
		if (ent != null && ent.equals( mActive)) {
			mCtrl.removeListener( ent);
			SwingUtilities.invokeLater( new EntityRemove( ent));
		}
	}

	@Override
	public void baseTreeChange( ModelEvent event) {
	}

	public EditController getController() {
		return mCtrl;
	}

	EntityQuestSet getQuestSet( FQuestSet base) {
		return (EntityQuestSet) EntityFactory.get( base, this);
	}

	public JToolBar getToolBar( ABase base) {
		AEntity<?> ent = EntityFactory.get( base, this);
		if (ent != null) {
			return ent.getToolBar();
		}
		return null;
	}

	boolean handleClickFor( LeafQuest current, MouseEvent evt) {
		return mActiveSet == null || mActiveSet.handleClickFor( current, evt);
	}

	private void markBase( ABase base, LinkType info) {
		base.setInformation( info);
		mCtrl.fireModified( base);
	}

	private void setCenter( AEntity<?> ent) {
		mActive = ent;
		removeAll();
		add( ent, GBC);
		validate();
		repaint();
	}

	void updateActiveSetClear() {
		if (mActiveSet != null) {
			mActiveSet.activeClear();
			mActiveSet = null;
		}
	}

	void updateClear( EntityQuestSet entity, LeafQuest activ) {
		FQuest quest = activ.getQuest();
		FQuestSet set = quest.getParent();
		for (FQuest prev : quest.mRequirements) {
			if (set.equals( prev.getParent())) {
				entity.updateLeaf( prev, LinkType.NORM);
			}
			else {
				EntityQuestSet other = getQuestSet( prev.getParent());
				other.activeClear();
				other.updateLeaf( prev, LinkType.NORM);
				markBase( prev.getParent(), LinkType.NORM);
			}
			markBase( prev, LinkType.NORM);
		}
		for (FQuest post : quest.mPosts) {
			if (set.equals( post.getParent())) {
				entity.updateLeaf( post, LinkType.NORM);
			}
			else {
				EntityQuestSet other = getQuestSet( post.getParent());
				other.activeClear();
				other.updateLeaf( post, LinkType.NORM);
				markBase( post.getParent(), LinkType.NORM);
			}
			markBase( post, LinkType.NORM);
		}
		activ.update( LinkType.NORM);
		markBase( set, LinkType.NORM);
		markBase( quest, LinkType.NORM);
	}

	void updateLinked( EntityQuestSet entity, LeafQuest activ) {
		FQuest quest = activ.getQuest();
		FQuestSet set = quest.getParent();
		for (FQuest prev : quest.mRequirements) {
			if (set.equals( prev.getParent())) {
				entity.updateLeaf( prev, LinkType.PREF);
			}
			else {
				EntityQuestSet other = getQuestSet( prev.getParent());
				other.updateLeaf( prev, LinkType.PREF);
				markBase( prev.getParent(), LinkType.PREF);
			}
			markBase( prev, LinkType.PREF);
		}
		for (FQuest post : quest.mPosts) {
			if (set.equals( post.getParent())) {
				entity.updateLeaf( post, LinkType.POST);
			}
			else {
				EntityQuestSet other = getQuestSet( post.getParent());
				other.updateLeaf( post, LinkType.POST);
				markBase( post.getParent(), LinkType.POST);
			}
			markBase( post, LinkType.POST);
		}
		activ.update( quest.containExt() ? LinkType.LINK : LinkType.BASE);
		markBase( set, LinkType.BASE);
		markBase( quest, LinkType.BASE);
		mActiveSet = entity;
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

	static class EntityFactory extends AHQMWorker<AEntity<? extends ABase>, EditView> {
//		private static final Logger LOGGER = Logger.getLogger( EntityFactory.class.getName());
		private static final EntityFactory WORKER = new EntityFactory();

		private EntityFactory() {
		}

		public static AEntity<? extends ABase> get( ABase base, EditView view) {
			return base.accept( WORKER, view);
		}

		@Override
		protected AEntity<? extends ABase> doMember( AMember member, EditView p) {
			return null;
		}

		@Override
		protected AEntity<? extends ABase> doTask( AQuestTask task, EditView p) {
			return null;
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
				ent = new EntityQuestSet( set, view);
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
