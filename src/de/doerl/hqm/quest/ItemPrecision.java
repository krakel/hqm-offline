// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name: ItemPrecision.java
package de.doerl.hqm.quest;

public enum ItemPrecision {
	PRECISE,
	NBT_FUZZY,
	FUZZY,
	ORE_DICTIONARY;
	public static ItemPrecision get( int idx) {
		ItemPrecision[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}
}
