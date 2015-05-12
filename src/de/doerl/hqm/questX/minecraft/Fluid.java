package de.doerl.hqm.questX.minecraft;

import de.doerl.hqm.questX.AWriter;

public class Fluid {
	private FluidStack mStack;

	public Fluid( FluidStack stack) {
		mStack = stack;
	}

	@Override
	public String toString() {
		if (mStack != null) {
			return mStack.toString();
		}
		return "Fluid";
	}

	public void writeTo( AWriter out) {
		if (mStack.mNBT != null) {
			out.print( "fluid", mStack.mNBT);
		}
	}
}
