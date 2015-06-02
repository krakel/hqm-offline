package de.doerl.hqm.utils.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import de.doerl.hqm.utils.Utils;

public class NbtWriter {
	private static final Logger LOGGER = Logger.getLogger( NbtWriter.class.getName());
	private Tokenizer mParser;

	public NbtWriter( String src) {
		mParser = new Tokenizer( src);
	}

	private static byte[] compress( byte[] bytes) {
		ByteArrayInputStream src = new ByteArrayInputStream( bytes);
		ByteArrayOutputStream dst = new ByteArrayOutputStream();
		GZIPOutputStream os = null;
		try {
			os = new GZIPOutputStream( dst);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = src.read( buffer)) != -1) {
				os.write( buffer, 0, len);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (IOException ex) {
				}
			}
		}
		return dst.toByteArray();
	}

	public static byte[] write( String src) {
		if (src != null) {
			try {
				NbtWriter wrt = new NbtWriter( src);
				byte[] res = wrt.doAll();
				return compress( res);
			}
			catch (IOException ex) {
			}
		}
		return null;
	}

	private byte[] doAll() throws IOException {
		switch (mParser.nextToken()) {
			case EOF:
				return new byte[0];
			case EQUAL:
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				doCompound( os);
				doEOF();
				return os.toByteArray();
			default:
				throw new IOException( "wrong nbt source");
		}
	}

	private void doArray( ByteArrayOutputStream os, int tag) throws IOException {
		ByteArrayOutputStream inner = new ByteArrayOutputStream();
		int size = 0;
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case LEFT_BRACKET:
					doValue( inner, tag);
					++size;
					loop = doNext();
					break;
				case RIGHT_BRACKET:
					loop = false;
					break;
				default:
					throw new IOException( "wrong byte array");
			}
		}
		while (loop);
		writeInt( os, size);
		os.write( inner.toByteArray());
	}

	private void doArrList( ByteArrayOutputStream os) throws IOException {
		int tag = getTag();
		os.write( tag);
		doArray( os, tag);
	}

	private void doCompound( ByteArrayOutputStream os) throws IOException {
		boolean loop = false;
		do {
			String name = mParser.nextValue();
			writeString( os, name);
			switch (mParser.nextToken()) {
				case LEFT_BRACKET:
					int tag = getTag();
					os.write( tag);
					doValue( os, tag);
					loop = doNext();
					break;
				default:
					throw new IOException( "wrong nbt pair");
			}
		}
		while (loop);
		os.write( 0);
	}

	private void doEOF() throws IOException {
		if (mParser.nextToken() != Token.EOF) {
			throw new IOException( "wrong eof");
		}
	}

	private boolean doNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_BRACKET:
			case EOF:
				return false;
			default:
				throw new IOException( "wrong nbt pair");
		}
	}

	private void doValue( ByteArrayOutputStream os, int tag) throws IOException {
		switch (mParser.nextToken()) {
			case EQUAL:
				doCompound( os);
				break;
			case RIGHT_BRACKET:
				switch (tag) {
					case 1: // Byte
						writeByte( os, Byte.parseByte( mParser.nextValue()));
						break;
					case 2: // Short
						writeShort( os, Short.parseShort( mParser.nextValue()));
						break;
					case 3: // Int
						writeInt( os, Integer.parseInt( mParser.nextValue()));
						break;
					case 4: // Long
						writeLong( os, Long.parseLong( mParser.nextValue()));
						break;
					case 5: // Float
						writeInt( os, Float.floatToIntBits( Float.parseFloat( mParser.nextValue())));
						break;
					case 6: // Double
						writeLong( os, Double.doubleToLongBits( Double.parseDouble( mParser.nextValue())));
						break;
					case 7: // Byte-Array
						doArray( os, 1);
						break;
					case 8: // String
						writeString( os, mParser.nextValue());
						break;
					case 9: // List
						doArrList( os);
						break;
					case 10: // Compound
						break;
					case 11: // Int-Array
						doArray( os, 3);
						break;
					default:
						throw new IOException( "wrong nbt tag");
				}
				break;
			default:
				throw new IOException( "wrong nbt pair");
		}
	}

	private int getTag() throws IOException {
		String key = mParser.nextValue();
		if ("END".equals( key)) {
			return 0;
		}
		if ("BYTE".equals( key)) {
			return 1;
		}
		if ("SHORT".equals( key)) {
			return 2;
		}
		if ("INT".equals( key)) {
			return 3;
		}
		if ("LONG".equals( key)) {
			return 4;
		}
		if ("FLOAT".equals( key)) {
			return 5;
		}
		if ("DOUBLE".equals( key)) {
			return 6;
		}
		if ("BYTE-ARRAY".equals( key)) {
			return 7;
		}
		if ("STRING".equals( key)) {
			return 8;
		}
		if ("LIST".equals( key)) {
			return 9;
		}
		if ("COMPOUND".equals( key)) {
			return 10;
		}
		if ("INT-ARRAY".equals( key)) {
			return 11;
		}
		throw new IOException( "wrong nbt type");
	}

	private void writeByte( ByteArrayOutputStream os, int val) {
		os.write( val & 0xFF);
	}

	private void writeInt( ByteArrayOutputStream os, int val) {
		writeShort( os, val >> 16);
		writeShort( os, val);
	}

	private void writeLong( ByteArrayOutputStream os, long val) {
		writeInt( os, (int) (val >> 32));
		writeInt( os, (int) val);
	}

	private void writeShort( ByteArrayOutputStream os, int val) {
		writeByte( os, val >> 8);
		writeByte( os, val);
	}

	private void writeString( ByteArrayOutputStream os, String val) throws IOException {
		int len = val.length();
		writeShort( os, len);
		os.write( val.getBytes());
	}
}
