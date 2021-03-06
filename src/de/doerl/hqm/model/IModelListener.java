package de.doerl.hqm.model;

import java.util.EventListener;

public interface IModelListener extends EventListener {
	void baseActivate( ModelEvent event);

	void baseAdded( ModelEvent event);

	void baseChanged( ModelEvent event);

	void baseRemoved( ModelEvent event);

	void baseTreeChange( ModelEvent event);
}
