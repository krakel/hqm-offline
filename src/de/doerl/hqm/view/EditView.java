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
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;

public class EditView extends JPanel implements IModelListener {
	private static final long serialVersionUID = -15489231166915296L;
	private static final Logger LOGGER = Logger.getLogger( EditView.class.getName());
	private static final GridBagConstraints GBC = createConstraints();
	private HashMap<ABase, AEntity<?>> mContent = new HashMap<ABase, AEntity<?>>();
	private EditController mCtrl;
	private AEntity<?> mEmpty = new EntityEmpty( this);

	public EditView( EditController ctrl) {
		setLayout( new GridBagLayout());
		mCtrl = ctrl;
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
	public void baseAdded( ModelEvent event) {
	}

	@Override
	public void baseChanged( ModelEvent event) {
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.getBase();
		if (base != null) {
			AEntity<?> ent = mContent.remove( base);
			if (ent != null) {
				SwingUtilities.invokeLater( new EntityRemove( ent));
			}
		}
	}

	@Override
	public void baseUpdate( ModelEvent event) {
		ABase base = event.getBase();
		if (base != null) {
			AEntity<?> ent = mContent.get( base);
			if (ent == null) {
				ent = EntityFactory.get( base, this);
				if (ent != null) {
					mContent.put( base, ent);
				}
			}
			if (ent != null) {
				SwingUtilities.invokeLater( new EntityUpdate( ent));
			}
			else {
				Utils.log( LOGGER, Level.WARNING, "missing AEntity for {0}", base);
			}
		}
	}

	public EditController getController() {
		return mCtrl;
	}

	public JToolBar getToolBar( ABase base) {
		if (base != null) {
			AEntity<?> ent = mContent.get( base);
			if (ent != null) {
				return ent.getToolBar();
			}
		}
		return null;
	}

	private void setCenter( AEntity<?> ent) {
		removeAll();
		add( ent, GBC);
		validate();
		repaint();
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

	private final class EntityUpdate implements Runnable {
		private AEntity<?> mEnt;

		public EntityUpdate( AEntity<?> ent) {
			mEnt = ent;
		}

		@Override
		public void run() {
			setCenter( mEnt);
		}
	}
}
