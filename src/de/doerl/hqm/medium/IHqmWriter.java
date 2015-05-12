package de.doerl.hqm.medium;

import de.doerl.hqm.base.FHqm;

public interface IHqmWriter {
	void closeDst();

	void writeDst( FHqm hqm, ICallback cb);
}
