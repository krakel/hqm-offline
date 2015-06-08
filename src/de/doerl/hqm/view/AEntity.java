package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.utils.ResourceManager;

abstract class AEntity<T extends ABase> extends JPanel implements IModelListener {
	private static final long serialVersionUID = -3039298434411863516L;
	static final int ZOOM = 2;
	static final int GAP = 8;
	static final int ICON_SIZE = 36;
	static final Dimension VIEW_SIZE = new Dimension( 2 * ZOOM * 170, ZOOM * 234);
	static final Font FONT_SMALL = new Font( "SansSerif", Font.PLAIN, 11);
	static final Font FONT_NORMAL = new Font( "SansSerif", Font.PLAIN, 14);
	static final Font FONT_TITLE = new Font( "SansSerif", Font.PLAIN, 18);
	static final Font FONT_STACK = new Font( "SansSerif", Font.PLAIN | Font.BOLD, 14);
	static final Color SELECTED = new Color( 0xAAAAAA);
	static final Color UNSELECTED = new Color( 0x404040);
	private static int sColValue;
	protected EditController mCtrl;

	AEntity( EditController ctrl, LayoutManager layout) {
		super( layout, true);
		mCtrl = ctrl;
		setOpaque( true);
		setMinimumSize( VIEW_SIZE.getSize());
		setPreferredSize( VIEW_SIZE.getSize());
//		setMaximumSize( new Dimension( min));
	}

	static void drawBackground( Graphics2D g2, Component c, boolean left) {
		drawBackground( g2, c, ResourceManager.getImageUI( "hqm.book.back"), left);
	}

	static void drawBackground( Graphics2D g2, Component c, Image img, boolean left) {
		AffineTransform form = new AffineTransform();
		double sx = (double) c.getWidth() / img.getWidth( null);
		double sy = (double) c.getHeight() / img.getHeight( null);
		if (left) {
			form.scale( sx, sy);
			g2.drawImage( img, form, null);
		}
		else {
			form.scale( -sx, sy);
			form.translate( -img.getWidth( null), 0);
			g2.drawImage( img, form, null);
		}
	}

	static void drawBackgroundHalf( Graphics2D g2, Component c, boolean left) {
		drawBackgroundHalf( g2, c, ResourceManager.getImageUI( "hqm.book.back"), left);
	}

	static void drawBackgroundHalf( Graphics2D g2, Component c, Image img, boolean left) {
		AffineTransform form = new AffineTransform();
		double sx = (double) c.getWidth() / img.getWidth( null) / 2;
		double sy = (double) c.getHeight() / img.getHeight( null);
		if (left) {
			form.scale( sx, sy);
			g2.drawImage( img, form, null);
		}
		else {
			form.scale( -sx, sy);
			form.translate( -2 * img.getWidth( null), 0);
			g2.drawImage( img, form, null);
		}
	}

	static void drawBottomLeftString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = c.getWidth() - fm.stringWidth( text);
		int y = c.getHeight() - fm.getDescent();
		g2.drawString( text, x, y);
	}

	static void drawCenteredImage( Graphics2D g2, Component c, Image img) {
		if (img != null) {
			AffineTransform form = new AffineTransform();
			double sx = (double) c.getWidth() / img.getWidth( null);
			double sy = (double) c.getHeight() / img.getHeight( null);
			double sm = Math.min( sx, sy);
			form.translate( c.getWidth() / 2, c.getHeight() / 2);
			form.scale( sm, sm);
			form.translate( -img.getWidth( null) / 2, -img.getHeight( null) / 2);
			g2.drawImage( img, form, null);
		}
	}

	static void drawCenteredImage( Graphics2D g2, Component c, Image img, double zoom) {
		if (img != null) {
			AffineTransform form = new AffineTransform();
			double sx = zoom * c.getWidth() / img.getWidth( null);
			double sy = zoom * c.getHeight() / img.getHeight( null);
			double sm = Math.min( sx, sy);
			form.translate( c.getWidth() / 2, c.getHeight() / 2);
			form.scale( sm, sm);
			form.translate( -img.getWidth( null) / 2, -img.getHeight( null) / 2);
			g2.drawImage( img, form, null);
		}
	}

	static void drawCenteredString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = (c.getWidth() - fm.stringWidth( text)) / 2;
		int y = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
		g2.drawString( text, x, y);
	}

	static void drawImage( Graphics2D g2, Component c, Image img) {
		if (img != null) {
			AffineTransform form = new AffineTransform();
			double sx = (double) c.getWidth() / img.getWidth( null);
			double sy = (double) c.getHeight() / img.getHeight( null);
			form.scale( sx, sy);
			g2.drawImage( img, form, null);
		}
	}

	static void drawImage( Graphics2D g2, Image img, double sx, double sy, double tx, double ty) {
		if (img != null) {
			AffineTransform form = new AffineTransform();
			form.scale( sx, sy);
			form.translate( tx, ty);
			g2.drawImage( img, form, null);
		}
	}

	protected static JComponent leafBox( int axis) {
		JPanel result = new JPanel();
		result.setLayout( new BoxLayout( result, axis));
		if (axis == BoxLayout.Y_AXIS) {
			result.setAlignmentX( LEFT_ALIGNMENT);
		}
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JComponent leafBoxHorizontal( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.X_AXIS));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JComponent leafBoxVertical( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.Y_AXIS));
		result.setAlignmentY( CENTER_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JComponent leafButtons( JComponent... btns) {
		JComponent result = leafBoxHorizontal( ICON_SIZE);
		for (int i = 0; i < btns.length; ++i) {
			result.add( btns[i]);
			result.add( Box.createHorizontalStrut( GAP));
		}
		result.add( Box.createHorizontalGlue());
		return result;
	}

	protected static JScrollPane leafScoll( JComponent view, int height) {
		JScrollPane result = new JScrollPane( view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.getViewport().setOpaque( false);
		result.setBorder( null);
		result.setPreferredSize( new Dimension( 100, height));
		return result;
	}

	protected static JPanel leafTest() {
		JPanel result = new JPanel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( BorderFactory.createLineBorder( nextColor()));
		return result;
	}

	private static Color nextColor() {
		sColValue += 0x3773;
		sColValue &= 0xFFFF;
		return new Color( sColValue | 0x3F0000);
	}

	public static Dimension sizeOf( Image img) {
		return new Dimension( ZOOM * img.getWidth( null), ZOOM * img.getHeight( null));
	}

	protected void createLeafs() {
		LeafPanel left = new LeafPanel( true);
		LeafPanel right = new LeafPanel( false);
		createLeft( left);
		createRight( right);
		add( left);
		add( right);
	}

	protected abstract void createLeft( JPanel leaf);

	protected abstract void createRight( JPanel leaf);

	public abstract T getBase();

	public abstract JToolBar getToolBar();
}
