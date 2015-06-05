package de.doerl.hqm.ui;

import javax.swing.JPanel;

abstract class AConfigControl extends JPanel {
	private static final long serialVersionUID = 8270930528093156623L;

	public abstract void applyChange();

	public abstract void initControl();
}
