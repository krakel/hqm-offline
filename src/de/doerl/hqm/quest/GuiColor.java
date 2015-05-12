package de.doerl.hqm.quest;

import de.doerl.hqm.utils.Helper;

public enum GuiColor {
	BLACK( 0, 0.1F, 0.1F, 0.1F),
	BLUE( 1, 0.2F, 0.3F, 0.7F),
	GREEN( 2, 0.4F, 0.5F, 0.2F),
	CYAN( 3, 0.3F, 0.5F, 0.6F),
	RED( 4, 0.6F, 0.2F, 0.2F),
	PURPLE( 5, 0.5F, 0.25F, 0.7F),
	ORANGE( 6, 0.85F, 0.5F, 0.2F),
	LIGHT_GRAY( 7, 0.6F, 0.6F, 0.6F),
	GRAY( 8, 0.3F, 0.3F, 0.3F),
	LIGHT_BLUE( 9, 0.4F, 0.6F, 0.85F),
	LIME( 10, 0.5F, 0.8F, 0.1F),
	TURQUOISE( 11, 0.0F, 1.0F, 0.9F),
	PINK( 12, 0.95F, 0.5F, 0.65F),
	MAGENTA( 13, 0.7F, 0.3F, 0.85F),
	YELLOW( 14, 0.9F, 0.9F, 0.2F),
	WHITE( 15, 0.4F, 0.3F, 0.2F);
	private int mHex;

	private GuiColor( int number, float red, float green, float blue) {
		mHex = 0xff000000 | (int) (red * 255F) << 16 | (int) (green * 255F) << 8 | (int) (blue * 255F) << 0;
	}

	public static GuiColor get( int idx) {
		GuiColor[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public int getHexColor() {
		return mHex;
	}

	@Override
	public String toString() {
		return Helper.toHex( mHex);
	}
}
