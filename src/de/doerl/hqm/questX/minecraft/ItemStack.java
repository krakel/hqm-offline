package de.doerl.hqm.questX.minecraft;

import de.doerl.hqm.questX.AWriter;
import de.doerl.hqm.questX.IWriter;

public class ItemStack implements IWriter {
	Item mItem;
	int mSize;
	int mDmg;
	NBTTagCompound mNBT;

	public ItemStack( Item item, int size, int dmg) {
		mItem = item;
		mSize = size;
		mDmg = dmg;
	}

	public ItemStack( Item item, int size, int dmg, NBTTagCompound nbt) {
		mItem = item;
		mSize = size;
		mDmg = dmg;
		mNBT = nbt;
	}

	public ItemStack( NBTTagCompound nbt) {
		mNBT = nbt;
	}

	public void apply( NBTTagCompound compound) {
		mNBT = compound;
	}

	public NBTTagCompound getCompound() {
		return mNBT;
	}

	public int getDmg() {
		return mDmg;
	}

	public Item getItem() {
		return mItem;
	}

	public int getSize() {
		return mSize;
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "item", mItem);
		out.print( "size", mSize);
		out.print( "dmg", mDmg);
		if (mNBT != null) {
			out.print( "nbt", mNBT);
		}
		out.endObject();
	}
}
