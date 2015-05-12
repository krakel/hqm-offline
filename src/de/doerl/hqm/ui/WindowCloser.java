package de.doerl.hqm.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowCloser implements WindowListener {
	public WindowCloser() {
	}

	public void windowActivated( WindowEvent event) {
	}

	public void windowClosed( WindowEvent event) {
	}

	public void windowClosing( WindowEvent event) {
		event.getWindow().dispose();
	}

	public void windowDeactivated( WindowEvent event) {
	}

	public void windowDeiconified( WindowEvent event) {
	}

	public void windowIconified( WindowEvent event) {
	}

	public void windowOpened( WindowEvent event) {
	}
}
