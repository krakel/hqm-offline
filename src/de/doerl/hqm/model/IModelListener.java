package de.doerl.hqm.model;

import java.util.EventListener;

public interface IModelListener extends EventListener {
	void baseAdded( ModelEvent event);

	void baseChanged( ModelEvent event);

	void baseRemoved( ModelEvent event);

	void baseUpdate( ModelEvent event);
}
