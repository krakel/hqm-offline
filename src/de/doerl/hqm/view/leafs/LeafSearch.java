package de.doerl.hqm.view.leafs;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.doerl.hqm.ui.ADialog.TextFieldAscii;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.ItemNEI;
import de.doerl.hqm.view.IconUpdate;
import de.doerl.hqm.view.StackIcon;

public class LeafSearch extends JPanel {
	private static final long serialVersionUID = -1967387880066210929L;
	private static final double ICON_ZOOM = 0.6;
	private static final Logger LOGGER = Logger.getLogger( LeafSearch.class.getName());
	private static final Border BORDER = BorderFactory.createLineBorder( Color.BLACK);
	private static final int MAX_ICONS = 49;
	private static StackIcon sDummy = new StackIcon( ICON_ZOOM);
	private List<ISearchListener> mListener = new ArrayList<>();
	private JTextField mSearch = new TextFieldAscii();
	private ArrayList<IconRefresh> mRefs = new ArrayList<>();
	private MouseAdapter mClick = new MouseAdapter() {
		@Override
		public void mouseClicked( MouseEvent evt) {
			Object src = evt.getSource();
			for (IconRefresh ref : mRefs) {
				if (Utils.equals( src, ref.mLeaf)) {
					ref.mLeaf.setBorder( BORDER);
					SwingUtilities.invokeLater( new MatchRunner( ref.mItem));
				}
				else {
					ref.mLeaf.setBorder( null);
				}
			}
		}
	};

	public LeafSearch() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		setOpaque( false);
		setBorder( null);
		add( mSearch);
		add( createMatrix());
		mSearch.getDocument().addDocumentListener( new DocumentListener() {
			@Override
			public void changedUpdate( DocumentEvent e) {
				doSearch();
			}

			@Override
			public void insertUpdate( DocumentEvent e) {
				doSearch();
			}

			@Override
			public void removeUpdate( DocumentEvent e) {
				doSearch();
			}
		});
	}

	public void addSearchListener( ISearchListener l) {
		if (!mListener.contains( l)) {
			mListener.add( l);
		}
	}

	private JPanel createMatrix() {
		JPanel matrix = new JPanel( new GridLayout( 7, 7, 3, 3));
		matrix.setAlignmentX( LEFT_ALIGNMENT);
		matrix.setOpaque( false);
		matrix.setBorder( null);
		for (int i = 0; i < MAX_ICONS; ++i) {
			LeafIcon icon = new LeafIcon();
			icon.setIcon( sDummy);
			icon.addMouseListener( mClick);
			matrix.add( icon);
			mRefs.add( new IconRefresh( icon));
		}
		return matrix;
	}

	public void doSearch() {
		List<ItemNEI> lst = ImageLoader.find( mSearch.getText(), MAX_ICONS);
		int size = lst.size();
		for (int i = 0; i < MAX_ICONS; ++i) {
			IconRefresh ref = mRefs.get( i);
			if (i < size) {
				ref.mItem = lst.get( i);
			}
			else {
				ref.mItem = null;
			}
			IconUpdate.create( ref.mLeaf, ref.mItem);
		}
	}

	void fireEvent( ItemNEI item) {
		SearchEvent evt = new SearchEvent( item);
		for (ISearchListener l : mListener) {
			try {
				l.doAction( evt);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	public void removeSearchListener( ISearchListener l) {
		mListener.remove( l);
	}

	private static class IconRefresh {
		private LeafIcon mLeaf;
		public volatile ItemNEI mItem;

		public IconRefresh( LeafIcon leaf) {
			mLeaf = leaf;
		}
	}

	public static interface ISearchListener extends EventListener {
		void doAction( SearchEvent event);
	}

	private final class MatchRunner implements Runnable {
		private ItemNEI mItem;

		public MatchRunner( ItemNEI item) {
			mItem = item;
		}

		@Override
		public void run() {
			fireEvent( mItem);
		}
	}

	public static class SearchEvent {
		private ItemNEI mItem;

		private SearchEvent( ItemNEI item) {
			mItem = item;
		}

		public ItemNEI getItem() {
			return mItem;
		}
	}
}
