package de.doerl.hqm.ui.treetable;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FParameterBoolean;
import de.doerl.hqm.base.FParameterEnum;
import de.doerl.hqm.base.FParameterInt;
import de.doerl.hqm.base.FParameterInteger;
import de.doerl.hqm.base.FParameterIntegerArr;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FParameterString;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class RowFactory extends AHQMWorker<ATreeTableRow, TreeTableModel> {
	private static final RowFactory WORKER = new RowFactory();

	public RowFactory() {
	}

	public static ATreeTableRow get( ABase base, TreeTableModel model) {
		return base.accept( WORKER, model);
	}

	@Override
	protected ATreeTableRow doBase( ABase base, TreeTableModel model) {
		ATreeTableRow parent = base.getParent().accept( this, model);
		ATreeTableRow node = model.getNode( base);
		if (node == null) {
			node = new BaseRow( parent, base);
			model.addNewNode( parent, base, node);
		}
		return node;
	}

	@Override
	protected ATreeTableRow doNamed( ANamed named, TreeTableModel model) {
		ATreeTableRow parent = named.getParent().accept( this, model);
		ATreeTableRow node = model.getNode( named);
		if (node == null) {
			node = new NamedRow( parent, named);
			model.addNewNode( parent, named, node);
		}
		return node;
	}

	@Override
	protected ATreeTableRow doSet( ACategory<? extends ANamed> set, TreeTableModel model) {
		ATreeTableRow parent = set.getParent().accept( this, model);
		ATreeTableRow node = model.getNode( set);
		if (node == null) {
			node = new SetRow( parent, set);
			model.addNewNode( parent, set, node);
		}
		return node;
	}

	@Override
	public ATreeTableRow forHQM( FHqm hqm, TreeTableModel model) {
		ATreeTableRow root = (ATreeTableRow) model.getRoot();
		ATreeTableRow node = model.getNode( hqm);
		if (node == null) {
			node = new MainRow( root, hqm);
			model.addNewNode( root, hqm, node);
		}
		return node;
	}

	@Override
	public ATreeTableRow forParameterBoolean( FParameterBoolean par, TreeTableModel model) {
		ATreeTableRow parent = par.getParent().accept( this, model);
		AParameterRow node = new ParameterBooleanRow( parent, par);
		model.addNewNode( parent, par, node);
		return node;
	}

	@Override
	public ATreeTableRow forParameterEnum( FParameterEnum<? extends Enum<?>> par, TreeTableModel model) {
		ATreeTableRow parent = par.getParent().accept( this, model);
		ATreeTableRow node = new ParameterEnumRow( parent, par);
		model.addNewNode( parent, par, node);
		return node;
	}

	@Override
	public ATreeTableRow forParameterInt( FParameterInt par, TreeTableModel model) {
		ATreeTableRow parent = par.getParent().accept( this, model);
		AParameterRow node = new ParameterIntRow( parent, par);
		model.addNewNode( parent, par, node);
		return node;
	}

	@Override
	public ATreeTableRow forParameterInteger( FParameterInteger par, TreeTableModel model) {
		ATreeTableRow parent = par.getParent().accept( this, model);
		AParameterRow node = new ParameterIntegerRow( parent, par);
		model.addNewNode( parent, par, node);
		return node;
	}

	@Override
	public ATreeTableRow forParameterIntegerArr( FParameterIntegerArr par, TreeTableModel model) {
		ATreeTableRow parent = par.getParent().accept( this, model);
		AParameterRow node = new ParameterIntegerArrRow( parent, par);
		model.addNewNode( parent, par, node);
		return node;
	}

	@Override
	public ATreeTableRow forParameterStack( FParameterStack par, TreeTableModel model) {
		ATreeTableRow parent = par.getParent().accept( this, model);
		ParameterStackRow node = new ParameterStackRow( parent, par);
		model.addNewNode( parent, par, node);
		return node;
	}

	@Override
	public ATreeTableRow forParameterString( FParameterString par, TreeTableModel model) {
		ATreeTableRow parent = par.getParent().accept( this, model);
		ParameterStringRow node = new ParameterStringRow( parent, par);
		model.addNewNode( parent, par, node);
		return node;
	}
}
