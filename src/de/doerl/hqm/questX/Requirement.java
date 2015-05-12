package de.doerl.hqm.questX;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.questX.minecraft.Fluid;
import de.doerl.hqm.questX.minecraft.FluidStack;
import de.doerl.hqm.questX.minecraft.ItemStack;

public class Requirement {
	public static ARequirement[] read( BitInputStream is) {
		int count = is.readData( DataBitHelper.TASK_ITEM_COUNT);
		ARequirement[] items = new ARequirement[count];
		for (int i = 0; i < count; ++i) {
			if (is.readBoolean()) {
				items[i] = new ItemRequirement( is);
			}
			else {
				items[i] = new FluidRequirement( is);
			}
		}
		return items;
	}

	static abstract class ARequirement implements IWriter {
//		int mX, mY;
		int mRequired;
		ItemPrecision mPrecision;

		public ARequirement() {
		}
	}

	static class FluidRequirement extends ARequirement {
		Fluid mFluid;

		public FluidRequirement( BitInputStream is) {
			FluidStack fluidStack = new FluidStack( is.readNBT());
			mFluid = fluidStack.getFluid();
			mRequired = fluidStack.getAmount();
			mPrecision = ItemPrecision.PRECISE;
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "fluid", mFluid);
			out.print( "required", mRequired);
			out.print( "precision", mPrecision);
			out.endObject();
		}
	}

	static class ItemRequirement extends ARequirement {
		ItemStack mStack;

		public ItemRequirement( BitInputStream is) {
//			mStack = is.readItemStack( false);
			mRequired = is.readData( DataBitHelper.TASK_REQUIREMENT);
			mPrecision = ItemPrecision.get( is.readData( DataBitHelper.ITEM_PRECISION));
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "item", mStack);
			out.print( "required", mRequired);
			out.print( "precision", mPrecision);
			out.endObject();
		}
	}
}
