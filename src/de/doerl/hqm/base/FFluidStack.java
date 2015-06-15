package de.doerl.hqm.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.doerl.hqm.utils.Utils;

public final class FFluidStack extends AStack {
	private static final Logger LOGGER = Logger.getLogger( FFluidStack.class.getName());
	private static final Pattern PATTERN = Pattern.compile( "(.*?) size\\((\\d*)\\)");
	private static final String FLUID_MOD = "fluid:";
	private static final String OLD_FLUID = "id:";
	private String mKey;
	private String mName;
	private int mSize;

	private FFluidStack( Matcher mm) {
		super( null);
		mm.find();
		mName = mm.group( 1);
		mSize = Utils.parseInteger( mm.group( 2));
		mKey = mName + "%0";
	}

	public FFluidStack( String nbt) {
		super( nbt);
		mName = getValueStr( "FluidName", null);
		mSize = getValueInt( "Amount", 1);
		if (mName == null) {
			mName = OLD_FLUID + getValueID( "id", "0");
		}
		else {
			mName = FLUID_MOD + mName;
		}
		mKey = mName + "%0";
	}

	public FFluidStack( String name, int size) {
		super( createFluidNBT( name, size));
		mName = FLUID_MOD + name;
		mSize = size;
		mKey = mName + "%0";
	}

	private static String createFluidNBT( String name, int size) {
		int pos = name.indexOf( ':') + 1;
		return String.format( "=COMPOUND( Amount=INT(%d), FluidName=STRING('%s') )", size, name.substring( pos));
	}

	public static FFluidStack parse( String nbt) {
		if (nbt == null) {
			return null;
		}
		else if (nbt.startsWith( "=COMPOUND(")) {
			return new FFluidStack( nbt);
		}
		else {
			try {
				return new FFluidStack( PATTERN.matcher( nbt));
			}
			catch (RuntimeException ex) {
				Utils.log( LOGGER, Level.WARNING, "illagle pattern: {0}", nbt);
				return new FFluidStack( "item:unknown", 0);
			}
		}
	}

	@Override
	public int getCount() {
		return mSize;
	}

	@Override
	public int getDamage() {
		return 0;
	}

	@Override
	public String getKey() {
		return mKey;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getNBT() {
		if (mNBT != null) {
			return mNBT;
		}
		else if (mKey.startsWith( OLD_FLUID)) {
			int id = Utils.parseInteger( mName, 0);
			return String.format( "=COMPOUND( id=SHORT(%d), Amount=INT(%d) )", id, mSize);
		}
		else {
			int pos = mName.indexOf( ':') + 1;
			return String.format( "=COMPOUND( Amount=INT(%d), FluidName=STRING('&s') )", mSize, mName.substring( pos));
		}
	}

	@Override
	public String toString() {
		return String.format( "%s size(%d)", mName, mSize);
	}
}
