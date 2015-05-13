package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

abstract class AEntity<T extends ABase> extends JPanel {
	private static final long serialVersionUID = -3039298434411863516L;
	protected static final Font FONT_NORMAL = new Font( "SansSerif", Font.PLAIN, 14);
	protected static final Font FONT_TITLE = new Font( "SansSerif", Font.PLAIN, 20);
	protected static final Color SELECTED = new Color( 0xAAAAAA);
	protected static final Color UNSELECTED = new Color( 0x404040);
	protected static final int GAP = 10;
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

	protected static JPanel createBox( int axis) {
		JPanel result = new JPanel();
		result.setLayout( new BoxLayout( result, axis));
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JLabel createLabel( Color color, String text) {
		JLabel result = new JLabel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setFont( AEntity.FONT_NORMAL);
		result.setForeground( color);
		result.setBorder( null);
		result.setText( text);
		return result;
	}

	protected static JPanel createLeaf( boolean left) {
		ListLeaf result = new ListLeaf();
		result.setOpaque( false);
		if (left) {
			result.setBorder( BorderFactory.createEmptyBorder( 40, 40, 40, 10));
		}
		else {
			result.setBorder( BorderFactory.createEmptyBorder( 40, 10, 40, 40));
		}
		return result;
	}

	protected static <E> JList<E> createList( ListModel<E> model) {
		JList<E> result = new JList<E>( model);
		result.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setBorder( null);
		return result;
	}

	protected static JScrollPane createScoll( JComponent view, int height) {
		JScrollPane result = new JScrollPane( view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.getViewport().setOpaque( false);
		result.setBorder( null);
		result.setPreferredSize( new Dimension( Integer.MAX_VALUE, height));
		result.setMaximumSize( new Dimension( Integer.MAX_VALUE, height));
		return result;
	}

	protected static JTextArea createText() {
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

	protected static JLabel createTitle( Color color, String text) {
		JLabel result = new JLabel();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setOpaque( false);
		result.setFont( AEntity.FONT_TITLE);
		result.setForeground( color);
		result.setBorder( null);
		result.setText( text);
		return result;
	}

	protected static JLabel createTitle( String text) {
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
	};

	public void update() {
//		mView.addMouse( this);
		setVisible( true);
		mView.add( this);
	}

	private static class ListLeaf extends JPanel {
		private static final long serialVersionUID = -3474402067796441059L;

		public ListLeaf() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		}
	}
}
