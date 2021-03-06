package de.doerl.hqm.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FLanguage;
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

	@Override
	File normalize( File choose) {
		String norm = truncName( choose.getName(), ".txt");
		norm = truncName( norm, "_story");
		return new File( choose.getParent(), norm + "_story.txt");
	}

	@Override
	void saveFile( FHqm hqm, File file) {
		PrintWriter out = null;
		try {
			out = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file), "UTF-8"));
			Collector.get( hqm, out, hqm.mMain);
			out.flush();
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( out);
		}
	}

	@Override
	File suggest( String name) {
		return name != null ? new File( name + "_story.txt") : null;
	}

	private static final class Collector extends AHQMWorker<Object, Object> {
		private PrintWriter mOut;
		private FLanguage mLang;
		private int mSet, mQuest, mTask;

		private Collector( PrintWriter out, FLanguage lang) {
			mOut = out;
			mLang = lang;
		}

		public static void get( FHqm hqm, PrintWriter out, FLanguage lang) {
			Collector worker = new Collector( out, lang);
			hqm.accept( worker, out);
		}

		@Override
		protected Object doTask( AQuestTask task, Object p) {
			++mTask;
//			mOut.print( mSet);
//			mOut.write( '.');
			mOut.print( "Task_");
//			mOut.write( '.');
			mOut.print( mTask);
			mOut.write( ": ");
			mOut.print( task.getName( mLang));
			mOut.write( NL);
			mOut.write( '\t');
			mOut.print( task.getDescr( mLang));
			mOut.write( NL);
			return null;
		}

		@Override
		public Object forHQM( FHqm hqm, Object p) {
			mOut.print( hqm.getDescr( mLang));
			mOut.write( NL);
			mSet = 0;
			hqm.mQuestSetCat.forEachMember( this, p);
			return null;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			++mQuest;
			mOut.write( NL);
			mOut.print( mSet);
			mOut.write( '.');
			mOut.print( mQuest);
			mOut.write( ": ");
			mOut.print( quest.getName( mLang));
			mOut.write( NL);
			mOut.write( '\t');
			mOut.print( quest.getDescr( mLang));
			mOut.write( NL);
			mTask = 0;
			quest.forEachTask( this, p);
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, Object p) {
			++mSet;
			mOut.write( NL);
			mOut.print( mSet);
			mOut.write( ": ");
			mOut.print( set.getName( mLang));
			mOut.write( NL);
			mOut.write( '\t');
			mOut.print( set.getDescr( mLang));
			mOut.write( NL);
			mQuest = 0;
			set.forEachQuest( this, p);
			return null;
		}
	}
}
