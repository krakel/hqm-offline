package de.doerl.hqm.medium;

import java.awt.Window;

import javax.swing.Action;

public interface IRefreshListener extends Action {
	void action( Window frame);

	void updateAction( RefreshEvent event);
}
