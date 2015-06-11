package de.doerl.hqm.view;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.quest.ItemPrecision;

class StackEntry {
	public boolean mItem;
	private String mKey;
	public int mDmg;
	public String mNbt;
	public int mCount;
	private ItemPrecision mPrecision;

	public StackEntry() {
		mItem = true;
		setKey( "name");
		mCount = 1;
		mDmg = 0;
		setPrecision( ItemPrecision.PRECISE);
	}

	public StackEntry( boolean item, AStack stk, int count, ItemPrecision precition) {
		mItem = item;
		setKey( stk.getName());
		mDmg = stk.getDamage();
		mNbt = stk.getNBT();
		mCount = count;
		setPrecision( precition);
	}

	public StackEntry( boolean item, String name, int dmg, int count, ItemPrecision precition) {
		mItem = item;
		setKey( name);
		mDmg = dmg;
		mCount = count;
		setPrecision( precition);
	}

	public String getKey() {
		return mKey;
	}

	public ItemPrecision getPrecision() {
		return mPrecision;
	}

	public void setKey( String name) {
		if (name.indexOf( ':') < 0) {
			mKey = "unknown:" + name;
		}
		else {
			mKey = name;
		}
	}

	public void setPrecision( ItemPrecision value) {
		mPrecision = value != null ? value : ItemPrecision.PRECISE;
	}
}
