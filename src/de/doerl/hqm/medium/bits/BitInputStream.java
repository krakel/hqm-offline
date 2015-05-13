package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.medium.FNbt;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;

class BitInputStream extends InputStream {
	private static final Charset UTF_8 = Charset.forName( "UTF-8");
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

	public boolean readBoolean() {
		return readData( DataBitHelper.BOOLEAN) != 0;
	}

	public int readByte() {
		return readData( DataBitHelper.BYTE);
	}

	public int readData( DataBitHelper bitCount) {
		return readData( bitCount.getBitCount( mVersion));
	}

	private int readData( int count) {
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

	public FItemStack readFixedItemStack( boolean useSize) {
		if (mVersion.contains( FileVersion.NO_ITEM_IDS_FIX)) {
			return readRawItem( useSize);
		}
		else {
			FNbt nbt = readNBT();
			return nbt != null ? new FItemStack( nbt) : null;
		}
	}

	public FFluidStack readFluidStack() {
		FNbt nbt = readNBT();
		return nbt != null ? new FFluidStack( nbt) : null;
	}

	public FItemStack readIconIf() {
		return readBoolean() ? readRawItem( false) : null;
	}

	public int[] readIds( DataBitHelper size) {
		int count = readData( size);
		int[] result = new int[count];
		for (int i = 0; i < result.length; ++i) {
			result[i] = readData( size);
		}
		return result;
	}

	public FItemStack readItemStack() {
		return readRawItem( false);
	}

	private FNbt readNBT() {
		if (readBoolean()) {
			byte bytes[] = new byte[readData( DataBitHelper.NBT_LENGTH)];
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte) readByte();
			}
			try {
//				return CompressedStreamTools.func_152457_a( bytes, new NBTSizeTracker( 0x1fd8f0L));
				return new FNbt( bytes);
			}
			catch (IOException ex) {
				return null;
			}
		}
		else {
			return null;
		}
	}

	private FItemStack readRawItem( boolean useSize) {
		String id = readRawItemID();
		int size = useSize ? readData( DataBitHelper.SHORT) : 1;
		int dmg = readData( DataBitHelper.SHORT);
		FNbt nbt = readNBT();
		return new FItemStack( nbt, id, size, dmg);
	}

	private String readRawItemID() {
		if (mVersion.contains( FileVersion.NO_ITEM_IDS)) {
			return readString( DataBitHelper.SHORT);
		}
		else {
			return Integer.toString( readData( DataBitHelper.SHORT));
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
		return new String( bytes, UTF_8);
	}
}
