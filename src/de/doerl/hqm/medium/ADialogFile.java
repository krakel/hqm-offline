package de.doerl.hqm.medium;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;

import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;

public abstract class ADialogFile extends ABundleAction implements IRefreshListener {
	private static final long serialVersionUID = -5353239946352595216L;
	protected ICallback mCallback;

	public ADialogFile( String name, ICallback cb) {
		super( name);
		mCallback = cb;
		cb.addRefreshListener( this);
	}

	protected static JFileChooser createChooser( String pfad) {
		JFileChooser chooser = new JFileChooser( pfad);
		chooser.setLocale( Locale.getDefault());
		chooser.setControlButtonsAreShown( true);
		chooser.setAcceptAllFileFilterUsed( true);
		chooser.setMultiSelectionEnabled( false);
		return chooser;
	}

	protected static String getLastOpen() {
		return PreferenceManager.getString( BaseDefaults.FILE_OPEN_DIR);
	}

	protected static String getLastOpenDir( String prop) {
		int max = PreferenceManager.getArrayCount( prop);
		if (max > 0) {
			return PreferenceManager.getArrayString( prop, 0);
		}
		return getLastOpen();
	}

	protected static File selectOpenDialog( Window frame, JFileChooser chooser) {
		if (chooser.showOpenDialog( frame) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}

	protected static File selectSaveDialog( Window frame, JFileChooser chooser) {
		if (chooser.showSaveDialog( frame) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}

	protected static void setLastHQM( File file) {
		String uri = file.toURI().toString();
		PreferenceManager.deleteArrayString( BaseDefaults.LAST_OPEN, uri);
		PreferenceManager.addArrayString( BaseDefaults.LAST_OPEN, 0, uri);
		String parent = file.getParent();
		PreferenceManager.setString( BaseDefaults.FILE_OPEN_DIR, parent);
		PreferenceManager.deleteArrayString( BaseDefaults.LAST_OPEN_DIR, parent);
		PreferenceManager.addArrayString( BaseDefaults.LAST_OPEN_DIR, 0, parent);
	}

	protected static void setLastOpen( File file) {
		String parent = file.getParent();
		PreferenceManager.setString( BaseDefaults.FILE_OPEN_DIR, parent);
	}

	@Override
	public void actionPerformed( ActionEvent event) {
		Window frame = ADialog.getParentFrame( (Container) event.getSource());
		action( frame);
	}
}
