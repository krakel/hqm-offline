package de.doerl.hqm.view;

import de.doerl.hqm.quest.ItemPrecision;

class StackEntry {
	public boolean mItem;
	private String mName;
	public String mIcon;
	public int mCount;
	public int mDmg;
	private ItemPrecision mPrecision;

	public StackEntry() {
		mItem = true;
		setName( "name");
		mCount = 1;
		mDmg = 0;
		setPrecision( ItemPrecision.PRECISE);
	}

	public StackEntry( boolean item, String name, int count, int dmg, ItemPrecision precition) {
		mItem = item;
		setName( name);
		mCount = count;
		mDmg = dmg;
		setPrecision( precition);
	}

	public StackEntry( boolean item, String name, String icon, int count, int dmg, ItemPrecision precition) {
		mItem = item;
		setName( name);
		mIcon = icon;
		mCount = count;
		mDmg = dmg;
		setPrecision( precition);
	}

	public String getName() {
		return mName;
	}

	public ItemPrecision getPrecision() {
		return mPrecision;
	}

	public void setName( String name) {
		if (name.indexOf( ':') < 0) {
			mName = "unknown:" + name;
		}
		else {
			mName = name;
		}
	}

	public void setPrecision( ItemPrecision value) {
		mPrecision = value != null ? value : ItemPrecision.PRECISE;
	}
}
