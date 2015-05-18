package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.JOptionPane;

import de.doerl.hqm.utils.ResourceManager;

public class WarnDialogs {
	public static boolean askOverwrite( Window owner) {
		String msg = ResourceManager.getString( "hqm.overwrite.comment");
		String title = ResourceManager.getString( "hqm.overwrite.title");
		return JOptionPane.showConfirmDialog( owner, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
	}

	public static boolean warnOk( Window owner) {
		String msg = ResourceManager.getString( "hqm.complete.comment");
		String title = ResourceManager.getString( "hqm.complete.title");
		JOptionPane.showConfirmDialog( owner, msg, title, JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
}
