package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.utils.ResourceManager;

abstract class AEntity<T extends ABase> extends JPanel implements IModelListener {
	private static final long serialVersionUID = -3039298434411863516L;
	static final int ZOOM = 2;
	static final int GAP = 8;
	static final int ICON_SIZE = 36;
	static final Dimension VIEW_SIZE = new Dimension( 2 * ZOOM * 170, ZOOM * 234);
	static final Font FONT_NORMAL = new Font( "SansSerif", Font.PLAIN, 14);
	static final Font FONT_TITLE = new Font( "SansSerif", Font.PLAIN, 18);
	static final Color SELECTED = new Color( 0xAAAAAA);
	static final Color UNSELECTED = new Color( 0x404040);
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

	static void drawBackground( Graphics2D g2, Component c, boolean left) {
		drawBackground( g2, c, ResourceManager.getImageUI( "hqm.book.back"), left);
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

	static void drawBackgroundHalf( Graphics2D g2, Component c, boolean left) {
		drawBackgroundHalf( g2, c, ResourceManager.getImageUI( "hqm.book.back"), left);
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

	static void drawImage( Graphics2D g2, Component c, Image img, double sx, double sy) {
		if (img != null) {
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			g2.drawImage( img, xform, null);
		}
	}

	static void drawImage( Graphics2D g2, Image img, double sx, double sy, double tx, double ty) {
		if (img != null) {
			AffineTransform xform = AffineTransform.getScaleInstance( sx, sy);
			xform.translate( tx, ty);
			g2.drawImage( img, xform, null);
		}
	}

	protected static JComponent leafBox( int axis) {
		JPanel result = new JPanel();
		result.setLayout( new BoxLayout( result, axis));
		result.setOpaque( false);
		if (axis == BoxLayout.X_AXIS) {
//			result.setBorder( BorderFactory.createLineBorder( Color.RED));
		}
		else {
			result.setAlignmentX( LEFT_ALIGNMENT);
//			result.setBorder( BorderFactory.createLineBorder( Color.BLUE));
		}
//		result.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		return result;
	}

	protected static JComponent leafBoxFloat( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new FlowLayout( FlowLayout.LEFT, 0, GAP / 2));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( BorderFactory.createLineBorder( Color.BLACK));
		return result;
	}

	protected static JComponent leafBoxHorizontal( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.X_AXIS));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( BorderFactory.createLineBorder( Color.GREEN));
		return result;
	}

	protected static JComponent leafBoxVertical( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.Y_AXIS));
		result.setAlignmentY( CENTER_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( BorderFactory.createLineBorder( Color.ORANGE));
		return result;
	}

	protected static JLabel leafButton( String text) {
		JLabel result = new JLabel( new CenterIcon( ResourceManager.getImageUI( "hqm.button"), text));
		result.setMinimumSize( new Dimension( 114, 36));
		result.setPreferredSize( new Dimension( 114, 36));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( BorderFactory.createLineBorder( Color.CYAN));
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
//		result.setBorder( BorderFactory.createLineBorder( Color.BLUE));
		return result;
	}

	protected static JLabel leafImage( int w, int h, BufferedImage img) {
		JLabel result = new JLabel( new CenterIcon( img));
		result.setPreferredSize( new Dimension( w, h));
		result.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( BorderFactory.createLineBorder( Color.RED));
		return result;
	}

	protected static JLabel leafLabel( Color color, String text) {
		JLabel result = new JLabel( text);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( BorderFactory.createLineBorder( Color.PINK));
		result.setFont( FONT_NORMAL);
		result.setForeground( color);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, FONT_NORMAL.getSize()));
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
		return result;
	}

	protected static JLabel leafRepImage( FSetting rs) {
		JLabel result = new JLabel( new ReputationIcon( rs));
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, ICON_SIZE));
		result.setMaximumSize( new Dimension( Short.MAX_VALUE, ICON_SIZE));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
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
		JLabel result = new JLabel( new CountIcon( count, ResourceManager.getImageUI( "hqm.icon.back"), null));
		result.setPreferredSize( new Dimension( ICON_SIZE, ICON_SIZE));
		result.setMaximumSize( new Dimension( ICON_SIZE, ICON_SIZE));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
//		result.setBorder( BorderFactory.createLineBorder( Color.BLUE));
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
//		result.setBorder( BorderFactory.createLineBorder( Color.DARK_GRAY));
		result.setFont( FONT_TITLE);
		result.setForeground( color);
		result.setText( text);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, FONT_TITLE.getSize() + 3));
		return result;
	}

	protected static JLabel leafTitle( String text) {
		return leafTitle( Color.BLACK, text);
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

	public abstract JToolBar getToolBar();

	public EditView getView() {
		return mView;
	}
}
