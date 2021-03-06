package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.doerl.hqm.base.AQuestTask;

abstract class ATaskBox extends JPanel {
	private static final long serialVersionUID = 3021007353052213384L;
	protected static final int WIDTH = 7 * AEntity.ICON_SIZE;
	protected static final int HEIGHT = 6 * AEntity.ICON_SIZE;

	protected ATaskBox() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
//		setMinimumSize( new Dimension( 0, HEIGHT));
//		setPreferredSize( new Dimension( 0, HEIGHT));
		setPreferredSize( new Dimension( WIDTH, HEIGHT));
//		setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		setAlignmentY( CENTER_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
	}

	public void addClickListener( ActionListener l) {
	}

	public abstract AQuestTask getTask();

	public abstract boolean onAction( Window owner);

	public void removeClickListener( ActionListener l) {
	}

	public abstract void update();
}
