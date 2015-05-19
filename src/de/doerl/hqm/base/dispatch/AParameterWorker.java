package de.doerl.hqm.base.dispatch;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AParameter;
import de.doerl.hqm.base.FParameterBoolean;
import de.doerl.hqm.base.FParameterEnum;
import de.doerl.hqm.base.FParameterInt;
import de.doerl.hqm.base.FParameterInteger;
import de.doerl.hqm.base.FParameterIntegerArr;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FParameterString;
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public class AParameterWorker<T, U> implements IParameterWorker<T, U> {
	private static final Logger LOGGER = Logger.getLogger( AParameterWorker.class.getName());

	protected T doParameter( AParameter par, U p) {
		Utils.log( LOGGER, Level.WARNING, "{0} missing handler for {1}", ToString.clsName( this), par);
		return null;
	}

	@Override
	public T forParameterBoolean( FParameterBoolean par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterEnum( FParameterEnum<? extends Enum<?>> par, U p) {
		return doParameter( par, p);
	}

	public T forParameterInt( FParameterInt par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterInteger( FParameterInteger par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterIntegerArr( FParameterIntegerArr par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterStack( FParameterStack par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterString( FParameterString par, U p) {
		return doParameter( par, p);
	}
}
