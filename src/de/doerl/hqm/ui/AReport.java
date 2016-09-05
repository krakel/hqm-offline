package de.doerl.hqm.ui;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ADialogFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.RefreshEvent;
import de.doerl.hqm.utils.Security;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.SerializerAtJson;

abstract class AReport extends ABundleAction implements IRefreshListener, Runnable {
	private static final long serialVersionUID = -3001796596162739183L;
	private static final Logger LOGGER = Logger.getLogger( AReport.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "txt file", "txt");
	protected static final String NL = Security.getProperty( "line.separator", "\n");
	private static final Comparator<String> STING_COMPERATOR = Comparator.nullsFirst( Comparator.naturalOrder());
	protected ICallback mCallback;

	public AReport( String name, ICallback cb) {
		super( name);
		mCallback = cb;
		cb.addRefreshListener( this);
		setEnabled( false);
	}

	static void saveStacks( Map<AStack, Integer> map, File file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file), "UTF-8"));
			for (AStack key : map.keySet()) {
				out.print( String.format( "%3d x %s", map.get( key), key.getDisplay()));
//				out.print( String.format( ", name='%s'", key.getName()));
				if (key.getDamage() > 0) {
					out.print( String.format( ", dmg=%d", key.getDamage()));
				}
				if (key.getNBT() != null) {
					out.print( String.format( ", nbt%s", SerializerAtJson.write( key.getNBT())));
				}
				out.write( NL);
			}
			out.flush();
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( out);
		}
	}

	protected static String truncName( String name, String suffix) {
		if (name.toLowerCase().endsWith( suffix)) {
			return name.substring( 0, name.length() - suffix.length());
		}
		return name;
	}

	@Override
	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File last = (File) MediaManager.getProperty( hqm, MediaManager.ACTIV_PATH);
			String pfad = last != null ? last.getAbsolutePath() : ADialogFile.getLastOpenDir();
			JFileChooser chooser = ADialogFile.createChooser( pfad);
			chooser.setFileFilter( FILTER);
			chooser.setSelectedFile( suggest( hqm.mName));
			File choose = ADialogFile.selectSaveDialog( frame, chooser);
			if (choose != null) {
				File file = normalize( choose);
				if (!file.exists() || mCallback.askOverwrite()) {
					saveFile( hqm, file);
				}
			}
		}
	}

	@Override
	public void actionPerformed( ActionEvent event) {
		Window frame = ADialog.getParentFrame( (Container) event.getSource());
		action( frame);
	}

	abstract File normalize( File choose);

	@Override
	public void run() {
		FHqm hqm = mCallback.updateHQM();
		setEnabled( hqm != null);
	}

	abstract void saveFile( FHqm hqm, File file);

	abstract File suggest( String name);

	@Override
	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}

	protected static final class ItemCompare implements Comparator<AStack> {
		@Override
		public int compare( AStack o1, AStack o2) {
//			Comparator<AStack> cc1 = Comparator.nullsFirst( Comparator.comparing( stk -> stk.getDisplay()));
//			Comparator<AStack> cc2 = cc1.thenComparing( null);
			int diff = STING_COMPERATOR.compare( o1.getDisplay(), o2.getDisplay());
			if (diff != 0) {
				return diff;
			}
			diff = STING_COMPERATOR.compare( o1.getName(), o2.getName());
			if (diff != 0) {
				return diff;
			}
			diff = Integer.compare( o1.getDamage(), o2.getDamage());
			if (diff != 0) {
				return diff;
			}
			return STING_COMPERATOR.compare( SerializerAtJson.write( o1.getNBT()), SerializerAtJson.write( o2.getNBT()));
		}
	}
}
