package de.doerl.hqm.utils.nbt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {
	private static void doTest( String test, String exp) {
		FCompound act = NbtParser.parse( test);
		assertEquals( exp, act.toString());
	}

	@Test
	public void testParse1() {
		String exp = "=COMPOUND()";
		doTest( exp, exp);
	}

	@Test
	public void testParse2() {
		String exp = "=COMPOUND(Energy=INT(400000))";
		doTest( exp, exp);
	}

	@Test
	public void testParse3() {
		String exp = "=COMPOUND(title=STRING('TG.personaltitle.seeker'))";
		doTest( exp, exp);
	}

	@Test
	public void testParse4() {
		String exp = "=COMPOUND(InfiTool=COMPOUND(BaseDurability=INT(800),Head=INT(55),HeadEXP=LONG(0),BaseAttack=INT(7),ToolEXP=LONG(0),HarvestLevel=INT(0),Attack=INT(7),RenderHead=INT(55),ModDurability=FLOAT(0.0),Handle=INT(598),Broken=BYTE(0),Shoddy=FLOAT(0.0),RenderHandle=INT(598),Accessory=INT(656),MiningSpeed=INT(500),RenderAccessory=INT(656),ToolLevel=INT(1),HarvestLevelModified=BYTE(0),Unbreaking=INT(2),Damage=INT(0),BonusDurability=INT(0),TotalDurability=INT(800),Modifiers=INT(0)),display=COMPOUND(Name=STRING('§fPick of Tears')))";
		doTest( exp, exp);
	}

	@Test
	public void testParse5() {
		String exp = "=COMPOUND(Recv=INT(80),RSControl=BYTE(1),Facing=BYTE(3),Energy=INT(400000),SideCache=BYTE-ARRAY(1,2,2,2,2,2),Send=INT(80))";
		doTest( exp, exp);
	}

	@Test
	public void testParse6() {
		String exp = "=COMPOUND(ForgeData=COMPOUND(sAI=COMPOUND(AvoidExplosions=BYTE(1),DefendVillage=BYTE(0),Griefing=BYTE(0),uAI=COMPOUND(),Rider=BYTE(0)),HungerOverhaulCheck=INT(6)),Attributes=LIST(COMPOUND(Base=DOUBLE(10.0),Name=STRING('generic.maxHealth')),COMPOUND(Base=DOUBLE(0.0),Name=STRING('generic.knockbackResistance')),COMPOUND(Base=DOUBLE(0.20000000298023224),Name=STRING('generic.movementSpeed')),COMPOUND(Base=DOUBLE(16.0),Modifiers=LIST(COMPOUND(UUIDMost=LONG(1593451547207684033),UUIDLeast=LONG(-7078328387847374506),Amount=DOUBLE(-0.04186616479037544),Operation=INT(1),Name=STRING('Random spawn bonus'))),Name=STRING('generic.followRange'))),Invulnerable=BYTE(0),PortalCooldown=INT(0),AbsorptionAmount=FLOAT(0.0),FallDistance=FLOAT(0.0),InLove=INT(0),DeathTime=SHORT(0),isSoul=BYTE(0),jarredCritterName=STRING('Cow'),DropChances=LIST(FLOAT(0.085),FLOAT(0.085),FLOAT(0.085),FLOAT(0.085),FLOAT(0.085)),PersistenceRequired=BYTE(0),id=STRING('Cow'),HealF=FLOAT(9.0),Age=INT(0),Motion=LIST(DOUBLE(0.0),DOUBLE(-0.0784000015258789),DOUBLE(0.0)),Leashed=BYTE(0),UUIDLeast=LONG(-6817217859154290623),Health=SHORT(9),Air=SHORT(300),OnGround=BYTE(1),Dimension=INT(0),Rotation=LIST(FLOAT(-3551.1572),FLOAT(0.0)),CreatureInfusion=COMPOUND(PlayerInfusions=INT-ARRAY(0,0,0,0,0,0,0,0,0,0,0,0),InfusionCosts=COMPOUND(Aspects=LIST()),tumorWarpTemp=INT(0),Infusions=INT-ARRAY(0,0,0,0,0,0,0,0,0,0,0,0),tumorWarp=INT(0),sitting=BYTE(0)),UUIDMost=LONG(1482314938347242401),Equipment=LIST(COMPOUND(),COMPOUND(),COMPOUND(),COMPOUND(),COMPOUND()),CustomName=STRING(''),Pos=LIST(DOUBLE(-866.0243300245936),DOUBLE(63.0),DOUBLE(1001.028515625055)),Fire=SHORT(-1),CanPickUpLoot=BYTE(0),HurtTime=SHORT(0),AttackTime=SHORT(0),CustomNameVisible=BYTE(0))";
		doTest( exp, exp);
	}

	@Test
	public void testParse7() {
		String exp = "=COMPOUND(ench=LIST(COMPOUND(lvl=SHORT(6),id=SHORT(34)),COMPOUND(lvl=SHORT(6),id=SHORT(35))))";
		doTest( exp, exp);
	}

	@Test
	public void testParse8() {
		String exp = "=COMPOUND(Send=INT(800),Facing=BYTE(3),Energy=INT(2000000),Recv=INT(800),SideCache=BYTE-ARRAY(1,2,2,2,2,2),RSControl=BYTE(1))";
		doTest( exp, exp);
	}
}
