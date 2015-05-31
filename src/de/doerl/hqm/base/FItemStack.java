package de.doerl.hqm.base;

import java.util.regex.Pattern;

import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.utils.Nbt;
import de.doerl.hqm.utils.Utils;

public final class FItemStack extends AStack {
	private static final Pattern PATTERN = Pattern.compile( "(.*) size\\((\\d*)\\) dmg\\((\\d*)\\)");
	private Nbt mNBT;
	private String mItem;
	private int mSize;
	private int mDmg;
	private String mKey;

	public FItemStack( Nbt nbt) {
		mNBT = nbt;
		mKey = getName() + "%" + getDamage();
	}

	public FItemStack( Nbt nbt, String item, int size, int dmg) {
		mNBT = nbt;
		mItem = item;
		mSize = size;
		mDmg = dmg;
		mKey = getName() + "%" + getDamage();
	}

	private FItemStack( Nbt nbt, String[] vals) {
		mNBT = nbt;
		mItem = vals.length > 0 ? vals[0] : "unknown";
		mSize = vals.length > 1 ? Utils.parseInteger( vals[1]) : 0;
		mDmg = vals.length > 2 ? Utils.parseInteger( vals[2]) : 0;
		mKey = getName() + "%" + getDamage();
	}

	public static FItemStack parse( String name) {
		return new FItemStack( null, PATTERN.split( name));
	}

	public static FItemStack parse( String name, String nbt) {
		return new FItemStack( Nbt.parse( nbt), PATTERN.split( name));
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
