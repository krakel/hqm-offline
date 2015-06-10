package de.doerl.hqm.utils.nei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class ReaderTest {
	private static FObject createObj( String... vals) {
		FObject obj = new FObject();
		putValue( obj, vals);
		return obj;
	}

	private static void putObject( FArray arr, IJson other) {
		arr.add( other);
	}

	private static void putObject( FObject obj, String key, IJson other) {
		obj.put( key, other);
	}

	private static void putValue( FArray arr, Integer... vals) {
		for (int i = 0; i < vals.length; ++i) {
			arr.add( new FValue( String.valueOf( vals[i])));
		}
	}

	private static void putValue( FArray arr, String val) {
		arr.add( new FValue( val));
	}

	private static void putValue( FObject obj, String... vals) {
		for (int i = 0; i < vals.length; i += 2) {
			obj.put( vals[i], new FValue( vals[i + 1]));
		}
	}

	private static void putValue( FObject obj, String key, String val) {
		obj.put( key, new FValue( val));
	}

	@Test
	public void testParse1() {
		String test = "{id:1s,Damage:0s}";
		//
		FObject exp = new FObject();
		putValue( exp, "id", "1s", "Damage", "0s");
		//
		NEIReader rdr = new NEIReader( test);
		try {
			IJson act = rdr.doLine();
			assertEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}

	@Test
	public void testParse2() {
		String test = "{id:4806s,Damage:0s,tag:{ownerName:\"Server-wide Soul Network\"}}";
		//
		FObject obj = new FObject();
		putValue( obj, "ownerName", "\"Server-wide Soul Network\"");
		//
		FObject exp = new FObject();
		putValue( exp, "id", "4806s", "Damage", "0s");
		putObject( exp, "tag", obj);
		//
		NEIReader rdr = new NEIReader( test);
		try {
			IJson act = rdr.doLine();
			assertEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}

	@Test
	public void testParse3() {
		String test = "{id:5018s,Damage:0s,tag:{ex:[0:\"buildcraft:timer\"],mat:0b,logic:0b}}";
		//
		FArray arr = new FArray();
		putValue( arr, "\"buildcraft:timer\"");
		//
		FObject obj = new FObject();
		putObject( obj, "ex", arr);
		putValue( obj, "mat", "0b", "logic", "0b");
		//
		FObject exp = new FObject();
		putValue( exp, "id", "5018s", "Damage", "0s");
		putObject( exp, "tag", obj);
		//
		NEIReader rdr = new NEIReader( test);
		try {
			IJson act = rdr.doLine();
			assertEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}

	@Test
	public void testParse4() {
		String test = "{id:766s,Damage:0s,tag:{reagentTanks:[0:{amount:16000,capacity:16000,Reagent:\"aether\"}]}}";
		//
		FArray arr = new FArray();
		putObject( arr, createObj( "amount", "16000", "capacity", "16000", "Reagent", "\"aether\""));
		//
		FObject obj2 = new FObject();
		putObject( obj2, "reagentTanks", arr);
		//
		FObject exp = new FObject();
		putValue( exp, "id", "766s", "Damage", "0s");
		putObject( exp, "tag", obj2);
		//
		NEIReader rdr = new NEIReader( test);
		try {
			IJson act = rdr.doLine();
			assertEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}

	@Test
	public void testParse5() {
		String test = "{id:4696s,Damage:0s,tag:" //
			+ "{Age:0,IsAnalyzed:0b,Genome:" //
			+ "{Chromosomes:[" //
			+ "0:{UID0:\"botany.flowers.species.dandelion\",UID1:\"botany.flowers.species.dandelion\",Slot:0b}," //
			+ "1:{UID0:\"botany.colorYellow\",UID1:\"botany.colorYellow\",Slot:1b}," //
			+ "2:{UID0:\"botany.colorYellow\",UID1:\"botany.colorYellow\",Slot:2b}" //
			+ "]}," //
			+ "Wilt:0b,Flowered:1b}}";
		//
		//
		FArray arr = new FArray();
		putObject( arr, createObj( "UID0", "\"botany.flowers.species.dandelion\"", "UID1", "\"botany.flowers.species.dandelion\"", "Slot", "0b"));
		putObject( arr, createObj( "UID0", "\"botany.colorYellow\"", "UID1", "\"botany.colorYellow\"", "Slot", "1b"));
		putObject( arr, createObj( "UID0", "\"botany.colorYellow\"", "UID1", "\"botany.colorYellow\"", "Slot", "2b"));
		//
		FObject obj1 = new FObject();
		putObject( obj1, "Chromosomes", arr);
		//
		FObject obj2 = new FObject();
		putValue( obj2, "Age", "0", "IsAnalyzed", "0b");
		putObject( obj2, "Genome", obj1);
		putValue( obj2, "Wilt", "0b", "Flowered", "1b");
		//
		FObject exp = new FObject();
		putValue( exp, "id", "4696s", "Damage", "0s");
		putObject( exp, "tag", obj2);
		//
		NEIReader rdr = new NEIReader( test);
		try {
			IJson act = rdr.doLine();
			assertEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}

	@Test
	public void testParse6() {
		String test = "{id:5718s,Damage:0s,tag:{" //
			+ "InfiTool:{" //
			+ "Unbreaking:2,Damage:0,Blaze:[21474836,0,0,],Shoddy:0.0f,Broken:0b,Effect1:7,TotalDurability:21474836" //
			+ "},display:{" //
			+ "Name:\"�fBane of Pigs\"}}}";
		//
		FArray arr = new FArray();
		putValue( arr, 21474836, 0, 0);
		//
		FObject obj1 = new FObject();
		putValue( obj1, "Unbreaking", "2", "Damage", "0");
		putObject( obj1, "Blaze", arr);
		putValue( obj1, "Shoddy", "0.0f", "Broken", "0b", "Effect1", "7", "TotalDurability", "21474836");
		//
		FObject obj2 = new FObject();
		putObject( obj2, "InfiTool", obj1);
		putObject( obj2, "display", createObj( "Name", "\"�fBane of Pigs\""));
		//
		FObject exp = new FObject();
		putValue( exp, "id", "5718s", "Damage", "0s");
		putObject( exp, "tag", obj2);
		//
		NEIReader rdr = new NEIReader( test);
		try {
			IJson act = rdr.doLine();
			assertEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}

	@Test
	public void testParse7() {
		String test = "{id:2008s,Damage:0s,tag:{Facing:3b,Energy:0,SideCache:[6 bytes],Level:3b,RSControl:0b}}";
		//
		FArray arr = new FArray();
		arr.setByteArr( "6 bytes");
		//
		FObject obj = new FObject();
		putValue( obj, "Facing", "3b", "Energy", "0");
		putObject( obj, "SideCache", arr);
		putValue( obj, "Level", "3b", "RSControl", "0b");
		//
		FObject exp = new FObject();
		putValue( exp, "id", "2008s", "Damage", "0s");
		putObject( exp, "tag", obj);
		//
		NEIReader rdr = new NEIReader( test);
		try {
			IJson act = rdr.doLine();
			assertEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}
}
