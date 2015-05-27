package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.medium.FNbt;
import de.doerl.hqm.utils.Utils;

public final class FFluidStack extends AStack {
	private FNbt mNBT;

	public FFluidStack( FNbt compound) {
		mNBT = compound;
	}

	@Override
	public <T, U> T accept( IStackWorker<T, U> w, U p) {
		return w.forFluidStack( this, p);
	}

	@Override
	public int getCount() {
		if (mNBT != null) {
			return Utils.parseInteger( mNBT.getValue( "Count"), 1);
		}
		else {
			return 0;
		}
	}

	@Override
	public int getDamage() {
		if (mNBT != null) {
			return Utils.parseInteger( mNBT.getValue( "Damage"), 0);
		}
		else {
			return 0;
		}
	}

	@Override
	public String getName() {
		if (mNBT != null) {
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
		return mNBT.toString();
	}
}
