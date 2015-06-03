package de.doerl.hqm.utils.nbt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReaderTest {
	private static void doTest( byte[] test, String exp) {
		String act = NbtReader.read0( test);
		assertEquals( exp, act);
	}

	@Test
	public void testParse1() {
		byte[] test = {
			10, 0, 0, 0
		};
		String exp = "=COMPOUND(  )";
		doTest( test, exp);
	}

	@Test
	public void testParse2() {
		byte[] test = {
			10, 0, 0, 3, 0, 6, 69, 110, 101, 114, 103, 121, 0, 6, 26, -128, 0
		};
		String exp = "=COMPOUND( Energy=INT(400000) )";
		doTest( test, exp);
	}

	@Test
	public void testParse3() {
		byte[] test = {
			10, 0, 0, 8, 0, 5, 116, 105, 116, 108, 101, 0, 23, 84, 71, 46, 112, 101, 114, 115, 111, 110, 97, 108, 116, 105, 116, 108, 101, 46, 115, 101, 101, 107, 101, 114, 0
		};
		String exp = "=COMPOUND( title=STRING('TG.personaltitle.seeker') )";
		doTest( test, exp);
	}

	@Test
	public void testParse4() {
		byte[] test = {
			10, 0, 0, 10, 0, 8, 73, 110, 102, 105, 84, 111, 111, 108, 3, 0, 14, 66, 97, 115, 101, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 3, 32, 3, 0, 4, 72, 101, 97, 100, 0, 0, 0, 55, 4, 0, 7, 72, 101, 97, 100, 69, 88, 80, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 10, 66, 97, 115, 101, 65, 116, 116, 97, 99, 107, 0, 0, 0, 7, 4, 0, 7, 84, 111, 111, 108, 69, 88, 80, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 12, 72, 97, 114, 118, 101, 115, 116, 76, 101, 118, 101, 108, 0, 0, 0, 0, 3, 0, 6, 65, 116, 116, 97, 99, 107, 0, 0, 0, 7, 3, 0, 10, 82, 101, 110, 100, 101, 114, 72, 101, 97, 100, 0, 0, 0, 55, 5, 0, 13, 77, 111, 100, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 0, 0, 3, 0, 6, 72, 97, 110, 100, 108, 101, 0, 0, 2, 86, 1, 0, 6, 66, 114, 111, 107, 101, 110, 0, 5, 0, 6, 83, 104, 111, 100, 100, 121, 0, 0, 0, 0, 3, 0, 12, 82, 101, 110, 100, 101, 114, 72, 97, 110, 100, 108, 101, 0, 0, 2, 86, 3, 0, 9, 65, 99, 99, 101, 115, 115, 111, 114, 121, 0, 0, 2, -112, 3, 0, 11, 77, 105, 110, 105, 110, 103,
			83, 112, 101, 101, 100, 0, 0, 1, -12, 3, 0, 15, 82, 101, 110, 100, 101, 114, 65, 99, 99, 101, 115, 115, 111, 114, 121, 0, 0, 2, -112, 3, 0, 9, 84, 111, 111, 108, 76, 101, 118, 101, 108, 0, 0, 0, 1, 1, 0, 20, 72, 97, 114, 118, 101, 115, 116, 76, 101, 118, 101, 108, 77, 111, 100, 105, 102, 105, 101, 100, 0, 3, 0, 10, 85, 110, 98, 114, 101, 97, 107, 105, 110, 103, 0, 0, 0, 2, 3, 0, 6, 68, 97, 109, 97, 103, 101, 0, 0, 0, 0, 3, 0, 15, 66, 111, 110, 117, 115, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 0, 0, 3, 0, 15, 84, 111, 116, 97, 108, 68, 117, 114, 97, 98, 105, 108, 105, 116, 121, 0, 0, 3, 32, 3, 0, 9, 77, 111, 100, 105, 102, 105, 101, 114, 115, 0, 0, 0, 0, 0, 10, 0, 7, 100, 105, 115, 112, 108, 97, 121, 8, 0, 4, 78, 97, 109, 101, 0, 16, -62, -89, 102, 80, 105, 99, 107, 32, 111, 102, 32, 84, 101, 97, 114, 115, 0, 0
		};
		String exp = "=COMPOUND( InfiTool=COMPOUND( BaseDurability=INT(800), Head=INT(55), HeadEXP=LONG(0), BaseAttack=INT(7), ToolEXP=LONG(0), HarvestLevel=INT(0), Attack=INT(7), RenderHead=INT(55), ModDurability=FLOAT(0.0), Handle=INT(598), Broken=BYTE(0), Shoddy=FLOAT(0.0), RenderHandle=INT(598), Accessory=INT(656), MiningSpeed=INT(500), RenderAccessory=INT(656), ToolLevel=INT(1), HarvestLevelModified=BYTE(0), Unbreaking=INT(2), Damage=INT(0), BonusDurability=INT(0), TotalDurability=INT(800), Modifiers=INT(0) ), display=COMPOUND( Name=STRING('§fPick of Tears') ) )";
		doTest( test, exp);
	}

	@Test
	public void testParse5() {
		byte[] test = {
			10, 0, 0, 3, 0, 4, 82, 101, 99, 118, 0, 0, 0, 80, 1, 0, 9, 82, 83, 67, 111, 110, 116, 114, 111, 108, 1, 1, 0, 6, 70, 97, 99, 105, 110, 103, 3, 3, 0, 6, 69, 110, 101, 114, 103, 121, 0, 6, 26, -128, 7, 0, 9, 83, 105, 100, 101, 67, 97, 99, 104, 101, 0, 0, 0, 6, 1, 2, 2, 2, 2, 2, 3, 0, 4, 83, 101, 110, 100, 0, 0, 0, 80, 0
		};
		String exp = "=COMPOUND( Recv=INT(80), RSControl=BYTE(1), Facing=BYTE(3), Energy=INT(400000), SideCache=BYTE-ARRAY( 1, 2, 2, 2, 2, 2 ), Send=INT(80) )";
		doTest( test, exp);
	}

	@Test
	public void testParse6() {
		byte[] test = {
			10, 0, 0, 10, 0, 9, 70, 111, 114, 103, 101, 68, 97, 116, 97, 10, 0, 3, 115, 65, 73, 1, 0, 15, 65, 118, 111, 105, 100, 69, 120, 112, 108, 111, 115, 105, 111, 110, 115, 1, 1, 0, 13, 68, 101, 102, 101, 110, 100, 86, 105, 108, 108, 97, 103, 101, 0, 1, 0, 8, 71, 114, 105, 101, 102, 105, 110, 103, 0, 10, 0, 3, 117, 65, 73, 0, 1, 0, 5, 82, 105, 100, 101, 114, 0, 0, 3, 0, 19, 72, 117, 110, 103, 101, 114, 79, 118, 101, 114, 104, 97, 117, 108, 67, 104, 101, 99, 107, 0, 0, 0, 6, 0, 9, 0, 10, 65, 116, 116, 114, 105, 98, 117, 116, 101, 115, 10, 0, 0, 0, 4, 6, 0, 4, 66, 97, 115, 101, 64, 36, 0, 0, 0, 0, 0, 0, 8, 0, 4, 78, 97, 109, 101, 0, 17, 103, 101, 110, 101, 114, 105, 99, 46, 109, 97, 120, 72, 101, 97, 108, 116, 104, 0, 6, 0, 4, 66, 97, 115, 101, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 4, 78, 97, 109, 101, 0, 27, 103, 101, 110, 101, 114, 105, 99, 46, 107, 110, 111, 99, 107, 98, 97, 99, 107, 82, 101, 115, 105, 115, 116, 97, 110, 99, 101, 0, 6, 0, 4, 66, 97, 115, 101, 63, -55, -103, -103, -96,
			0, 0, 0, 8, 0, 4, 78, 97, 109, 101, 0, 21, 103, 101, 110, 101, 114, 105, 99, 46, 109, 111, 118, 101, 109, 101, 110, 116, 83, 112, 101, 101, 100, 0, 6, 0, 4, 66, 97, 115, 101, 64, 48, 0, 0, 0, 0, 0, 0, 9, 0, 9, 77, 111, 100, 105, 102, 105, 101, 114, 115, 10, 0, 0, 0, 1, 4, 0, 8, 85, 85, 73, 68, 77, 111, 115, 116, 22, 29, 19, -67, 94, 28, 79, -63, 4, 0, 9, 85, 85, 73, 68, 76, 101, 97, 115, 116, -99, -60, -71, -43, 118, -53, 57, 86, 6, 0, 6, 65, 109, 111, 117, 110, 116, -65, -91, 111, 123, 97, 42, -50, -44, 3, 0, 9, 79, 112, 101, 114, 97, 116, 105, 111, 110, 0, 0, 0, 1, 8, 0, 4, 78, 97, 109, 101, 0, 18, 82, 97, 110, 100, 111, 109, 32, 115, 112, 97, 119, 110, 32, 98, 111, 110, 117, 115, 0, 8, 0, 4, 78, 97, 109, 101, 0, 19, 103, 101, 110, 101, 114, 105, 99, 46, 102, 111, 108, 108, 111, 119, 82, 97, 110, 103, 101, 0, 1, 0, 12, 73, 110, 118, 117, 108, 110, 101, 114, 97, 98, 108, 101, 0, 3, 0, 14, 80, 111, 114, 116, 97, 108, 67, 111, 111, 108, 100, 111, 119, 110, 0, 0, 0, 0, 5,
			0, 16, 65, 98, 115, 111, 114, 112, 116, 105, 111, 110, 65, 109, 111, 117, 110, 116, 0, 0, 0, 0, 5, 0, 12, 70, 97, 108, 108, 68, 105, 115, 116, 97, 110, 99, 101, 0, 0, 0, 0, 3, 0, 6, 73, 110, 76, 111, 118, 101, 0, 0, 0, 0, 2, 0, 9, 68, 101, 97, 116, 104, 84, 105, 109, 101, 0, 0, 1, 0, 6, 105, 115, 83, 111, 117, 108, 0, 8, 0, 17, 106, 97, 114, 114, 101, 100, 67, 114, 105, 116, 116, 101, 114, 78, 97, 109, 101, 0, 3, 67, 111, 119, 9, 0, 11, 68, 114, 111, 112, 67, 104, 97, 110, 99, 101, 115, 5, 0, 0, 0, 5, 61, -82, 20, 123, 61, -82, 20, 123, 61, -82, 20, 123, 61, -82, 20, 123, 61, -82, 20, 123, 1, 0, 19, 80, 101, 114, 115, 105, 115, 116, 101, 110, 99, 101, 82, 101, 113, 117, 105, 114, 101, 100, 0, 8, 0, 2, 105, 100, 0, 3, 67, 111, 119, 5, 0, 5, 72, 101, 97, 108, 70, 65, 16, 0, 0, 3, 0, 3, 65, 103, 101, 0, 0, 0, 0, 9, 0, 6, 77, 111, 116, 105, 111, 110, 6, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, -65, -76, 18, 5, -62, -113, 92, 41, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 7, 76, 101, 97, 115,
			104, 101, 100, 0, 4, 0, 9, 85, 85, 73, 68, 76, 101, 97, 115, 116, -95, 100, 96, 121, -20, -88, -40, 65, 2, 0, 6, 72, 101, 97, 108, 116, 104, 0, 9, 2, 0, 3, 65, 105, 114, 1, 44, 1, 0, 8, 79, 110, 71, 114, 111, 117, 110, 100, 1, 3, 0, 9, 68, 105, 109, 101, 110, 115, 105, 111, 110, 0, 0, 0, 0, 9, 0, 8, 82, 111, 116, 97, 116, 105, 111, 110, 5, 0, 0, 0, 2, -59, 93, -14, -124, 0, 0, 0, 0, 10, 0, 16, 67, 114, 101, 97, 116, 117, 114, 101, 73, 110, 102, 117, 115, 105, 111, 110, 11, 0, 15, 80, 108, 97, 121, 101, 114, 73, 110, 102, 117, 115, 105, 111, 110, 115, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 13, 73, 110, 102, 117, 115, 105, 111, 110, 67, 111, 115, 116, 115, 9, 0, 7, 65, 115, 112, 101, 99, 116, 115, 0, 0, 0, 0, 0, 0, 3, 0, 13, 116, 117, 109, 111, 114, 87, 97, 114, 112, 84, 101, 109, 112, 0, 0, 0, 0, 11, 0, 9, 73, 110, 102, 117, 115, 105, 111, 110, 115,
			0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 9, 116, 117, 109, 111, 114, 87, 97, 114, 112, 0, 0, 0, 0, 1, 0, 7, 115, 105, 116, 116, 105, 110, 103, 0, 0, 4, 0, 8, 85, 85, 73, 68, 77, 111, 115, 116, 20, -110, 61, -107, 49, 113, 67, -95, 9, 0, 9, 69, 113, 117, 105, 112, 109, 101, 110, 116, 10, 0, 0, 0, 5, 0, 0, 0, 0, 0, 8, 0, 10, 67, 117, 115, 116, 111, 109, 78, 97, 109, 101, 0, 0, 9, 0, 3, 80, 111, 115, 6, 0, 0, 0, 3, -64, -117, 16, 49, -45, -16, -97, -122, 64, 79, -128, 0, 0, 0, 0, 0, 64, -113, 72, 58, 102, 102, 104, 74, 2, 0, 4, 70, 105, 114, 101, -1, -1, 1, 0, 13, 67, 97, 110, 80, 105, 99, 107, 85, 112, 76, 111, 111, 116, 0, 2, 0, 8, 72, 117, 114, 116, 84, 105, 109, 101, 0, 0, 2, 0, 10, 65, 116, 116, 97, 99, 107, 84, 105, 109, 101, 0, 0, 1, 0, 17, 67, 117, 115, 116, 111, 109, 78, 97, 109, 101, 86, 105, 115, 105, 98, 108, 101, 0, 0
		};
		String exp = "=COMPOUND( ForgeData=COMPOUND( sAI=COMPOUND( AvoidExplosions=BYTE(1), DefendVillage=BYTE(0), Griefing=BYTE(0), uAI=COMPOUND(  ), Rider=BYTE(0) ), HungerOverhaulCheck=INT(6) ), Attributes=LIST( COMPOUND( Base=DOUBLE(10.0), Name=STRING('generic.maxHealth') ), COMPOUND( Base=DOUBLE(0.0), Name=STRING('generic.knockbackResistance') ), COMPOUND( Base=DOUBLE(0.20000000298023224), Name=STRING('generic.movementSpeed') ), COMPOUND( Base=DOUBLE(16.0), Modifiers=LIST( COMPOUND( UUIDMost=LONG(1593451547207684033), UUIDLeast=LONG(-7078328387847374506), Amount=DOUBLE(-0.04186616479037544), Operation=INT(1), Name=STRING('Random spawn bonus') ) ), Name=STRING('generic.followRange') ) ), Invulnerable=BYTE(0), PortalCooldown=INT(0), AbsorptionAmount=FLOAT(0.0), FallDistance=FLOAT(0.0), InLove=INT(0), DeathTime=SHORT(0), isSoul=BYTE(0), jarredCritterName=STRING('Cow'), DropChances=LIST( FLOAT(0.085), FLOAT(0.085), FLOAT(0.085), FLOAT(0.085), FLOAT(0.085) ), PersistenceRequired=BYTE(0), id=STRING('Cow'), HealF=FLOAT(9.0), Age=INT(0), Motion=LIST( DOUBLE(0.0), DOUBLE(-0.0784000015258789), DOUBLE(0.0) ), Leashed=BYTE(0), UUIDLeast=LONG(-6817217859154290623), Health=SHORT(9), Air=SHORT(300), OnGround=BYTE(1), Dimension=INT(0), Rotation=LIST( FLOAT(-3551.1572), FLOAT(0.0) ), CreatureInfusion=COMPOUND( PlayerInfusions=INT-ARRAY( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ), InfusionCosts=COMPOUND( Aspects=LIST(  ) ), tumorWarpTemp=INT(0), Infusions=INT-ARRAY( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ), tumorWarp=INT(0), sitting=BYTE(0) ), UUIDMost=LONG(1482314938347242401), Equipment=LIST( COMPOUND(  ), COMPOUND(  ), COMPOUND(  ), COMPOUND(  ), COMPOUND(  ) ), CustomName=STRING(''), Pos=LIST( DOUBLE(-866.0243300245936), DOUBLE(63.0), DOUBLE(1001.028515625055) ), Fire=SHORT(-1), CanPickUpLoot=BYTE(0), HurtTime=SHORT(0), AttackTime=SHORT(0), CustomNameVisible=BYTE(0) )";
		doTest( test, exp);
	}

	@Test
	public void testParse7() {
		byte[] test = {
			10, 0, 0, 9, 0, 4, 101, 110, 99, 104, 10, 0, 0, 0, 2, 2, 0, 3, 108, 118, 108, 0, 6, 2, 0, 2, 105, 100, 0, 34, 0, 2, 0, 3, 108, 118, 108, 0, 6, 2, 0, 2, 105, 100, 0, 35, 0, 0
		};
		String exp = "=COMPOUND( ench=LIST( COMPOUND( lvl=SHORT(6), id=SHORT(34) ), COMPOUND( lvl=SHORT(6), id=SHORT(35) ) ) )";
		doTest( test, exp);
	}

	@Test
	public void testParse8() {
		byte[] test = {
			10, 0, 0, 3, 0, 4, 83, 101, 110, 100, 0, 0, 3, 32, 1, 0, 6, 70, 97, 99, 105, 110, 103, 3, 3, 0, 6, 69, 110, 101, 114, 103, 121, 0, 30, -124, -128, 3, 0, 4, 82, 101, 99, 118, 0, 0, 3, 32, 7, 0, 9, 83, 105, 100, 101, 67, 97, 99, 104, 101, 0, 0, 0, 6, 1, 2, 2, 2, 2, 2, 1, 0, 9, 82, 83, 67, 111, 110, 116, 114, 111, 108, 1, 0
		};
		String exp = "=COMPOUND( Send=INT(800), Facing=BYTE(3), Energy=INT(2000000), Recv=INT(800), SideCache=BYTE-ARRAY( 1, 2, 2, 2, 2, 2 ), RSControl=BYTE(1) )";
		doTest( test, exp);
	}
}
