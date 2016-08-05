package de.doerl.hqm.utils.nbt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class WriterTest1 {
	private static void doTest( FCompound test, byte[] exp) {
		try {
			byte[] act = NbtWriter1.write0( test);
			assertArrayEquals( exp, act);
		}
		catch (IOException ex) {
			fail( "should not happen: " + ex);
		}
	}

	@Test
	public void testParse1() {
		FCompound test = FCompound.create();
		byte[] exp = { 10, 0, 0, 0
		};
		doTest( test, exp);
	}

	@Test
	public void testParse2() {
		FCompound test = FCompound.create( //
			FLong.createInt( "Energy", 400000));
		byte[] exp = { 10, 0, 0, 3, 0, 6, 69, 110, 101, 114, 103, 121, 0, 6, 26, -128, 0
		};
		doTest( test, exp);
	}

	@Test
	public void testParse3() {
		FCompound test = FCompound.create( //
			FString.create( "title", "TG.personaltitle.seeker"));
		byte[] exp = { 10, 0, 0, 8, 0, 5, 116, 105, 116, 108, 101, 0, 23, 84, 71, 46, 112, 101, 114, 115, 111, 110, 97, 108, 116, 105, 116, 108, 101, 46, 115, 101, 101, 107, 101, 114, 0
		};
		doTest( test, exp);
	}

	@Test
	public void testParse4() {
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
		byte[] exp = {
			10, 0, 0, 10, 0, 8, 73, 110, 102, 105, 84, 111, 111, 108, 3, 0, 14, 66, 97, 115, 101, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 3, 32, 3, 0, 4, 72, 101, 97, 100, 0, 0, 0, 55, 4, 0, 7, 72, 101, 97, 100, 69, 88, 80, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 10, 66, 97, 115, 101, 65, 116, 116, 97, 99, 107, 0, 0, 0, 7, 4, 0, 7, 84, 111, 111, 108, 69, 88, 80, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 12, 72, 97, 114, 118, 101, 115, 116, 76, 101, 118, 101, 108, 0, 0, 0, 0, 3, 0, 6, 65, 116, 116, 97, 99, 107, 0, 0, 0, 7, 3, 0, 10, 82, 101, 110, 100, 101, 114, 72, 101, 97, 100, 0, 0, 0, 55, 5, 0, 13, 77, 111, 100, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 0, 0, 3, 0, 6, 72, 97, 110, 100, 108, 101, 0, 0, 2, 86, 1, 0, 6, 66, 114, 111, 107, 101, 110, 0, 5, 0, 6, 83, 104, 111, 100, 100, 121, 0, 0, 0, 0, 3, 0, 12, 82, 101, 110, 100, 101, 114, 72, 97, 110, 100, 108, 101, 0, 0, 2, 86, 3, 0, 9, 65, 99, 99, 101, 115, 115, 111, 114, 121, 0, 0, 2, -112, 3, 0, 11, 77, 105, 110, 105, 110, 103,
			83, 112, 101, 101, 100, 0, 0, 1, -12, 3, 0, 15, 82, 101, 110, 100, 101, 114, 65, 99, 99, 101, 115, 115, 111, 114, 121, 0, 0, 2, -112, 3, 0, 9, 84, 111, 111, 108, 76, 101, 118, 101, 108, 0, 0, 0, 1, 1, 0, 20, 72, 97, 114, 118, 101, 115, 116, 76, 101, 118, 101, 108, 77, 111, 100, 105, 102, 105, 101, 100, 0, 3, 0, 10, 85, 110, 98, 114, 101, 97, 107, 105, 110, 103, 0, 0, 0, 2, 3, 0, 6, 68, 97, 109, 97, 103, 101, 0, 0, 0, 0, 3, 0, 15, 66, 111, 110, 117, 115, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 0, 0, 3, 0, 15, 84, 111, 116, 97, 108, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 3, 32, 3, 0, 9, 77, 111, 100, 105, 102, 105, 101, 114, 115, 0, 0, 0, 0, 0, 10, 0, 7, 100, 105, 115, 112, 108, 97, 121, 8, 0, 4, 78, 97, 109, 101, 0, 16, -62, -89, 102, 80, 105, 99, 107, 32, 111, 102, 32, 84, 101, 97, 114, 115, 0, 0
		};
		doTest( test, exp);
	}

	@Test
	public void testParse5() {
		FCompound test = FCompound.create( //
			FLong.createInt( "Recv", 80), //
			FLong.createByte( "RSControl", 1), //
			FLong.createByte( "Facing", 3), //
			FLong.createInt( "Energy", 400000), //
			FByteArray.create( "SideCache", 1, 2, 2, 2, 2, 2), //
			FLong.createInt( "Send", 80));
		byte[] exp = { 10, 0, 0, 3, 0, 4, 82, 101, 99, 118, 0, 0, 0, 80, 1, 0, 9, 82, 83, 67, 111, 110, 116, 114, 111, 108, 1, 1, 0, 6, 70, 97, 99, 105, 110, 103, 3, 3, 0, 6, 69, 110, 101, 114, 103, 121, 0, 6, 26, -128, 7, 0, 9, 83, 105, 100, 101, 67, 97, 99, 104, 101, 0, 0, 0, 6, 1, 2, 2, 2, 2, 2, 3, 0, 4, 83, 101, 110, 100, 0, 0, 0, 80, 0
		};
		doTest( test, exp);
	}

	@Test
	public void testParse6() {
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
		byte[] exp = {
			10, 0, 0, 10, 0, 9, 70, 111, 114, 103, 101, 68, 97, 116, 97, 10, 0, 3, 115, 65, 73, 1, 0, 15, 65, 118, 111, 105, 100, 69, 120, 112, 108, 111, 115, 105, 111, 110, 115, 1, 1, 0, 13, 68, 101, 102, 101, 110, 100, 86, 105, 108, 108, 97, 103, 101, 0, 1, 0, 8, 71, 114, 105, 101, 102, 105, 110, 103, 0, 10, 0, 3, 117, 65, 73, 0, 1, 0, 5, 82, 105, 100, 101, 114, 0, 0, 3, 0, 19, 72, 117, 110, 103, 101, 114, 79, 118, 101, 114, 104, 97, 117, 108, 67, 104, 101, 99, 107, 0, 0, 0, 6, 0, 9, 0, 10, 65, 116, 116, 114, 105, 98, 117, 116, 101, 115, 10, 0, 0, 0, 4, 6, 0, 4, 66, 97, 115, 101, 64, 36, 0, 0, 0, 0, 0, 0, 8, 0, 4, 78, 97, 109, 101, 0, 17, 103, 101, 110, 101, 114, 105, 99, 46, 109, 97, 120, 72, 101, 97, 108, 116, 104, 0, 6, 0, 4, 66, 97, 115, 101, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 4, 78, 97, 109, 101, 0, 27, 103, 101, 110, 101, 114, 105, 99, 46, 107, 110, 111, 99, 107, 98, 97, 99, 107, 82, 101, 115, 105, 115, 116, 97, 110, 99, 101, 0, 6, 0, 4, 66, 97, 115, 101, 63, -55, -103, -103, -96,
			0, 0, 0, 8, 0, 4, 78, 97, 109, 101, 0, 21, 103, 101, 110, 101, 114, 105, 99, 46, 109, 111, 118, 101, 109, 101, 110, 116, 83, 112, 101, 101, 100, 0, 6, 0, 4, 66, 97, 115, 101, 64, 48, 0, 0, 0, 0, 0, 0, 9, 0, 9, 77, 111, 100, 105, 102, 105, 101, 114, 115, 10, 0, 0, 0, 1, 4, 0, 8, 85, 85, 73, 68, 77, 111, 115, 116, 22, 29, 19, -67, 94, 28, 79, -63, 4, 0, 9, 85, 85, 73, 68, 76, 101, 97, 115, 116, -99, -60, -71, -43, 118, -53, 57, 86, 6, 0, 6, 65, 109, 111, 117, 110, 116, -65, -91, 111, 123, 97, 42, -50, -44, 3, 0, 9, 79, 112, 101, 114, 97, 116, 105, 111, 110, 0, 0, 0, 1, 8, 0, 4, 78, 97, 109, 101, 0, 18, 82, 97, 110, 100, 111, 109, 32, 115, 112, 97, 119, 110, 32, 98, 111, 110, 117, 115, 0, 8, 0, 4, 78, 97, 109, 101, 0, 19, 103, 101, 110, 101, 114, 105, 99, 46, 102, 111, 108, 108, 111, 119, 82, 97, 110, 103, 101, 0, 1, 0, 12, 73, 110, 118, 117, 108, 110, 101, 114, 97, 98, 108, 101, 0, 3, 0, 14, 80, 111, 114, 116, 97, 108, 67, 111, 111, 108, 100, 111, 119, 110, 0, 0, 0, 0, 5, 0,
			16, 65, 98, 115, 111, 114, 112, 116, 105, 111, 110, 65, 109, 111, 117, 110, 116, 0, 0, 0, 0, 5, 0, 12, 70, 97, 108, 108, 68, 105, 115, 116, 97, 110, 99, 101, 0, 0, 0, 0, 3, 0, 6, 73, 110, 76, 111, 118, 101, 0, 0, 0, 0, 2, 0, 9, 68, 101, 97, 116, 104, 84, 105, 109, 101, 0, 0, 1, 0, 6, 105, 115, 83, 111, 117, 108, 0, 8, 0, 17, 106, 97, 114, 114, 101, 100, 67, 114, 105, 116, 116, 101, 114, 78, 97, 109, 101, 0, 3, 67, 111, 119, 9, 0, 11, 68, 114, 111, 112, 67, 104, 97, 110, 99, 101, 115, 5, 0, 0, 0, 5, 61, -82, 20, 123, 61, -82, 20, 123, 61, -82, 20, 123, 61, -82, 20, 123, 61, -82, 20, 123, 1, 0, 19, 80, 101, 114, 115, 105, 115, 116, 101, 110, 99, 101, 82, 101, 113, 117, 105, 114, 101, 100, 0, 8, 0, 2, 105, 100, 0, 3, 67, 111, 119, 5, 0, 5, 72, 101, 97, 108, 70, 65, 16, 0, 0, 3, 0, 3, 65, 103, 101, 0, 0, 0, 0, 9, 0, 6, 77, 111, 116, 105, 111, 110, 6, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, -65, -76, 18, 5, -62, -113, 92, 41, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 7, 76, 101, 97, 115, 104,
			101, 100, 0, 4, 0, 9, 85, 85, 73, 68, 76, 101, 97, 115, 116, -95, 100, 96, 121, -20, -88, -40, 65, 2, 0, 6, 72, 101, 97, 108, 116, 104, 0, 9, 2, 0, 3, 65, 105, 114, 1, 44, 1, 0, 8, 79, 110, 71, 114, 111, 117, 110, 100, 1, 3, 0, 9, 68, 105, 109, 101, 110, 115, 105, 111, 110, 0, 0, 0, 0, 9, 0, 8, 82, 111, 116, 97, 116, 105, 111, 110, 5, 0, 0, 0, 2, -59, 93, -14, -124, 0, 0, 0, 0, 10, 0, 16, 67, 114, 101, 97, 116, 117, 114, 101, 73, 110, 102, 117, 115, 105, 111, 110, 11, 0, 15, 80, 108, 97, 121, 101, 114, 73, 110, 102, 117, 115, 105, 111, 110, 115, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 13, 73, 110, 102, 117, 115, 105, 111, 110, 67, 111, 115, 116, 115, 9, 0, 7, 65, 115, 112, 101, 99, 116, 115, 0, 0, 0, 0, 0, 0, 3, 0, 13, 116, 117, 109, 111, 114, 87, 97, 114, 112, 84, 101, 109, 112, 0, 0, 0, 0, 11, 0, 9, 73, 110, 102, 117, 115, 105, 111, 110, 115, 0, 0,
			0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 9, 116, 117, 109, 111, 114, 87, 97, 114, 112, 0, 0, 0, 0, 1, 0, 7, 115, 105, 116, 116, 105, 110, 103, 0, 0, 4, 0, 8, 85, 85, 73, 68, 77, 111, 115, 116, 20, -110, 61, -107, 49, 113, 67, -95, 9, 0, 9, 69, 113, 117, 105, 112, 109, 101, 110, 116, 10, 0, 0, 0, 5, 0, 0, 0, 0, 0, 8, 0, 10, 67, 117, 115, 116, 111, 109, 78, 97, 109, 101, 0, 0, 9, 0, 3, 80, 111, 115, 6, 0, 0, 0, 3, -64, -117, 16, 49, -45, -16, -97, -122, 64, 79, -128, 0, 0, 0, 0, 0, 64, -113, 72, 58, 102, 102, 104, 74, 2, 0, 4, 70, 105, 114, 101, -1, -1, 1, 0, 13, 67, 97, 110, 80, 105, 99, 107, 85, 112, 76, 111, 111, 116, 0, 2, 0, 8, 72, 117, 114, 116, 84, 105, 109, 101, 0, 0, 2, 0, 10, 65, 116, 116, 97, 99, 107, 84, 105, 109, 101, 0, 0, 1, 0, 17, 67, 117, 115, 116, 111, 109, 78, 97, 109, 101, 86, 105, 115, 105, 98, 108, 101, 0, 0
		};
		doTest( test, exp);
	}

	@Test
	public void testParse7() {
		FCompound test = FCompound.create( //
			FList.create( "ench", //
				FCompound.create( FLong.createShort( "lvl", 6), FLong.createShort( "id", 34)), //
				FCompound.create( FLong.createShort( "lvl", 6), FLong.createShort( "id", 35))));
		byte[] exp = { 10, 0, 0, 9, 0, 4, 101, 110, 99, 104, 10, 0, 0, 0, 2, 2, 0, 3, 108, 118, 108, 0, 6, 2, 0, 2, 105, 100, 0, 34, 0, 2, 0, 3, 108, 118, 108, 0, 6, 2, 0, 2, 105, 100, 0, 35, 0, 0
		};
		doTest( test, exp);
	}

	@Test
	public void testParse8() {
		FCompound test = FCompound.create( //
			FLong.createInt( "Send", 800), //
			FLong.createByte( "Facing", 3), //
			FLong.createInt( "Energy", 2000000), //
			FLong.createInt( "Recv", 800), //
			FByteArray.create( "SideCache", 1, 2, 2, 2, 2, 2), //
			FLong.createByte( "RSControl", 1));
		byte[] exp = { 10, 0, 0, 3, 0, 4, 83, 101, 110, 100, 0, 0, 3, 32, 1, 0, 6, 70, 97, 99, 105, 110, 103, 3, 3, 0, 6, 69, 110, 101, 114, 103, 121, 0, 30, -124, -128, 3, 0, 4, 82, 101, 99, 118, 0, 0, 3, 32, 7, 0, 9, 83, 105, 100, 101, 67, 97, 99, 104, 101, 0, 0, 0, 6, 1, 2, 2, 2, 2, 2, 1, 0, 9, 82, 83, 67, 111, 110, 116, 114, 111, 108, 1, 0
		};
		doTest( test, exp);
	}
}
