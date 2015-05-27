package de.doerl.hqm.utils.mods;

import java.io.File;
import java.io.FileFilter;

class JarFilter implements FileFilter {
	private static FileFilter FILTER = new JarFilter();

	private JarFilter() {
	}

	static boolean isJar( File curr) {
		return FILTER.accept( curr);
	}

	@Override
	public boolean accept( File src) {
		return src.isFile() && src.getName().endsWith( ".jar");
	}
}
