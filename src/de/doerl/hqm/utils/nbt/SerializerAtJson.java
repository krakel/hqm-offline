package de.doerl.hqm.utils.nbt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class SerializerAtJson {
	private static final Logger LOGGER = Logger.getLogger( SerializerAtJson.class.getName());

	private SerializerAtJson() {
	}

	public static String write( FCompound main) {
		StringBuilder sb = new StringBuilder();
		if (main != null) {
			try {
				write0( main, sb);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return sb.toString();
	}

	private static void write0( FCompound main, StringBuilder sb) throws IOException {
		SerializerAtJson wrt = new SerializerAtJson();
		wrt.doAll( main, sb);
	}

	private void doAll( FCompound main, StringBuilder sb) throws IOException {
		sb.append( "=");
		doCompound( main, sb);
	}

	private void doByte( FLong nbt, StringBuilder sb) {
		sb.append( "BYTE(");
		sb.append( nbt.asByte());
		sb.append( ")");
	}

	private void doByteArray( FByteArray nbt, StringBuilder sb) {
		boolean comma = false;
		sb.append( "BYTE-ARRAY(");
		for (Byte i : nbt) {
			if (comma) {
				sb.append( ",");
			}
			sb.append( i);
			comma = true;
		}
		sb.append( ")");
	}

	private void doCompound( FCompound main, StringBuilder sb) throws IOException {
		boolean comma = false;
		sb.append( "COMPOUND(");
		for (ANbt nbt : main.mList) {
			if (comma) {
				sb.append( ',');
			}
			sb.append( nbt.getName());
			sb.append( '=');
			doNBT( nbt, sb);
			comma = true;
		}
		sb.append( ')');
	}

	private void doDouble( FDouble nbt, StringBuilder sb) {
		sb.append( "DOUBLE(");
		sb.append( nbt.asDouble());
		sb.append( ")");
	}

	private void doFloat( FDouble nbt, StringBuilder sb) {
		sb.append( "FLOAT(");
		sb.append( nbt.asFloat());
		sb.append( ")");
	}

	private void doInt( FLong nbt, StringBuilder sb) {
		sb.append( "INT(");
		sb.append( nbt.asInt());
		sb.append( ")");
	}

	private void doIntArray( FIntArray nbt, StringBuilder sb) {
		boolean comma = false;
		sb.append( "INT-ARRAY(");
		for (Integer i : nbt) {
			if (comma) {
				sb.append( ",");
			}
			sb.append( i);
			comma = true;
		}
		sb.append( ")");
	}

	private void doList( FList nbt, StringBuilder sb) throws IOException {
		boolean comma = false;
		sb.append( "LIST(");
		for (ANbt nbt1 : nbt.mList) {
			if (comma) {
				sb.append( ",");
			}
			doNBT( nbt1, sb);
			comma = true;
		}
		sb.append( ")");
	}

	private void doLong( FLong nbt, StringBuilder sb) {
		sb.append( "LONG(");
		sb.append( nbt.asLong());
		sb.append( ")");
	}

	private void doNBT( ANbt nbt, StringBuilder sb) throws IOException {
		switch (nbt.getTag()) {
			case ANbt.ID_BYTE:
				doByte( (FLong) nbt, sb);
				break;
			case ANbt.ID_SHORT:
				doShort( (FLong) nbt, sb);
				break;
			case ANbt.ID_INT:
				doInt( (FLong) nbt, sb);
				break;
			case ANbt.ID_LONG:
				doLong( (FLong) nbt, sb);
				break;
			case ANbt.ID_FLOAT:
				doFloat( (FDouble) nbt, sb);
				break;
			case ANbt.ID_DOUBLE:
				doDouble( (FDouble) nbt, sb);
				break;
			case ANbt.ID_BYTE_ARRAY:
				doByteArray( (FByteArray) nbt, sb);
				break;
			case ANbt.ID_STRING:
				doString( (FString) nbt, sb);
				break;
			case ANbt.ID_LIST:
				doList( (FList) nbt, sb);
				break;
			case ANbt.ID_COMPOUND:
				doCompound( (FCompound) nbt, sb);
				break;
			case ANbt.ID_INT_ARRAY:
				doIntArray( (FIntArray) nbt, sb);
				break;
			default:
				throw new IOException( "wrong value tag");
		}
	}

	private void doShort( FLong nbt, StringBuilder sb) {
		sb.append( "SHORT(");
		sb.append( nbt.asShort());
		sb.append( ")");
	}

	private void doString( FString nbt, StringBuilder sb) {
		sb.append( "STRING('");
		sb.append( nbt.getValue().replace( "'", "\\'"));
		sb.append( "')");
	}
}
