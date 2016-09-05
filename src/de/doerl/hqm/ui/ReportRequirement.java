package de.doerl.hqm.ui;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.medium.ICallback;

class ReportRequirement extends AReport {
	private static final long serialVersionUID = 1797472349925557794L;

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
		saveStacks( Collector.get( hqm), file);
	}

	@Override
	File suggest( String name) {
		return name != null ? new File( name + "_require.txt") : null;
	}

	private static final class Collector extends AHQMWorker<Object, Object> {
		private Map<AStack, Integer> mMap = new TreeMap<>( new ItemCompare());

		private Collector() {
		}

		public static Map<AStack, Integer> get( FHqm hqm) {
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
			Integer old = mMap.get( stack);
			if (old != null) {
				mMap.put( stack, old + fluid.mAmount);
			}
			else {
				mMap.put( stack, fluid.mAmount);
			}
			return null;
		}

		@Override
		public Object forItemRequirement( FItemRequirement item, Object p) {
			FItemStack stack = item.getStack();
			Integer old = mMap.get( stack);
			if (old != null) {
				mMap.put( stack, old + stack.getStackSize());
			}
			else {
				mMap.put( stack, stack.getStackSize());
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
