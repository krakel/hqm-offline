package de.doerl.hqm.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FLong;
import de.doerl.hqm.utils.nbt.FString;
import de.doerl.hqm.utils.nbt.NbtParser;

public final class FFluidStack extends AStack {
	private static final Logger LOGGER = Logger.getLogger( FFluidStack.class.getName());
	private static final Pattern PATTERN = Pattern.compile( "(.*?) size\\((\\d*)\\)");
	private static final String FLUID_MOD = "fluid:";
	private static final String OLD_FLUID = "id:";
	private String mKey;
	private String mName;
	private int mSize;

	public FFluidStack( FCompound nbt) {
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

	private FFluidStack( Matcher mm) {
		super( null);
		mm.find();
		mName = mm.group( 1);
		mSize = Utils.parseInteger( mm.group( 2));
		mKey = mName + "%0";
	}

	public FFluidStack( String name, int size) {
		super( createFluidNBT( name, size));
		mName = FLUID_MOD + name;
		mSize = size;
		mKey = mName + "%0";
	}

	private static FCompound createFluidNBT( String name, int size) {
		int pos = name.indexOf( ':') + 1;
		return FCompound.create( FLong.createInt( "Amount", size), FString.create( "FluidName", name.substring( pos)));
	}

	public static FFluidStack parse( String src) {
		if (src == null) {
			return null;
		}
		else if (src.startsWith( "=COMPOUND(")) {
			return new FFluidStack( NbtParser.parse( src));
		}
		else {
			try {
				return new FFluidStack( PATTERN.matcher( src));
			}
			catch (RuntimeException ex) {
				Utils.log( LOGGER, Level.WARNING, "illagle pattern: {0}", src);
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
	public String getDisplay() {
		return mName;
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
	public FCompound getNBT() {
		if (mNBT != null) {
			return mNBT;
		}
		else if (mKey.startsWith( OLD_FLUID)) {
			int id = Utils.parseInteger( mName, 0);
			return FCompound.create( FLong.createShort( "id", id), FLong.createInt( "Amount", mSize));
		}
		else {
			int pos = mName.indexOf( ':') + 1;
			return FCompound.create( FLong.createInt( "Amount", mSize), FString.create( "FluidName", mName.substring( pos)));
		}
	}

	@Override
	public String toString() {
		return String.format( "%s size(%d)", mName, mSize);
	}
}
