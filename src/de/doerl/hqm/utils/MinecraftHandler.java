package de.doerl.hqm.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class MinecraftHandler implements IHandler {
	private static Logger LOGGER = Logger.getLogger( ImageLoader.class.getName());
	private static FileFilter JAR_FILTER = new FileFilter() {
		@Override
		public boolean accept( File src) {
			return src.isFile() && src.getName().endsWith( ".jar");
		}
	};
	private static final String ITEMS = "assets/minecraft/textures/items";
	private static final String BLOCKS = "assets/minecraft/textures/blocks";
	private static HashMap<String, String> sNames;
	static {
		sNames = new HashMap<>();
		sNames.put( "apple", "apple");
		sNames.put( "apple_golden", "golden_apple");
		sNames.put( "arrow", "arrow");
		sNames.put( "bed", "bed");
		sNames.put( "beef_cooked", "cooked_beef");
		sNames.put( "beef_raw", "beef");
		sNames.put( "blaze_powder", "blaze_powder");
		sNames.put( "blaze_rod", "blaze_rod");
		sNames.put( "boat", "boat");
		sNames.put( "bone", "bone");
		sNames.put( "book_enchanted", "enchanted_book");
		sNames.put( "book_normal", "book");
		sNames.put( "book_writable", "writable_book");
		sNames.put( "book_written", "written_book");
		sNames.put( "bow", "bow");
		sNames.put( "bowl", "bowl");
		sNames.put( "bread", "bread");
		sNames.put( "brewing_stand", "brewing_stand");
		sNames.put( "brick", "brick");
		sNames.put( "bucket_empty", "bucket");
		sNames.put( "bucket_lava", "lava_bucket");
		sNames.put( "bucket_milk", "milk_bucket");
		sNames.put( "bucket_water", "water_bucket");
		sNames.put( "cake", "cake");
		sNames.put( "carrot", "carrot");
		sNames.put( "carrot_golden", "golden_carrot");
		sNames.put( "carrot_on_a_stick", "carrot_on_a_stick");
		sNames.put( "cauldron", "cauldron");
		sNames.put( "chainmail_boots", "chainmail_boots");
		sNames.put( "chainmail_chestplate", "chainmail_chestplate");
		sNames.put( "chainmail_helmet", "chainmail_helmet");
		sNames.put( "chainmail_leggings", "chainmail_leggings");
		sNames.put( "chicken_cooked", "cooked_chicken");
		sNames.put( "chicken_raw", "chicken");
		sNames.put( "clay_ball", "clay_ball");
		sNames.put( "coal", "coal");
		sNames.put( "comparator", "comparator");
		sNames.put( "clock", "clock");
		sNames.put( "compass", "compass");
		sNames.put( "cookie", "cookie");
		sNames.put( "diamond", "diamond");
		sNames.put( "diamond_axe", "diamond_axe");
		sNames.put( "diamond_boots", "diamond_boots");
		sNames.put( "diamond_chestplate", "diamond_chestplate");
		sNames.put( "diamond_helmet", "diamond_helmet");
		sNames.put( "diamond_hoe", "diamond_hoe");
		sNames.put( "diamond_horse_armor", "diamond_horse_armor");
		sNames.put( "diamond_leggings", "diamond_leggings");
		sNames.put( "diamond_pickaxe", "diamond_pickaxe");
		sNames.put( "diamond_shovel", "diamond_shovel");
		sNames.put( "diamond_sword", "diamond_sword");
		sNames.put( "door_iron", "iron_door");
		sNames.put( "door_wood", "wooden_door");
		sNames.put( "dye_powder", "dye");
		sNames.put( "egg", "egg");
		sNames.put( "emerald", "emerald");
		sNames.put( "ender_eye", "ender_eye");
		sNames.put( "ender_pearl", "ender_pearl");
		sNames.put( "experience_bottle", "experience_bottle");
		sNames.put( "feather", "feather");
		sNames.put( "fireball", "fire_charge");
		sNames.put( "fireworks_charge", "firework_charge");
		sNames.put( "fireworks", "fireworks");
		sNames.put( "fish_cooked", "cooked_fished");
		sNames.put( "fish_raw", "fish");
		sNames.put( "fishing_rod_uncast", "fishing_rod");
		sNames.put( "flint", "flint");
		sNames.put( "flint_and_steel", "flint_and_steel");
		sNames.put( "flower_pot", "flower_pot");
		sNames.put( "ghast_tear", "ghast_tear");
		sNames.put( "glowstone_dust", "glowstone_dust");
		sNames.put( "gold_ingot", "gold_ingot");
		sNames.put( "gold_nugget", "gold_nugget");
		sNames.put( "gold_axe", "golden_axe");
		sNames.put( "gold_boots", "golden_boots");
		sNames.put( "gold_chestplate", "golden_chestplate");
		sNames.put( "gold_helmet", "golden_helmet");
		sNames.put( "gold_hoe", "golden_hoe");
		sNames.put( "gold_horse_armor", "golden_horse_armor");
		sNames.put( "gold_leggings", "golden_leggings");
		sNames.put( "gold_pickaxe", "golden_pickaxe");
		sNames.put( "gold_shovel", "golden_shovel");
		sNames.put( "gold_sword", "golden_sword");
		sNames.put( "gunpowder", "gunpowder");
		sNames.put( "iron_axe", "iron_axe");
		sNames.put( "iron_boots", "iron_boots");
		sNames.put( "iron_chestplate", "iron_chestplate");
		sNames.put( "iron_ingot", "iron_ingot");
		sNames.put( "iron_helmet", "iron_helmet");
		sNames.put( "iron_hoe", "iron_hoe");
		sNames.put( "iron_horse_armor", "iron_horse_armor");
		sNames.put( "iron_leggings", "iron_leggings");
		sNames.put( "iron_pickaxe", "iron_pickaxe");
		sNames.put( "iron_shovel", "iron_shovel");
		sNames.put( "iron_sword", "iron_sword");
		sNames.put( "item_frame", "item_frame");
		sNames.put( "lead", "lead");
		sNames.put( "leather", "leather");
		sNames.put( "leather_boots", "leather_boots");
		sNames.put( "leather_chestplate", "leather_chestplate");
		sNames.put( "leather_helmet", "leather_helmet");
		sNames.put( "leather_leggings", "leather_leggings");
		sNames.put( "magma_cream", "magma_cream");
		sNames.put( "map_empty", "map");
		sNames.put( "melon", "melon");
		sNames.put( "map_filled", "filled_map");
		sNames.put( "melon_speckled", "speckled_melon");
		sNames.put( "minecart_chest", "chest_minecart");
		sNames.put( "minecart_command_block", "command_block_minecart");
		sNames.put( "minecart_furnace", "furnace_minecart");
		sNames.put( "minecart_hopper", "hopper_minecart");
		sNames.put( "minecart_normal", "minecart");
		sNames.put( "minecart_tnt", "tnt_minecart");
		sNames.put( "mushroom_stew", "mushroom_stew");
		sNames.put( "name_tag", "name_tag");
		sNames.put( "nether_wart", "nether_wart");
		sNames.put( "nether_star", "nether_star");
		sNames.put( "netherbrick", "netherbrick");
		sNames.put( "painting", "painting");
		sNames.put( "paper", "paper");
		sNames.put( "potato_baked", "baked_potato");
		sNames.put( "potato_poisonous", "poisonous_potato");
		sNames.put( "porkchop_cooked", "cooked_porkchop");
		sNames.put( "porkchop_raw", "porkchop");
		sNames.put( "potato", "potato");
		sNames.put( "potion", "potion");
		sNames.put( "potion_bottle_empty", "glass_bottle");
		sNames.put( "pumpkin_pie", "pumpkin_pie");
		sNames.put( "quartz", "quartz");
		sNames.put( "record_11", "record_11");
		sNames.put( "record_13", "record_13");
		sNames.put( "record_blocks", "record_blocks");
		sNames.put( "record_cat", "record_cat");
		sNames.put( "record_chirp", "record_chirp");
		sNames.put( "record_far", "record_far");
		sNames.put( "record_mall", "record_mall");
		sNames.put( "record_mellohi", "record_mellohi");
		sNames.put( "record_stal", "record_stal");
		sNames.put( "record_strad", "record_strad");
		sNames.put( "record_wait", "record_wait");
		sNames.put( "record_ward", "record_ward");
		sNames.put( "redstone_dust", "redstone");
		sNames.put( "reeds", "reeds");
		sNames.put( "repeater", "repeater");
		sNames.put( "rotten_flesh", "rotten_flesh");
		sNames.put( "saddle", "saddle");
		sNames.put( "seeds_pumpkin", "pumpkin_seeds");
		sNames.put( "seeds_melon", "melon_seeds");
		sNames.put( "seeds_wheat", "wheat_seeds");
		sNames.put( "shears", "shears");
		sNames.put( "sign", "sign");
		sNames.put( "skull_skeleton", "skull");
		sNames.put( "slimeball", "slime_ball");
		sNames.put( "snowball", "snowball");
		sNames.put( "spawn_egg", "spawn_egg");
		sNames.put( "spider_eye", "spider_eye");
		sNames.put( "spider_eye_fermented", "fermented_spider_eye");
		sNames.put( "stick", "stick");
		sNames.put( "stone_axe", "stone_axe");
		sNames.put( "stone_hoe", "stone_hoe");
		sNames.put( "stone_pickaxe", "stone_pickaxe");
		sNames.put( "stone_shovel", "stone_shovel");
		sNames.put( "stone_sword", "stone_sword");
		sNames.put( "string", "string");
		sNames.put( "sugar", "sugar");
		sNames.put( "wheat", "wheat");
		sNames.put( "wood_axe", "wooden_axe");
		sNames.put( "wood_hoe", "wooden_hoe");
		sNames.put( "wood_pickaxe", "wooden_pickaxe");
		sNames.put( "wood_shovel", "wooden_shovel");
		sNames.put( "wood_sword", "wooden_sword");
//
		sNames.put( "anvil_base", "anvil");
		sNames.put( "beacon", "beacon");
		sNames.put( "bed", "bed");
		sNames.put( "bedrock", "bedrock");
		sNames.put( "bookshelf", "bookshelf");
		sNames.put( "brewing_stand", "brewing_stand");
		sNames.put( "brick", "brick_block");
		sNames.put( "cactus_side", "cactus");
		sNames.put( "cake_side", "cake");
		sNames.put( "coal_block", "coal_block");
		sNames.put( "coal_ore", "coal_ore");
		sNames.put( "cobblestone", "cobblestone");
		sNames.put( "cobblestone_mossy", "mossy_cobblestone");
		sNames.put( "cocoa_stage_2", "cocoa");
		sNames.put( "command_block", "command_block");
		sNames.put( "crafting_table_side", "crafting_table");
		sNames.put( "daylight_detector_side", "daylight_detector");
		sNames.put( "deadbush", "deadbush");
		sNames.put( "diamond_block", "diamond_block");
		sNames.put( "diamond_ore", "diamond_ore");
		sNames.put( "dirt", "dirt");
		sNames.put( "dispenser_front_horizontal", "dispenser");
		sNames.put( "dragon_egg", "dragon_egg");
		sNames.put( "dropper", "dropper");
		sNames.put( "enchanting_table_side", "enchanting_table");
		sNames.put( "endframe_side", "end_portal_frame");
		sNames.put( "end_stone", "end_stone");
		sNames.put( "emerald_block", "emerald_block");
		sNames.put( "emerald_ore", "emerald_ore");
		sNames.put( "farmland_dry", "farmland");
		sNames.put( "furnace_side", "furnace");
		sNames.put( "flower_dandelion", "yellow_flower");
		sNames.put( "flower_pot", "flower_pot");
		sNames.put( "flower_rose", "red_flower");
		sNames.put( "glass", "glass");
		sNames.put( "glass_white", "glass_pane");
		sNames.put( "glowstone", "glowstone");
		sNames.put( "gold_block", "gold_block");
		sNames.put( "gold_ore", "gold_ore");
		sNames.put( "grass_side", "grass");
		sNames.put( "gravel", "gravel");
		sNames.put( "hardened_clay", "hardened_clay");
		sNames.put( "hay_block_side", "hay_block");
		sNames.put( "ice", "ice");
		sNames.put( "ice_packed", "packed_ice");
		sNames.put( "iron_bars", "iron_bars");
		sNames.put( "iron_block", "iron_block");
		sNames.put( "iron_ore", "iron_ore");
		sNames.put( "jukebox_side", "jukebox");
		sNames.put( "ladder", "ladder");
		sNames.put( "lapis_ore", "lapis_ore");
		sNames.put( "lapis_block", "lapis_block");
		sNames.put( "lava_still", "lava");
		sNames.put( "leaves_oak", "leaves2");
		sNames.put( "leaves_oak_opaque", "leaves");
		sNames.put( "lever", "lever");
		sNames.put( "log_oak", "log");
		sNames.put( "log_big_oak", "log2");
		sNames.put( "melon_side", "melon_block");
		sNames.put( "mob_spawner", "mob_spawner");
		sNames.put( "mushroom_brown", "brown_mushroom");
		sNames.put( "mushroom_red", "red_mushroom");
		sNames.put( "mycelium_side", "mycelium");
		sNames.put( "nether_brick", "nether_brick");
		sNames.put( "netherrack", "netherrack");
		sNames.put( "noteblock", "noteblock");
		sNames.put( "obsidian", "obsidian");
		sNames.put( "piston_side", "piston");
		sNames.put( "piston_top_sticky", "sticky_piston");
		sNames.put( "planks_oak", "planks");
		sNames.put( "pumpkin_side", "pumpkin");
		sNames.put( "quartz_block", "quartz_block");
		sNames.put( "quartz_ore", "quartz_ore");
		sNames.put( "quartz_block_side", "quartz_stairs");
		sNames.put( "rail_activator", "activator_rail");
		sNames.put( "rail_detector", "detector_rail");
		sNames.put( "rail_golden", "golden_rail");
		sNames.put( "rail_normal", "rail");
		sNames.put( "redstone_block", "redstone_block");
		sNames.put( "redstone_lamp_on", "lit_redstone_lamp");
		sNames.put( "redstone_lamp_off", "redstone_lamp");
		sNames.put( "redstone_ore", "lit_redstone_ore");
		sNames.put( "redstone_ore", "redstone_ore");
		sNames.put( "redstone_torch_on", "redstone_torch");
		sNames.put( "reeds", "reeds");
		sNames.put( "sapling_oak", "sapling");
		sNames.put( "sand", "sand");
		sNames.put( "sandstone_top", "sandstone");
		sNames.put( "snow", "snow");
		sNames.put( "soul_sand", "soul_sand");
		sNames.put( "sponge", "sponge");
		sNames.put( "stone", "stone");
		sNames.put( "stonebrick", "stone_stairs");
		sNames.put( "stonebrick", "stonebrick");
		sNames.put( "tallgrass", "tallgrass");
		sNames.put( "trip_wire", "tripwire");
		sNames.put( "trip_wire_source", "tripwire_hook");
		sNames.put( "tnt_side", "tnt");
		sNames.put( "torch_on", "torch");
		sNames.put( "trapdoor", "trapdoor");
		sNames.put( "vine", "vine");
		sNames.put( "water_still", "water");
		sNames.put( "waterlily", "waterlily");
		sNames.put( "web", "web");
		sNames.put( "wool_colored_white", "wool");
//
//		sNames.put( "???", "acacia_stairs");
//		sNames.put( "???", "birch_stairs");
//		sNames.put( "???", "brick_stairs");
//		sNames.put( "???", "carpet");
//		sNames.put( "???", "chest");
//		sNames.put( "???", "cobblestone_wall");
//		sNames.put( "???", "dark_oak_stairs");
//		sNames.put( "???", "double_stone_slab");
//		sNames.put( "???", "double_wooden_slab");
//		sNames.put( "???", "ender_chest");
//		sNames.put( "???", "fence");
//		sNames.put( "???", "fence_gate");
//		sNames.put( "???", "heavy_weighted_pressure_plate");
//		sNames.put( "???", "iron_door");
//		sNames.put( "???", "jungle_stairs");
//		sNames.put( "???", "light_weighted_pressure_plate");
//		sNames.put( "???", "lit_furnace");
//		sNames.put( "???", "lit_pumpkin");
//		sNames.put( "???", "monster_egg");
//		sNames.put( "???", "nether_brick_fence");
//		sNames.put( "???", "nether_brick_stairs");
//		sNames.put( "???", "oak_stairs");
//		sNames.put( "???", "redstone_wire");
//		sNames.put( "???", "sandstone_stairs");
//		sNames.put( "???", "spruce_stairs");
//		sNames.put( "???", "stained_hardened_clay");
//		sNames.put( "???", "stained_glass");
//		sNames.put( "???", "stained_glass_pane");
//		sNames.put( "???", "standing_sign");
//		sNames.put( "???", "stone_brick_stairs");
//		sNames.put( "???", "stone_button");
//		sNames.put( "???", "stone_pressure_plate");
//		sNames.put( "???", "stone_slab");
//		sNames.put( "???", "trapped_chest");
//		sNames.put( "???", "wall_sign");
//		sNames.put( "???", "wooden_button");
//		sNames.put( "???", "wooden_door");
//		sNames.put( "???", "wooden_pressure_plate");
//		sNames.put( "???", "wooden_slab");
	}
	private HashMap<String, Image> mCache = new HashMap<>();
	private File mJarFile;

	public MinecraftHandler() {
	}

	private String createID( String path) {
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
					if (JAR_FILTER.accept( curr)) {
						return curr;
					}
				}
			}
		}
		return null;
	}

	private String getKey( String key) {
		String name = sNames.get( key);
		return name != null ? name : key;
	}

	@Override
	public String getName() {
		return "minecraft";
	}

	@Override
	public Image load( String stk) {
		if (mJarFile == null) {
			mJarFile = getJarFile();
			if (mJarFile != null) {
				parseFile();
			}
		}
		return mCache.get( stk);
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
		String id = createID( name);
		if (Utils.validString( id)) {
			String key = getKey( id);
			if (ignore && mCache.containsKey( key)) {
//				Utils.log( LOGGER, Level.WARNING, "found double item name: {0}", key);
			}
			else {
				Image img = readImage( jar, entry);
				if (img != null) {
					mCache.put( key, img);
					Utils.log( LOGGER, Level.WARNING, "found {0}", key);
//					int pos = key.lastIndexOf( '_');
//					if (pos > 0) {
//						mCache.put( key.substring( pos + 1) + '_' + key.substring( 0, pos), img);
//						mCache.put( key.substring( 0, pos), img);
//					}
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
