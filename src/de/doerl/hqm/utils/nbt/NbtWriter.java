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
	private Tokenizer mParser;

	public NbtWriter( String src) {
		mParser = new Tokenizer( src);
	}

	static void compress( InputStream in, OutputStream out) {
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

	public static byte[] write( String src) {
		if (src != null) {
			try {
				byte[] res = write0( src);
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

	static byte[] write0( String src) throws IOException {
		NbtWriter wrt = new NbtWriter( src);
		return wrt.doAll();
	}

	private byte[] doAll() throws IOException {
		switch (mParser.nextToken()) {
			case EOF:
				return new byte[0];
			case EQUAL:
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				doPair( os);
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
				case COMMA:
					doValue( inner, tag, mParser.nextValue());
					++size;
					loop = true;
					break;
				case RIGHT_BRACKET:
					String val = mParser.nextValue();
					if (!"".equals( val)) {
						doValue( inner, tag, val);
						++size;
					}
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

	private void doCompound( ByteArrayOutputStream os) throws IOException {
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case EQUAL:
					doPair( os);
					loop = doNext();
					break;
				case RIGHT_BRACKET:
					loop = false;
					break;
				default:
					throw new IOException( "wrong start pair");
			}
		}
		while (loop);
	}

	private void doEOF() throws IOException {
		if (mParser.nextToken() != Token.EOF) {
			throw new IOException( "wrong eof");
		}
	}

	private void doList( ByteArrayOutputStream os) throws IOException {
		ByteArrayOutputStream inner = new ByteArrayOutputStream();
		int size = 0;
		int tag = 0;
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case LEFT_BRACKET:
					tag = getTag();
					switch (tag) {
						case 7: // Byte-Array
							doArray( inner, 1);
							break;
						case 9: // List
							doList( inner);
							break;
						case 10: // Compound
							doCompound( inner);
							writeByte( inner, 0);
							break;
						case 11: // Int-Array
							doArray( inner, 3);
							break;
						default:
							doValueEnd( inner, tag);
					}
					++size;
					loop = doNext(); // COMMA
					break;
				case RIGHT_BRACKET:
					loop = false;
					break;
				default:
					throw new IOException( "wrong byte array");
			}
		}
		while (loop);
		writeByte( os, tag);
		writeInt( os, size);
		os.write( inner.toByteArray());
	}

	private boolean doNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_BRACKET:
			case EOF:
				return false;
			default:
				throw new IOException( "wrong end pair");
		}
	}

	private void doPair( ByteArrayOutputStream os) throws IOException {
		String name = mParser.nextValue();
		int tag = doValueBegin();
		writeByte( os, tag);
		writeKey( os, name);
		switch (tag) {
			case 7: // Byte-Array
				doArray( os, 1);
				break;
			case 9: // List
				doList( os);
				break;
			case 10: // Compound
				doCompound( os);
				writeByte( os, 0);
				break;
			case 11: // Int-Array
				doArray( os, 3);
				break;
			default:
				doValueEnd( os, tag);
		}
	}

	private void doValue( ByteArrayOutputStream os, int tag, String val) throws IOException {
		switch (tag) {
			case 1: // Byte
				writeByte( os, Byte.parseByte( val));
				break;
			case 2: // Short
				writeShort( os, Short.parseShort( val));
				break;
			case 3: // Int
				writeInt( os, Integer.parseInt( val));
				break;
			case 4: // Long
				writeLong( os, Long.parseLong( val));
				break;
			case 5: // Float
				writeInt( os, Float.floatToIntBits( Float.parseFloat( val)));
				break;
			case 6: // Double
				writeLong( os, Double.doubleToLongBits( Double.parseDouble( val)));
				break;
			case 8: // String
				writeString( os, val);
				break;
			default:
				throw new IOException( "wrong value tag");
		}
	}

	private int doValueBegin() throws IOException {
		if (mParser.nextToken() == Token.LEFT_BRACKET) {
			return getTag();
		}
		else {
			throw new IOException( "wrong start value");
		}
	}

	private void doValueEnd( ByteArrayOutputStream os, int tag) throws IOException {
		if (mParser.nextToken() == Token.RIGHT_BRACKET) {
			doValue( os, tag, mParser.nextValue());
		}
		else {
			throw new IOException( "wrong end value");
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

	private void writeKey( ByteArrayOutputStream os, String val) throws IOException {
		byte[] bb = val.getBytes();
		writeShort( os, bb.length);
		os.write( bb);
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
		byte[] bb = val.getBytes();
		int len = bb.length - 2;
		if (len >= 0) {
			writeShort( os, len);
			os.write( bb, 1, len);
		}
		else {
			throw new IOException( "wrong string");
		}
	}
}
