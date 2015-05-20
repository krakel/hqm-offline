package de.doerl.hqm.view;

import java.awt.event.MouseEvent;
import java.util.EventListener;

public interface IClickListener extends EventListener {
	void onDoubleClick( MouseEvent evt);

	void onSingleClick( MouseEvent evt);
}
