package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import de.doerl.hqm.utils.ResourceManager;

@SuppressWarnings( "nls")
public class ExitAction extends ABundleAction {
	private static final long serialVersionUID = 6920098751121756081L;
	private JFrame mFrame;

	public ExitAction( JFrame frame) {
		super( "hqm.exit", ResourceManager.RESOURCE);
		mFrame = frame;
	}

	public void actionPerformed( ActionEvent event) {
		mFrame.dispose();
	}
}
