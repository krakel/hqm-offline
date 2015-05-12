package de.doerl.hqm.medium.bits;

import java.io.IOException;
import java.io.OutputStream;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IHqmWriter;

class Serializer implements IHqmWriter {
	public Serializer( OutputStream dst) throws IOException {
	}

	@Override
	public void closeDst() {
	}

	@Override
	public void writeDst( FHqm hqm, ICallback cb) {
	}
}
