package de.doerl.hqm.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class ImageManager {
	private static final Logger LOGGER = Logger.getLogger( ImageManager.class.getName());

	private ImageManager() {
	}

	static BufferedImage centerImage( String text, int size) {
		BufferedImage image = new BufferedImage( size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor( Color.LIGHT_GRAY);
		g2.setFont( new Font( Font.DIALOG, Font.BOLD, 12));
		FontMetrics fm = g2.getFontMetrics();
		int x = (size - fm.stringWidth( text)) / 2;
		int y = (size + fm.getAscent() - fm.getDescent()) / 2;
		g2.drawString( text, x, y);
		g2.dispose();
		return image;
	}

	private static BufferedImage colored( BufferedImage src, float r, float g, float b) {
		float[] scales = {
			r, g, b, 1F
		};
		RescaleOp op = new RescaleOp( scales, new float[4], null);
		BufferedImage cc = copy( src);
		return op.filter( cc, cc);
	}

	private static BufferedImage copy( BufferedImage src) {
		ColorModel cm = src.getColorModel();
		BufferedImage result = new BufferedImage( cm, src.getRaster().createCompatibleWritableRaster(), cm.isAlphaPremultiplied(), null);
		Graphics2D g = result.createGraphics();
		g.drawImage( src, null, null);
		g.dispose();
		return result;
	}

	static BufferedImage errorImage( String value) {
		Font font = new Font( Font.DIALOG, Font.BOLD, 12);
		FontRenderContext fc = new FontRenderContext( null, true, true);
		Rectangle2D bounds = font.getStringBounds( value, fc);
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();
		BufferedImage image = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor( Color.WHITE);
		g2.fillRect( 0, 0, w, h);
		g2.setColor( Color.RED);
		g2.setFont( font);
		g2.drawString( value, (int) bounds.getX(), (int) -bounds.getY());
		g2.dispose();
		return image;
	}

	private static BufferedImage getImageUI( String key) {
		try {
			Object obj = UIManager.get( key);
			if (obj != null) {
				return (BufferedImage) obj;
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return errorImage( key);
	}

	public static Icon loadIcon( String name) {
		return ResourceManager.getIcon( name);
	}

	public static BufferedImage loadImage() {
		return centerImage( "?", 18);
	}

	public static BufferedImage loadImage( String name) {
		return ResourceManager.getImage( name);
	}

	public static BufferedImage loadImage( String base, Float r, Float g, Float b) {
		try {
			BufferedImage img = getImageUI( base);
			if (img != null) {
				return colored( img, r, g, b);
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return null;
	}

	public static BufferedImage loadImage( String base, int x, int y, int w, int h) {
		try {
			BufferedImage img = getImageUI( base);
			if (img != null) {
				return img.getSubimage( x, y, w, h);
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return null;
	}

	static void makeIcon( String key, String name) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadIcon", new Object[] {
				name
			}));
		}
	}

	static void makeImage( String key) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {}));
		}
	}

	static void makeImage( String key, String name) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {
				name
			}));
		}
	}

	static void makeImage( String key, String base, Float r, Float g, Float b) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {
				base, r, g, b
			}));
		}
	}

	static void makeImage( String key, String base, int x, int y, int w, int h) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {
				base, x, y, w, h
			}));
		}
	}
}
