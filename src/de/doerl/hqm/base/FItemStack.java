package de.doerl.hqm.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.doerl.hqm.utils.Utils;

public final class FItemStack extends AStack {
	private static final Pattern PATTERN = Pattern.compile( "(.*?) size\\((\\d*)\\) dmg\\((\\d*)\\)"); //  "(.*) size\\((\\d*)\\) dmg\\((\\d*)\\)"
	private String mItem;
	private int mSize;
	private int mDmg;
	private String mKey;

	public FItemStack( String nbt) {
		super( nbt);
		mKey = getName() + "%" + getDamage();
	}

	private FItemStack( String nbt, Matcher mm) {
		super( nbt);
		mm.find();
		int size = mm.groupCount();
		mItem = size > 1 ? mm.group( 1) : "unknown";
		mSize = size > 2 ? Utils.parseInteger( mm.group( 2)) : 0;
		mDmg = size > 3 ? Utils.parseInteger( mm.group( 3)) : 0;
		mKey = getName() + "%" + getDamage();
	}

	public FItemStack( String nbt, String item, int size, int dmg) {
		super( nbt);
		mItem = item;
		mSize = size;
		mDmg = dmg;
		mKey = getName() + "%" + getDamage();
	}

	public static AStack parse( String name) {
		if (name != null) {
			return new FItemStack( null, PATTERN.matcher( name));
		}
		else {
			return null;
		}
	}

	public static FItemStack parse( String name, String nbt) {
		if (name != null) {
			return new FItemStack( nbt, PATTERN.matcher( name));
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
		if (mItem != null) {
			return mSize;
		}
		else {
			return super.getCount();
		}
	}

	@Override
	public int getDamage() {
		if (mItem != null) {
			return mDmg;
		}
		else {
			return super.getDamage();
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
		else {
			return super.getName();
		}
	}

	@Override
	public String toString() {
		return String.format( "%s size(%d) dmg(%d)", mItem, mSize, mDmg);
	}
}
