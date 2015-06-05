package de.doerl.hqm.ui;

import java.awt.Window;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class CloseWorker extends AHQMWorker<Boolean, Window> {
	private static final CloseWorker WORKER = new CloseWorker();

	private CloseWorker() {
	}

	public static boolean get( EditFrame frame) {
		return frame.getModel().forEachHQM( WORKER, frame) == null;
	}

	@Override
	public Boolean forHQM( FHqm hqm, Window owner) {
		if (EditFrame.canClose( hqm, owner)) {
			return null;
		}
		return Boolean.TRUE;
	}
}
