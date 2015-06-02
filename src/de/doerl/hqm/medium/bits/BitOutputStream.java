package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.OutputStream;

import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.Nbt;
import de.doerl.hqm.utils.Utils;

class BitOutputStream {
	private OutputStream mOutput;
	private FileVersion mVersion = FileVersion.last();
	private int mBuffer;
	private int mBits;

	public BitOutputStream( OutputStream out) {
		mOutput = out;
	}

	public void close() throws IOException {
		if (mOutput != null) {
			mOutput.close();
		}
	}

	public boolean contains( FileVersion other) {
		return mVersion.ordinal() >= other.ordinal();
	}

	public void flush() throws IOException {
		if (mBits > 0) {
			mOutput.write( mBuffer);
			mBits = 0;
		}
	}

	public void setVersion( FileVersion version) {
		mVersion = version;
		writeData( version.ordinal(), DataBitHelper.BYTE);
	}

	public void writeBoolean( boolean data) {
		writeData( data ? 1 : 0, DataBitHelper.BOOLEAN);
	}

	public void writeByte( int data) {
		writeData( data, DataBitHelper.BYTE);
	}

	public void writeData( int data, DataBitHelper bits) {
		writeData( data, bits.getCount());
	}

	private void writeData( int data, int count) {
		data &= (int) ((1L << count) - 1L);
		while (mBits + count >= 8) {
			int used = 8 - mBits;
			int mask = (1 << used) - 1;
			mBuffer |= (data & mask) << mBits;
			try {
				mOutput.write( mBuffer);
			}
			catch (Exception ex) {
			}
			data >>>= used;
			mBuffer = 0;
			mBits = 0;
			count -= used;
		}
		mBuffer |= data << mBits;
		mBits += count;
	}

	public <E extends Enum<E>> void writeEnum( Enum<E> data, E type) {
		E[] values = type.getDeclaringClass().getEnumConstants();
		writeEnum( data, values);
	}

	public <E extends Enum<E>> void writeEnum( Enum<E> data, E[] values) {
		int length = values.length;
		if (length > 0) {
			writeData( data.ordinal(), Integer.bitCount( length) + 1);
		}
	}

	public void writeFluidStack( FFluidStack stk) {
		writeNBT( stk.getNBT());
	}

	public void writeIconIf( FItemStack stk) {
		if (stk != null) {
			writeBoolean( true);
			writeItemStack( stk);
		}
		else {
			writeBoolean( false);
		}
	}

	public void writeIds( int[] ids, DataBitHelper bits) {
		writeData( ids.length, bits);
		for (int i = 0; i < ids.length; ++i) {
			writeData( ids[i], bits);
		}
	}

	public void writeItemStack( FItemStack stk) {
		writeRawItemID( stk.getName());
		writeData( stk.getDamage(), DataBitHelper.SHORT);
		writeNBT( stk.getNBT());
	}

	public void writeItemStackFix( FItemStack stk) {
		if (mVersion.contains( FileVersion.NO_ITEM_IDS_FIX)) {
			writeRawItemID( stk.getName());
			writeData( stk.getCount(), DataBitHelper.SHORT);
			writeData( stk.getDamage(), DataBitHelper.SHORT);
			writeNBT( stk.getNBT());
		}
		else {
			writeNBT( stk.getNBT());
		}
	}

	public void writeNBT( Nbt nbt) {
		byte bytes[] = nbt != null ? nbt.getCompressed() : null;
		if (bytes != null) {
			writeBoolean( true);
			writeData( bytes.length, DataBitHelper.NBT_LENGTH);
			for (int i = 0; i < bytes.length; ++i) {
				writeByte( bytes[i]);
			}
		}
		else {
			writeBoolean( false);
		}
	}

	private void writeRawItemID( String id) {
		if (mVersion.contains( FileVersion.NO_ITEM_IDS)) {
			writeString( id, DataBitHelper.SHORT);
		}
		else {
			writeData( Utils.parseInteger( id), DataBitHelper.SHORT);
		}
	}

	public void writeString( String s, DataBitHelper bits) {
		if (s != null) {
			byte bytes[] = s.getBytes();
			int len = Math.min( bytes.length, bits.getMaximum());
			writeData( len, bits);
			for (int i = 0; i < len; ++i) {
				writeByte( bytes[i]);
			}
		}
		else {
			writeData( 0, bits);
		}
	}
}
