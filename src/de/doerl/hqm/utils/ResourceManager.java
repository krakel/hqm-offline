package de.doerl.hqm.utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class ResourceManager {
	private static final String DIALOG_RES = "de.doerl.hqm.resources.dialog";
	private static final String ICON_DIR = "/de/doerl/hqm/pictures/";
	private static final Logger LOGGER = Logger.getLogger( ResourceManager.class.getName());
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle( DIALOG_RES, Locale.getDefault(), ResourceManager.class.getClassLoader());

	private ResourceManager() {
	}

	public static String[] getArray( String key) {
		String[] result = null;
		try {
			result = parseString( BUNDLE.getString( key));
		}
		catch (RuntimeException ex) {
			result = new String[] { "<" + key + ">"
			};
		}
		return result;
	}

	public static String getBundleString( String key, String bundle) {
		try {
			ResourceBundle rb = ResourceBundle.getBundle( bundle, Locale.getDefault(), ResourceManager.class.getClassLoader());
			return rb.getString( key);
		}
		catch (RuntimeException ex) {
			return "<" + key + ">";
		}
	}

	public static int getH( boolean big) {
		return big ? 37 : 30;
	}

	public static int getH5( boolean big) {
		return big ? 18 : 15;
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

	static BufferedImage getImage( String name) {
		try {
			URL url = ResourceManager.class.getResource( ICON_DIR + name);
			if (url != null) {
				return ImageIO.read( url);
			}
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return null;
	}

	public static javafx.scene.image.Image getImageFx( String name) {
		try {
			URL url = ResourceManager.class.getResource( ICON_DIR + name);
			if (url != null) {
				return new javafx.scene.image.Image( url.toString());
			}
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return null;
	}

	public static Image getImageUI( String key) {
		try {
			Object obj = UIManager.get( key);
			if (obj != null) {
				return (Image) obj;
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return ImageManager.errorImage( key);
	}

	public static int getInteger( String key) {
		return Utils.parseInteger( getString( key), 0);
	}

	public static int getInteger( String key, int def) {
		return Utils.parseInteger( getString( key), def);
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

	public static String getVersion( String bundle) {
		StringBuilder sb = new StringBuilder();
		sb.append( getBundleString( "build.version", bundle));
		sb.append( '.');
		sb.append( getBundleString( "build.number", bundle));
		return sb.toString();
	}

	public static int getW( boolean big) {
		return big ? 31 : 25;
	}

	public static int getW5( boolean big) {
		return big ? 15 : 12;
	}

	public static void init() {
		ImageManager.makeIcon( "Tree.expandedIcon", "collaps.gif");
		ImageManager.makeIcon( "Tree.collapsedIcon", "expand.gif");
		//
		ImageManager.makeImage( "hqm.unknown");
		ImageManager.makeImage( "hqm.map", "questmap.png");
		ImageManager.makeImage( "hqm.book", "book.png");
		ImageManager.makeImage( "hqm.front", "front.png");
		//
		ImageManager.makeImage( "hqm.icon.back", "hqm.map", 18, 235, 18, 18);
		ImageManager.makeImage( "hqm.button", "hqm.map", 54, 235, 57, 18);
		ImageManager.makeImage( "hqm.reputation", "hqm.map", 0, 101, 125, 3);
		ImageManager.makeImage( "hqm.marker", "hqm.map", 10, 93, 5, 5);
		ImageManager.makeImage( "hqm.neutral", "hqm.map", 20, 93, 5, 5);
		ImageManager.makeImage( "hqm.current", "hqm.map", 0, 93, 5, 5);
		//
		ImageManager.makeImage( "hqm.rep.base", "hqm.map", 30, 82, 16, 16);
		ImageManager.makeImage( "hqm.rep.good", "hqm.map", 78, 82, 16, 16);
		ImageManager.makeImage( "hqm.rep.bad", "hqm.map", 94, 82, 16, 16);
		ImageManager.makeImage( "hqm.rep.norm", "hqm.map", 110, 82, 16, 16);
		//
		ImageManager.makeImage( "hqm.quest.normA", "hqm.map", 170, 0, 25, 30);
		ImageManager.makeImage( "hqm.quest.normB", "hqm.map", 170, 30, 25, 30);
		ImageManager.makeImage( "hqm.quest.bigA", "hqm.map", 195, 0, 31, 37);
		ImageManager.makeImage( "hqm.quest.bigB", "hqm.map", 195, 37, 31, 37);
		//
		ImageManager.makeImage( "hqm.dark.normA", "hqm.quest.normA", 0.6F, 0.6F, 0.6F);
		ImageManager.makeImage( "hqm.base.normA", "hqm.quest.normA", 0.6F, 1F, 0.6F);
		ImageManager.makeImage( "hqm.link.normA", "hqm.quest.normA", 0.6F, 1F, 1F);
		ImageManager.makeImage( "hqm.pref.normA", "hqm.quest.normA", 0.6F, 0.6F, 1F);
		ImageManager.makeImage( "hqm.post.normA", "hqm.quest.normA", 1F, 0.6F, 0.6F);
		//
		ImageManager.makeImage( "hqm.dark.normB", "hqm.quest.normB", 0.7F, 0.7F, 0.7F);
		ImageManager.makeImage( "hqm.base.normB", "hqm.quest.normB", 0.7F, 1F, 0.7F);
		ImageManager.makeImage( "hqm.link.normB", "hqm.quest.normB", 0.7F, 1F, 1F);
		ImageManager.makeImage( "hqm.pref.normB", "hqm.quest.normB", 0.7F, 0.7F, 1F);
		ImageManager.makeImage( "hqm.post.normB", "hqm.quest.normB", 1F, 0.7F, 0.7F);
		//
		ImageManager.makeImage( "hqm.dark.bigA", "hqm.quest.bigA", 0.6F, 0.6F, 0.6F);
		ImageManager.makeImage( "hqm.base.bigA", "hqm.quest.bigA", 0.6F, 1F, 0.6F);
		ImageManager.makeImage( "hqm.link.bigA", "hqm.quest.bigA", 0.6F, 1F, 1F);
		ImageManager.makeImage( "hqm.pref.bigA", "hqm.quest.bigA", 0.6F, 0.6F, 1F);
		ImageManager.makeImage( "hqm.post.bigA", "hqm.quest.bigA", 1F, 0.6F, 0.6F);
		//
		ImageManager.makeImage( "hqm.dark.bigB", "hqm.quest.bigB", 0.7F, 0.7F, 0.7F);
		ImageManager.makeImage( "hqm.base.bigB", "hqm.quest.bigB", 0.7F, 1F, 0.7F);
		ImageManager.makeImage( "hqm.link.bigB", "hqm.quest.bigB", 0.7F, 1F, 1F);
		ImageManager.makeImage( "hqm.pref.bigB", "hqm.quest.bigB", 0.7F, 0.7F, 1F);
		ImageManager.makeImage( "hqm.post.bigB", "hqm.quest.bigB", 1F, 0.7F, 0.7F);
		//
		ImageManager.makeImage( "hqm.book.back", "hqm.book", 0, 0, 170, 234);
		ImageManager.makeImage( "hqm.default", "hqm.front", 0, 0, 280, 360);
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

	public static Image stringImage( String value) {
		return ImageManager.centerImage( value, 30);
	}

	private static class ErrorIcon implements Icon {
		private Rectangle2D mBounds;
		private Font mFont = new Font( Font.DIALOG, Font.BOLD, 12);
		private String mText;

		public ErrorIcon( String text) {
			mText = text;
			BufferedImage buffer = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_RGB);
			FontRenderContext fc = buffer.createGraphics().getFontRenderContext();
			mBounds = mFont.getStringBounds( mText, fc);
		}

		@Override
		public int getIconHeight() {
			return (int) mBounds.getHeight() + 4;
		}

		@Override
		public int getIconWidth() {
			return (int) mBounds.getWidth() + 4;
		}

		@Override
		public synchronized void paintIcon( Component c, Graphics g, int x, int y) {
			Font oldFont = g.getFont();
			g.setFont( mFont);
			g.translate( x + 2, y + 2);
			g.drawString( mText, 0, (int) mBounds.getHeight());
			g.setFont( oldFont);
		}
	}
}
