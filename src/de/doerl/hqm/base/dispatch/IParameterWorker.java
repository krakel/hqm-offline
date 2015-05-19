package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FParameterBoolean;
import de.doerl.hqm.base.FParameterEnum;
import de.doerl.hqm.base.FParameterInt;
import de.doerl.hqm.base.FParameterInteger;
import de.doerl.hqm.base.FParameterIntegerArr;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FParameterString;

public interface IParameterWorker<T, U> extends IWorker {
	T forParameterBoolean( FParameterBoolean par, U p);

	T forParameterEnum( FParameterEnum<? extends Enum<?>> par, U p);

	T forParameterInt( FParameterInt par, U p);

	T forParameterInteger( FParameterInteger par, U p);

	T forParameterIntegerArr( FParameterIntegerArr par, U p);

	T forParameterStack( FParameterStack par, U p);

	T forParameterString( FParameterString par, U p);
}
