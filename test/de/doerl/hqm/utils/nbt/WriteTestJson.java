package de.doerl.hqm.utils.nbt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WriteTestJson {
	private static void doTest( FCompound test, String exp) {
		String act = SerializerAtJson.write( test);
		assertEquals( exp, act);
	}

	@Test
	public void testParse1() {
		String exp = "=COMPOUND()";
		FCompound test = FCompound.create();
		doTest( test, exp);
	}

	@Test
	public void testParse2() {
		String exp = "=COMPOUND(Energy=INT(400000))";
		FCompound test = FCompound.create( //
			FLong.createInt( "Energy", 400000));
		doTest( test, exp);
	}

	@Test
	public void testParse3() {
		String exp = "=COMPOUND(title=STRING('TG.personaltitle.seeker'))";
		FCompound test = FCompound.create( //
			FString.create( "title", "TG.personaltitle.seeker"));
		doTest( test, exp);
	}

	@Test
	public void testParse4() {
		String exp = "=COMPOUND(" //
			+ "InfiTool=COMPOUND(BaseDurability=INT(800),Head=INT(55),HeadEXP=LONG(0),BaseAttack=INT(7),ToolEXP=LONG(0),HarvestLevel=INT(0),Attack=INT(7),RenderHead=INT(55),ModDurability=FLOAT(0.0),Handle=INT(598),Broken=BYTE(0),Shoddy=FLOAT(0.0),RenderHandle=INT(598),Accessory=INT(656),MiningSpeed=INT(500),RenderAccessory=INT(656),ToolLevel=INT(1),HarvestLevelModified=BYTE(0),Unbreaking=INT(2),Damage=INT(0),BonusDurability=INT(0),TotalDurability=INT(800),Modifiers=INT(0))," //
			+ "display=COMPOUND(Name=STRING('§fPick of Tears')))";
		FCompound test = FCompound.create( //
			FCompound.create( "InfiTool", //
				FLong.createInt( "BaseDurability", 800), //
				FLong.createInt( "Head", 55), //
				FLong.createLong( "HeadEXP", 0), //
				FLong.createInt( "BaseAttack", 7), //
				FLong.createLong( "ToolEXP", 0), //
				FLong.createInt( "HarvestLevel", 0), //
				FLong.createInt( "Attack", 7), //
				FLong.createInt( "RenderHead", 55), //
				FDouble.createFloat( "ModDurability", 0), //
				FLong.createInt( "Handle", 598), //
				FLong.createByte( "Broken", 0), //
				FDouble.createFloat( "Shoddy", 0), //
				FLong.createInt( "RenderHandle", 598), //
				FLong.createInt( "Accessory", 656), //
				FLong.createInt( "MiningSpeed", 500), //
				FLong.createInt( "RenderAccessory", 656), //
				FLong.createInt( "ToolLevel", 1), //
				FLong.createByte( "HarvestLevelModified", 0), //
				FLong.createInt( "Unbreaking", 2), //
				FLong.createInt( "Damage", 0), //
				FLong.createInt( "BonusDurability", 0), //
				FLong.createInt( "TotalDurability", 800), //
				FLong.createInt( "Modifiers", 0)), //
			FCompound.create( "display", //
				FString.create( "Name", "§fPick of Tears")));
		doTest( test, exp);
	}

	@Test
	public void testParse5() {
		String exp = "=COMPOUND(Recv=INT(80),RSControl=BYTE(1),Facing=BYTE(3),Energy=INT(400000),SideCache=BYTE-ARRAY(1,2,2,2,2,2),Send=INT(80))";
		FCompound test = FCompound.create( //
			FLong.createInt( "Recv", 80), //
			FLong.createByte( "RSControl", 1), //
			FLong.createByte( "Facing", 3), //
			FLong.createInt( "Energy", 400000), //
			FByteArray.create( "SideCache", 1, 2, 2, 2, 2, 2), //
			FLong.createInt( "Send", 80));
		doTest( test, exp);
	}

	@Test
	public void testParse6() {
		String exp = "=COMPOUND(" //
			+ "ForgeData=COMPOUND(" //
			+ "sAI=COMPOUND(AvoidExplosions=BYTE(1),DefendVillage=BYTE(0),Griefing=BYTE(0),uAI=COMPOUND(),Rider=BYTE(0)),HungerOverhaulCheck=INT(6)),Attributes=LIST(" //
			+ "COMPOUND(Base=DOUBLE(10.0),Name=STRING('generic.maxHealth'))," //
			+ "COMPOUND(Base=DOUBLE(0.0),Name=STRING('generic.knockbackResistance'))," //
			+ "COMPOUND(Base=DOUBLE(0.20000000298023224),Name=STRING('generic.movementSpeed'))," //
			+ "COMPOUND(Base=DOUBLE(16.0),Modifiers=LIST(" //
			+ "COMPOUND(UUIDMost=LONG(1593451547207684033),UUIDLeast=LONG(-7078328387847374506),Amount=DOUBLE(-0.04186616479037544),Operation=INT(1),Name=STRING('Random spawn bonus')))," //
			+ "Name=STRING('generic.followRange')))," //
			+ "Invulnerable=BYTE(0),PortalCooldown=INT(0),AbsorptionAmount=FLOAT(0.0),FallDistance=FLOAT(0.0),InLove=INT(0),DeathTime=SHORT(0),isSoul=BYTE(0),jarredCritterName=STRING('Cow')," //
			+ "DropChances=LIST(FLOAT(0.085),FLOAT(0.085),FLOAT(0.085),FLOAT(0.085),FLOAT(0.085))," //
			+ "PersistenceRequired=BYTE(0),id=STRING('Cow'),HealF=FLOAT(9.0),Age=INT(0)," //
			+ "Motion=LIST(DOUBLE(0.0),DOUBLE(-0.0784000015258789),DOUBLE(0.0))," //
			+ "Leashed=BYTE(0),UUIDLeast=LONG(-6817217859154290623),Health=SHORT(9),Air=SHORT(300),OnGround=BYTE(1),Dimension=INT(0)," //
			+ "Rotation=LIST(FLOAT(-3551.1572),FLOAT(0.0))," //
			+ "CreatureInfusion=COMPOUND(PlayerInfusions=INT-ARRAY(0,0,0,0,0,0,0,0,0,0,0,0)," //
			+ "InfusionCosts=COMPOUND(Aspects=LIST()),tumorWarpTemp=INT(0),Infusions=INT-ARRAY(0,0,0,0,0,0,0,0,0,0,0,0),tumorWarp=INT(0),sitting=BYTE(0)),UUIDMost=LONG(1482314938347242401)," //
			+ "Equipment=LIST(COMPOUND(),COMPOUND(),COMPOUND(),COMPOUND(),COMPOUND()),CustomName=STRING('')," //
			+ "Pos=LIST(DOUBLE(-866.0243300245936),DOUBLE(63.0),DOUBLE(1001.028515625055)),Fire=SHORT(-1),CanPickUpLoot=BYTE(0),HurtTime=SHORT(0),AttackTime=SHORT(0),CustomNameVisible=BYTE(0))";
		FCompound test = FCompound.create( //
			FCompound.create( "ForgeData", //
				FCompound.create( "sAI", //
					FLong.createByte( "AvoidExplosions", 1), //
					FLong.createByte( "DefendVillage", 0), //
					FLong.createByte( "Griefing", 0), //
					FCompound.create( "uAI"), //
					FLong.createByte( "Rider", 0)), //
				FLong.createInt( "HungerOverhaulCheck", 6)), //
			FList.create( "Attributes", //
				FCompound.create( FDouble.createDouble( "Base", 10.0), FString.create( "Name", "generic.maxHealth")), //
				FCompound.create( FDouble.createDouble( "Base", 0.0), FString.create( "Name", "generic.knockbackResistance")), //
				FCompound.create( FDouble.createDouble( "Base", 0.20000000298023224), FString.create( "Name", "generic.movementSpeed")), //
				FCompound.create( FDouble.createDouble( "Base", 16.0), //
					FList.create( "Modifiers", //
						FCompound.create( //
							FLong.createLong( "UUIDMost", 1593451547207684033L), //
							FLong.createLong( "UUIDLeast", -7078328387847374506L), //
							FDouble.createDouble( "Amount", -0.04186616479037544), //
							FLong.createInt( "Operation", 1), //
							FString.create( "Name", "Random spawn bonus"))), //
					FString.create( "Name", "generic.followRange"))), //
			FLong.createByte( "Invulnerable", 0), //
			FLong.createInt( "PortalCooldown", 0), //
			FDouble.createFloat( "AbsorptionAmount", 0), //
			FDouble.createFloat( "FallDistance", 0), //
			FLong.createInt( "InLove", 0), //
			FLong.createShort( "DeathTime", 0), //
			FLong.createByte( "isSoul", 0), //
			FString.create( "jarredCritterName", "Cow"), //
			FList.create( "DropChances", //
				FDouble.createFloat( 0.085F), //
				FDouble.createFloat( 0.085F), //
				FDouble.createFloat( 0.085F), //
				FDouble.createFloat( 0.085F), //
				FDouble.createFloat( 0.085F)), //
			FLong.createByte( "PersistenceRequired", 0), //
			FString.create( "id", "Cow"), //
			FDouble.createFloat( "HealF", 9.0F), //
			FLong.createInt( "Age", 0), //
			FList.create( "Motion", //
				FDouble.createDouble( 0), //
				FDouble.createDouble( -0.0784000015258789), //
				FDouble.createDouble( 0)), //
			FLong.createByte( "Leashed", 0), //
			FLong.createLong( "UUIDLeast", -6817217859154290623L), //
			FLong.createShort( "Health", 9), //
			FLong.createShort( "Air", 300), //
			FLong.createByte( "OnGround", 1), //
			FLong.createInt( "Dimension", 0), //
			FList.create( "Rotation", //
				FDouble.createFloat( -3551.1572F), //
				FDouble.createFloat( 0)), //
			FCompound.create( "CreatureInfusion", //
				FIntArray.create( "PlayerInfusions", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), //
				FCompound.create( "InfusionCosts", FList.create( "Aspects")), //
				FLong.createInt( "tumorWarpTemp", 0), //
				FIntArray.create( "Infusions", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), //
				FLong.createInt( "tumorWarp", 0), //
				FLong.createByte( "sitting", 0)), //
			FLong.createLong( "UUIDMost", 1482314938347242401L), //
			FList.create( "Equipment", FCompound.create(), FCompound.create(), FCompound.create(), FCompound.create(), FCompound.create()), //
			FString.create( "CustomName", ""), //
			FList.create( "Pos", //
				FDouble.createDouble( -866.0243300245936), //
				FDouble.createDouble( 63.0), //
				FDouble.createDouble( 1001.028515625055)), //
			FLong.createShort( "Fire", -1), //
			FLong.createByte( "CanPickUpLoot", 0), //
			FLong.createShort( "HurtTime", 0), //
			FLong.createShort( "AttackTime", 0), //
			FLong.createByte( "CustomNameVisible", 0) //
		);
		doTest( test, exp);
	}

	@Test
	public void testParse7() {
		String exp = "=COMPOUND(ench=LIST(COMPOUND(lvl=SHORT(6),id=SHORT(34)),COMPOUND(lvl=SHORT(6),id=SHORT(35))))";
		FCompound test = FCompound.create( //
			FList.create( "ench", //
				FCompound.create( FLong.createShort( "lvl", 6), FLong.createShort( "id", 34)), //
				FCompound.create( FLong.createShort( "lvl", 6), FLong.createShort( "id", 35))));
		doTest( test, exp);
	}

	@Test
	public void testParse8() {
		String exp = "=COMPOUND(Send=INT(800),Facing=BYTE(3),Energy=INT(2000000),Recv=INT(800),SideCache=BYTE-ARRAY(1,2,2,2,2,2),RSControl=BYTE(1))";
		FCompound test = FCompound.create( //
			FLong.createInt( "Send", 800), //
			FLong.createByte( "Facing", 3), //
			FLong.createInt( "Energy", 2000000), //
			FLong.createInt( "Recv", 800), //
			FByteArray.create( "SideCache", 1, 2, 2, 2, 2, 2), //
			FLong.createByte( "RSControl", 1));
		doTest( test, exp);
	}
}
