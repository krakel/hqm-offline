package de.doerl.hqm.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

class KeyAdaptorInterger extends KeyAdapter {
	private boolean isValidSignal( char ch, Object src) {
		try {
			if (ch == '-') {
				JTextField fld = (JTextField) src;
				return fld.getText() == null || "".equals( fld.getText().trim());
			}
		}
		catch (ClassCastException ex) {
		}
		return false;
	}

	@Override
	public void keyTyped( KeyEvent e) {
		char ch = e.getKeyChar();
		if (!Character.isDigit( ch) && !isValidSignal( ch, e.getSource()) && ch != KeyEvent.VK_BACK_SPACE && ch != KeyEvent.VK_DELETE) {
			e.consume();
		}
	}
}
