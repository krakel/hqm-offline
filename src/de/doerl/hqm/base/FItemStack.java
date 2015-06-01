package de.doerl.hqm.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.utils.Nbt;
import de.doerl.hqm.utils.Utils;

public final class FItemStack extends AStack {
	private static final Pattern PATTERN = Pattern.compile( "(.*?) size\\((\\d*)\\) dmg\\((\\d*)\\)"); //  "(.*) size\\((\\d*)\\) dmg\\((\\d*)\\)"
	private Nbt mNBT;
	private String mItem;
	private int mSize;
	private int mDmg;
	private String mKey;

	public FItemStack( Nbt nbt) {
		mNBT = nbt;
		mKey = getName() + "%" + getDamage();
	}

	private FItemStack( Nbt nbt, Matcher mm) {
		mNBT = nbt;
		mm.find();
		int size = mm.groupCount();
		mItem = size > 1 ? mm.group( 1) : "unknown";
		mSize = size > 2 ? Utils.parseInteger( mm.group( 2)) : 0;
		mDmg = size > 3 ? Utils.parseInteger( mm.group( 3)) : 0;
		mKey = getName() + "%" + getDamage();
	}

	public FItemStack( Nbt nbt, String item, int size, int dmg) {
		mNBT = nbt;
		mItem = item;
		mSize = size;
		mDmg = dmg;
		mKey = getName() + "%" + getDamage();
	}

	public static FItemStack parse( String name) {
		if (name != null) {
			return new FItemStack( null, PATTERN.matcher( name));
		}
		else {
			return null;
		}
	}

	public static FItemStack parse( String name, String nbt) {
		if (name != null) {
			return new FItemStack( Nbt.parse( nbt), PATTERN.matcher( name));
		}
		else if (nbt != null) {
			return new FItemStack( Nbt.parse( nbt));
		}
		else {
			return null;
		}
	}

	@Override
	public <T, U> T accept( IStackWorker<T, U> w, U p) {
		return w.forItemStack( this, p);
	}

	@Override
	public int getCount() {
		if (mItem != null) {
			return mSize;
		}
		else if (mNBT != null) {
			return Utils.parseInteger( mNBT.getValue( "Count"), 1);
		}
		else {
			return 1;
		}
	}

	@Override
	public int getDamage() {
		if (mItem != null) {
			return mDmg;
		}
		else if (mNBT != null) {
			return Utils.parseInteger( mNBT.getValue( "Damage"), 0);
		}
		else {
			return 0;
		}
	}

	public String getItem() {
		return mItem;
	}

	@Override
	public String getKey() {
		return mKey;
	}

	@Override
	public String getName() {
		if (mItem != null) {
			return mItem;
		}
		else if (mNBT != null) {
			return mNBT.getValue( "id");
		}
		else {
			return "unknown";
		}
	}

	@Override
	public Nbt getNBT() {
		return mNBT;
	}

	@Override
	public String toString() {
		return String.format( "%s size(%d) dmg(%d)", mItem, mSize, mDmg);
	}
}
