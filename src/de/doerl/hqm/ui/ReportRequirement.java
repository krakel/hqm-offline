package de.doerl.hqm.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.utils.Utils;

class ReportRequirement extends AReport {
	private static final long serialVersionUID = 1797472349925557794L;
	private static final Logger LOGGER = Logger.getLogger( ReportRequirement.class.getName());

	public ReportRequirement( ICallback cb) {
		super( "hqm.require", cb);
	}

	@Override
	File normalize( File choose) {
		String norm = truncName( choose.getName(), ".txt");
		norm = truncName( norm, "_require");
		return new File( choose.getParent(), norm + "_require.txt");
	}

	@Override
	void saveFile( FHqm hqm, File file) {
		PrintWriter out = null;
		try {
			Map<Item, Integer> map = Collector.get( hqm);
			out = new PrintWriter( new OutputStreamWriter( new FileOutputStream( file), "UTF-8"));
			for (Item key : map.keySet()) {
				out.print( String.format( "%3d x %s", map.get( key), key.mName));
				if (key.mDmg > 0) {
					out.print( String.format( ", dmg(%d)", key.mDmg));
				}
				if (key.mNBT != null) {
					out.print( String.format( ", nbt(\"%s\")", key.mNBT));
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

	@Override
	File suggest( String name) {
		return name != null ? new File( name + "_require.txt") : null;
	}

	private static final class Collector extends AHQMWorker<Object, Object> {
		private Map<Item, Integer> mMap = new TreeMap<>( new ItemCompare());

		private Collector() {
		}

		public static Map<Item, Integer> get( FHqm hqm) {
			Collector worker = new Collector();
			hqm.mQuestSetCat.forEachMember( worker, null);
			return worker.mMap;
		}

		@Override
		protected Object doTask( AQuestTask task, Object p) {
			return null;
		}

		@Override
		protected Object doTaskItems( AQuestTaskItems task, Object p) {
			task.forEachRequirement( this, p);
			return null;
		}

		@Override
		public Object forFluidRequirement( FFluidRequirement fluid, Object p) {
			FFluidStack stack = fluid.getStack();
			Item key = new Item( stack.getName(), stack.getDamage(), null);
			Integer old = mMap.get( key);
			if (old != null) {
				mMap.put( key, old + fluid.mAmount);
			}
			else {
				mMap.put( key, fluid.mAmount);
			}
			return null;
		}

		@Override
		public Object forItemRequirement( FItemRequirement item, Object p) {
			FItemStack stack = item.getStack();
			Item key = new Item( stack.getName(), stack.getDamage(), stack.getNBT());
			Integer old = mMap.get( key);
			if (old != null) {
				mMap.put( key, old + stack.getStackSize());
			}
			else {
				mMap.put( key, stack.getStackSize());
			}
			return null;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			quest.forEachTask( this, p);
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, Object p) {
			set.forEachQuest( this, p);
			return null;
		}
	}
}
