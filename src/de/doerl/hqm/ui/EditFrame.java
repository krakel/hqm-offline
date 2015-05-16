package de.doerl.hqm.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.doerl.hqm.EditManager;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.ui.tree.ElementTree;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.EditView;

@SuppressWarnings( "nls")
public class EditFrame extends JFrame implements ChangeListener {
	private static final long serialVersionUID = -2516074296297195438L;
	private static final String BASE_TITLE = ResourceManager.RESOURCE.getString( "hqm.editor.title");
	private static final String FILE = "hqm.file";
	private static final String EDIT = "hqm.edit";
	private static final String HELP = "hqm.help";
	private static final String POPUP = "hqm.popup";
	private EditModel mModel = new EditModel();
	private EditView mView;
//	private TreeTable mTable;
	private ElementTree mTree;
	private Action mNewAction = new NewAction( this);
	private JLabel mStatusBar;
	private EditCallback mCB;

	public EditFrame() throws HeadlessException {
		EditManager.init();
		EditController ctrl = new EditController( mModel);
		mView = new EditView( ctrl);
//		mTable = new TreeTable( ctrl);
		mTree = new ElementTree( ctrl);
		mModel.addListener( mTree.getModel());
//		mTable.addMouseListener( new SelectHandler( ctrl));
//		mModel.addListener( mTable.getModel());
		mCB = new EditCallback( this);
	}

	public static void addKeyAction( JComponent comp, String key, Action action) {
		Object o = new Object();
		comp.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke( key), o);
		comp.getActionMap().put( o, action);
	}

	private static JMenu createMenu( String name) {
		JMenu menu = new JMenu();
		menu.setActionCommand( name);
		menu.setBackground( UIManager.getColor( "panel.background"));
		menu.setAction( new ABundleAction( name, ResourceManager.RESOURCE) {
			private static final long serialVersionUID = -2822453428777922839L;

			public void actionPerformed( ActionEvent ev) {
			}
		});
		return menu;
	}

	public static EditFrame createNew() {
		EditFrame main = new EditFrame();
		main.init();
		main.createContent( main.getContentPane());
		main.setJMenuBar( main.createMenuBar());
		main.setSize( main.getSize());
		Utils.centerFrame( main);
		main.addWindowListener( new WindowCloser());
		ResourceManager.setLookAndFeel( PreferenceManager.getString( BaseDefaults.LOOK_AND_FEEL));
		main.setVisible( true);
		return main;
	}

	protected static JPopupMenu createPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		popup.add( new JMenuItem( new ABundleAction( POPUP, ResourceManager.RESOURCE) {
			private static final long serialVersionUID = 2766110645403746850L;

			public void actionPerformed( ActionEvent ev) {
			}
		}));
		return popup;
	}

	private JPanel createCentered( Container cp) {
		JPanel result = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.CENTER;
		layout.setConstraints( result, gbc);
		result.setLayout( layout);
		result.add( cp);
		return result;
	}

	private void createContent( Container cp) {
		createPopupMenu();
		GroupLayout lm = new GroupLayout( cp);
		lm.setAutoCreateGaps( false);
		lm.setAutoCreateContainerGaps( false);
		cp.setLayout( lm);
		JToolBar tool = createToolBar();
		JSplitPane split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, new JScrollPane( mTree), createCentered( mView));
//		JSplitPane split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, new JScrollPane( mTable), createCentered( mView));
		split.setDividerSize( 5);
		split.setDividerLocation( 320);
		JLabel bar = createStatusBar();
		ParallelGroup mainHori = lm.createParallelGroup();
		mainHori.addComponent( tool);
		mainHori.addComponent( split);
		mainHori.addComponent( bar);
		SequentialGroup mainVert = lm.createSequentialGroup();
		mainVert.addComponent( tool);
		mainVert.addComponent( split);
		mainVert.addComponent( bar);
		lm.setHorizontalGroup( mainHori);
		lm.setVerticalGroup( mainVert);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menu = new JMenuBar();
		menu.setBackground( UIManager.getColor( "panel.background"));
		menu.add( createMenuFile());
		menu.add( createMenuEdit());
		menu.add( createMenuHelp());
		return menu;
	}

	private JMenu createMenuEdit() {
		JMenu menu = createMenu( EDIT);
//		menu.add( new CopyAction( mMaster));
//		menu.add( new CutAction( mMaster));
//		menu.add( new PasteAction( mMaster));
//		menu.addSeparator();
//		menu.add( new DeleteAction( mMaster));
//		menu.addSeparator();
//		menu.add( mModel.getUndoable().getUndoAction());
//		menu.add( mModel.getUndoable().getRedoAction());
		return menu;
	}

	private JMenu createMenuFile() {
		JMenu menu = createMenu( FILE);
		menu.add( mNewAction);
		menu.addSeparator();
		IMedium bit = MediaManager.get( "bit");
		menu.add( bit.getOpen( mCB));
		menu.add( bit.getSave( mCB));
		menu.add( bit.getSaveAs( mCB));
		menu.addSeparator();
		IMedium json = MediaManager.get( "json");
		menu.add( json.getOpen( mCB));
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
		JMenu menu = createMenu( HELP);
		menu.add( new AboutAction( this));
		return menu;
	}

	private JLabel createStatusBar() {
		mStatusBar = new JLabel();
		mStatusBar.setText( " ");
		return mStatusBar;
	}

	private JToolBar createToolBar() {
		JToolBar tool = new JToolBar();
		tool.setFloatable( false);
		try {
			tool.setRollover( true);
		}
		catch (NoSuchMethodError e) {
		}
		tool.add( mNewAction);
		tool.addSeparator();
		IMedium bit = MediaManager.get( "bit");
		tool.add( bit.getOpen( mCB));
		tool.add( bit.getSave( mCB));
		tool.add( bit.getSaveAs( mCB));
		tool.addSeparator();
		IMedium json = MediaManager.get( "json");
		tool.add( json.getOpen( mCB));
		tool.add( json.getSave( mCB));
		tool.add( json.getSaveAs( mCB));
		tool.addSeparator();
//		tool.add( createToolBtn( mModel.getUndoable().getUndoAction()));
//		tool.add( createToolBtn( mModel.getUndoable().getRedoAction()));
//		tool.addSeparator();
		return tool;
	}

	public void displayHelp( String text) {
		mStatusBar.setText( text);
	}

	@Override
	public void dispose() {
		int result = getModifiedResult();
		switch (result) {
			case ADialog.APPROVE:
//				save();
//				if (!mCB.isModifiedPipeDef()) {
				super.dispose();
//				}
				break;
			case ADialog.NO:
				super.dispose();
				break;
			default:
		}
	}

	public EditModel getModel() {
		return mModel;
	}

	int getModifiedResult() {
//		if (mCB.isModifiedPipeDef()) {
//			return new ModifiedDialog().showDialog( this);
//		}
		return ADialog.NO;
	}

	private void init() {
		setTitle( null);
		setLocale( Locale.getDefault());
		setSize( new Dimension( 1024, 600));
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);
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

	@Override
	public void stateChanged( ChangeEvent event) {
		mCB.fireActionUpdate();
//		mTable.getModel().fireTableDataChanged();
	}
}
