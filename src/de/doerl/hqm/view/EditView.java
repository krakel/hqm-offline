package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
	private static final BufferedImage BACKGROUND = ResourceManager.getImage( "book.png").getSubimage( 0, 0, 170, 234);
	protected HashMap<ABase, AEntity<?>> mContent = new HashMap<ABase, AEntity<?>>();
	private EditController mCtrl;

	public EditView( EditController ctrl) {
		setLayout( new GridLayout( 1, 1));
		mCtrl = ctrl;
		ctrl.getModel().addListener( this);
//		setPreferredSize( new Dimension( 2 * 170, 234));
//		setMaximumSize( new Dimension( 2 * 170, 234));
	}

	public EditView( EditController ctrl, ABase base) {
		mCtrl = ctrl;
	}

	static void drawBackground( Graphics2D g2, Component unit) {
		g2.setColor( unit.getBackground());
		g2.fillRect( 0, 0, unit.getWidth(), unit.getHeight());
		drawImage( g2, unit, BACKGROUND, 0.5, 1, false);
		drawImage( g2, unit, BACKGROUND, 0.5, 1, true);
	}

	static void drawBottomLeftString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = c.getWidth() - fm.stringWidth( text);
		int y = c.getHeight() - fm.getDescent();
		g2.drawString( text, x, y);
	}

	static void drawCenteredString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = (c.getWidth() - fm.stringWidth( text)) / 2;
		int y = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
		g2.drawString( text, x, y);
	}

	static void drawImage( Graphics2D g2, Component c, BufferedImage img) {
		if (img != null) {
			double sx = (double) c.getWidth() / img.getWidth();
			double sy = (double) c.getHeight() / img.getHeight();
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			g2.drawImage( img, xform, null);
		}
	}

	static void drawImage( Graphics2D g2, Component c, BufferedImage img, boolean flip) {
		if (img != null) {
			double sx = (double) c.getWidth() / img.getWidth();
			double sy = (double) c.getHeight() / img.getHeight();
			if (flip) {
				AffineTransform xform = AffineTransform.getScaleInstance( -sx, sy);
				xform.translate( -2 * img.getWidth(), 0);
				g2.drawImage( img, xform, null);
			}
			else {
				AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
				g2.drawImage( img, xform, null);
			}
		}
	}

	static void drawImage( Graphics2D g2, Component c, BufferedImage img, double zoomX, double zoomY, boolean flip) {
		if (img != null) {
			double sx = zoomX * ((double) c.getWidth() / img.getWidth());
			double sy = zoomY * ((double) c.getHeight() / img.getHeight());
			if (flip) {
				AffineTransform xform = AffineTransform.getScaleInstance( -sx, sy);
				xform.translate( -2 * img.getWidth(), 0);
				g2.drawImage( img, xform, null);
			}
			else {
				AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
				g2.drawImage( img, xform, null);
			}
		}
	}

	static void drawImage( Graphics2D g2, Component c, BufferedImage img, int width, int height) {
		if (img != null) {
			double sx = (double) width / img.getWidth();
			double sy = (double) height / img.getHeight();
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			double tx = (double) (c.getWidth() - width) / 2;
			double ty = (double) (c.getHeight() - height) / 2;
			xform.translate( tx, ty);
			g2.drawImage( img, xform, null);
		}
	}

	static void drawZOrder( Graphics2D g2, JPanel unit) {
		Container view = unit.getParent();
		if (view != null) {
			int pos = view.getComponentZOrder( unit);
			g2.setPaint( Color.BLACK);
			g2.drawString( Integer.toString( pos), 3, unit.getHeight() - 3);
		}
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
			SwingUtilities.invokeLater( new EntityAdd( ent));
		}
		else {
			Utils.log( LOGGER, Level.WARNING, "missing AEntity for {0}", base);
		}
	}

	public ABase getBase() {
		return null;
	}

	public EditController getController() {
		return mCtrl;
	}

	@Override
	protected void paintComponent( Graphics g) {
		drawBackground( (Graphics2D) g, this);
	}

	private class EntityAdd implements Runnable {
		private AEntity<?> mEnt;

		public EntityAdd( AEntity<?> ent) {
			mEnt = ent;
		}

		@Override
		public void run() {
			removeAll();
			mEnt.update();
			validate();
			repaint();
		}
	}

	private class EntityRemove implements Runnable {
		private AEntity<?> mEnt;

		public EntityRemove( AEntity<?> ent) {
			mEnt = ent;
		}

		@Override
		public void run() {
			mEnt.remove();
			removeAll();
			validate();
			repaint();
		}
	}
}
