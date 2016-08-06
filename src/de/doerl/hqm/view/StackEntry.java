package de.doerl.hqm.view;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.NbtParser;

class StackEntry {
	public boolean mItem;
	private String mKey;
	private String mName;
	private String mDisplay;
	public int mDmg;
	public FCompound mNbt;
	public int mCount;
	private ItemPrecision mPrecision;

	public StackEntry() {
		mItem = true;
		setName( "name");
		mDisplay = "display";
		mCount = 1;
		mDmg = 0;
		setPrecision( ItemPrecision.PRECISE);
		updateKey();
	}

	public StackEntry( boolean item, AStack stk, int count, ItemPrecision precition) {
		mItem = item;
		mKey = stk.getKey();
		mDisplay = stk.getDisplay();
		setName( stk.getName());
		mDmg = stk.getDamage();
		mNbt = stk.getNBT();
		mCount = count;
		setPrecision( precition);
	}

	public StackEntry( boolean item, String name, int dmg, String area, int count, ItemPrecision precition) {
		mItem = item;
		setName( name);
		mDisplay = name;
		mDmg = dmg;
		mNbt = Utils.validString( area) ? NbtParser.parse( area) : null;
		mCount = count;
		setPrecision( precition);
		updateKey();
	}

	public String getDisplay() {
		return mDisplay;
	}

	public String getDisplayLong() {
		if (Utils.validString( mDisplay)) {
			return mDisplay + " (" + mName + ")";
		}
		else {
			return mName;
		}
	}

	public String getKey() {
		return mKey;
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

	public void updateKey() {
		mKey = mName + '%' + mDmg;
	}
}
