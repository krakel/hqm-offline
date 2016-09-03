package de.doerl.hqm.utils.nbt;

public class SerializerAtJson {
//	private static final Logger LOGGER = Logger.getLogger( SerializerAtJson.class.getName());
	private SerializerAtJson() {
	}

	public static String write( FCompound main) {
		StringBuilder sb = new StringBuilder();
		if (main != null) {
			write0( main, sb);
		}
		return sb.toString();
	}

	private static void write0( FCompound main, StringBuilder sb) {
		SerializerAtJson wrt = new SerializerAtJson();
		wrt.doAll( main, sb);
	}

	private void doAll( FCompound main, StringBuilder sb) {
		sb.append( "=");
		main.toString( sb);
	}
}
