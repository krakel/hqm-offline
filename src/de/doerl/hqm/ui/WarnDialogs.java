package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.JOptionPane;

import de.doerl.hqm.utils.ResourceManager;

public class WarnDialogs {
	public static boolean askDelete( Window owner) {
		String msg = ResourceManager.getString( "warn.delete.comment");
		String title = ResourceManager.getString( "warn.delete.title");
		return JOptionPane.showConfirmDialog( owner, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
	}

	public static boolean askMissing( Window owner) {
		String msg = ResourceManager.getString( "warn.missing.comment");
		String title = ResourceManager.getString( "warn.missing.title");
		return JOptionPane.showConfirmDialog( owner, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
	}

	public static boolean askOverwrite( Window owner) {
		String msg = ResourceManager.getString( "warn.overwrite.comment");
		String title = ResourceManager.getString( "warn.overwrite.title");
		return JOptionPane.showConfirmDialog( owner, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
	}

	public static boolean warnOk( Window owner) {
		String msg = ResourceManager.getString( "warn.complete.comment");
		String title = ResourceManager.getString( "warn.complete.title");
		JOptionPane.showConfirmDialog( owner, msg, title, JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
}
