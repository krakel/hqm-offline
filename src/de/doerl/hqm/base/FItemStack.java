package de.doerl.hqm.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.ItemNEI;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FLong;
import de.doerl.hqm.utils.nbt.FString;
import de.doerl.hqm.utils.nbt.NbtParser;

public final class FItemStack extends AStack {
	private static final Logger LOGGER = Logger.getLogger( FItemStack.class.getName());
	private static final Pattern PATTERN = Pattern.compile( "(.*?) size\\((\\d*)\\) dmg\\((\\d*)\\)");
	private static final String OLD_ITEM = "id:";
	private String mKey;
	private ItemNEI mItem;
	private int mStackSize;
	private int mDmg;
	private FCompound mNBT;

	private FItemStack() {
		mStackSize = 1;
		mDmg = 0;
		mKey = OLD_ITEM + "0%" + mDmg;
		mItem = ImageLoader.get( mKey, null);
	}

	private FItemStack( FCompound nbt) {
		mNBT = nbt;
		mStackSize = getValueInt( "Count", 1);
		mDmg = getValueInt( "Damage", 0);
		mKey = OLD_ITEM + getValueID( "id", "0") + "%" + mDmg;
		mItem = ImageLoader.get( mKey, nbt);
	}

	private FItemStack( FCompound nbt, Matcher mm) {
		mNBT = nbt;
		mm.find();
		String name = mm.group( 1);
		mStackSize = Utils.parseInteger( mm.group( 2));
		mDmg = Utils.parseInteger( mm.group( 3));
		mKey = name + "%" + mDmg;
		mItem = ImageLoader.get( mKey, nbt);
	}

	private FItemStack( int dmg, int size, FCompound nbt) {
		mNBT = nbt;
		mStackSize = getValueInt( "Count", 1);
		mDmg = getValueInt( "Damage", 0);
		mKey = OLD_ITEM + getValueID( "id", "0") + "%" + mDmg;
		mItem = ImageLoader.get( mKey, nbt);
	}

	private FItemStack( int id, int dmg, int size) {
		mStackSize = size;
		mDmg = dmg;
		mKey = OLD_ITEM + String.valueOf( id) + "%" + mDmg;
		mItem = ImageLoader.get( mKey, null);
	}

	private FItemStack( int id, int dmg, int size, FCompound nbt) {
		this( id, dmg, size);
		mNBT = nbt;
	}

	public FItemStack( String name, int dmg, int size) {
		this( name, dmg, size, null);
	}

	public FItemStack( String name, int dmg, int size, FCompound nbt) {
		mStackSize = size;
		mDmg = dmg;
		mKey = name + "%" + mDmg;
		mItem = ImageLoader.get( mKey, nbt);
		mNBT = nbt;
	}

	public static FItemStack createOld() {
		return new FItemStack();
	}

	public static FItemStack createOld( FCompound nbt) {
		return new FItemStack( nbt);
	}

	public static FItemStack createOld( int dmg, int size) {
		return new FItemStack( 0, dmg, size);
	}

	public static FItemStack createOld( int dmg, int size, FCompound nbt) {
		return new FItemStack( dmg, size, nbt);
	}

	public static FItemStack createOld( int id, int dmg, int size) {
		return new FItemStack( id, dmg, size);
	}

	public static FItemStack createOld( int id, int dmg, int size, FCompound nbt) {
		return new FItemStack( id, dmg, size, nbt);
	}

	public static FItemStack parse( String sequence) {
		if (sequence != null) {
			try {
				return new FItemStack( null, PATTERN.matcher( sequence));
			}
			catch (RuntimeException ex) {
				Utils.log( LOGGER, Level.WARNING, "illagle pattern: {0}", sequence);
				return new FItemStack( "item:unknown", 0, 0);
			}
		}
		else {
			return null;
		}
	}

	public static FItemStack parse( String sequence, String nbt) {
		if (sequence != null) {
			try {
				return new FItemStack( NbtParser.parse( nbt), PATTERN.matcher( sequence));
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
				return new FItemStack( "item:unknown", 0, 0);
			}
		}
		else if (nbt != null) {
			return createOld( NbtParser.parse( nbt));
		}
		else {
			return null;
		}
	}

	public String countOf() {
		return mStackSize > 1 ? Integer.toString( mStackSize) : null;
	}

	@Override
	public int getDamage() {
		return mDmg;
	}

	@Override
	public String getDisplay() {
		return mItem.getDisplay();
	}

	public String getItem() {
		return mItem.mName;
	}

	@Override
	public String getKey() {
		return mKey;
	}

	@Override
	public String getName() {
		return mItem.mName;
	}

	public FCompound getNBT() {
		if (mNBT != null) {
			return mNBT;
		}
		else if (isOldItem()) {
			int id = Utils.parseInteger( mItem.mName, 0);
			return FCompound.create( FLong.createShort( "id", id), FLong.createShort( "Damage", mDmg), FLong.createByte( "Count", mStackSize));
		}
		else if (mItem.mName != null) {
			return null;
		}
		else {
			return FCompound.create();
		}
	}

	public String getNbtStr() {
		if (mNBT != null) {
			return mNBT.toString();
		}
		else {
			return "";
		}
	}

	@Override
	public int getStackSize() {
		return mStackSize;
	}

	private String getValueID( String key, String def) {
		if (mNBT != null) {
			return FString.to( mNBT.get( key), def);
		}
		else {
			return def;
		}
	}

	private int getValueInt( String key, int def) {
		if (mNBT != null) {
			return FLong.toInt( mNBT.get( key), def);
		}
		else {
			return def;
		}
	}

	boolean isOldItem() {
		return mKey.startsWith( OLD_ITEM);
	}

	public void setStackSize( int value) {
		mStackSize = value;
	}

	@Override
	public String toString() {
		return String.format( "%s size(%d) dmg(%d)", mItem.mName, mStackSize, mDmg);
	}
}
