package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;

class Leaf extends JPanel {
	private static final long serialVersionUID = -6885713614243919455L;

	public Leaf() {
		setOpaque( false);
	}

	public Leaf( Color color) {
		setOpaque( false);
		setBackground( Color.LIGHT_GRAY);
	}

	public Leaf( LayoutManager layout) {
		super( layout);
		setOpaque( false);
	}
}
