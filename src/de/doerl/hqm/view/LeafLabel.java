package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

import de.doerl.hqm.ui.ADialog;

class LeafLabel extends JLabel {
	private static final long serialVersionUID = 6627294162311351931L;

	public LeafLabel( Color color, String text) {
		this( color, text, false);
	}

	public LeafLabel( Color color, String text, boolean title) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setFont( title ? ADialog.FONT_TITLE : ADialog.FONT_NORMAL);
		setForeground( color);
		setPreferredSize( new Dimension( Short.MAX_VALUE, getFont().getSize()));
		setText( text);
	}

	public LeafLabel( String text) {
		this( Color.BLACK, text, false);
	}

	public LeafLabel( String text, boolean title) {
		this( Color.BLACK, text, title);
	}
}
