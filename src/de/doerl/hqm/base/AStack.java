package de.doerl.hqm.base;

import java.awt.Image;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.medium.FNbt;

public abstract class AStack {
	AStack() {
	}

	public abstract <T, U> T accept( IStackWorker<T, U> w, U p);

	public abstract int getCount();

	public abstract int getDamage();

	public Image getImage() {
		return null;
	}

	public abstract String getName();

	public abstract FNbt getNBT();
}
