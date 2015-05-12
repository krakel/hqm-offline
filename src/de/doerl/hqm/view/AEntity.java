package de.doerl.hqm.view;

import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import de.doerl.hqm.base.ABase;

abstract class AEntity<T extends ABase> extends JPanel {
	private static final long serialVersionUID = -3039298434411863516L;
	protected static final Font FONT_NORMAL = new Font( "SansSerif", Font.PLAIN, 14);
	protected static final Font FONT_TITLE = new Font( "SansSerif", Font.PLAIN, 20);
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

	public abstract T getBase();;

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
}
