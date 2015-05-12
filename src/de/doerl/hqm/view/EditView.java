package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

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
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png"); //.getSubimage( 20, 20, 260, 340);
	protected HashMap<ABase, AEntity<?>> mContent = new HashMap<ABase, AEntity<?>>();
	private EditController mCtrl;

	public EditView( EditController ctrl) {
		mCtrl = ctrl;
		ctrl.getModel().addListener( this);
	}

	public EditView( EditController ctrl, ABase base) {
		mCtrl = ctrl;
	}

	void addBase( ABase base, AEntity<?> ent) {
		mContent.put( base, ent);
		ent.update();
	}

	@Override
	public void baseAdded( ModelEvent event) {
		ABase base = event.getBase();
		if (base != null && !mContent.containsKey( base)) {
			EntityFactory.get( base, this);
		}
	}

	@Override
	public void baseChanged( ModelEvent event) {
	}

	@Override
	public void baseRemoved( ModelEvent event) {
	}

	@Override
	public void baseUpdate( ModelEvent event) {
		ABase base = event.getBase();
		AEntity<?> ent = EntityFactory.get( base, this);
		if (ent != null) {
			addBase( base, ent);
//			SwingUtilities.invokeLater( new Runnable() {
//			@Override
//			public void run() {
//			}
//		});
		}
		else {
			Utils.log( LOGGER, Level.WARNING, "missing AEntity for {0}", base);
		}
	}

	protected void drawImage( Graphics2D g2, JPanel unit, BufferedImage img) {
		if (img != null) {
			float sx = (float) unit.getWidth() / img.getWidth();
			float sy = (float) unit.getHeight() / img.getHeight();
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			g2.drawImage( img, xform, null);
		}
	}

	protected void drawImage( Graphics2D g2, JPanel unit, BufferedImage img, float dw, float dh, boolean flip) {
		if (img != null) {
			float sx = dw * ((float) unit.getWidth() / img.getWidth());
			float sy = dh * ((float) unit.getHeight() / img.getHeight());
//			AffineTransform xform = new AffineTransform( m00, 0, 0, m11, 0, 0);
			if (flip) {
				AffineTransform xform = AffineTransform.getScaleInstance( -sx, sy);
				xform.translate( -2 * img.getWidth( null), 0);
				g2.drawImage( img, xform, null);
			}
			else {
				AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
				g2.drawImage( img, xform, null);
			}
		}
	}

	protected void drawImage( Graphics2D g2, JPanel unit, BufferedImage img, int width, int height) {
		if (img != null) {
			float m00 = (float) width / img.getWidth();
			float m11 = (float) height / img.getHeight();
			float m02 = (float) (unit.getWidth() - width) / 2;
			float m12 = (float) (unit.getHeight() - height) / 2;
			AffineTransform xform = new AffineTransform( m00, 0, 0, m11, m02, m12);
			g2.drawImage( img, xform, null);
		}
	}

	protected void drawImage1( Graphics2D g2, JPanel unit, BufferedImage img, int dw, int dh) {
		if (img != null) {
			int width = unit.getWidth() - 5 * dw;
			int height = unit.getHeight() - 3 * dh;
			float m00 = (float) width / img.getWidth();
			float m11 = (float) height / img.getHeight();
			AffineTransform xform = new AffineTransform( m00, 0, 0, m11, dw, dh);
			g2.drawImage( img, xform, null);
		}
	}

	protected void drawZOrder( Graphics2D g2, JPanel unit) {
		Container view = unit.getParent();
		if (view != null) {
			int pos = view.getComponentZOrder( unit);
			g2.setPaint( Color.BLACK);
			g2.drawString( Integer.toString( pos), 3, unit.getHeight() - 3);
		}
	}

	public ABase getBase() {
		return null;
	}

	public EditController getCtrl() {
		return mCtrl;
	}

	@Override
	protected void paintComponent( Graphics g) {
		if (ui != null) {
//			drawImage( (Graphics2D) g, this, BACKGROUND, 0.5F, 1.0F); // 170, 234
			drawImage( (Graphics2D) g, this, BACKGROUND, 0.5F, 1F, false);
			drawImage( (Graphics2D) g, this, BACKGROUND, 0.5F, 1F, true);
//			drawImage( (Graphics2D) g, this, FRONT, 1F, 1F, false);
			drawImage1( (Graphics2D) g, this, FRONT, 20, 20);
		}
	}

	public void showBase( ABase base) {
	}
}
