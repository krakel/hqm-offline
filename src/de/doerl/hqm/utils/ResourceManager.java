package de.doerl.hqm.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

@SuppressWarnings( "nls")
public class ResourceManager {
	public static final String DIALOG_RES = "de.doerl.hqm.resources.dialog";
	public static final String ICON_DIR = "/de/doerl/hqm/pictures/";
	private static final Logger LOGGER = Logger.getLogger( ResourceManager.class.getName());
	public static final ResourceManager RESOURCE = new ResourceManager( DIALOG_RES);
	private static Color sDisabledColor = Color.gray;
	private static Color sEnabledColor = Color.black;
	private ResourceBundle mBundle;

	public ResourceManager( String bundle) {
		mBundle = ResourceBundle.getBundle( bundle, Locale.getDefault(), ResourceManager.class.getClassLoader());
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

	public static Icon getIcon( String name) {
		try {
			URL url = ResourceManager.class.getResource( ICON_DIR + name);
			if (url != null) {
				return new ImageIcon( url);
			}
		}
		catch (RuntimeException ex) {
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
		}
		return null;
	}

	public static Color getSimulationEnableColor( boolean enable) {
		return enable ? sEnabledColor : sDisabledColor;
	}

	public static String getVersion( String bundle) {
		StringBuffer sb = new StringBuffer();
		sb.append( getBundleString( "build.version", bundle));
		sb.append( '.');
		sb.append( getBundleString( "build.number", bundle));
		return sb.toString();
	}

	private static void gotoLookAndFeel( String name) {
		try {
			UIManager.setLookAndFeel( name);
		}
		catch (Exception ex) {
		}
		sDisabledColor = UIManager.getColor( "Button.background");
		if (sDisabledColor == null) {
			sDisabledColor = Color.black;
		}
		float[] hsb = Color.RGBtoHSB( sDisabledColor.getRed(), sDisabledColor.getGreen(), sDisabledColor.getBlue(), null);
		hsb[2] *= 1.1f;
		if (hsb[2] > 1.0f) {
			hsb[2] = 1.0f;
		}
		sEnabledColor = Color.getHSBColor( hsb[0], hsb[1], hsb[2]);
		setTreeIcons();
	}

	public static void makeIcon( String key, String name) {
		if (!GraphicsEnvironment.isHeadless()) {
			UIManager.put( key, new LazyObject( name));
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

	public String[] getArray( String key) {
		String[] result = null;
		try {
			result = parseString( mBundle.getString( key));
		}
		catch (RuntimeException ex) {
			result = new String[] {
				"<" + key + ">"
			};
		}
		return result;
	}

	public int getInteger( String key) {
		return getInteger( key, 0);
	}

	public int getInteger( String key, int def) {
		String value = getString( key);
		try {
			return Integer.parseInt( value);
		}
		catch (NumberFormatException ex) {
			return def;
		}
	}

	public String getString( String key) {
		try {
			return mBundle.getString( key);
		}
		catch (RuntimeException ex) {
			return "<" + key + ">";
		}
	}

	public String getString( String key, String def) {
		try {
			return mBundle.getString( key);
		}
		catch (RuntimeException ex) {
			return def;
		}
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

	private static class LazyObject implements UIDefaults.LazyValue {
		private String mName;

		public LazyObject( String name) {
			mName = name;
		}

		public Object createValue( UIDefaults table) {
			byte[] buffer = null;
			BufferedInputStream in = null;
			InputStream res = null;
			try {
				res = ResourceManager.class.getResourceAsStream( ICON_DIR + mName);
				if (res != null) {
					in = new BufferedInputStream( res);
					ByteArrayOutputStream out = new ByteArrayOutputStream( 1024);
					byte[] temp = new byte[1024];
					int n;
					while ((n = in.read( temp)) > 0) {
						out.write( temp, 0, n);
					}
					out.flush();
					out.close();
					buffer = out.toByteArray();
				}
			}
			catch (IOException ex) {
				Utils.log( LOGGER, Level.WARNING, "io.read", ex);
			}
			finally {
				if (in != null) {
					try {
						in.close();
						in = null;
					}
					catch (IOException ex) {
						Utils.log( LOGGER, Level.WARNING, "io.close", ex);
					}
				}
				if (res != null) {
					try {
						res.close();
						res = null;
					}
					catch (IOException ex) {
						Utils.log( LOGGER, Level.WARNING, "io.close", ex);
					}
				}
			}
			if (buffer == null) {
				Utils.log( LOGGER, Level.WARNING, "{0}/{1} not found.", ResourceManager.class.getName(), mName);
				return null;
			}
			if (buffer.length == 0) {
				Utils.log( LOGGER, Level.WARNING, "{1} is empty.", mName);
				return null;
			}
			return new ImageIcon( buffer);
		}
	}
}
