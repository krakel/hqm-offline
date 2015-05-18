package de.doerl.hqm.ui;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

public class GroupPanel extends JPanel {
	private static final long serialVersionUID = -8317918292275470993L;
	private GroupLayout mLayout = new GroupLayout( this);

	public GroupPanel() {
		this( false, false);
	}

	public GroupPanel( boolean gaps, boolean containerGaps) {
		setLayout( mLayout);
		mLayout.setAutoCreateGaps( gaps);
		mLayout.setAutoCreateContainerGaps( containerGaps);
	}

	@Override
	public GroupLayout getLayout() {
		return mLayout;
	}
}
