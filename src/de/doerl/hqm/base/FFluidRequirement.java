package de.doerl.hqm.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.medium.json.IToken;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;
import de.doerl.hqm.utils.nbt.ANbt;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FLong;
import de.doerl.hqm.utils.nbt.FString;
import de.doerl.hqm.utils.nbt.NbtParser;

public final class FFluidRequirement extends ARequirement {
	private static final Logger LOGGER = Logger.getLogger( FFluidRequirement.class.getName());
	private static final Pattern PATTERN = Pattern.compile( "(.*?) size\\((\\d*)\\)");
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
		String name = mStack.getName();
		if (mStack.isOldFluid()) {
			int id = Utils.parseInteger( name, 0);
			return FCompound.create( FLong.createShort( "id", id), FLong.createInt( "Amount", mAmount));
		}
		else {
			int pos = name.indexOf( ':') + 1;
			return FCompound.create( FString.create( "FluidName", name.substring( pos)), FLong.createInt( "Amount", mAmount));
		}
	}

	@Override
	public ItemPrecision getPrecision() {
		return ItemPrecision.PRECISE;
	}

	@Override
	public FFluidStack getStack() {
		return mStack;
	}

	public void parse( FObject obj) {
		String src = FValue.toString( obj.get( IToken.FLUID_OBJECT));
		if (src == null) {
			return;
		}
		if (src.startsWith( "=COMPOUND(")) {
			FCompound nbt = NbtParser.parse( src);
			ANbt json = nbt.get( "FluidName");
			if (json == null) {
				setStack( new FFluidStack( FLong.toInt( nbt.get( "id"), 0)));
			}
			else {
				setStack( new FFluidStack( FString.to( json, "unknown.fluid")));
			}
			mAmount = FLong.toInt( nbt.get( "Amount"), 1);
		}
		else {
			try {
				Matcher mm = PATTERN.matcher( src);
				mm.find();
				setStack( new FFluidStack( mm.group( 1)));
				mAmount = Utils.parseInteger( mm.group( 2));
			}
			catch (RuntimeException ex) {
				Utils.log( LOGGER, Level.WARNING, "illagle pattern: {0}", src);
				setStack( new FFluidStack( "item:unknown"));
			}
		}
	}

	public void setStack( FFluidStack stack) {
		mStack = stack;
	}
}
