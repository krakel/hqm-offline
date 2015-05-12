package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.medium.FNbt;

public final class FItemStack extends AStack {
	private FNbt mNBT;
	private String mItem;
	private int mDmg;
	private int mSize;

	public FItemStack( FNbt nbt) {
		mNBT = nbt;
	}

	public FItemStack( FNbt nbt, String item, int dmg, int size) {
		mNBT = nbt;
		mItem = item;
		mDmg = dmg;
		mSize = size;
	}

	@Override
	public <T, U> T accept( IStackWorker<T, U> w, U p) {
		return w.forItemStack( this, p);
	}

	public int getDmg() {
		return mDmg;
	}

	public String getItem() {
		return mItem;
	}

	@Override
	public FNbt getNBT() {
		return mNBT;
	}

	public int getSize() {
		return mSize;
	}

	@Override
	public String toString() {
		return String.format( "%s dmg(%d) size(%d)", mItem, mDmg, mSize);
	}
}
