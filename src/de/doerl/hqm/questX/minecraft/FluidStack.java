package de.doerl.hqm.questX.minecraft;

import de.doerl.hqm.questX.AWriter;
import de.doerl.hqm.utils.Utils;

public class FluidStack {
	NBTTagCompound mNBT;
	Fluid mFluid;

	public FluidStack( NBTTagCompound compound) {
		mNBT = compound;
		mFluid = new Fluid( this);
	}

	public int getAmount() {
		return Utils.parseInteger( mNBT.getValue( "Amount"), 1);
	}

	public NBTTagCompound getCompound() {
		return mNBT;
	}

	public Fluid getFluid() {
		return mFluid;
	}

	@Override
	public String toString() {
		if (mNBT != null) {
			return mNBT.toString();
		}
		return "FluidStack";
	}

	public void writeTo( AWriter out) {
		out.beginObject();
		if (mNBT != null) {
			out.print( "nbt", mNBT);
		}
		out.endObject();
	}
}
