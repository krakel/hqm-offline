package de.doerl.hqm.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class ResourceManager {
	private static final String DIALOG_RES = "de.doerl.hqm.resources.dialog";
	private static final String ICON_DIR = "/de/doerl/hqm/pictures/";
	private static final Logger LOGGER = Logger.getLogger( ResourceManager.class.getName());
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle( DIALOG_RES, Locale.getDefault(), ResourceManager.class.getClassLoader());

	private ResourceManager() {
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

	public static String[] getArray( String key) {
		String[] result = null;
		try {
			result = parseString( BUNDLE.getString( key));
		}
		catch (RuntimeException ex) {
			result = new String[] {
				"<" + key + ">"
			};
		}
		return result;
	}

	public static int getH( boolean big) {
		return big ? 37 : 30;
	}

	public static Icon getIcon( String name) {
		try {
			URL url = ResourceManager.class.getResource( ICON_DIR + name);
			if (url != null) {
				return new ImageIcon( url);
			}
		}
		catch (RuntimeException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return new ErrorIcon( name);
	}

	public static BufferedImage getImage( String name) {
		try {
			URL url = ResourceManager.class.getResource( ICON_DIR + name);
			if (url != null) {
				return ImageIO.read( url);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return null;
	}

	public static BufferedImage getImage( String base, Float r, Float g, Float b) {
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

	public static BufferedImage getImage( String base, int x, int y, int w, int h) {
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

	public static BufferedImage getImageUI( String key) {
		try {
			Object obj = UIManager.get( key);
			if (obj != null) {
				return (BufferedImage) obj;
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return getStringImage( key);
	}

	public static int getInteger( String key) {
		return getInteger( key, 0);
	}

	public static int getInteger( String key, int def) {
		String value = getString( key);
		try {
			return Integer.parseInt( value);
		}
		catch (NumberFormatException ex) {
			return def;
		}
	}

	public static String getString( String key) {
		try {
			return BUNDLE.getString( key);
		}
		catch (RuntimeException ex) {
			return "<" + key + ">";
		}
	}

	public static String getString( String key, String def) {
		try {
			return BUNDLE.getString( key);
		}
		catch (RuntimeException ex) {
			return def;
		}
	}

	private static BufferedImage getStringImage( String value) {
		Font font = new Font( "Dialog", Font.BOLD, 12);
		FontRenderContext frc = new FontRenderContext( null, true, true);
		Rectangle2D bounds = font.getStringBounds( value, frc);
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();
		BufferedImage image = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor( Color.WHITE);
		g.fillRect( 0, 0, w, h);
		g.setColor( Color.BLACK);
		g.setFont( font);
		g.drawString( value, (float) bounds.getX(), (float) -bounds.getY());
		g.dispose();
		return image;
	}

	public static int getW( boolean big) {
		return big ? 31 : 25;
	}

	private static void gotoLookAndFeel( String name) {
		try {
			UIManager.setLookAndFeel( name);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		setTreeIcons();
	}

	public static void makeIcon( String key, String name) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ResourceManager.class.getName(), "getIcon", new Object[] {
				name
			}));
		}
	}

	public static void makeImage( String key, String name) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ResourceManager.class.getName(), "getImage", new Object[] {
				name
			}));
		}
	}

	public static void makeImage( String key, String base, Float r, Float g, Float b) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ResourceManager.class.getName(), "getImage", new Object[] {
				base, r, g, b
			}));
		}
	}

	public static void makeImage( String key, String base, int x, int y, int w, int h) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new UIDefaults.ProxyLazyValue( ResourceManager.class.getName(), "getImage", new Object[] {
				base, x, y, w, h
			}));
		}
	}

	private static String[] parseString( String value) {
		String[] result = null;
		if (value.length() > 0) {
			char ch = value.charAt( 0);
			int count = 1;
			int pos = 1;
			while ((pos = value.indexOf( ch, pos) + 1) > 0) {
				++count;
			}
			result = new String[count];
			pos = 1;
			for (int i = 0; i < count; ++i) {
				int next = value.indexOf( ch, pos);
				if (next > 0) {
					result[i] = value.substring( pos, next);
				}
				else {
					result[i] = value.substring( pos);
				}
				pos = next + 1;
			}
		}
		else {
			result = new String[0];
		}
		return result;
	}

	public static void setEntityImages() {
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

	public static void setLookAndFeel( String laf) {
		if (!GraphicsEnvironment.isHeadless()) {
			LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
			for (int i = 0; i < infos.length; ++i) {
				if (Utils.equals( infos[i].getName(), laf)) {
					gotoLookAndFeel( infos[i].getClassName());
					return;
				}
			}
			gotoLookAndFeel( UIManager.getSystemLookAndFeelClassName());
		}
	}

	public static void setTreeIcons() {
		makeIcon( "Tree.expandedIcon", "collaps.gif");
		makeIcon( "Tree.collapsedIcon", "expand.gif");
	}

	private static class ErrorIcon implements Icon {
		private Rectangle2D mBounds;
		private Font mFont = new Font( "Dialog", Font.BOLD, 12);
		private String mText;

		public ErrorIcon( String text) {
			mText = text;
			BufferedImage buffer = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_RGB);
			FontRenderContext fc = buffer.createGraphics().getFontRenderContext();
			mBounds = mFont.getStringBounds( mText, fc);
		}

		public int getIconHeight() {
			return (int) mBounds.getHeight() + 4;
		}

		public int getIconWidth() {
			return (int) mBounds.getWidth() + 4;
		}

		public synchronized void paintIcon( Component c, Graphics g, int x, int y) {
			Font oldFont = g.getFont();
			g.setFont( mFont);
			g.translate( x + 2, y + 2);
			g.drawString( mText, 0, (int) mBounds.getHeight());
			g.setFont( oldFont);
		}
	}
}
