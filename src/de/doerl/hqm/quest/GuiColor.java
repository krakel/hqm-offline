package de.doerl.hqm.quest;

import java.awt.Color;

import de.doerl.hqm.utils.Helper;

public enum GuiColor {
	BLACK( 0.1F, 0.1F, 0.1F),
	BLUE( 0.2F, 0.3F, 0.7F),
	GREEN( 0.4F, 0.5F, 0.2F),
	CYAN( 0.3F, 0.5F, 0.6F),
	RED( 0.6F, 0.2F, 0.2F),
	PURPLE( 0.5F, 0.25F, 0.7F),
	ORANGE( 0.85F, 0.5F, 0.2F),
	LIGHT_GRAY( 0.6F, 0.6F, 0.6F),
	GRAY( 0.3F, 0.3F, 0.3F),
	LIGHT_BLUE( 0.4F, 0.6F, 0.85F),
	LIME( 0.5F, 0.8F, 0.1F),
	TURQUOISE( 0.0F, 1.0F, 0.9F),
	PINK( 0.95F, 0.5F, 0.65F),
	MAGENTA( 0.7F, 0.3F, 0.85F),
	YELLOW( 0.9F, 0.9F, 0.2F),
	WHITE( 0.4F, 0.3F, 0.2F);
	private int mHex;
	private Color mColor;

	private GuiColor( float r, float g, float b) {
		mColor = new Color( (int) (r * 255F), (int) (g * 255F), (int) (b * 255F));
		mHex = mColor.getRGB(); //0xff000000 | (int) (r * 0xFF) << 16 | (int) (g * 255F) << 8 | (int) (b * 255F) << 0;
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

	public Color getColor() {
		return mColor;
	}

	public int getHexColor() {
		return mHex;
	}

	@Override
	public String toString() {
		return Helper.toHex( mHex);
	}
}
