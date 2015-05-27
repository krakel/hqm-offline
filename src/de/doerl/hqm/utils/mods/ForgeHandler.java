package de.doerl.hqm.utils.mods;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

public class ForgeHandler implements IHandler {
	private static Logger LOGGER = Logger.getLogger( ForgeHandler.class.getName());
	private static final String ITEMS = "assets/minecraft/textures/items";
	private static final String BLOCKS = "assets/minecraft/textures/blocks";
	private static HashMap<String, String> sNames;
	static {
		sNames = new HashMap<>();
		sNames.put( "apple", "apple%0");
		sNames.put( "apple_golden", "golden_apple%0");
		sNames.put( "arrow", "arrow%0");
		sNames.put( "bed", "bed%0");
		sNames.put( "beef_cooked", "cooked_beef%0");
		sNames.put( "beef_raw", "beef%0");
		sNames.put( "blaze_powder", "blaze_powder%0");
		sNames.put( "blaze_rod", "blaze_rod%0");
		sNames.put( "boat", "boat%0");
		sNames.put( "bone", "bone%0");
		sNames.put( "book_enchanted", "enchanted_book%0");
		sNames.put( "book_normal", "book%0");
		sNames.put( "book_writable", "writable_book%0");
		sNames.put( "book_written", "written_book%0");
		sNames.put( "bow", "bow%0");
		sNames.put( "bowl", "bowl%0");
		sNames.put( "bread", "bread%0");
		sNames.put( "brewing_stand", "brewing_stand%0");
		sNames.put( "brick", "brick%0");
		sNames.put( "bucket_empty", "bucket%0");
		sNames.put( "bucket_lava", "lava_bucket%0");
		sNames.put( "bucket_milk", "milk_bucket%0");
		sNames.put( "bucket_water", "water_bucket%0");
		sNames.put( "cake", "cake%0");
		sNames.put( "carrot", "carrot%0");
		sNames.put( "carrot_golden", "golden_carrot%0");
		sNames.put( "carrot_on_a_stick", "carrot_on_a_stick%0");
		sNames.put( "cauldron", "cauldron%0");
		sNames.put( "chainmail_boots", "chainmail_boots%0");
		sNames.put( "chainmail_chestplate", "chainmail_chestplate%0");
		sNames.put( "chainmail_helmet", "chainmail_helmet%0");
		sNames.put( "chainmail_leggings", "chainmail_leggings%0");
		sNames.put( "chicken_cooked", "cooked_chicken%0");
		sNames.put( "chicken_raw", "chicken%0");
		sNames.put( "clay_ball", "clay_ball%0");
		sNames.put( "coal", "coal%0");
		sNames.put( "comparator", "comparator%0");
		sNames.put( "clock", "clock%0");
		sNames.put( "compass", "compass%0");
		sNames.put( "cookie", "cookie%0");
		sNames.put( "diamond", "diamond%0");
		sNames.put( "diamond_axe", "diamond_axe%0");
		sNames.put( "diamond_boots", "diamond_boots%0");
		sNames.put( "diamond_chestplate", "diamond_chestplate%0");
		sNames.put( "diamond_helmet", "diamond_helmet%0");
		sNames.put( "diamond_hoe", "diamond_hoe%0");
		sNames.put( "diamond_horse_armor", "diamond_horse_armor%0");
		sNames.put( "diamond_leggings", "diamond_leggings%0");
		sNames.put( "diamond_pickaxe", "diamond_pickaxe%0");
		sNames.put( "diamond_shovel", "diamond_shovel%0");
		sNames.put( "diamond_sword", "diamond_sword%0");
		sNames.put( "door_iron", "iron_door%0");
		sNames.put( "door_wood", "wooden_door%0");
		sNames.put( "dye_powder_black", "dye%0");
		sNames.put( "dye_powder_red", "dye%1");
		sNames.put( "dye_powder_green", "dye%2");
		sNames.put( "dye_powder_brown", "dye%3");
		sNames.put( "dye_powder_blue", "dye%4");
		sNames.put( "dye_powder_purple", "dye%5");
		sNames.put( "dye_powder_cyan", "dye%6");
		sNames.put( "dye_powder_silver", "dye%7");
		sNames.put( "dye_powder_gray", "dye%8");
		sNames.put( "dye_powder_pink", "dye%9");
		sNames.put( "dye_powder_lime", "dye%10");
		sNames.put( "dye_powder_yellow", "dye%11");
		sNames.put( "dye_powder_light_blue", "dye%12");
		sNames.put( "dye_powder_magenta", "dye%13");
		sNames.put( "dye_powder_orange", "dye%14");
		sNames.put( "dye_powder_white", "dye%15");
		sNames.put( "egg", "egg%0");
		sNames.put( "emerald", "emerald%0");
		sNames.put( "ender_eye", "ender_eye%0");
		sNames.put( "ender_pearl", "ender_pearl%0");
		sNames.put( "experience_bottle", "experience_bottle%0");
		sNames.put( "feather", "feather%0");
		sNames.put( "fireball", "fire_charge%0");
		sNames.put( "fireworks_charge", "firework_charge%0");
		sNames.put( "fireworks", "fireworks%0");
		sNames.put( "fish_cooked", "cooked_fished%0");
		sNames.put( "fish_raw", "fish%0");
		sNames.put( "fishing_rod_uncast", "fishing_rod%0");
		sNames.put( "flint", "flint%0");
		sNames.put( "flint_and_steel", "flint_and_steel%0");
		sNames.put( "flower_pot", "flower_pot%0");
		sNames.put( "ghast_tear", "ghast_tear%0");
		sNames.put( "glowstone_dust", "glowstone_dust%0");
		sNames.put( "gold_ingot", "gold_ingot%0");
		sNames.put( "gold_nugget", "gold_nugget%0");
		sNames.put( "gold_axe", "golden_axe%0");
		sNames.put( "gold_boots", "golden_boots%0");
		sNames.put( "gold_chestplate", "golden_chestplate%0");
		sNames.put( "gold_helmet", "golden_helmet%0");
		sNames.put( "gold_hoe", "golden_hoe%0");
		sNames.put( "gold_horse_armor", "golden_horse_armor%0");
		sNames.put( "gold_leggings", "golden_leggings%0");
		sNames.put( "gold_pickaxe", "golden_pickaxe%0");
		sNames.put( "gold_shovel", "golden_shovel%0");
		sNames.put( "gold_sword", "golden_sword%0");
		sNames.put( "gunpowder", "gunpowder%0");
		sNames.put( "iron_axe", "iron_axe%0");
		sNames.put( "iron_boots", "iron_boots%0");
		sNames.put( "iron_chestplate", "iron_chestplate%0");
		sNames.put( "iron_ingot", "iron_ingot%0");
		sNames.put( "iron_helmet", "iron_helmet%0");
		sNames.put( "iron_hoe", "iron_hoe%0");
		sNames.put( "iron_horse_armor", "iron_horse_armor%0");
		sNames.put( "iron_leggings", "iron_leggings%0");
		sNames.put( "iron_pickaxe", "iron_pickaxe%0");
		sNames.put( "iron_shovel", "iron_shovel%0");
		sNames.put( "iron_sword", "iron_sword%0");
		sNames.put( "item_frame", "item_frame%0");
		sNames.put( "lead", "lead%0");
		sNames.put( "leather", "leather%0");
		sNames.put( "leather_boots", "leather_boots%0");
		sNames.put( "leather_chestplate", "leather_chestplate%0");
		sNames.put( "leather_helmet", "leather_helmet%0");
		sNames.put( "leather_leggings", "leather_leggings%0");
		sNames.put( "magma_cream", "magma_cream%0");
		sNames.put( "map_empty", "map%0");
		sNames.put( "melon", "melon%0");
		sNames.put( "map_filled", "filled_map%0");
		sNames.put( "melon_speckled", "speckled_melon%0");
		sNames.put( "minecart_chest", "chest_minecart%0");
		sNames.put( "minecart_command_block", "command_block_minecart%0");
		sNames.put( "minecart_furnace", "furnace_minecart%0");
		sNames.put( "minecart_hopper", "hopper_minecart%0");
		sNames.put( "minecart_normal", "minecart%0");
		sNames.put( "minecart_tnt", "tnt_minecart%0");
		sNames.put( "mushroom_stew", "mushroom_stew%0");
		sNames.put( "name_tag", "name_tag%0");
		sNames.put( "nether_wart", "nether_wart%0");
		sNames.put( "nether_star", "nether_star%0");
		sNames.put( "netherbrick", "netherbrick%0");
		sNames.put( "painting", "painting%0");
		sNames.put( "paper", "paper%0");
		sNames.put( "potato_baked", "baked_potato%0");
		sNames.put( "potato_poisonous", "poisonous_potato%0");
		sNames.put( "porkchop_cooked", "cooked_porkchop%0");
		sNames.put( "porkchop_raw", "porkchop%0");
		sNames.put( "potato", "potato%0");
		sNames.put( "potion_bottle_drinkable", "potion%0");
		sNames.put( "potion_bottle_splash", "potion%1");
		sNames.put( "potion_overlay", "potion%2");
		sNames.put( "potion_bottle_empty", "glass_bottle");
		sNames.put( "pumpkin_pie", "pumpkin_pie%0");
		sNames.put( "quartz", "quartz%0");
		sNames.put( "record_11", "record_11%0");
		sNames.put( "record_13", "record_13%0");
		sNames.put( "record_blocks", "record_blocks%0");
		sNames.put( "record_cat", "record_cat%0");
		sNames.put( "record_chirp", "record_chirp%0");
		sNames.put( "record_far", "record_far%0");
		sNames.put( "record_mall", "record_mall%0");
		sNames.put( "record_mellohi", "record_mellohi%0");
		sNames.put( "record_stal", "record_stal%0");
		sNames.put( "record_strad", "record_strad%0");
		sNames.put( "record_wait", "record_wait%0");
		sNames.put( "record_ward", "record_ward%0");
		sNames.put( "redstone_dust", "redstone%0");
		sNames.put( "reeds", "reeds%0");
		sNames.put( "repeater", "repeater%0");
		sNames.put( "rotten_flesh", "rotten_flesh%0");
		sNames.put( "saddle", "saddle%0");
		sNames.put( "seeds_pumpkin", "pumpkin_seeds%0");
		sNames.put( "seeds_melon", "melon_seeds%0");
		sNames.put( "seeds_wheat", "wheat_seeds%0");
		sNames.put( "shears", "shears%0");
		sNames.put( "sign", "sign%0");
		sNames.put( "skull_skeleton", "skull%0");
		sNames.put( "skull_wither", "skull%1");
		sNames.put( "skull_zombie", "skull%2");
		sNames.put( "skull_steve", "skull%3");
		sNames.put( "skull_creeper", "skull%4");
		sNames.put( "slimeball", "slime_ball%0");
		sNames.put( "snowball", "snowball%0");
		sNames.put( "spawn_egg", "spawn_egg%0");
		sNames.put( "spider_eye", "spider_eye%0");
		sNames.put( "spider_eye_fermented", "fermented_spider_eye%0");
		sNames.put( "stick", "stick%0");
		sNames.put( "stone_axe", "stone_axe%0");
		sNames.put( "stone_hoe", "stone_hoe%0");
		sNames.put( "stone_pickaxe", "stone_pickaxe%0");
		sNames.put( "stone_shovel", "stone_shovel%0");
		sNames.put( "stone_sword", "stone_sword%0");
		sNames.put( "string", "string%0");
		sNames.put( "sugar", "sugar%0");
		sNames.put( "wheat", "wheat%0");
		sNames.put( "wood_axe", "wooden_axe%0");
		sNames.put( "wood_hoe", "wooden_hoe%0");
		sNames.put( "wood_pickaxe", "wooden_pickaxe%0");
		sNames.put( "wood_shovel", "wooden_shovel%0");
		sNames.put( "wood_sword", "wooden_sword%0");
//
		sNames.put( "anvil_base", "anvil%0");
		sNames.put( "beacon", "beacon%0");
		sNames.put( "bed", "bed%0");
		sNames.put( "bedrock", "bedrock%0");
		sNames.put( "bookshelf", "bookshelf%0");
		sNames.put( "brewing_stand", "brewing_stand%0");
		sNames.put( "brick", "brick_block%0");
		sNames.put( "cactus_side", "cactus%0");
		sNames.put( "cake_side", "cake%0");
		sNames.put( "coal_block", "coal_block%0");
		sNames.put( "coal_ore", "coal_ore%0");
		sNames.put( "cobblestone", "cobblestone%0");
		sNames.put( "cobblestone_mossy", "mossy_cobblestone%0");
		sNames.put( "cocoa_stage_2", "cocoa%0");
		sNames.put( "command_block", "command_block%0");
		sNames.put( "crafting_table_side", "crafting_table%0");
		sNames.put( "daylight_detector_side", "daylight_detector%0");
		sNames.put( "deadbush", "deadbush%0");
		sNames.put( "diamond_block", "diamond_block%0");
		sNames.put( "diamond_ore", "diamond_ore%0");
		sNames.put( "dirt", "dirt%0");
		sNames.put( "dispenser_front_horizontal", "dispenser%0");
		sNames.put( "dragon_egg", "dragon_egg%0");
		sNames.put( "dropper", "dropper%0");
		sNames.put( "enchanting_table_side", "enchanting_table%0");
		sNames.put( "endframe_side", "end_portal_frame%0");
		sNames.put( "end_stone", "end_stone%0");
		sNames.put( "emerald_block", "emerald_block%0");
		sNames.put( "emerald_ore", "emerald_ore%0");
		sNames.put( "farmland_dry", "farmland%0");
		sNames.put( "furnace_side", "furnace%0");
		sNames.put( "flower_allium", "allium_flower%0");
		sNames.put( "flower_dandelion", "yellow_flower%0");
		sNames.put( "flower_houstonia", "houstonia_flower%0");
		sNames.put( "flower_oxeye_daisy", "oxeye_daisy_flower%0");
		sNames.put( "flower_paeonia", "paeonia_flower%0");
		sNames.put( "flower_pot", "flower_pot%0");
		sNames.put( "flower_rose", "red_flower%0");
		sNames.put( "flower_tulip_orange", "tulip_orange_flower%0");
		sNames.put( "flower_tulip_pink", "tulip_pink_flower%0");
		sNames.put( "flower_tulip_red", "tulip_red_flower%0");
		sNames.put( "flower_tulip_white", "tulip_white_flower%0");
		sNames.put( "glass", "glass%0");
		sNames.put( "glass_white", "glass_pane%0");
		sNames.put( "glowstone", "glowstone%0");
		sNames.put( "gold_block", "gold_block%0");
		sNames.put( "gold_ore", "gold_ore%0");
		sNames.put( "grass_side", "grass%0");
		sNames.put( "gravel", "gravel%0");
		sNames.put( "hardened_clay", "hardened_clay%0");
		sNames.put( "hay_block_side", "hay_block%0");
		sNames.put( "ice", "ice%0");
		sNames.put( "ice_packed", "packed_ice%0");
		sNames.put( "iron_bars", "iron_bars%0");
		sNames.put( "iron_block", "iron_block%0");
		sNames.put( "iron_ore", "iron_ore%0");
		sNames.put( "jukebox_side", "jukebox%0");
		sNames.put( "ladder", "ladder%0");
		sNames.put( "lapis_ore", "lapis_ore%0");
		sNames.put( "lapis_block", "lapis_block%0");
		sNames.put( "lava_still", "lava%0");
		sNames.put( "leaves_oak", "leaves2%0");
		sNames.put( "leaves_oak_opaque", "leaves%0");
		sNames.put( "lever", "lever%0");
		sNames.put( "log_oak", "log%0");
		sNames.put( "log_big_oak", "log2%0");
		sNames.put( "melon_side", "melon_block%0");
		sNames.put( "mob_spawner", "mob_spawner%0");
		sNames.put( "mushroom_brown", "brown_mushroom%0");
		sNames.put( "mushroom_red", "red_mushroom%0");
		sNames.put( "mycelium_side", "mycelium%0");
		sNames.put( "nether_brick", "nether_brick%0");
		sNames.put( "netherrack", "netherrack%0");
		sNames.put( "noteblock", "noteblock%0");
		sNames.put( "obsidian", "obsidian%0");
		sNames.put( "piston_side", "piston%0");
		sNames.put( "piston_top_sticky", "sticky_piston%0");
		sNames.put( "planks_oak", "planks%0");
		sNames.put( "pumpkin_side", "pumpkin%0");
		sNames.put( "quartz_block", "quartz_block%0");
		sNames.put( "quartz_ore", "quartz_ore%0");
		sNames.put( "quartz_block_side", "quartz_stairs%0");
		sNames.put( "rail_activator", "activator_rail%0");
		sNames.put( "rail_detector", "detector_rail%0");
		sNames.put( "rail_golden", "golden_rail%0");
		sNames.put( "rail_normal", "rail%0");
		sNames.put( "redstone_block", "redstone_block%0");
		sNames.put( "redstone_lamp_on", "lit_redstone_lamp%0");
		sNames.put( "redstone_lamp_off", "redstone_lamp%0");
		sNames.put( "redstone_ore", "lit_redstone_ore%0");
		sNames.put( "redstone_ore", "redstone_ore%0");
		sNames.put( "redstone_torch_on", "redstone_torch%0");
		sNames.put( "reeds", "reeds%0");
		sNames.put( "sapling_oak", "sapling%0");
		sNames.put( "sand", "sand%0");
		sNames.put( "sandstone_top", "sandstone%0");
		sNames.put( "snow", "snow%0");
		sNames.put( "soul_sand", "soul_sand%0");
		sNames.put( "sponge", "sponge%0");
		sNames.put( "stone", "stone%0");
		sNames.put( "stonebrick", "stone_stairs%0");
		sNames.put( "stonebrick", "stonebrick%0");
		sNames.put( "tallgrass", "tallgrass%0");
		sNames.put( "trip_wire", "tripwire%0");
		sNames.put( "trip_wire_source", "tripwire_hook%0");
		sNames.put( "tnt_side", "tnt%0");
		sNames.put( "torch_on", "torch%0");
		sNames.put( "trapdoor", "trapdoor%0");
		sNames.put( "vine", "vine%0");
		sNames.put( "water_still", "water%0");
		sNames.put( "waterlily", "waterlily%0");
		sNames.put( "web", "web%0");
		sNames.put( "wool_colored_white", "wool%0");
		sNames.put( "wool_colored_orange", "wool%1");
		sNames.put( "wool_colored_magenta", "wool%2");
		sNames.put( "wool_colored_light_blue", "wool%3");
		sNames.put( "wool_colored_yellow", "wool%4");
		sNames.put( "wool_colored_lime", "wool%5");
		sNames.put( "wool_colored_pink", "wool%6");
		sNames.put( "wool_colored_gray", "wool%7");
		sNames.put( "wool_colored_silver", "wool%8");
		sNames.put( "wool_colored_cyan", "wool%9");
		sNames.put( "wool_colored_purple", "wool%10");
		sNames.put( "wool_colored_blue", "wool%11");
		sNames.put( "wool_colored_brown", "wool%12");
		sNames.put( "wool_colored_green", "wool%13");
		sNames.put( "wool_colored_red", "wool%14");
		sNames.put( "wool_colored_black", "wool%15");
//
//		sNames.put( "???", "acacia_stairs%0");
//		sNames.put( "???", "birch_stairs%0");
//		sNames.put( "???", "brick_stairs%0");
//		sNames.put( "???", "carpet%0");
//		sNames.put( "???", "chest%0");
//		sNames.put( "???", "cobblestone_wall%0");
//		sNames.put( "???", "dark_oak_stairs%0");
//		sNames.put( "???", "double_stone_slab%0");
//		sNames.put( "???", "double_wooden_slab%0");
//		sNames.put( "???", "ender_chest%0");
//		sNames.put( "???", "fence%0");
//		sNames.put( "???", "fence_gate%0");
//		sNames.put( "???", "heavy_weighted_pressure_plate%0");
//		sNames.put( "???", "iron_door%0");
//		sNames.put( "???", "jungle_stairs%0");
//		sNames.put( "???", "light_weighted_pressure_plate%0");
//		sNames.put( "???", "lit_furnace%0");
//		sNames.put( "???", "lit_pumpkin%0");
//		sNames.put( "???", "monster_egg%0");
//		sNames.put( "???", "nether_brick_fence%0");
//		sNames.put( "???", "nether_brick_stairs%0");
//		sNames.put( "???", "oak_stairs%0");
//		sNames.put( "???", "redstone_wire%0");
//		sNames.put( "???", "sandstone_stairs%0");
//		sNames.put( "???", "spruce_stairs%0");
//		sNames.put( "???", "stained_hardened_clay%0");
//		sNames.put( "???", "stained_glass%0");
//		sNames.put( "???", "stained_glass_pane%0");
//		sNames.put( "???", "standing_sign%0");
//		sNames.put( "???", "stone_brick_stairs%0");
//		sNames.put( "???", "stone_button%0");
//		sNames.put( "???", "stone_pressure_plate%0");
//		sNames.put( "???", "stone_slab%0");
//		sNames.put( "???", "trapped_chest%0");
//		sNames.put( "???", "wall_sign%0");
//		sNames.put( "???", "wooden_button%0");
//		sNames.put( "???", "wooden_door%0");
//		sNames.put( "???", "wooden_pressure_plate%0");
//		sNames.put( "???", "wooden_slab%0");
	}
	private HashMap<String, Image> mCache = new HashMap<>();
	private File mJarFile;

	public ForgeHandler() {
	}

	private String createFileName( String path) {
		int p1 = path.lastIndexOf( '/');
		int p2 = path.lastIndexOf( '.');
		if (p1 < 0 || p2 <= p1) {
			return null;
		}
		return path.substring( p1 + 1, p2);
	}

	private File getJarFile() {
		String path = PreferenceManager.getString( BaseDefaults.MINECRAFT_DIR);
		if (path != null) {
			File src = new File( path);
			if (src.isDirectory()) {
				File[] arr = src.listFiles();
				for (File curr : arr) {
					if (JarFilter.isJar( curr)) {
						return curr;
					}
				}
			}
		}
		return null;
	}

	private String getKey( String fileName) {
		String name = sNames.get( fileName);
		return name != null ? name : fileName;
	}

	@Override
	public String getName() {
		return "minecraft";
	}

	@Override
	public Image load( String stk) {
		if (mJarFile == null) {
			mJarFile = getJarFile();
			if (mJarFile == null) {
				mJarFile = new File( "."); // only one try
			}
			else {
				parseFile();
			}
		}
		Image img = mCache.get( stk);
		if (img == null) {
			img = ResourceManager.stringImage( "fo" + mCache.size());
			mCache.put( stk, img);
		}
		return img;
	}

	private void parseFile() {
		JarFile jar = null;
		try {
			jar = new JarFile( mJarFile);
			for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				String name = entry.getName();
				if (name.endsWith( ".mcmeta")) {
				}
				else if (name.startsWith( ITEMS)) {
					putIf( jar, entry, name, false);
				}
				else if (name.startsWith( BLOCKS)) {
					putIf( jar, entry, name, true);
				}
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			try {
				if (jar != null) {
					jar.close();
				}
			}
			catch (IOException ex) {
			}
		}
	}

	private void putIf( JarFile jar, JarEntry entry, String name, boolean ignore) throws IOException {
		String fileName = createFileName( name);
		if (Utils.validString( fileName)) {
			String key = getKey( fileName);
			if (ignore && mCache.containsKey( key)) {
//				Utils.log( LOGGER, Level.WARNING, "found double item name: {0}", key);
			}
			else {
				Image img = readImage( jar, entry);
				if (img != null) {
					mCache.put( key, img);
//					Utils.log( LOGGER, Level.WARNING, "found {0}", key);
				}
			}
		}
	}

	private Image readImage( JarFile jar, JarEntry entry) throws IOException {
		BufferedImage img = null;
		InputStream is = null;
		try {
			is = jar.getInputStream( entry);
			img = ImageIO.read( is);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
			}
			catch (Exception ex) {
			}
		}
		return img;
	}
}
