package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.medium.FNbt;
import de.doerl.hqm.utils.Utils;

public final class FItemStack extends AStack {
	private FNbt mNBT;
	private String mItem;
	private int mSize;
	private int mDmg;
	private String mKey;

	public FItemStack( FNbt nbt) {
		mNBT = nbt;
		mKey = getName() + "%" + getDamage();
	}

	public FItemStack( FNbt nbt, String item, int size, int dmg) {
		mNBT = nbt;
		mItem = item;
		mSize = size;
		mDmg = dmg;
		mKey = getName() + "%" + getDamage();
	}

	@Override
	public <T, U> T accept( IStackWorker<T, U> w, U p) {
		return w.forItemStack( this, p);
	}

	@Override
	public int getCount() {
		if (mItem != null) {
			return mSize;
		}
		else if (mNBT != null) {
			return Utils.parseInteger( mNBT.getValue( "Count"), 1);
		}
		else {
			return 1;
		}
	}

	@Override
	public int getDamage() {
		if (mItem != null) {
			return mDmg;
		}
		else if (mNBT != null) {
			return Utils.parseInteger( mNBT.getValue( "Damage"), 0);
		}
		else {
			return 0;
		}
	}

	public String getItem() {
		return mItem;
	}

	@Override
	public String getKey() {
		return mKey;
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
