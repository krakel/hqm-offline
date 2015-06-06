package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.nbt.NbtReader;

class BitInputStream {
	private static final Charset UTF_8 = Charset.forName( "UTF-8");
	public final FileVersion mVersion;
	private InputStream mInput;
	private int mBuffer;
	private int mBits;

	public BitInputStream( InputStream is) {
		mInput = is;
		mVersion = FileVersion.get( readByte());
	}

	public boolean contains( FileVersion other) {
		return mVersion.ordinal() >= other.ordinal();
	}

	public int read() throws IOException {
		return mInput.read();
	}

	public boolean readBoolean() {
		return readData( DataBitHelper.BOOLEAN) != 0;
	}

	public int readByte() {
		return readData( DataBitHelper.BYTE);
	}

	public int readData( DataBitHelper bits) {
		return readData( bits.getBitCount( mVersion));
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

	public FFluidStack readFluidStack() {
		if (readBoolean()) {
			return new FFluidStack( readNBT());
		}
		else {
			return null;
		}
	}

	public FItemStack readIconIf() {
		if (readBoolean()) {
			return readItemStack();
		}
		else {
			return null;
		}
	}

	public int[] readIds( DataBitHelper bits) {
		int count = readData( bits);
		int[] result = new int[count];
		for (int i = 0; i < count; ++i) {
			result[i] = readData( bits);
		}
		return result;
	}

	public FItemStack readItemStack() {
		return readItemStackDef( false);
	}

	private FItemStack readItemStackDef( boolean withSize) {
		if (mVersion.contains( FileVersion.NO_ITEM_IDS)) {
			return readItemStackName( withSize);
		}
		else {
			return readItemStackID( withSize);
		}
	}

	public FItemStack readItemStackFix() {
		if (mVersion.contains( FileVersion.NO_ITEM_IDS_FIX)) {
			return readItemStackDef( true);
		}
		else {
			if (readBoolean()) {
				return new FItemStack( readNBT());
			}
			else {
				return null;
			}
		}
	}

	private FItemStack readItemStackID( boolean withSize) {
		int id = readData( DataBitHelper.SHORT);
		int size = withSize ? readData( DataBitHelper.SHORT) : 1;
		int dmg = readData( DataBitHelper.SHORT);
		if (readBoolean()) {
			return new FItemStack( readNBT(), id, size, dmg);
		}
		else {
			return new FItemStack( id, size, dmg);
		}
	}

	private FItemStack readItemStackName( boolean withSize) {
		String name = readString( DataBitHelper.SHORT);
		int size = withSize ? readData( DataBitHelper.SHORT) : 1;
		int dmg = readData( DataBitHelper.SHORT);
		if (readBoolean()) {
			return new FItemStack( readNBT(), name, size, dmg);
		}
		else {
			return new FItemStack( name, size, dmg);
		}
	}

	private String readNBT() {
		byte bytes[] = new byte[readData( DataBitHelper.NBT_LENGTH)];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) readByte();
		}
		return NbtReader.read( bytes);
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
