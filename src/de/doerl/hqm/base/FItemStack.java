package de.doerl.hqm.base;

import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.ItemNEI;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.FLong;
import de.doerl.hqm.utils.nbt.SerializerAtJson;

public final class FItemStack extends AStack {
	private String mKey;
	private ItemNEI mItem;
	private int mStackSize;
	private int mDmg;
	private FCompound mNBT;

	private FItemStack( int id, int dmg, int size, FCompound nbt) {
		mNBT = nbt;
		mStackSize = size;
		mDmg = dmg;
		mKey = "id:" + id + "%" + dmg;
		mItem = ImageLoader.get( mKey, null);
	}

	public FItemStack( String name, int dmg, int size, FCompound nbt) {
		mNBT = nbt;
		mStackSize = size;
		mDmg = dmg;
		mKey = name + "%" + dmg;
		mItem = ImageLoader.get( mKey, nbt);
	}

	public static FItemStack applyOld( FCompound nbt) {
		if (nbt != null) {
			int id = FLong.toInt( nbt.get( "id"), 0);
			int size = FLong.toInt( nbt.get( "Count"), 1);
			int dmg = FLong.toInt( nbt.get( "Damage"), 0);
			return new FItemStack( id, dmg, size, nbt);
		}
		return new FItemStack( 0, 0, 1, null);
	}

	public static FItemStack applyOld( int id, int dmg, int size, FCompound nbt) {
		return new FItemStack( id, dmg, size, nbt);
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
		return SerializerAtJson.write( mNBT);
	}

	@Override
	public int getStackSize() {
		return mStackSize;
	}

	boolean isOldItem() {
		return mKey.startsWith( "id:");
	}

	public void setStackSize( int value) {
		mStackSize = value;
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "key", mKey);
		sb.appendMsg( "dmg", mDmg);
		sb.appendMsg( "size", mStackSize);
		if (mNBT != null) {
			sb.appendMsg( "nbt", mNBT);
		}
		return sb.toString();
	}
}
