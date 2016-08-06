package de.doerl.hqm.utils.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import de.doerl.hqm.utils.Utils;

public class NbtWriter {
	private static final Logger LOGGER = Logger.getLogger( NbtWriter.class.getName());
	private ByteArrayOutputStream mOut = new ByteArrayOutputStream();

	public NbtWriter() {
	}

	private static void compress( InputStream in, OutputStream out) {
		GZIPOutputStream os = null;
		try {
			os = new GZIPOutputStream( out);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read( buffer)) != -1) {
				os.write( buffer, 0, len);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
	}

	public static byte[] write( FCompound main) {
		if (main != null) {
			try {
				byte[] res = write0( main);
				ByteArrayInputStream in = new ByteArrayInputStream( res);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				compress( in, out);
				return out.toByteArray();
			}
			catch (IOException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	static byte[] write0( FCompound main) throws IOException {
		NbtWriter wrt = new NbtWriter();
		return wrt.doAll( main);
	}

	private byte[] doAll( FCompound main) throws IOException {
		writeByte( main.getTag());
		writeKey( main.getName());
		writeCompund( main);
		writeByte( 0);
		return mOut.toByteArray();
	}

	private void doArrByte( FByteArray arr) {
		writeInt( arr.size());
		for (Byte b : arr) {
			writeByte( b);
		}
	}

	private void doArrInt( FIntArray arr) {
		writeInt( arr.size());
		for (Integer i : arr) {
			writeInt( i);
		}
	}

	private void doList( FList lst) throws IOException {
		writeByte( lst.getElement());
		writeInt( lst.size());
		for (ANbt nbt : lst) {
			doValue( nbt);
		}
	}

	private void doValue( ANbt nbt) throws IOException {
		switch (nbt.getTag()) {
			case 1: // Byte
				writeByte( ((FLong) nbt).asByte());
				break;
			case 2: // Short
				writeShort( ((FLong) nbt).asShort());
				break;
			case 3: // Int
				writeInt( ((FLong) nbt).asInt());
				break;
			case 4: // Long
				writeLong( ((FLong) nbt).asLong());
				break;
			case 5: // Float
				writeInt( Float.floatToIntBits( ((FDouble) nbt).asFloat()));
				break;
			case 6: // Double
				writeLong( Double.doubleToLongBits( ((FDouble) nbt).asDouble()));
				break;
			case 7: // Byte-Array
				doArrByte( (FByteArray) nbt);
				break;
			case 8: // String
				writeString( ((FString) nbt).getValue());
				break;
			case 9: // List
				doList( (FList) nbt);
				break;
			case 10: // Compound
				writeCompund( (FCompound) nbt);
				writeByte( 0);
				break;
			case 11: // Int-Array
				doArrInt( (FIntArray) nbt);
				break;
			default:
				throw new IOException( "wrong value tag");
		}
	}

	private void writeByte( int val) {
		mOut.write( val & 0xFF);
	}

	private void writeCompund( FCompound main) throws IOException {
		for (ANbt nbt : main) {
			writeByte( nbt.getTag());
			writeKey( nbt.getName());
			doValue( nbt);
		}
	}

	private void writeInt( int val) {
		writeShort( val >> 16);
		writeShort( val);
	}

	private void writeKey( String val) throws IOException {
		byte[] bb = val.getBytes();
		writeShort( bb.length);
		mOut.write( bb);
	}

	private void writeLong( long val) {
		writeInt( (int) (val >> 32));
		writeInt( (int) val);
	}

	private void writeShort( int val) {
		writeByte( val >> 8);
		writeByte( val);
	}

	private void writeString( String val) throws IOException {
		byte[] bb = val.getBytes();
		int len = bb.length;
		if (len >= 0) {
			writeShort( len);
			mOut.write( bb, 0, len);
		}
		else {
			throw new IOException( "wrong string");
		}
	}
}
