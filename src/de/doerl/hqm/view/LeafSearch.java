package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.Matcher;

class LeafSearch extends JPanel {
	private static final long serialVersionUID = -1967387880066210929L;
	private static final Logger LOGGER = Logger.getLogger( LeafSearch.class.getName());
	private static final int MAX_ICONS = 49;
	private static StackIcon sDummy = new StackIcon();
	private List<IMatcherListener> mListener = new ArrayList<>();
	private JTextField mSearch = new JTextField();
	private ArrayList<IconRefresh> mRefs = new ArrayList<>();
	private MouseAdapter mClick = new MouseAdapter() {
		@Override
		public void mouseClicked( MouseEvent evt) {
			Object src = evt.getSource();
			for (IconRefresh ref : mRefs) {
				if (Utils.equals( src, ref.mLeaf)) {
					SwingUtilities.invokeLater( new MatchRunner( ref.mMatch));
					break;
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

	public void addActionListener( IMatcherListener l) {
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

	private void doSearch() {
		List<Matcher> lst = ImageLoader.find( mSearch.getText(), MAX_ICONS);
		int size = lst.size();
		for (int i = 0; i < MAX_ICONS; ++i) {
			IconRefresh ref = mRefs.get( i);
			if (i < size) {
				ref.mMatch = lst.get( i);
			}
			else {
				ref.mMatch = null;
			}
			IconUpdate.create( ref.mLeaf, ref.mMatch);
		}
	}

	void fireEvent( Matcher match) {
		MatcherEvent evt = new MatcherEvent( match);
		for (IMatcherListener l : mListener) {
			try {
				l.doAction( evt);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	public void removeActionListener( IMatcherListener l) {
		mListener.remove( l);
	}

	private static class IconRefresh {
		private LeafIcon mLeaf;
		public volatile Matcher mMatch;

		public IconRefresh( LeafIcon leaf) {
			mLeaf = leaf;
		}
	}

	public static interface IMatcherListener extends EventListener {
		void doAction( MatcherEvent event);
	}

	public static class MatcherEvent {
		private Matcher mMatch;

		private MatcherEvent( Matcher match) {
			mMatch = match;
		}

		public Matcher getMatch() {
			return mMatch;
		}
	}

	private final class MatchRunner implements Runnable {
		private Matcher mMatcher;

		public MatchRunner( Matcher matcher) {
			mMatcher = matcher;
		}

		@Override
		public void run() {
			fireEvent( mMatcher);
		}
	}
}
