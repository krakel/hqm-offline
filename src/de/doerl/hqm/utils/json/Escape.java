package de.doerl.hqm.utils.json;

class Escape {
	private Escape() {
	}

	static String escape( String s) {
		StringBuffer sb = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; ++i) {
			char c = s.charAt( i);
			switch (c) {
				case '\b':
					sb.append( "\\b");
					break;
				case '\f':
					sb.append( "\\f");
					break;
				case '\n':
					sb.append( "\\n");
					break;
				case '\r':
					sb.append( "\\r");
					break;
				case '\t':
					sb.append( "\\t");
					break;
				case '"':
					sb.append( "\\\"");
					break;
				case '\\':
					sb.append( "\\\\");
					break;
				case '/':
					sb.append( "\\/");
					break;
				default:
					if (c >= '\u0000' && c <= '\u001F' || c >= '\u007F' && c <= '\u009F' || c >= '\u2000' && c <= '\u20FF') {
						String hex = Integer.toHexString( c);
						sb.append( "\\u");
						for (int j = hex.length(); j < 4; ++j) {
							sb.append( '0');
						}
						sb.append( hex.toUpperCase());
					}
					else {
						sb.append( c);
					}
			}
		}
		return sb.toString();
	}

	static String unescape( String s) {
		boolean inEscape = false;
		int inUnicode = 0;
		StringBuffer sb = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; ++i) {
			char c = s.charAt( i);
			if (inUnicode > 0) {
				--inUnicode;
				if (inUnicode == 0) {
					sb.append( (char) Integer.parseInt( s.substring( i - 3, i + 1), 16));
				}
			}
			else if (inEscape) {
				switch (c) {
					case 'b':
						sb.append( '\b');
						break;
					case 'f':
						sb.append( '\f');
						break;
					case 'n':
						sb.append( '\n');
						break;
					case 'r':
						sb.append( '\r');
						break;
					case 't':
						sb.append( '\t');
						break;
					case 'u':
						inUnicode = 4;
						break;
					default:
						sb.append( c);
				}
				inEscape = false;
			}
			else if (c == '\\') {
				inEscape = true;
			}
			else {
				sb.append( c);
			}
		}
		return sb.toString();
	}
}
