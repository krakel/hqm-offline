package de.doerl.hqm.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;

import de.doerl.hqm.utils.ResourceManager;

@SuppressWarnings( "nls")
public class AboutAction extends ABundleAction {
	private static final long serialVersionUID = -7364269566865078131L;
	private Window mParent;

	public AboutAction( Window parent) {
		super( "hqm.help.about", ResourceManager.RESOURCE);
		mParent = parent;
	}

	public void actionPerformed( ActionEvent event) {
		new AboutDialog( mParent).showDialog();
	}
}
