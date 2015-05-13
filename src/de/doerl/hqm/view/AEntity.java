package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.utils.ResourceManager;

abstract class AEntity<T extends ABase> extends JPanel {
	private static final long serialVersionUID = -3039298434411863516L;
	protected static final BufferedImage MAP = ResourceManager.getImage( "questmap.png");
	protected static final BufferedImage ICON_BACK = MAP.getSubimage( 18, 235, 18, 18);
	protected static final BufferedImage LARGE_BTN = MAP.getSubimage( 54, 235, 57, 18);
	protected static final Font FONT_NORMAL = new Font( "SansSerif", Font.PLAIN, 14);
	protected static final Font FONT_TITLE = new Font( "SansSerif", Font.PLAIN, 20);
	protected static final Color SELECTED = new Color( 0xAAAAAA);
	protected static final Color UNSELECTED = new Color( 0x404040);
	protected static final int GAP = 10;
	protected static final int ICON_SIZE = 32;
	protected EditView mView;

	AEntity( EditView view) {
		mView = view;
		setOpaque( true);
	}

	AEntity( EditView view, LayoutManager layout) {
		super( layout, true);
		mView = view;
	}

	public static void addKeyAction( JComponent comp, String key, Action action) {
		Object o = new Object();
		comp.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke( key), o);
		comp.getActionMap().put( o, action);
	}

	protected static JPanel leafBox( int axis) {
		JPanel result = new JPanel();
		result.setLayout( new BoxLayout( result, axis));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JLabel leafButton( String text) {
		JLabel result = new JLabel( new CenterIcon( LARGE_BTN, text, 100, 32));
		result.setPreferredSize( new Dimension( 100, 32));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JLabel leafImage( BufferedImage[] arr) {
		JLabel result = new JLabel( new MultiIcon( arr));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JLabel leafLabel( Color color, String text) {
		JLabel result = new JLabel( text);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setFont( AEntity.FONT_NORMAL);
		result.setForeground( color);
		result.setBorder( null);
		return result;
	}

	protected static <E> JList<E> leafList( ListModel<E> model) {
		JList<E> result = new JList<E>( model);
		result.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JPanel leafPanel( boolean left) {
		LeafPanel result = new LeafPanel();
		result.setOpaque( false);
		if (left) {
			result.setBorder( BorderFactory.createEmptyBorder( 40, 40, 40, 10));
		}
		else {
			result.setBorder( BorderFactory.createEmptyBorder( 40, 10, 40, 40));
		}
		return result;
	}

	protected static JScrollPane leafScoll( JComponent view, int height) {
		JScrollPane result = new JScrollPane( view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.getViewport().setOpaque( false);
		result.setBorder( null);
		result.setPreferredSize( new Dimension( Integer.MAX_VALUE, height));
		result.setMaximumSize( new Dimension( Integer.MAX_VALUE, height));
		return result;
	}

	protected static JTextArea leafTextArea() {
		JTextArea result = new JTextArea();
		result.setLineWrap( true);
		result.setWrapStyleWord( true);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setFont( AEntity.FONT_NORMAL);
		result.setBorder( null);
//		result.setPreferredSize( new Dimension( 200, 80));
//		result.setMaximumSize( new Dimension( Integer.MAX_VALUE, 50));
		return result;
	}

	protected static JLabel leafTitle( Color color, String text) {
		JLabel result = new JLabel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setFont( AEntity.FONT_TITLE);
		result.setForeground( color);
		result.setBorder( null);
		result.setText( text);
		return result;
	}

	protected static JLabel leafTitle( String text) {
		JLabel result = new JLabel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setFont( AEntity.FONT_TITLE);
		result.setForeground( Color.BLACK);
		result.setBorder( null);
		result.setText( text);
		return result;
	}

	public abstract T getBase();

	@Override
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		EditView.drawBackground( g2, this);
	}

	public void remove() {
//		mView.removeMouse( this);
		mView.remove( this);
		setVisible( false);
	}

	public void update() {
//		mView.addMouse( this);
		setVisible( true);
		mView.add( this);
	};

	static class CenterIcon implements Icon {
		private BufferedImage mImage;
		private String mText;
		private int mWidth, mHeight;

		public CenterIcon( BufferedImage img, String text, int w, int h) {
			mImage = img;
			mText = text;
			mWidth = w;
			mHeight = h;
		}

		public int getIconHeight() {
			return mWidth;
		}

		public int getIconWidth() {
			return mHeight;
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			EditView.drawImage( g2, c, mImage);
			if (mText != null) {
				g2.setFont( FONT_NORMAL);
				g2.setColor( Color.BLACK);
				EditView.drawCenteredString( g2, c, mText);
			}
		}
	}

	static class CountIcon implements Icon {
		private BufferedImage mImage;
		private String mText;

		public CountIcon( BufferedImage img, String text) {
			mImage = img;
			mText = text;
		}

		public int getIconHeight() {
			return ICON_SIZE;
		}

		public int getIconWidth() {
			return ICON_SIZE;
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			EditView.drawImage( g2, c, mImage);
			if (mText != null) {
				g2.setFont( FONT_TITLE);
				g2.setColor( Color.BLACK);
				EditView.drawBottomLeftString( g2, c, mText);
			}
		}
	}

	private static class LeafPanel extends JPanel {
		private static final long serialVersionUID = -3474402067796441059L;

		public LeafPanel() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		}
	}

	static class MultiIcon implements Icon {
		private BufferedImage[] mArr;

		public MultiIcon( BufferedImage[] arr) {
			mArr = arr;
		}

		public int getIconHeight() {
			return ICON_SIZE;
		}

		public int getIconWidth() {
			return ICON_SIZE;
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			for (BufferedImage img : mArr) {
				EditView.drawImage( g2, c, img);
			}
		}
	}
}
