package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.medium.FNbt;

public final class FFluidStack extends AStack {
	private FNbt mNBT;

	public FFluidStack( FNbt compound) {
		mNBT = compound;
	}

	@Override
	public <T, U> T accept( IStackWorker<T, U> w, U p) {
		return w.forFluidStack( this, p);
	}

	public int getAmount() {
		return Integer.parseInt( mNBT.getValue( "Amount"));
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
