package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.Image;
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
import de.doerl.hqm.utils.mods.SimpleMatcher;

class LeafSearch extends JPanel {
	private static final long serialVersionUID = -1967387880066210929L;
	private static final Logger LOGGER = Logger.getLogger( LeafSearch.class.getName());
	private static final int MAX_ICONS = 49;
	private static StackIcon sDummy = new StackIcon( null, 0.6);
	private List<IMatcherListener> mListener = new ArrayList<>();
	private JTextField mSearch = new JTextField();
	private ArrayList<IconRefresh> mRefs = new ArrayList<>();
	private MouseAdapter mClick = new MouseAdapter() {
		@Override
		public void mouseClicked( MouseEvent evt) {
			Object src = evt.getSource();
			for (IconRefresh ref : mRefs) {
				if (Utils.equals( src, ref.mLeaf)) {
					SwingUtilities.invokeLater( new MatchRunner( ref.mMatcher));
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
			LeafIcon icon = new LeafIcon( sDummy);
			icon.addMouseListener( mClick);
			matrix.add( icon);
			mRefs.add( new IconRefresh( icon, null));
		}
		return matrix;
	}

	private void doSearch() {
		List<SimpleMatcher> lst = ImageLoader.find( mSearch.getText(), MAX_ICONS);
		update( lst);
	}

	void fireEvent( SimpleMatcher match) {
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

	public void update( List<SimpleMatcher> lst) {
		int size = lst.size();
		for (int i = 0; i < MAX_ICONS; ++i) {
			IconRefresh ref = mRefs.get( i);
			if (i < size) {
				ref.setMatcher( lst.get( i));
			}
			else {
				ref.setMatcher( null);
			}
		}
	}

	private static class IconRefresh implements Runnable {
		private LeafIcon mLeaf;
		private SimpleMatcher mMatcher;

		public IconRefresh( LeafIcon leaf, SimpleMatcher mm) {
			mLeaf = leaf;
			mMatcher = mm;
		}

		@Override
		public void run() {
			updateIcon( null);
		}

		public void setMatcher( SimpleMatcher match) {
			mMatcher = match;
			updateIcon( this);
		}

		public void updateIcon( IconRefresh cb) {
			Image img = ImageLoader.getImage( mMatcher, cb);
			if (img != null) {
				mLeaf.setIcon( new StackIcon( img, 0.6, null));
				mLeaf.setEnabled( true);
			}
			else {
				mLeaf.setIcon( sDummy);
				mLeaf.setEnabled( false);
			}
		}
	}

	public static interface IMatcherListener extends EventListener {
		void doAction( MatcherEvent event);
	}

	public static class MatcherEvent {
		private SimpleMatcher mMatch;

		private MatcherEvent( SimpleMatcher match) {
			mMatch = match;
		}

		public SimpleMatcher getMatch() {
			return mMatch;
		}
	}

	private final class MatchRunner implements Runnable {
		private SimpleMatcher mMatcher;

		public MatchRunner( SimpleMatcher matcher) {
			mMatcher = matcher;
		}

		@Override
		public void run() {
			fireEvent( mMatcher);
		}
	}
}
