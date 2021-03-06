package de.doerl.hqm.ui;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.medium.ICallback;

class ReportRewards extends AReport {
	private static final long serialVersionUID = -2143378505414016857L;

	public ReportRewards( ICallback cb) {
		super( "hqm.rewards", cb);
	}

	@Override
	File normalize( File choose) {
		String norm = truncName( choose.getName(), ".txt");
		norm = truncName( norm, "_reward");
		return new File( choose.getParent(), norm + "_reward.txt");
	}

	@Override
	void saveFile( FHqm hqm, File file) {
		saveStacks( Collector.get( hqm), file);
	}

	@Override
	File suggest( String name) {
		return name != null ? new File( name + "_reward.txt") : null;
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
		public Object forQuest( FQuest quest, Object p) {
			for (FItemStack stack : quest.mRewards) {
				Integer old = mMap.get( stack);
				if (old != null) {
					mMap.put( stack, old + stack.getStackSize());
				}
				else {
					mMap.put( stack, stack.getStackSize());
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
