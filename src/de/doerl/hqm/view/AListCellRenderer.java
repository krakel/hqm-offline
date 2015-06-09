package de.doerl.hqm.view;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

abstract class AListCellRenderer<E> extends JPanel implements ListCellRenderer<E> {
	private static final long serialVersionUID = -658604546833347897L;

	public AListCellRenderer() {
	}

	public AListCellRenderer( LayoutManager layout) {
		super( layout);
	}

	@Override
	public void firePropertyChange( String propertyName, boolean oldValue, boolean newValue) {
	}

	@Override
	public void firePropertyChange( String propertyName, byte oldValue, byte newValue) {
	}

	@Override
	public void firePropertyChange( String propertyName, char oldValue, char newValue) {
	}

	@Override
	public void firePropertyChange( String propertyName, double oldValue, double newValue) {
	}

	@Override
	public void firePropertyChange( String propertyName, float oldValue, float newValue) {
	}

	@Override
	public void firePropertyChange( String propertyName, int oldValue, int newValue) {
	}

	@Override
	public void firePropertyChange( String propertyName, long oldValue, long newValue) {
	}

	@Override
	public void firePropertyChange( String propertyName, short oldValue, short newValue) {
	}
}
