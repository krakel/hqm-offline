package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.nbt.FCompound;

public final class FFluidRequirement extends ARequirement {
//	private static final Logger LOGGER = Logger.getLogger( FFluidRequirement.class.getName());
	private FFluidStack mStack;

	public FFluidRequirement( AQuestTaskItems parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forFluidRequirement( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.FLUID_REQUIREMENT;
	}

	@Override
	public FCompound getNBT() {
		return null;
	}

	@Override
	public ItemPrecision getPrecision() {
		return ItemPrecision.PRECISE;
	}

	@Override
	public FFluidStack getStack() {
		return mStack;
	}

	public void setStack( FFluidStack stack) {
		mStack = stack;
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "stack", mStack);
		sb.appendMsg( "amount", mAmount);
		return sb.toString();
	}
}
