package de.doerl.hqm.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.doerl.hqm.EditManager;
import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.ADialog.DialogResult;
import de.doerl.hqm.ui.tree.ANode;
import de.doerl.hqm.ui.tree.ElementTree;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.EditView;

public class EditFrame extends JFrame implements IModelListener {
	private static final long serialVersionUID = -2516074296297195438L;
	private static final String BASE_TITLE = ResourceManager.getString( "hqm.editor.title");
	private static final String FILE = "hqm.file";
	private static final String EDIT = "hqm.edit";
	private static final String HELP = "hqm.help";
	private static final String POPUP = "hqm.popup";
	private EditModel mModel = new EditModel();
	private EditView mView;
	private ElementTree mTree;
	private NewAction mNewAction = new NewAction( this);
	private CloseAction mCloseAction = new CloseAction( this);
	private JLabel mStatusBar;
	private EditCallback mCB;
	private Box mTop = Box.createHorizontalBox();
	private JToolBar mToolBar = createToolBar();

	private EditFrame() throws HeadlessException {
		EditController ctrl = new EditController( mModel, this);
		mView = new EditView( ctrl);
		mTree = new ElementTree( ctrl);
		mModel.addListener( mView);
		mModel.addListener( mTree.getModel());
		mModel.addListener( this);
		mCB = new EditCallback( this);
		mCB.addRefreshListener( mCloseAction);
	}

	private static JMenu createMenu( String name) {
		JMenu result = new JMenu();
		result.setActionCommand( name);
		result.setBackground( UIManager.getColor( "panel.background"));
		result.setAction( new ABundleAction( name) {
			private static final long serialVersionUID = -2822453428777922839L;

			public void actionPerformed( ActionEvent ev) {
			}
		});
		return result;
	}

	static EditFrame createNew() {
		EditManager.init();
		ResourceManager.init();
		EditFrame result = new EditFrame();
		result.init();
		Utils.centerFrame( result);
		result.addWindowListener( new WindowCloser());
		result.setVisible( true);
		return result;
	}

	protected static JPopupMenu createPopupMenu() {
		JPopupMenu result = new JPopupMenu();
		result.add( new JMenuItem( new ABundleAction( POPUP) {
			private static final long serialVersionUID = 2766110645403746850L;

			public void actionPerformed( ActionEvent ev) {
			}
		}));
		return result;
	}

	public static JToolBar createToolBar() {
		JToolBar result = new JToolBar();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.setFloatable( false);
		try {
			result.setRollover( true);
		}
		catch (NoSuchMethodError ex) {
		}
		return result;
	}

	@Override
	public void baseActivate( ModelEvent event) {
		ABase base = event.mBase;
		if (base != null) {
			JToolBar tool = mView.getToolBar( base);
			SwingUtilities.invokeLater( new ToolUpdate( tool));
			mCB.fireActionUpdate();
		}
	}

	@Override
	public void baseAdded( ModelEvent event) {
		mCB.fireActionUpdate();
	}

	@Override
	public void baseChanged( ModelEvent event) {
		mCB.fireActionUpdate();
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		if (base instanceof FHqm) {
			SwingUtilities.invokeLater( new ToolUpdate( null));
			mCB.fireActionUpdate();
		}
	}

	private Box createContent() {
		Box result = Box.createVerticalBox();
		result.add( mTop);
		result.add( createSplit());
		result.add( createStatusBar());
		return result;
	}

	private JMenuBar createMenuBar() {
		JMenuBar result = new JMenuBar();
		result.setBackground( UIManager.getColor( "panel.background"));
		result.add( createMenuFile());
		result.add( createMenuEdit());
		result.add( createMenuHelp());
//		result.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED));
		return result;
	}

	private JMenu createMenuEdit() {
		JMenu result = createMenu( EDIT);
//		result.add( new CopyAction( mMaster));
//		result.add( new CutAction( mMaster));
//		result.add( new PasteAction( mMaster));
//		result.addSeparator();
//		result.add( new DeleteAction( mMaster));
//		result.addSeparator();
//		result.add( mModel.getUndoable().getUndoAction());
//		result.add( mModel.getUndoable().getRedoAction());
		return result;
	}

	private JMenu createMenuFile() {
		JMenu result = createMenu( FILE);
		result.add( mNewAction);
		IMedium bit = MediaManager.get( "bit");
		IMedium json = MediaManager.get( "json");
		result.add( bit.getOpen( mCB));
		result.add( json.getOpen( mCB));
		result.add( mCloseAction);
		result.addSeparator();
		result.add( bit.getSave( mCB));
		result.add( bit.getSaveAs( mCB));
		result.addSeparator();
		result.add( json.getSave( mCB));
		result.add( json.getSaveAs( mCB));
		result.addSeparator();
//		result.add( new RestoreBackupAction( mBackup));
//		result.add( new RestorePrevAction( mBackup));
//		result.add( new RestoreNextAction( mBackup));
//		result.addSeparator();
//		result.add( new ConfigurationAction( this));
//		result.addSeparator();
		result.add( new ExitAction( this));
		return result;
	}

	private JMenu createMenuHelp() {
		JMenu result = createMenu( HELP);
		result.add( new ConfigAction( this));
//		result.addSeparator();
		result.add( new AboutAction( this));
		return result;
	}

	private Box createSplit() {
		Box result = Box.createHorizontalBox();
		result.setAlignmentX( LEFT_ALIGNMENT);
//		result.setBorder( BorderFactory.createLineBorder( Color.BLACK));
		result.add( createSplitLeft());
		result.add( mView);
		return result;
	}

	private JComponent createSplitLeft() {
		JComponent result = new JScrollPane( mTree);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		return result;
	}

	private JComponent createStatusBar() {
		Box result = Box.createHorizontalBox();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED));
		mStatusBar = new JLabel();
		mStatusBar.setText( "???");
		result.add( mStatusBar);
		result.add( Box.createHorizontalGlue());
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, mStatusBar.getPreferredSize().height));
		return result;
	}

	public void displayHelp( String text) {
		mStatusBar.setText( text);
	}

	@Override
	public void dispose() {
		DialogResult result = getModifiedResult();
		switch (result) {
			case APPROVE:
//				save();
//				if (!mCB.isModifiedPipeDef()) {
				super.dispose();
//				}
				break;
			case NO:
				super.dispose();
				break;
			default:
		}
	}

	public FHqm getCurrent() {
		TreePath path = mTree.getSelectionPath();
		if (path != null) {
			Object obj = path.getLastPathComponent();
			if (obj != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
				ANode user = (ANode) node.getUserObject();
				return getHqm( user.getBase());
			}
		}
		return null;
	}

	private FHqm getHqm( ABase base) {
		ABase parent = base.getParent();
		if (parent != null) {
			return getHqm( parent);
		}
		return (FHqm) base;
	}

	public EditModel getModel() {
		return mModel;
	}

	DialogResult getModifiedResult() {
//		if (mCB.isModifiedPipeDef()) {
//			return new ModifiedDialog().showDialog( this);
//		}
		return DialogResult.NO;
	}

	private void init() {
		setTitle( null);
		setLocale( Locale.getDefault());
		setSize( new Dimension( 1000, 600));
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);
		createPopupMenu();
		updateToolBar();
		updateTop();
		getContentPane().add( createContent());
		setJMenuBar( createMenuBar());
		repaint();
//		pack();
	}

	public void saveHQM() {
	}

	@Override
	public void setTitle( String title) {
		StringBuffer sb = new StringBuffer( BASE_TITLE);
		if (title != null) {
			sb.append( " : ");
			sb.append( title.substring( title.lastIndexOf( '/') + 1));
		}
		super.setTitle( sb.toString());
	}

	public void showHqm( FHqm hqm) {
		mTree.showHqm( hqm);
	}

	private void updateToolBar() {
		mToolBar.add( mNewAction);
		IMedium bit = MediaManager.get( "bit");
		IMedium json = MediaManager.get( "json");
		mToolBar.add( bit.getOpen( mCB));
		mToolBar.add( json.getOpen( mCB));
		mToolBar.add( mCloseAction);
		mToolBar.addSeparator();
		mToolBar.add( bit.getSave( mCB));
		mToolBar.add( bit.getSaveAs( mCB));
		mToolBar.addSeparator();
		mToolBar.add( json.getSave( mCB));
		mToolBar.add( json.getSaveAs( mCB));
		mToolBar.addSeparator( new Dimension( 80, 0));
	}

	private void updateTop() {
		mTop.setAlignmentX( LEFT_ALIGNMENT);
		mTop.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED));
		mTop.add( mToolBar);
		mTop.add( Box.createHorizontalGlue());
	}

	private final class ToolUpdate implements Runnable {
		private JToolBar mTool;

		public ToolUpdate( JToolBar tool) {
			mTool = tool;
		}

		@Override
		public void run() {
			mTop.removeAll();
			mTop.add( mToolBar);
			if (mTool != null) {
				mTop.add( mTool);
			}
			mTop.add( Box.createHorizontalGlue());
			mTop.revalidate();
			mTop.repaint();
		}
	}
}
