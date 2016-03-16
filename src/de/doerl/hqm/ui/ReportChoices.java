package de.doerl.hqm.ui;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.medium.ADialogFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.RefreshEvent;
import de.doerl.hqm.utils.Security;
import de.doerl.hqm.utils.Utils;

class ReportChoices extends ABundleAction implements IRefreshListener, Runnable {
	private static final long serialVersionUID = -2143378505414016857L;
	private static final Logger LOGGER = Logger.getLogger( ReportRewards.class.getName());
	private static final String NL = Security.getProperty( "line.separator", "\n");
	public static final FileFilter FILTER = new FileNameExtensionFilter( "choice file", "txt");
	private ICallback mCallback;

	public ReportChoices( ICallback cb) {
		super( "hqm.choices");
		mCallback = cb;
		cb.addRefreshListener( this);
		setEnabled( false);
	}

	static File normalize( File choose) {
		String norm = truncName( choose.getName(), ".txt");
		norm = truncName( norm, ".choice");
		return new File( choose.getParent(), norm + ".choice.txt");
	}

	static boolean saveReward( FHqm hqm, File file) {
		OutputStream os = null;
		try {
			Map<String, Integer> map = Collector.get( hqm);
			os = new FileOutputStream( file);
			writeChoice( map, os);
			return true;
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
		return false;
	}

	static File suggest( String name) {
		return name != null ? new File( name + ".choice.txt") : null;
	}

	public static String truncName( String name, String suffix) {
		if (name.toLowerCase().endsWith( suffix)) {
			return name.substring( 0, name.length() - suffix.length());
		}
		return name;
	}

	private static void writeChoice( Map<String, Integer> map, OutputStream os) throws IOException {
		PrintWriter out = new PrintWriter( new OutputStreamWriter( os, "UTF-8"));
		for (String key : map.keySet()) {
			out.print( key);
			out.print( " : ");
			out.print( map.get( key));
			out.write( NL);
		}
		out.flush();
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
					saveReward( hqm, file);
				}
			}
		}
	}

	@Override
	public void actionPerformed( ActionEvent event) {
		Window frame = ADialog.getParentFrame( (Container) event.getSource());
		action( frame);
	}

	@Override
	public void run() {
		FHqm hqm = mCallback.updateHQM();
		setEnabled( hqm != null);
	}

	@Override
	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}

	private static final class Collector extends AHQMWorker<Object, Object> {
		private Map<String, Integer> mMap = new TreeMap<>();

		private Collector() {
		}

		public static Map<String, Integer> get( FHqm hqm) {
			Collector worker = new Collector();
			hqm.mQuestSetCat.forEachMember( worker, null);
			return worker.mMap;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			for (FItemStack item : quest.mChoices) {
				String key = item.getKey();
				int count = item.getCount();
				Integer old = mMap.get( key);
				if (old != null) {
					mMap.put( key, old + count);
				}
				else {
					mMap.put( key, count);
				}
			}
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, Object p) {
			set.forEachQuest( this, p);
			return null;
		}
	}
}
