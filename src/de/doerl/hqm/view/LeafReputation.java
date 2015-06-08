package de.doerl.hqm.view;

import java.awt.Dimension;

import javax.swing.JLabel;

import de.doerl.hqm.base.FSetting;

class LeafReputation extends JLabel {
	private static final long serialVersionUID = -7438409499635939565L;

	public LeafReputation( FSetting rs) {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setIcon( new ReputationIcon( rs.mRep));
		Dimension size = new Dimension( Short.MAX_VALUE, AEntity.ICON_SIZE);
		setPreferredSize( size);
		setMaximumSize( size.getSize());
	}
}
