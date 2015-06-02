package de.doerl.hqm.base;

public final class FFluidStack extends AStack {
	private String mKey;

	public FFluidStack( String nbt) {
		super( nbt);
		mKey = getName() + "%" + getDamage();
	}

	public static FFluidStack parse( String nbt) {
		if (nbt != null) {
			return new FFluidStack( nbt);
		}
		else {
			return null;
		}
	}

	@Override
	public String getKey() {
		return mKey;
	}

	@Override
	public String toString() {
		return getNbtStr();
	}
}
