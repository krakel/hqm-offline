package de.doerl.hqm.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class SingleLineFormatter extends Formatter {
	private static final String FORMAT = "{0,date,short} {0,time}";
	private static final MessageFormat FORMATTER = new MessageFormat( FORMAT);
	private static final String LINE_SEP = System.getProperty( "line.separator", "\n");
	private Date mDate = new Date();

	@Override
	public String format( LogRecord record) {
		StringBuilder sb = new StringBuilder();
		mDate.setTime( record.getMillis());
		StringBuffer text = new StringBuffer();
		FORMATTER.format( new Object[] { mDate
		}, text, null);
		sb.append( text.toString());
		sb.append( ": ");
		Level lvl = record.getLevel();
		if (lvl != null) {
			sb.append( lvl.getLocalizedName());
		}
		else {
			sb.append( "___LEVEL___");
		}
		sb.append( ": ");
		String source;
		if (record.getSourceClassName() != null) {
			source = record.getSourceClassName();
		}
		else {
			source = record.getLoggerName();
		}
		int pos = source.lastIndexOf( '.');
		sb.append( pos < 0 ? source : source.substring( pos + 1));
		if (record.getSourceMethodName() != null) {
			sb.append( " ");
			sb.append( record.getSourceMethodName());
		}
		sb.append( " - ");
		sb.append( formatMessage( record));
		sb.append( LINE_SEP);
		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter( sw);
				record.getThrown().printStackTrace( pw);
				pw.close();
				sb.append( sw.toString());
			}
			catch (Exception ex) {
			}
		}
		return sb.toString();
	}
}
