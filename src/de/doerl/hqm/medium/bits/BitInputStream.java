package de.doerl.hqm.medium.bits;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.NbtReader;

class BitInputStream {
	private static final Charset UTF_8 = Charset.forName( "UTF-8");
	private InputStream mInput;
	private int mBuffer;
	private int mBits;

	public BitInputStream( InputStream is) {
		mInput = is;
	}

	public int read() throws IOException {
		return mInput.read();
	}

	public boolean readBoolean() {
		return readData( DataBitHelper.BOOLEAN.getCount()) != 0;
	}

	public int readByte() {
		return readData( DataBitHelper.BYTE.getCount());
	}

	public int readData( DataBitHelper bits) {
		return readData( bits.getCount());
	}

	public int readData( DataBitHelper bits, FileVersion version) {
		return readData( bits.getBitCount( version));
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

	public FFluidStack readFluidStack() {
		if (readBoolean()) {
			return new FFluidStack( readNBT());
		}
		else {
			return null;
		}
	}

	public FItemStack readIconIf( FileVersion version) {
		if (readBoolean()) {
			return readItemStack( version);
		}
		else {
			return null;
		}
	}

	public int[] readIds( DataBitHelper bits, FileVersion version) {
		int count = readData( bits, version);
		int[] result = new int[count];
		for (int i = 0; i < count; ++i) {
			result[i] = readData( bits, version);
		}
		return result;
	}

	public FItemStack readItemStack( FileVersion version) {
		return readItemStackDef( false, version);
	}

	private FItemStack readItemStackDef( boolean withSize, FileVersion version) {
		if (version.contains( FileVersion.NO_ITEM_IDS)) {
			return readItemStackName( withSize, version);
		}
		else {
			return readItemStackID( withSize);
		}
	}

	public FItemStack readItemStackFix( FileVersion version) {
		if (version.contains( FileVersion.NO_ITEM_IDS_FIX)) {
			return readItemStackDef( true, version);
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
			return new FItemStack( readNBT(), id, dmg, size);
		}
		else {
			return new FItemStack( id, dmg, size);
		}
	}

	private FItemStack readItemStackName( boolean withSize, FileVersion version) {
		String name = readString( DataBitHelper.SHORT);
		int size = withSize ? readData( DataBitHelper.SHORT) : 1;
		int dmg = readData( DataBitHelper.SHORT);
		if (readBoolean()) {
			return new FItemStack( readNBT(), name, dmg, size);
		}
		else {
			return new FItemStack( name, dmg, size);
		}
	}

	private FCompound readNBT() {
		byte bytes[] = new byte[readData( DataBitHelper.NBT_LENGTH)];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) readByte();
		}
		ByteArrayInputStream in = new ByteArrayInputStream( bytes);
		return NbtReader.readAsCompound( in);
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

	public String readString( DataBitHelper bits, FileVersion version) {
		int length = readData( bits, version);
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
