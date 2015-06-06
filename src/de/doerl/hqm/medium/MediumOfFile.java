package de.doerl.hqm.medium;

public class MediumOfFile implements IMediumWorker<IMedium, String> {
	private static final MediumOfFile WORKER = new MediumOfFile();

	private MediumOfFile() {
	}

	public static IMedium get( String file) {
		return MediaManager.forEachMedium( WORKER, file);
	}

	@Override
	public IMedium forMedium( IMedium m, String file) {
		return m.parse( file);
	}
}
