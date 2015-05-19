package de.doerl.hqm.ui;

import java.awt.Container;

import javax.swing.JOptionPane;

import de.doerl.hqm.utils.ResourceManager;

public class WarnDialogs {
	public static boolean askOverwrite( Container owner) {
		String msg = ResourceManager.getString( "warn.overwrite.comment");
		String title = ResourceManager.getString( "warn.overwrite.title");
		return JOptionPane.showConfirmDialog( ADialog.getParentFrame( owner), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
	}

	public static boolean warnMissing( Container owner) {
		String msg = ResourceManager.getString( "warn.missing.comment");
		String title = ResourceManager.getString( "warn.missing.title");
		JOptionPane.showConfirmDialog( ADialog.getParentFrame( owner), msg, title, JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
		return true;
	}

	public static boolean warnOk( Container owner) {
		String msg = ResourceManager.getString( "warn.complete.comment");
		String title = ResourceManager.getString( "warn.complete.title");
		JOptionPane.showConfirmDialog( ADialog.getParentFrame( owner), msg, title, JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
}
