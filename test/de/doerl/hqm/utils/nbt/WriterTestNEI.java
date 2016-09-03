package de.doerl.hqm.utils.nbt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WriterTestNEI {
	private static void doTest( FCompound test, String exp) {
		String act = SerializerAtNEI.write( test);
		assertEquals( exp, act);
	}

	@Test
	public void testParse1() {
		String exp = "{id:1s,Damage:0s}";
		//
		FCompound test = FCompound.create( //
			FLong.createShort( "id", 1), //
			FLong.createShort( "Damage", 0));
		doTest( test, exp);
	}

	@Test
	public void testParse2() {
		String exp = "{id:4806s,Damage:0s,tag:{ownerName:\"Server-wide Soul Network\"}}";
		//
		FCompound test = FCompound.create( //
			FLong.createShort( "id", 4806), //
			FLong.createShort( "Damage", 0), //
			FCompound.create( "tag", //
				FString.create( "ownerName", "Server-wide Soul Network")));
		doTest( test, exp);
	}

	@Test
	public void testParse3() {
		String exp = "{id:5018s,Damage:0s,tag:{ex:[0:\"buildcraft:timer\"],mat:0b,logic:0b}}";
		//
		FCompound test = FCompound.create( //
			FLong.createShort( "id", 5018), //
			FLong.createShort( "Damage", 0), //
			FCompound.create( "tag", //
				FList.create( "ex", //
					FString.create( "", "buildcraft:timer")), //
				FLong.createByte( "mat", 0), //
				FLong.createByte( "logic", 0)));
		doTest( test, exp);
	}

	@Test
	public void testParse4() {
		String exp = "{id:766s,Damage:0s,tag:{reagentTanks:[0:{amount:16000,capacity:16000,Reagent:\"aether\"}]}}";
		//
		FCompound test = FCompound.create( //
			FLong.createShort( "id", 766), //
			FLong.createShort( "Damage", 0), //
			FCompound.create( "tag", //
				FList.create( "reagentTanks", //
					FCompound.create( //
						FLong.createInt( "amount", 16000), //
						FLong.createInt( "capacity", 16000), //
						FString.create( "Reagent", "aether")))));
		doTest( test, exp);
	}

	@Test
	public void testParse5() {
		String exp = "{id:4696s,Damage:0s,tag:" //
			+ "{Age:0,IsAnalyzed:0b,Genome:" //
			+ "{Chromosomes:[" //
			+ "0:{UID0:\"botany.flowers.species.dandelion\",UID1:\"botany.flowers.species.dandelion\",Slot:0b}," //
			+ "1:{UID0:\"botany.colorYellow\",UID1:\"botany.colorYellow\",Slot:1b}," //
			+ "2:{UID0:\"botany.colorYellow\",UID1:\"botany.colorYellow\",Slot:2b}" //
			+ "]}," //
			+ "Wilt:0b,Flowered:1b}}";
		//
		FCompound test = FCompound.create( //
			FLong.createShort( "id", 4696), //
			FLong.createShort( "Damage", 0), //
			FCompound.create( "tag", //
				FLong.createInt( "Age", 0), //
				FLong.createByte( "IsAnalyzed", 0), //
				FCompound.create( "Genome", //
					FList.create( "Chromosomes", //
						FCompound.create( FString.create( "UID0", "botany.flowers.species.dandelion"), FString.create( "UID1", "botany.flowers.species.dandelion"), FLong.createByte( "Slot", 0)), //
						FCompound.create( FString.create( "UID0", "botany.colorYellow"), FString.create( "UID1", "botany.colorYellow"), FLong.createByte( "Slot", 1)), //
						FCompound.create( FString.create( "UID0", "botany.colorYellow"), FString.create( "UID1", "botany.colorYellow"), FLong.createByte( "Slot", 2)))), //
				FLong.createByte( "Wilt", 0), //
				FLong.createByte( "Flowered", 1)));
		doTest( test, exp);
	}

	@Test
	public void testParse6() {
		String exp = "{id:5718s,Damage:0s,tag:{" //
			+ "InfiTool:{" //
			+ "Unbreaking:2,Damage:0,Blaze:[21474836,0,0,],Shoddy:0.0f,Broken:0b,Effect1:7,TotalDurability:21474836" //
			+ "}," //
			+ "display:{Name:\"�fBane of Pigs\"}}}";
		//
		FCompound test = FCompound.create( //
			FLong.createShort( "id", 5718), //
			FLong.createShort( "Damage", 0), //
			FCompound.create( "tag", //
				FCompound.create( "InfiTool", //
					FLong.createInt( "Unbreaking", 2), //
					FLong.createInt( "Damage", 0), //
					FIntArray.create( "Blaze", 21474836, 0, 0), //
					FDouble.createFloat( "Shoddy", 0.0F), //
					FLong.createByte( "Broken", 0), //
					FLong.createInt( "Effect1", 7), //
					FLong.createInt( "TotalDurability", 21474836)), //
				FCompound.create( "display", //
					FString.create( "Name", "�fBane of Pigs"))));
		doTest( test, exp);
	}

	@Test
	public void testParse7() {
		String exp = "{id:2008s,Damage:0s,tag:{Facing:3b,Energy:0,SideCache:[6 bytes],Level:3b,RSControl:0b}}";
		//
		FCompound test = FCompound.create( //
			FLong.createShort( "id", 2008), //
			FLong.createShort( "Damage", 0), //
			FCompound.create( "tag", //
				FLong.createByte( "Facing", 3), //
				FLong.createInt( "Energy", 0), //
				FByteArray.create( "SideCache", 0, 0, 0, 0, 0, 0), //
				FLong.createByte( "Level", 3), //
				FLong.createByte( "RSControl", 0)));
		doTest( test, exp);
	}
}
