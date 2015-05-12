package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemStack;

public interface IStackWorker<T, U> extends IWorker {
	T forFluidStack( FFluidStack stk, U p);

	T forItemStack( FItemStack stk, U p);
}
