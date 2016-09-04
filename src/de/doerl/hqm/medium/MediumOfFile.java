package de.doerl.hqm.medium;

import java.io.File;

public class MediumOfFile implements IMediumWorker<IMedium, File> {
	private static final MediumOfFile WORKER = new MediumOfFile();

	private MediumOfFile() {
	}

	public static IMedium get( File file) {
		return MediaManager.forEachMedium( WORKER, file);
	}

	@Override
	public IMedium forMedium( IMedium m, File file) {
		return m.parse( file);
	}
}
