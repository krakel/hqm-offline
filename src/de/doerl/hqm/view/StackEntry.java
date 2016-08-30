package de.doerl.hqm.view;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.NbtParser;

class StackEntry {
	public boolean mIsItem;
	private String mKey;
	private String mName;
	private String mDisplay;
	public int mDmg;
	public FCompound mNbt;
	public int mCount;
	private ItemPrecision mPrecision;

	public StackEntry() {
		mIsItem = true;
		setName( "name");
		mDisplay = "display";
		mCount = 1;
		mDmg = 0;
		setPrecision( ItemPrecision.PRECISE);
		updateKey();
	}

	public StackEntry( boolean isItem, FCompound nbt, AStack stk, int count, ItemPrecision precition) {
		mIsItem = isItem;
		mKey = stk.getKey();
		mDisplay = stk.getDisplay();
		setName( stk.getName());
		mDmg = stk.getDamage();
		mNbt = nbt;
		mCount = count;
		setPrecision( precition);
	}

	public StackEntry( boolean isItem, String name, int dmg, String area, int count, ItemPrecision precition) {
		mIsItem = isItem;
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

	public String getNbtStr() {
		if (mNbt != null) {
			return mNbt.toString();
		}
		else {
			return "";
		}
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
