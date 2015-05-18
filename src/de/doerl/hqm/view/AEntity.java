package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

abstract class AEntity<T extends ABase> extends JPanel {
	private static final long serialVersionUID = -3039298434411863516L;
	protected static final BufferedImage MAP = ResourceManager.getImage( "questmap.png");
	protected static final BufferedImage ICON_BACK = MAP.getSubimage( 18, 235, 18, 18);
	protected static final BufferedImage LARGE_BTN = MAP.getSubimage( 54, 235, 57, 18);
	protected static final BufferedImage REPUATION = MAP.getSubimage( 0, 101, 125, 3);
	protected static final BufferedImage REP_MARKER = MAP.getSubimage( 10, 93, 5, 5);
	static final BufferedImage BACKGROUND = ResourceManager.getImage( "book.png").getSubimage( 0, 0, 170, 234);
	private static final Dimension VIEW_SIZE = new Dimension( 4 * BACKGROUND.getWidth(), 2 * BACKGROUND.getHeight());
//	private static final int LEAF_WIDTH = BACKGROUND.getWidth();
	protected static final int FONT_NORMAL_HIGH = 14;
	protected static final int FONT_TITLE_HIGH = 18;
	protected static final Font FONT_NORMAL = new Font( "SansSerif", Font.PLAIN, FONT_NORMAL_HIGH);
	protected static final Font FONT_TITLE = new Font( "SansSerif", Font.PLAIN, FONT_TITLE_HIGH);
	protected static final Color SELECTED = new Color( 0xAAAAAA);
	protected static final Color UNSELECTED = new Color( 0x404040);
	protected static final int GAP = 8;
	protected static final int ICON_SIZE = 36;
	private static int sColValue;
	protected EditView mView;

	AEntity( EditView view, LayoutManager layout) {
		super( layout, true);
		mView = view;
		setOpaque( true);
		setMinimumSize( new Dimension( VIEW_SIZE));
		setPreferredSize( new Dimension( VIEW_SIZE));
//		setMaximumSize( new Dimension( min));
	}

	protected static BufferedImage copy( BufferedImage src) {
		ColorModel cm = src.getColorModel();
		BufferedImage result = new BufferedImage( cm, src.getRaster().createCompatibleWritableRaster(), cm.isAlphaPremultiplied(), null);
		Graphics2D g = result.createGraphics();
		g.drawImage( src, null, null);
		g.dispose();
		return result;
	}

	static JButton createToolButton( Icon icon) {
		JButton result = new JButton( icon);
		result.setEnabled( false);
		result.setBorderPainted( false);
		return result;
	}

	static void drawBackground( Graphics2D g2, Component c, BufferedImage img, boolean left) {
		double sx = (double) c.getWidth() / img.getWidth();
		double sy = (double) c.getHeight() / img.getHeight();
		if (left) {
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			g2.drawImage( img, xform, null);
		}
		else {
			AffineTransform xform = AffineTransform.getScaleInstance( -sx, sy);
			xform.translate( -img.getWidth(), 0);
			g2.drawImage( img, xform, null);
		}
	}

	static void drawBackgroundHalf( Graphics2D g2, Component c, BufferedImage img, boolean left) {
		double sx = (double) c.getWidth() / img.getWidth() / 2;
		double sy = (double) c.getHeight() / img.getHeight();
		if (left) {
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			g2.drawImage( img, xform, null);
		}
		else {
			AffineTransform xform = AffineTransform.getScaleInstance( -sx, sy);
			xform.translate( -2 * img.getWidth(), 0);
			g2.drawImage( img, xform, null);
		}
	}

	private static void drawBottomLeftString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = c.getWidth() - fm.stringWidth( text);
		int y = c.getHeight() - fm.getDescent();
		g2.drawString( text, x, y);
	}

	private static void drawCenteredString( Graphics2D g2, Component c, String text) {
		FontMetrics fm = g2.getFontMetrics();
		int x = (c.getWidth() - fm.stringWidth( text)) / 2;
		int y = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
		g2.drawString( text, x, y);
	}

	private static void drawImage( Graphics2D g2, BufferedImage img, double sx, double sy, double tx, double ty) {
		if (img != null) {
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			xform.translate( tx, ty);
			g2.drawImage( img, xform, null);
		}
	}

	private static void drawImage( Graphics2D g2, Component c, BufferedImage img) {
		if (img != null) {
			double sx = (double) c.getWidth() / img.getWidth();
			double sy = (double) c.getHeight() / img.getHeight();
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			g2.drawImage( img, xform, null);
		}
	}

	@SuppressWarnings( "unused")
	private static void drawImage( Graphics2D g2, Component c, BufferedImage img, double sx, double sy) {
		if (img != null) {
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			g2.drawImage( img, xform, null);
		}
	}

	protected static JComponent leafBox( int axis) {
		JPanel result = new JPanel();
		result.setLayout( new BoxLayout( result, axis));
		result.setOpaque( false);
		if (axis == BoxLayout.X_AXIS) {
//			result.setBorder( null);
			result.setBorder( BorderFactory.createLineBorder( Color.RED));
//			result.setPreferredSize( new Dimension( Short.MAX_VALUE, 36));
		}
		else {
			result.setAlignmentX( LEFT_ALIGNMENT);
//			result.setBorder( null);
			result.setBorder( BorderFactory.createLineBorder( Color.BLUE));
		}
//		result.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		return result;
	}

	protected static JComponent leafBoxFloat( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new FlowLayout( FlowLayout.LEFT, 0, GAP / 2));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.BLACK));
		return result;
	}

	protected static JComponent leafBoxHorizontal( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.X_AXIS));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.GREEN));
		return result;
	}

	protected static JComponent leafBoxVertical( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.Y_AXIS));
		result.setAlignmentY( CENTER_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.ORANGE));
		return result;
	}

	protected static JLabel leafButton( String text) {
		JLabel result = new JLabel( new CenterIcon( LARGE_BTN, text));
		result.setMinimumSize( new Dimension( 114, 36));
		result.setPreferredSize( new Dimension( 114, 36));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.CYAN));
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

	protected static JLabel leafIcon( BufferedImage... arr) {
		JLabel result = new JLabel( new MultiIcon( ICON_SIZE, ICON_SIZE, arr));
		result.setPreferredSize( new Dimension( ICON_SIZE, ICON_SIZE));
		result.setMaximumSize( new Dimension( ICON_SIZE, ICON_SIZE));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.BLUE));
		return result;
	}

	protected static JLabel leafImage( int w, int h, BufferedImage img) {
		JLabel result = new JLabel( new CenterIcon( img));
		result.setPreferredSize( new Dimension( w, h));
		result.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.RED));
		return result;
	}

	protected static JLabel leafImage( int x, int y, int w, int h, BufferedImage... arr) {
		JLabel result = new JLabel( new MultiIcon( w, h, arr));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		result.setBounds( x, y, w, h);
		return result;
	}

	protected static JLabel leafLabel( Color color, String text) {
		JLabel result = new JLabel( text);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.PINK));
		result.setFont( FONT_NORMAL);
		result.setForeground( color);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, FONT_NORMAL_HIGH));
		return result;
	}

	protected static JLabel leafLabel( String text) {
		return leafLabel( Color.BLACK, text);
	}

	protected static <E> JList<E> leafList( ListModel<E> model) {
		JList<E> result = new JList<E>( model);
		result.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JLabel leafRepImage( FSetting rs) {
		JLabel result = new JLabel( new ReputationIcon( rs));
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, ICON_SIZE));
		result.setMaximumSize( new Dimension( Short.MAX_VALUE, ICON_SIZE));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
//		result.setBorder( BorderFactory.createLineBorder( Color.BLUE));
		return result;
	}

	protected static JScrollPane leafScoll( JComponent view, int height) {
		JScrollPane result = new JScrollPane( view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, height));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.getViewport().setOpaque( false);
		result.setBorder( null);
//		result.setBorder( BorderFactory.createLineBorder( Color.CYAN));
		return result;
	}

	protected static JLabel leafStack( AStack stk) {
		int amount = stk != null ? stk.getAmount() : 0;
		String count = amount > 1 ? Integer.toString( amount) : null;
		JLabel result = new JLabel( new CountIcon( count, ICON_BACK, null));
		result.setPreferredSize( new Dimension( ICON_SIZE, ICON_SIZE));
		result.setMaximumSize( new Dimension( ICON_SIZE, ICON_SIZE));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.BLUE));
		return result;
	}

	protected static JPanel leafTest() {
		JPanel result = new JPanel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( BorderFactory.createLineBorder( nextColor()));
		return result;
	}

	protected static JLabel leafTitle( Color color, String text) {
		JLabel result = new JLabel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( null);
		result.setBorder( BorderFactory.createLineBorder( Color.DARK_GRAY));
		result.setFont( FONT_TITLE);
		result.setForeground( color);
		result.setText( text);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, FONT_TITLE_HIGH + 3));
		return result;
	}

	protected static JLabel leafTitle( String text) {
		return leafTitle( Color.BLACK, text);
	}

	protected static JComponent leafToolBar( boolean left) {
		Box hori = Box.createHorizontalBox();
		hori.setAlignmentX( LEFT_ALIGNMENT);
		hori.setOpaque( false);
//		hori.setBorder( null);
		hori.setBorder( BorderFactory.createLineBorder( Color.BLACK));
		hori.setPreferredSize( new Dimension( Short.MAX_VALUE, 20));
		hori.setMaximumSize( new Dimension( Short.MAX_VALUE, 20));
		JToolBar bar = new JToolBar();
//		bar.setPreferredSize( new Dimension( Short.MAX_VALUE, bar.getPreferredSize().height));
		bar.setFloatable( false);
		try {
			bar.setRollover( true);
		}
		catch (NoSuchMethodError ex) {
		}
		bar.setOpaque( false);
//		bar.addSeparator();
		if (left) {
			hori.add( bar);
			hori.add( Box.createHorizontalGlue());
		}
		else {
			hori.add( Box.createHorizontalGlue());
			hori.add( bar);
		}
		return hori;
	}

	private static Color nextColor() {
		sColValue += 0x3773;
		sColValue &= 0xFFFF;
		return new Color( sColValue | 0x3F0000);
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

	public EditView getView() {
		return mView;
	}

	static class CenterIcon implements Icon {
		private BufferedImage mImage;
		private String mText;

		public CenterIcon( BufferedImage img) {
			mImage = img;
		}

		public CenterIcon( BufferedImage img, String text) {
			mImage = img;
			mText = text;
		}

		public int getIconHeight() {
			return mImage.getWidth();
		}

		public int getIconWidth() {
			return mImage.getHeight();
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			drawImage( g2, c, mImage);
			if (mText != null) {
				g2.setFont( FONT_NORMAL);
				g2.setColor( Color.BLACK);
				drawCenteredString( g2, c, mText);
			}
		}
	}

	static class CountIcon implements Icon {
		private BufferedImage mBack;
		private BufferedImage mStack;
		private String mText;

		public CountIcon( String text, BufferedImage back, BufferedImage stk) {
			mText = text;
			mBack = back;
			mStack = stk;
		}

		public int getIconHeight() {
			return mBack.getHeight();
		}

		public int getIconWidth() {
			return mBack.getWidth();
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			drawImage( g2, c, mBack);
			if (mStack != null) {
				drawImage( g2, c, mStack);
			}
			if (mText != null) {
				g2.setFont( FONT_TITLE);
				g2.setColor( Color.BLACK);
				drawBottomLeftString( g2, c, mText);
			}
		}
	}

	static class MultiIcon implements Icon {
		private int mWidth, mHeight;
		private BufferedImage[] mArr;

		public MultiIcon( int w, int h, BufferedImage[] arr) {
			mWidth = w;
			mHeight = h;
			mArr = arr;
		}

		public int getIconHeight() {
			return mHeight;
		}

		public int getIconWidth() {
			return mWidth;
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			for (BufferedImage img : mArr) {
				if (img != null) {
					drawImage( g2, c, img);
				}
			}
		}
	}

	static class ReputationIcon implements Icon {
		private static double ZOOM = 2.0;
		private FSetting mSetting;

		public ReputationIcon( FSetting rs) {
			mSetting = rs;
			FMarker lower = rs.mLower;
			Vector<FMarker> marker = rs.mRep.mMarker;
			int size = marker.size();
			FMarker first = marker.get( 0);
			FMarker last = marker.get( size - 1);
			int lowerValue;
			boolean lowerOnMarker;
			if (lower == null) {
				lowerValue = Math.min( first.mMark.mValue, 0);
				lowerOnMarker = false;
			}
			else {
				lowerValue = lower.mMark.mValue;
				lowerOnMarker = Utils.equals( lower, first) && lower.mMark.mValue > 0;
				if (Utils.equals( lower.getName(), rs.mRep.mNeutral.mValue) && last.mMark.mValue < 0) {
					lowerValue = last.mMark.mValue;
					lowerOnMarker = true;
//					lowerMovedInner = true;
//					lowerMoved = true;
				}
				else if (Utils.equals( lower, last)) {
					lowerOnMarker = true;
				}
				else if (lowerValue <= 0) {
					for (int i = 0; i < size; ++i) {
						if (marker.get( i).mMark.mValue >= lowerValue) {
							if (i > 0) {
								lowerValue = marker.get( i - 1).mMark.mValue;
								if (i - 1 != 0) {
//									lowerMovedInner = true;
								}
//								lowerMoved = true;
							}
							break;
						}
					}
				}
			}
//			FReputationMarker upper = rs.mUpper;
//			FReputation rep = rs.mRep;
		}

		public int getIconHeight() {
			return REPUATION.getHeight();
		}

		public int getIconWidth() {
			return REPUATION.getWidth();
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			drawImage( g2, c, REPUATION); //, scale, scale, 0, 10);
			Vector<FMarker> marker = mSetting.mRep.mMarker;
			for (int i = 0; i < marker.size(); ++i) {
//				FMarker mark = marker.get( i);
				int pos = i * REPUATION.getWidth() / marker.size();
				drawImage( g2, REP_MARKER, ZOOM, ZOOM, pos + 2, 12);
			}
		}
	}
}
