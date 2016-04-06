package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.OutputStream;

import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.NbtWriter;

class BitOutputStream {
	private OutputStream mOutput;
	private int mBuffer;
	private int mBits;

	public BitOutputStream( OutputStream out) {
		mOutput = out;
	}

	public void flush() throws IOException {
		if (mBits > 0) {
			mOutput.write( mBuffer);
			mBits = 0;
		}
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

	public void writeData( int data, DataBitHelper bits, FileVersion version) {
		writeData( data, bits.getBitCount( version));
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

	public void writeFluidStack( FFluidStack stk) {
		writeNBT( NbtWriter.write( stk.getNBT()));
	}

	public void writeIconIf( FItemStack stk, FileVersion version) {
		if (stk != null) {
			writeBoolean( true);
			writeItemStackDef( stk, false, version);
		}
		else {
			writeBoolean( false);
		}
	}

	public void writeIds( int[] ids, DataBitHelper bits, FileVersion version) {
		writeData( ids.length, bits, version);
		for (int i = 0; i < ids.length; ++i) {
			writeData( ids[i], bits, version);
		}
	}

	public void writeItemStack( FItemStack stk, FileVersion version) {
		writeItemStackDef( stk, version);
	}

	private void writeItemStackDef( FItemStack stk, boolean withSize, FileVersion version) {
		if (version.contains( FileVersion.NO_ITEM_IDS)) {
			writeItemStackName( stk, withSize);
		}
		else {
			writeItemStackID( stk, withSize);
		}
	}

	private void writeItemStackDef( FItemStack stk, FileVersion version) {
		writeItemStackDef( stk, false, version);
	}

	public void writeItemStackFix( FItemStack stk, FileVersion version) {
		if (version.contains( FileVersion.NO_ITEM_IDS_FIX)) {
			writeItemStackDef( stk, true, version);
		}
		else {
			writeNBT( NbtWriter.write( stk.getNBT()));
		}
	}

	private void writeItemStackID( FItemStack stk, boolean withSize) {
		writeData( Utils.parseInteger( stk.getName()), DataBitHelper.SHORT);
		if (withSize) {
			writeData( stk.getCount(), DataBitHelper.SHORT);
		}
		writeData( stk.getDamage(), DataBitHelper.SHORT);
		writeNBT( NbtWriter.write( stk.getNBT()));
	}

	private void writeItemStackName( FItemStack stk, boolean withSize) {
		writeString( stk.getName(), DataBitHelper.SHORT);
		if (withSize) {
			writeData( stk.getCount(), DataBitHelper.SHORT);
		}
		writeData( stk.getDamage(), DataBitHelper.SHORT);
		writeNBT( NbtWriter.write( stk.getNBT()));
	}

	public void writeNBT( byte[] nbt) {
		if (nbt != null) {
			writeBoolean( true);
			writeData( nbt.length, DataBitHelper.NBT_LENGTH);
			for (int i = 0; i < nbt.length; ++i) {
				writeByte( nbt[i]);
			}
		}
		else {
			writeBoolean( false);
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

	public void writeString( String s, DataBitHelper bits, FileVersion version) {
		if (s != null) {
			byte bytes[] = s.getBytes();
			int len = Math.min( bytes.length, bits.getMaximum());
			writeData( len, bits, version);
			for (int i = 0; i < len; ++i) {
				writeByte( bytes[i]);
			}
		}
		else {
			writeData( 0, bits);
		}
	}
}
