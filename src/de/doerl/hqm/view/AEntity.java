package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
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
	protected static final int FONT_NORMAL_HIGH = 14;
	protected static final int FONT_TITLE_HIGH = 18;
	protected static final Font FONT_NORMAL = new Font( "SansSerif", Font.PLAIN, FONT_NORMAL_HIGH);
	protected static final Font FONT_TITLE = new Font( "SansSerif", Font.PLAIN, FONT_TITLE_HIGH);
	protected static final Color SELECTED = new Color( 0xAAAAAA);
	protected static final Color UNSELECTED = new Color( 0x404040);
	protected static final int GAP = 8;
	protected static final int ICON_SIZE = 36;
	protected static final int REP_WIDTH = 250;
	protected static final int REP_HEIGHT = 6;
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

	protected static JComponent leafBox( int axis) {
		JPanel result = new JPanel();
		result.setLayout( new BoxLayout( result, axis));
		if (axis == BoxLayout.X_AXIS) {
			result.setBackground( Color.RED);
//			result.setPreferredSize( new Dimension( Short.MAX_VALUE, 36));
		}
		else {
			result.setBackground( Color.BLUE);
			result.setAlignmentX( LEFT_ALIGNMENT);
		}
//		result.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
//		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JComponent leafBoxHorizontal( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.X_AXIS));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setBackground( Color.GREEN);
		result.setOpaque( true);
		result.setBorder( null);
		return result;
	}

	protected static JComponent leafBoxVertical( int heigh) {
		JComponent result = (JComponent) Box.createVerticalStrut( heigh);
		result.setLayout( new BoxLayout( result, BoxLayout.Y_AXIS));
		result.setAlignmentY( CENTER_ALIGNMENT);
		result.setBackground( Color.ORANGE);
		result.setOpaque( true);
		result.setBorder( null);
		return result;
	}

	protected static JLabel leafButton( String text) {
		JLabel result = new JLabel( new CenterIcon( LARGE_BTN, text, 114, 36));
		result.setMinimumSize( new Dimension( 114, 36));
		result.setPreferredSize( new Dimension( 114, 36));
		result.setAlignmentX( LEFT_ALIGNMENT);
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

	protected static JLabel leafImage( BufferedImage... arr) {
		JLabel result = new JLabel( new MultiIcon( arr));
		result.setAlignmentX( LEFT_ALIGNMENT);
//		result.setBackground( Color.BLUE);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JLabel leafLabel( Color color, String text) {
		JLabel result = new JLabel( text);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setBackground( Color.DARK_GRAY);
		result.setOpaque( true);
		result.setFont( AEntity.FONT_NORMAL);
		result.setForeground( color);
		result.setBorder( null);
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

	protected static JLabel leafRepImage( FSetting rs) {
		JLabel result = new JLabel( new ReputationIcon( rs));
		result.setAlignmentX( LEFT_ALIGNMENT);
//		result.setBackground( Color.BLUE);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JScrollPane leafScoll( JComponent view, int height) {
		JScrollPane result = new JScrollPane( view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.getViewport().setOpaque( false);
		result.setBorder( null);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, height));
//		result.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		return result;
	}

	protected static JLabel leafStack( AStack stk) {
		int amount = stk != null ? stk.getAmount() : 0;
		BufferedImage[] arr = new BufferedImage[] {
			ICON_BACK
		};
		JLabel result = new JLabel( new CountIcon( arr, amount > 1 ? Integer.toString( amount) : null));
		result.setMaximumSize( new Dimension( ICON_SIZE, ICON_SIZE));
		result.setAlignmentX( LEFT_ALIGNMENT);
//		result.setBackground( Color.BLUE);
		result.setOpaque( false);
		result.setBorder( null);
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
		return result;
	}

	protected static JLabel leafTitle( Color color, String text) {
		JLabel result = new JLabel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setBackground( Color.YELLOW);
		result.setOpaque( true);
		result.setFont( AEntity.FONT_TITLE);
		result.setForeground( color);
		result.setBorder( null);
		result.setText( text);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, FONT_TITLE_HIGH + 3));
		return result;
	}

	protected static JLabel leafTitle( String text) {
		return leafTitle( Color.BLACK, text);
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
		private BufferedImage[] mArr;
		private String mText;

		public CountIcon( BufferedImage[] arr, String text) {
			mArr = arr;
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
			for (BufferedImage img : mArr) {
				if (img != null) {
					EditView.drawImage( g2, c, img);
				}
			}
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
				if (img != null) {
					EditView.drawImage( g2, c, img);
				}
			}
		}
	}

	static class ReputationIcon implements Icon {
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
			return ICON_SIZE;
		}

		public int getIconWidth() {
			return REP_WIDTH;
		}

		public void paintIcon( Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			EditView.drawImage( g2, c, REPUATION, 0, 20);
			Vector<FMarker> marker = mSetting.mRep.mMarker;
			for (int i = 0; i < marker.size(); ++i) {
//				FMarker mark = marker.get( i);
				int pos = i * REP_WIDTH / marker.size();
				EditView.drawImage( g2, c, REP_MARKER, pos + 5, 23);
			}
		}
	}
}
