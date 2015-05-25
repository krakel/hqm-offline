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

import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class ImageManager {
	private static final Logger LOGGER = Logger.getLogger( ImageManager.class.getName());

	private ImageManager() {
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
		Font font = new Font( "Dialog", Font.BOLD, 12);
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

	public static BufferedImage loadImage() {
		String text = "?";
		int size = 18;
		BufferedImage image = new BufferedImage( size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor( Color.LIGHT_GRAY);
		g2.setFont( new Font( "Dialog", Font.BOLD, size));
		FontMetrics fm = g2.getFontMetrics();
		int x = (size - fm.stringWidth( text)) / 2;
		int y = (size + fm.getAscent() - fm.getDescent()) / 2;
		g2.drawString( text, x, y);
		g2.dispose();
		return image;
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

	private static void makeImage( String key) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {}));
		}
	}

	private static void makeImage( String key, String name) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {
				name
			}));
		}
	}

	private static void makeImage( String key, String base, Float r, Float g, Float b) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {
				base, r, g, b
			}));
		}
	}

	private static void makeImage( String key, String base, int x, int y, int w, int h) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ImageManager.class.getName(), "loadImage", new Object[] {
				base, x, y, w, h
			}));
		}
	}

	public static void setImages() {
		makeImage( "hqm.unknown");
		makeImage( "hqm.map", "questmap.png");
		makeImage( "hqm.book", "book.png");
		makeImage( "hqm.front", "front.png");
		//
		makeImage( "hqm.icon.back", "hqm.map", 18, 235, 18, 18);
		makeImage( "hqm.button", "hqm.map", 54, 235, 57, 18);
		makeImage( "hqm.reputation", "hqm.map", 0, 101, 125, 3);
		makeImage( "hqm.marker", "hqm.map", 10, 93, 5, 5);
		//
		makeImage( "hqm.rep.base", "hqm.map", 30, 82, 16, 16);
		makeImage( "hqm.rep.good", "hqm.map", 78, 82, 16, 16);
		makeImage( "hqm.rep.bad", "hqm.map", 94, 82, 16, 16);
		makeImage( "hqm.rep.norm", "hqm.map", 110, 82, 16, 16);
		//
		makeImage( "hqm.quest.norm", "hqm.map", 170, 0, 25, 30);
		makeImage( "hqm.quest.big", "hqm.map", 195, 0, 31, 37);
		//
		makeImage( "hqm.dark.norm", "hqm.quest.norm", 0.6F, 0.6F, 0.6F);
		makeImage( "hqm.base.norm", "hqm.quest.norm", 0.6F, 1F, 0.6F);
		makeImage( "hqm.pref.norm", "hqm.quest.norm", 0.6F, 0.6F, 1F);
		makeImage( "hqm.post.norm", "hqm.quest.norm", 1F, 0.6F, 0.6F);
		//
		makeImage( "hqm.dark.big", "hqm.quest.big", 0.6F, 0.6F, 0.6F);
		makeImage( "hqm.base.big", "hqm.quest.big", 0.6F, 1F, 0.6F);
		makeImage( "hqm.pref.big", "hqm.quest.big", 0.6F, 0.6F, 1F);
		makeImage( "hqm.post.big", "hqm.quest.big", 1F, 0.6F, 0.6F);
		//
		makeImage( "hqm.book.back", "hqm.book", 0, 0, 170, 234);
		makeImage( "hqm.default", "hqm.front", 0, 0, 280, 360);
	}
}
