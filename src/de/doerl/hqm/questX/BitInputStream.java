package de.doerl.hqm.questX;

import java.io.IOException;
import java.io.InputStream;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.questX.minecraft.Item;
import de.doerl.hqm.questX.minecraft.ItemStack;
import de.doerl.hqm.questX.minecraft.NBTTagCompound;

public class BitInputStream extends InputStream {
	private InputStream mInput;
	public final FileVersion mVersion;
	private int mBuffer;
	private int mBits;

	public BitInputStream( InputStream is) {
		mInput = is;
		mVersion = FileVersion.get( readByte());
	}

	@Override
	public void close() throws IOException {
		if (mInput != null) {
			mInput.close();
		}
	}

	public boolean contains( FileVersion other) {
		return mVersion.ordinal() >= other.ordinal();
	}

	@Override
	public int read() throws IOException {
		return mInput.read();
	}

	public ItemStack readAndFixItemStack( boolean useSize) {
		return readRawItemStack( false, useSize);
	}

	public boolean readBoolean() {
		return readData( DataBitHelper.BOOLEAN) != 0;
	}

	public int readByte() {
		return readData( DataBitHelper.BYTE);
	}

	public int readData( DataBitHelper bitCount) {
		return readData( bitCount.getBitCount( mVersion));
	}

	public int readData( int count) {
		int result = 0;
		int used = 0;
		do {
			int need = count - used;
			if (mBits >= need) {
				int mask = (1 << need) - 1;
				result |= (mBuffer & mask) << used;
				mBuffer >>>= need;
				mBits -= need;
				used += need;
				break;
			}
			result |= mBuffer << used;
			used += mBits;
			try {
				mBuffer = mInput.read();
			}
			catch (IOException ex) {
				mBuffer = 0;
			}
			mBits = 8;
		}
		while (true);
		return result;
	}

	public Enum<?> readEnum( Class<?> clazz) {
		try {
			Object[] values = (Object[]) clazz.getMethod( "values", new Class[0]).invoke( null, new Object[0]);
			int length = values.length;
			if (length == 0) {
				return null;
			}
			int bitCount = (int) (Math.log10( length) / Math.log10( 2D)) + 1;
			int val = readData( bitCount);
			return (Enum<?>) values[val];
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ItemStack readIcon( boolean useSize) {
		return readRawItemStack( true, useSize);
	}

	public int[] readIds( DataBitHelper size) {
		int count = readData( size);
		int[] result = new int[count];
		for (int i = 0; i < result.length; ++i) {
			result[i] = readData( size);
		}
		return result;
	}

	public Item readItem() {
		if (mVersion.contains( FileVersion.NO_ITEM_IDS)) {
			final String name = readString( DataBitHelper.SHORT);
			return new Item( name);
		}
		int val = readData( DataBitHelper.SHORT);
		return new Item( val);
	}

	public ItemStack readItemStack( boolean useSize) {
		return readRawItemStack( true, useSize);
	}

	public NBTTagCompound readNBT() {
		if (readBoolean()) {
			byte bytes[] = new byte[readData( DataBitHelper.NBT_LENGTH)];
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte) readByte();
			}
			try {
//				return CompressedStreamTools.func_152457_a( bytes, new NBTSizeTracker( 0x1fd8f0L));
				return new NBTTagCompound( bytes);
			}
			catch (IOException ex) {
				return null;
			}
		}
		else {
			return null;
		}
	}

	public ItemStack readRawItemStack( boolean withID, boolean useSize) {
		if (withID || mVersion.contains( FileVersion.NO_ITEM_IDS_FIX)) {
			Item item = readItem();
			int size = useSize ? readData( DataBitHelper.SHORT) : 1;
			int dmg = readData( DataBitHelper.SHORT);
			NBTTagCompound nbt = readNBT();
			return new ItemStack( item, size, dmg, nbt);
		}
		else {
			NBTTagCompound compound = readNBT();
			if (compound != null) {
//				return ItemStack.func_77949_a( compound);
				return new ItemStack( compound);
			}
			else {
				return null;
			}
		}
	}

	public ItemStack[] readRewardData() {
		if (readBoolean()) {
			int count = readData( DataBitHelper.REWARDS);
			ItemStack[] result = new ItemStack[count];
			for (int i = 0; i < result.length; i++) {
				result[i] = readAndFixItemStack( true);
			}
			return result;
		}
		else {
			return null;
		}
	}

	public String readString( DataBitHelper bits) {
		int length = readData( bits);
		if (length == 0) {
			return null;
		}
		byte bytes[] = new byte[length];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) readByte();
		}
		return new String( bytes);
	}

	void testFkt() {
		for (int i = 0; i < 20; ++i) {
			readBoolean();
		}
		@SuppressWarnings( "unused")
		String test = readString( DataBitHelper.QUEST_NAME_LENGTH);
	}
}
