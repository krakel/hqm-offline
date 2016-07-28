package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.mods.ItemNEI;
import de.doerl.hqm.view.leafs.LeafSearch;
import de.doerl.hqm.view.leafs.LeafSearch.ISearchListener;
import de.doerl.hqm.view.leafs.LeafSearch.SearchEvent;

class DialogIcon extends ADialog {
	private static final long serialVersionUID = -520943310358443074L;
	private DefaultAction mOk = new DefaultAction( BTN_OK, DialogResult.APPROVE);
	private LeafSearch mSearch = new LeafSearch();
	private ItemNEI mItem;

	private DialogIcon( Window owner) {
		super( owner);
		mOk.setEnabled( false);
		setTheme( "edit.textfield.theme");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( mOk);
		addEscapeAction();
		mSearch.addSearchListener( new ISearchListener() {
			@Override
			public void doAction( SearchEvent event) {
				mItem = event.getItem();
				mOk.setEnabled( mItem != null);
			}
		});
	}

	public static FItemStack update( FItemStack stk, Window owner) {
		DialogIcon dlg = new DialogIcon( owner);
		dlg.createMain();
		dlg.updateMain( stk);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return dlg.getResult();
		}
		else {
			return null;
		}
	}

	@Override
	protected void createMain() {
		mMain.add( mSearch);
	}

	private FItemStack getResult() {
		if (mItem != null) {
			return new FItemStack( mItem.mName, mItem.mDamage, 1);
		}
		else {
			return null;
		}
	}

	private void updateMain( FItemStack stk) {
		mSearch.doSearch();
	}
}
