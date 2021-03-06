package de.doerl.hqm.utils.nbt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class SerializerAtNEI {
	private static final Logger LOGGER = Logger.getLogger( SerializerAtNEI.class.getName());
	private int mKind;

	private SerializerAtNEI( int kind) {
		mKind = kind;
	}

	private static void write0( FCompound main, StringBuilder sb, int kind) throws Exception {
		SerializerAtNEI wrt = new SerializerAtNEI( kind);
		wrt.doCompound( main, sb);
	}

	public static String writeDbl( FCompound main) {
		StringBuilder sb = new StringBuilder();
		if (main != null) {
			try {
				write0( main, sb, 1);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return sb.toString();
	}

	public static String writeSng( FCompound main) {
		StringBuilder sb = new StringBuilder();
		if (main != null) {
			try {
				write0( main, sb, 2);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return sb.toString();
	}

	public static String writeWrong( FCompound main) {
		StringBuilder sb = new StringBuilder();
		if (main != null) {
			try {
				write0( main, sb, 0);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return sb.toString();
	}

	private void doByte( FLong nbt, StringBuilder sb) {
		sb.append( nbt.asByte());
		sb.append( 'b');
	}

	private void doByteArray( FByteArray arr, StringBuilder sb) {
		sb.append( '[');
		if (mKind == 0) {
			sb.append( arr.size());
			sb.append( " bytes");
		}
		else {
			int index = 0;
			for (byte i : arr) {
				if (index > 0) {
					sb.append( ',');
				}
				sb.append( i);
				sb.append( 'b');
				++index;
			}
		}
		sb.append( ']');
	}

	private void doCompound( FCompound main, StringBuilder sb) throws Exception {
		boolean comma = false;
		sb.append( '{');
		for (ANbt nbt : main) {
			if (comma) {
				sb.append( ',');
			}
			sb.append( nbt.getName());
			sb.append( ':');
			doNBT( nbt, sb);
			comma = true;
		}
		sb.append( '}');
	}

	private void doDouble( FDouble nbt, StringBuilder sb) {
		sb.append( nbt.asDouble());
		sb.append( 'd');
	}

	private void doFloat( FDouble nbt, StringBuilder sb) {
		sb.append( nbt.asFloat());
		sb.append( 'f');
	}

	private void doInt( FLong nbt, StringBuilder sb) {
		sb.append( nbt.asInt());
	}

	private void doIntArray( FIntArray arr, StringBuilder sb) {
		sb.append( '[');
		if (mKind == 0) {
			for (int i : arr) {
				sb.append( i);
				sb.append( ',');
			}
		}
		else {
			int index = 0;
			for (int i : arr) {
				if (index > 0) {
					sb.append( ',');
				}
				sb.append( i);
				++index;
			}
		}
		sb.append( ']');
	}

	private void doList( FList lst, StringBuilder sb) throws Exception {
		int index = 0;
		sb.append( '[');
		for (ANbt nbt : lst) {
			if (index > 0) {
				sb.append( ',');
			}
			sb.append( index);
			sb.append( ':');
			doNBT( nbt, sb);
			++index;
		}
		sb.append( ']');
	}

	private void doLong( FLong nbt, StringBuilder sb) {
		sb.append( nbt.asLong());
		sb.append( 'l');
	}

	private void doNBT( ANbt nbt, StringBuilder sb) throws Exception {
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
		sb.append( nbt.asShort());
		sb.append( 's');
	}

	private void doString( FString nbt, StringBuilder sb) {
		String value = nbt.getValue();
		switch (mKind) {
			case 0:
				doStringDbl( value, sb);
				break;
			case 1:
				if (value.contains( "\"")) {
					doStringSng( sb, value);
				}
				else {
					doStringDbl( value, sb);
				}
				break;
			case 2:
				if (value.contains( "'")) {
					doStringDbl( value, sb);
				}
				else {
					doStringSng( sb, value);
				}
				break;
		}
	}

	private void doStringDbl( String value, StringBuilder sb) {
		sb.append( '"');
		sb.append( value.replace( "\"", "\\\""));
		sb.append( '"');
	}

	private void doStringSng( StringBuilder sb, String value) {
		sb.append( '\'');
		sb.append( value.replace( "'", "\\'"));
		sb.append( '\'');
	}
}
