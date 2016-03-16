package de.doerl.hqm.ui;

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

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.utils.Utils;

public class ReportStory extends AReport {
	private static final long serialVersionUID = 2211030488129884122L;
	private static final Logger LOGGER = Logger.getLogger( ReportStory.class.getName());

	public ReportStory( ICallback cb) {
		super( "hqm.story", cb);
	}

	private static void writeFile( Map<String, Integer> map, OutputStream os) throws IOException {
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
	File normalize( File choose) {
		String norm = truncName( choose.getName(), ".txt");
		norm = truncName( norm, ".story");
		return new File( choose.getParent(), norm + ".story.txt");
	}

	@Override
	boolean saveFile( FHqm hqm, File file) {
		OutputStream os = null;
		try {
			Map<String, Integer> map = Collector.get( hqm);
			os = new FileOutputStream( file);
			writeFile( map, os);
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

	@Override
	File suggest( String name) {
		return name != null ? new File( name + ".story.txt") : null;
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
			for (FItemStack item : quest.mRewards) {
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
