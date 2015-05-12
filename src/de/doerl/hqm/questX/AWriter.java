package de.doerl.hqm.questX;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import de.doerl.hqm.utils.Security;
import de.doerl.hqm.utils.Tabber;

public abstract class AWriter extends PrintWriter {
	protected String mNL = Security.getProperty( "line.separator", "\n");
	protected Tabber mTabs = new Tabber();

	public AWriter( OutputStream out) {
		super( out);
	}

	public AWriter( Writer out) {
		super( out);
	}

	public abstract void beginArray( String key);

	public abstract void beginObject();

	public abstract void endArray();

	public abstract void endObject();

	public Tabber getTabber() {
		return mTabs;
	}

	public abstract void print( String key, boolean value);

	public abstract void print( String key, byte value);

	public abstract void print( String key, double value);

	public abstract void print( String key, float value);

	public abstract void print( String key, int value);

	public abstract void print( String key, IWriter obj);

	public abstract <T extends IWriter> void print( String key, List<T> arr);

	public abstract void print( String key, Object obj);

	public abstract void print( String key, short value);

	public abstract <T extends IWriter> void print( String key, T[] arr);

	public abstract void printKey( String key);
}
