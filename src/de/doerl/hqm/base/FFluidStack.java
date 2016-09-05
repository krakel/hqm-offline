package de.doerl.hqm.base;

import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.nbt.FCompound;

public final class FFluidStack extends AStack {
	private String mKey;
	private String mName;

	private FFluidStack( int id) {
		mName = "id:" + id;
		mKey = mName + "%0";
	}

	public FFluidStack( String name) {
		mName = "fluid:" + name;
		mKey = mName + "%0";
	}

	public static FFluidStack applyOld( int id) {
		return new FFluidStack( id);
	}

	@Override
	public int getDamage() {
		return 0;
	}

	@Override
	public String getDisplay() {
		return mName;
	}

	@Override
	public String getKey() {
		return mKey;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public FCompound getNBT() {
		return null;
	}

	@Override
	public int getStackSize() {
		return 1;
	}

	public boolean isOldFluid() {
		return mKey.startsWith( "id:");
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "key", mKey);
		return sb.toString();
	}
}
