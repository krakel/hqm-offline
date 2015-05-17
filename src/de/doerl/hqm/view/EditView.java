package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

@SuppressWarnings( "nls")
public class EditView extends JPanel implements IModelListener {
	private static final long serialVersionUID = -15489231166915296L;
	private static final Logger LOGGER = Logger.getLogger( EditView.class.getName());
	private static final GridBagConstraints GBC = createConstraints();
	private HashMap<ABase, AEntity<?>> mContent = new HashMap<ABase, AEntity<?>>();
	private EditController mCtrl;
	private JPanel mCenter = new JPanel( new GridBagLayout());
	private AEntity<?> mEmpty = new EmptyEntity( this);
	private JToolBar mLeftBar = createToolBar( true);
	private JToolBar mRightBar = createToolBar( false);

	public EditView( EditController ctrl) {
		setLayout( new GridLayout( 1, 1));
		mCtrl = ctrl;
		ctrl.getModel().addListener( this);
		mCenter.setOpaque( false);
		mCenter.setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		setCenter( mEmpty);
		add( createSplit());
	}

	static void addKeyAction( JComponent comp, String key, Action action) {
		Object o = new Object();
		comp.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke( key), o);
		comp.getActionMap().put( o, action);
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
		AEntity<?> ent = null;
		ABase base = event.getBase();
		if (base != null && !mContent.containsKey( base)) {
			ent = EntityFactory.get( base, this);
			if (ent == null) {
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

	private JComponent createLeafToolBar( JToolBar bar) {
		Box result = Box.createHorizontalBox();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setAlignmentY( TOP_ALIGNMENT);
		result.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED));
		result.add( bar);
		return result;
	}

	private JComponent createLeafTools() {
		Box result = Box.createHorizontalBox();
		result.add( createLeafToolBar( mLeftBar));
		result.add( createLeafToolBar( mRightBar));
		return result;
	}

	private JComponent createSplit() {
		Box result = Box.createVerticalBox();
		result.add( createLeafTools());
		result.add( Box.createVerticalGlue());
		result.add( mCenter);
		return result;
	}

	private JToolBar createToolBar( boolean left) {
		JToolBar result = new JToolBar();
		Dimension size = new Dimension( Short.MAX_VALUE, 36);
		result.setPreferredSize( new Dimension( size));
		result.setFloatable( false);
		try {
			result.setRollover( true);
		}
		catch (NoSuchMethodError ex) {
		}
		JComponent box = Box.createHorizontalBox();
		box.setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
//		box.setPreferredSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		result.add( box);
		result.add( Box.createHorizontalGlue());
		result.add( AEntity.createToolButton( ResourceManager.getIcon( "blank.gif")));
		return result;
	}

	public EditController getController() {
		return mCtrl;
	}

	private void setCenter( AEntity<?> ent) {
		mCenter.removeAll();
		mCenter.add( ent, GBC);
		mCenter.validate();
		mCenter.repaint();
		setLeftTool( ent.getLeftTool());
		setRightTool( ent.getRightTool());
	}

	void setLeftTool( JComponent tool) {
		JComponent box = (JComponent) mLeftBar.getComponent( 0);
		box.removeAll();
		if (tool != null) {
			box.add( tool);
		}
		mLeftBar.validate();
		mLeftBar.repaint();
	}

	void setRightTool( JComponent tool) {
		JComponent box = (JComponent) mRightBar.getComponent( 0);
		box.removeAll();
		if (tool != null) {
			box.add( tool);
		}
		mRightBar.validate();
		mRightBar.repaint();
	}

	private class EntityRemove implements Runnable {
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

	private class EntityUpdate implements Runnable {
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
