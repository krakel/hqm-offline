package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.medium.FNbt;

public abstract class AStack {
	AStack() {
	}

	public abstract <T, U> T accept( IStackWorker<T, U> w, U p);

	public abstract int getAmount();

	public abstract FNbt getNBT();
}
