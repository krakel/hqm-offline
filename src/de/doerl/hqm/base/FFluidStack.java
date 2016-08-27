package de.doerl.hqm.base;

public final class FFluidStack extends AStack {
	private static final String FLUID_MOD = "fluid:";
	private static final String OLD_FLUID = "id:";
	private String mKey;
	private String mName;

	public FFluidStack( int id) {
		mName = OLD_FLUID + id;
		mKey = mName + "%0";
	}

	public FFluidStack( String name) {
		mName = FLUID_MOD + name;
		mKey = mName + "%0";
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
	public int getStackSize() {
		return 1;
	}

	public boolean isOldFluid() {
		return mKey.startsWith( OLD_FLUID);
	}

	@Override
	public String toString() {
		return mName;
	}
}
