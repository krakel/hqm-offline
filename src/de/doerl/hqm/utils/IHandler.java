package de.doerl.hqm.utils;

import java.awt.Image;

public interface IHandler {
	String getName();

	Image load( String stk);
}
