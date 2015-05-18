package de.doerl.hqm.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.Action;
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
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.doerl.hqm.EditManager;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.ui.ADialog.DialogResult;
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

	private static JMenu createMenu( String name) {
		JMenu result = new JMenu();
		result.setActionCommand( name);
		result.setBackground( UIManager.getColor( "panel.background"));
		result.setAction( new ABundleAction( name, ResourceManager.RESOURCE) {
			private static final long serialVersionUID = -2822453428777922839L;

			public void actionPerformed( ActionEvent ev) {
			}
		});
		return result;
	}

	public static EditFrame createNew() {
		EditFrame result = new EditFrame();
		result.init();
		result.getContentPane().add( result.createContent());
		result.setJMenuBar( result.createMenuBar());
		result.repaint();
//		result.pack();
//		result.createBufferStrategy( 2);
		Utils.centerFrame( result);
		result.addWindowListener( new WindowCloser());
		ResourceManager.setLookAndFeel( PreferenceManager.getString( BaseDefaults.LOOK_AND_FEEL));
		result.setVisible( true);
		createPopupMenu();
		return result;
	}

	protected static JPopupMenu createPopupMenu() {
		JPopupMenu result = new JPopupMenu();
		result.add( new JMenuItem( new ABundleAction( POPUP, ResourceManager.RESOURCE) {
			private static final long serialVersionUID = 2766110645403746850L;

			public void actionPerformed( ActionEvent ev) {
			}
		}));
		return result;
	}

	private Box createContent() {
		Box result = Box.createVerticalBox();
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
		result.addSeparator();
		IMedium bit = MediaManager.get( "bit");
		result.add( bit.getOpen( mCB));
		result.add( bit.getSave( mCB));
		result.add( bit.getSaveAs( mCB));
		result.addSeparator();
		IMedium json = MediaManager.get( "json");
		result.add( json.getOpen( mCB));
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
		JMenu menu = createMenu( HELP);
		menu.add( new AboutAction( this));
		return menu;
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
		JComponent scroll = new JScrollPane( mTree);
		scroll.setAlignmentX( LEFT_ALIGNMENT);
		Box result = Box.createVerticalBox();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setPreferredSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		result.add( createToolBar());
		result.add( scroll);
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

	private JComponent createToolBar() {
		Box result = Box.createHorizontalBox();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED));
		JToolBar tool = new JToolBar();
		tool.setFloatable( false);
		try {
			tool.setRollover( true);
		}
		catch (NoSuchMethodError ex) {
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
		result.add( tool);
		result.add( Box.createHorizontalGlue());
//		result.setPreferredSize( new Dimension( Short.MAX_VALUE, tool.getPreferredSize().height));
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

	@Override
	public void stateChanged( ChangeEvent event) {
		mCB.fireActionUpdate();
//		mTable.getModel().fireTableDataChanged();
	}
}
