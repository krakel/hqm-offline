package de.doerl.hqm.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class KeyAdaptorASCII extends KeyAdapter {
	public static boolean isASCII( int codePoint) {
		return codePoint >= 32 && codePoint < 127;
	}

	@Override
	public void keyTyped( KeyEvent e) {
		char ch = e.getKeyChar();
		if (!isASCII( ch) && ch != KeyEvent.VK_BACK_SPACE && ch != KeyEvent.VK_DELETE) {
			e.consume();
		}
	}
}
