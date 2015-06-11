package de.doerl.hqm.utils.mods;

import java.awt.Image;

public interface IHandler {
	String getName();

	Image load( String key);
}
