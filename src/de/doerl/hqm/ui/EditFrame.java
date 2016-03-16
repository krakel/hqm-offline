package de.doerl.hqm.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Window;
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
import de.doerl.hqm.ui.tree.ANode;
import de.doerl.hqm.ui.tree.ElementTree;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.EditView;

public class EditFrame extends JFrame implements IModelListener {
	private static final long serialVersionUID = -2516074296297195438L;
	private static final String BASE_TITLE = ResourceManager.getString( "hqm.editor.title");
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

	static boolean canClose( FHqm hqm, Window owner) {
		return !hqm.isModified() || WarnDialogs.askMissing( owner);
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
		EditFrame frame = new EditFrame();
		frame.init();
		Utils.centerFrame( frame);
		frame.addWindowListener( new WindowCloser());
		frame.setVisible( true);
		frame.mCB.fireActionUpdate();
		return frame;
	}

	protected static JPopupMenu createPopupMenu() {
		JPopupMenu result = new JPopupMenu();
		result.add( new JMenuItem( new ABundleAction( "hqm.popup") {
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
			JToolBar tool = mView.getToolBar( event.mCtrlKey ? base.getParent() : base);
			SwingUtilities.invokeLater( new ToolUpdate( tool));
			mCB.fireActionUpdate();
			if (base instanceof FHqm) {
				showHqm( (FHqm) base);
			}
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

	@Override
	public void baseTreeChange( ModelEvent event) {
	}

	private Box createContent() {
		Box box = Box.createVerticalBox();
		box.add( mTop);
		box.add( createSplit());
		box.add( createStatusBar());
		return box;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menu = new JMenuBar();
		menu.setBackground( UIManager.getColor( "panel.background"));
		menu.add( createMenuFile());
		menu.add( createMenuReport());
		menu.add( createMenuHelp());
//		menu.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED));
		return menu;
	}

	private JMenu createMenuFile() {
		JMenu menu = createMenu( "hqm.file");
		menu.add( mNewAction);
		IMedium bit = MediaManager.get( "bit");
		IMedium json = MediaManager.get( "json");
		menu.add( bit.getOpen( mCB));
		menu.add( json.getOpen( mCB));
		menu.add( createMenuLastOpen());
		menu.add( mCloseAction);
		menu.addSeparator();
		menu.add( bit.getSave( mCB));
		menu.add( bit.getSaveAs( mCB));
		menu.addSeparator();
		menu.add( json.getSave( mCB));
		menu.add( json.getSaveAs( mCB));
		menu.addSeparator();
//		menu.add( new RestoreBackupAction( mBackup));
//		menu.add( new RestorePrevAction( mBackup));
//		menu.add( new RestoreNextAction( mBackup));
//		menu.addSeparator();
//		menu.add( new ConfigurationAction( this));
//		menu.addSeparator();
		menu.add( new ExitAction( this));
		return menu;
	}

	private JMenu createMenuHelp() {
		JMenu menu = createMenu( "hqm.help");
		menu.add( new ConfigAction( this));
//		menu.addSeparator();
		menu.add( new AboutAction( this));
		return menu;
	}

	private JMenuItem createMenuLastOpen() {
		JMenu menu = createMenu( "hqm.last");
		int max = BaseDefaults.LAST_OPEN_MAX;
		for (int i = 0; i < max; ++i) {
			OpenLastAction a = new OpenLastAction( i, mCB);
			JMenuItem mi = menu.add( a);
			a.setItem( mi); // poor
		}
		return menu;
	}

	private JMenu createMenuReport() {
		JMenu menu = createMenu( "hqm.report");
		menu.add( new ReportRewards( mCB));
		menu.add( new ReportChoices( mCB));
		menu.addSeparator();
		menu.add( new ReportStory( mCB));
//		menu.add( new DeleteAction( mMaster));
//		menu.addSeparator();
//		menu.add( mModel.getUndoable().getUndoAction());
//		menu.add( mModel.getUndoable().getRedoAction());
		return menu;
	}

	private Box createSplit() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX( LEFT_ALIGNMENT);
//		box.setBorder( BorderFactory.createLineBorder( Color.BLACK));
		box.add( createSplitLeft());
		box.add( mView);
		return box;
	}

	private JComponent createSplitLeft() {
		JComponent result = new JScrollPane( mTree);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		return result;
	}

	private JComponent createStatusBar() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX( LEFT_ALIGNMENT);
		box.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED));
		mStatusBar = new JLabel();
		mStatusBar.setText( "???");
		box.add( mStatusBar);
		box.add( Box.createHorizontalGlue());
		box.setPreferredSize( new Dimension( Short.MAX_VALUE, mStatusBar.getPreferredSize().height));
		return box;
	}

	public void displayHelp( String text) {
		mStatusBar.setText( text);
	}

	@Override
	public void dispose() {
		if (CloseWorker.get( this)) {
			super.dispose();
		}
	}

	public EditCallback getCallback() {
		return mCB;
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

	private void init() {
		setTitle( null);
		setLocale( Locale.getDefault());
		setSize( new Dimension( 950, 600));
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
