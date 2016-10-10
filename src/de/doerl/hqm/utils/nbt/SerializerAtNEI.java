package de.doerl.hqm.utils.nbt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class SerializerAtNEI {
	private static final Logger LOGGER = Logger.getLogger( SerializerAtNEI.class.getName());
	private boolean mCorrect;

	private SerializerAtNEI( boolean correct) {
		mCorrect = correct;
	}

	public static String write( FCompound main, boolean correct) {
		StringBuilder sb = new StringBuilder();
		if (main != null) {
			try {
				write0( main, sb, correct);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return sb.toString();
	}

	private static void write0( FCompound main, StringBuilder sb, boolean correct) throws Exception {
		SerializerAtNEI wrt = new SerializerAtNEI( correct);
		wrt.doCompound( main, sb);
	}

	private void doByte( FLong nbt, StringBuilder sb) {
		sb.append( nbt.asByte());
		sb.append( 'b');
	}

	private void doByteArray( FByteArray arr, StringBuilder sb) {
		sb.append( '[');
		if (mCorrect) {
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
		else {
			sb.append( arr.size());
			sb.append( " bytes");
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
		if (mCorrect) {
			int index = 0;
			for (int i : arr) {
				if (index > 0) {
					sb.append( ',');
				}
				sb.append( i);
				++index;
			}
		}
		else {
			for (int i : arr) {
				sb.append( i);
				sb.append( ',');
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
		sb.append( '"');
		sb.append( nbt.getValue().replace( "\"", "\\\""));
		sb.append( '"');
	}
}
