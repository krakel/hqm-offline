package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.medium.FNbt;

public final class FItemStack extends AStack {
	private FNbt mNBT;
	private String mItem;
	private int mSize;
	private int mDmg;

	public FItemStack( FNbt nbt) {
		mNBT = nbt;
	}

	public FItemStack( FNbt nbt, String item, int size, int dmg) {
		mNBT = nbt;
		mItem = item;
		mSize = size;
		mDmg = dmg;
	}

	@Override
	public <T, U> T accept( IStackWorker<T, U> w, U p) {
		return w.forItemStack( this, p);
	}

	@Override
	public int getAmount() {
		return mSize;
	}

	public int getDmg() {
		return mDmg;
	}

	public String getItem() {
		return mItem;
	}

	@Override
	public String getName() {
		if (mItem != null) {
			return mItem;
		}
		else if (mNBT != null) {
			return mNBT.getValue( "id");
		}
		else {
			return "unknown";
		}
	}

	@Override
	public FNbt getNBT() {
		return mNBT;
	}

	@Override
	public String toString() {
		return String.format( "%s size(%d) dmg(%d)", mItem, mSize, mDmg);
	}
}
