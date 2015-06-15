package de.doerl.hqm.base;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.doerl.hqm.utils.Utils;

public final class FItemStack extends AStack {
	private static final Logger LOGGER = Logger.getLogger( FItemStack.class.getName());
	private static final Pattern PATTERN = Pattern.compile( "(.*?) size\\((\\d*)\\) dmg\\((\\d*)\\)");
	private static final String OLD_ITEM = "id:";
	private String mKey;
	private String mName;
	private int mSize;
	private int mDmg;

	public FItemStack( int id, int dmg, int size) {
		super( null);
		mName = OLD_ITEM + String.valueOf( id);
		mSize = size;
		mDmg = dmg;
		mKey = mName + "%" + mDmg;
	}

	public FItemStack( String nbt) {
		super( nbt);
		mName = OLD_ITEM + getValueID( "id", "0");
		mSize = getValueInt( "Count", 1);
		mDmg = getValueInt( "Damage", 0);
		mKey = mName + "%" + mDmg;
	}

	public FItemStack( String name, int dmg, int size) {
		super( null);
		mName = name;
		mSize = size;
		mDmg = dmg;
		mKey = mName + "%" + mDmg;
	}

	public FItemStack( String nbt, int id, int dmg, int size) {
		super( nbt);
		mName = "id:" + String.valueOf( id);
		mSize = size;
		mDmg = dmg;
		mKey = mName + "%" + mDmg;
	}

	private FItemStack( String nbt, Matcher mm) {
		super( nbt);
		mm.find();
		mName = mm.group( 1);
		mSize = Utils.parseInteger( mm.group( 2));
		mDmg = Utils.parseInteger( mm.group( 3));
		mKey = mName + "%" + mDmg;
	}

	public FItemStack( String nbt, String name, int dmg, int size) {
		super( nbt);
		if (name != null) {
			mName = name;
			mSize = size;
			mDmg = dmg;
		}
		else {
			mName = OLD_ITEM + getValueID( "id", "0");
			mSize = getValueInt( "Count", 1);
			mDmg = getValueInt( "Damage", 0);
		}
		mKey = mName + "%" + mDmg;
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
				return new FItemStack( nbt, PATTERN.matcher( sequence));
			}
			catch (RuntimeException ex) {
				Utils.log( LOGGER, Level.WARNING, "illagle pattern: {0}", nbt);
				return new FItemStack( "item:unknown", 0, 0);
			}
		}
		else if (nbt != null) {
			return new FItemStack( nbt);
		}
		else {
			return null;
		}
	}

	@Override
	public int getCount() {
		return mSize;
	}

	@Override
	public int getDamage() {
		return mDmg;
	}

	public String getItem() {
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
	public String getNBT() {
		if (mNBT != null) {
			return mNBT;
		}
		else if (mKey.startsWith( OLD_ITEM)) {
			int id = Utils.parseInteger( mName, 0);
			return String.format( "=COMPOUND( id=SHORT(%d), Damage=SHORT(%d), Count=BYTE(%d) )", id, mDmg, mSize);
		}
		else if (mName != null) {
			return null;
		}
		else {
			return "=COMPOUND(  )";
		}
	}

	public void setNBT( String value) {
		mNBT = value;
	}

	@Override
	public String toString() {
		return String.format( "%s size(%d) dmg(%d)", mName, mSize, mDmg);
	}
}
