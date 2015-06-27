package de.doerl.hqm.view;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.ToolTipManager;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.utils.ResourceManager;

class LeafQuest extends JLabel {
	private static final long serialVersionUID = -2797500791761791369L;
	private ClickHandler mHandler = new ClickHandler();
	private FQuest mQuest;
	private Type mType = Type.NORM;

	public LeafQuest( FQuest quest) {
		mQuest = quest;
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		addMouseListener( mHandler);
		ToolTipManager.sharedInstance().registerComponent( this);
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public FQuest getQuest() {
		return mQuest;
	}

	@Override
	public String getToolTipText( MouseEvent event) {
		StringBuffer sb = new StringBuffer();
		sb.append( "<html><body bgcolor=#202020 text=#FFFFFF>");
		sb.append( String.format( "<div align=center>%s<br>", mQuest.mName));
		if (mQuest.mIcon != null) {
			sb.append( String.format( "<div align=left>Icon: %s<br>", mQuest.mIcon));
		}
		if (!mQuest.isFree()) {
			sb.append( "<div align=left style='color:#808080'>Locked Quest<br>");
		}
		int s1 = mQuest.mRequirements.size();
		if (s1 > 0) {
			sb.append( String.format( "<div align=left style='color:#808080'>Requires %d Quest%s to be completed.<br>", s1, s1 > 1 ? "s" : ""));
			sb.append( String.format( "<div align=left style='color:#FFC0CB'>Requires %d Quest%s to be completed elsewhere.<br>", 0, ""));
		}
		int s2 = SizeOf.getTasks( mQuest);
		if (s2 > 0) {
			sb.append( String.format( "<div align=left style='color:#00FFFF'>0/%d completed tasks.<br>", s2));
		}
		else {
			sb.append( "<div align=left style='color:#FF0000'>This quest has no tasks!<br>");
		}
//		sb.append( "<table border=\"1\">");
//		sb.append( "<tr><th>data type</th><th>type</th><th>port name</th></tr>");
//		sb.append( "<tr><td>");
//		sb.append( mQuest.mName);
//		sb.append( "</td><td>input</td><td>");
//		sb.append( "&nbsp;");
//		sb.append( "</td></tr>");
//		sb.append( "</table>");
		sb.append( "</body></html>");
		return sb.toString();
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	public void update( Type type) {
		updateType( type);
		updateBounds();
		updateIcon();
	}

	public void updateBounds() {
		boolean big = mQuest.mBig;
		int x = AEntity.ZOOM * mQuest.mX;
		int y = AEntity.ZOOM * mQuest.mY;
		int w = AEntity.ZOOM * ResourceManager.getW( big);
		int h = AEntity.ZOOM * ResourceManager.getH( big);
		setBounds( x, y, w, h);
	}

	private void updateIcon() {
		String key = mType.getKey( mQuest.mBig);
		Image back = ResourceManager.getImageUI( key);
		IconUpdate.create( this, back, mQuest.mIcon, 0.6, mQuest.mTriggerType.getMarker());
	}

	public void updateLocation() {
		int x = AEntity.ZOOM * mQuest.mX;
		int y = AEntity.ZOOM * mQuest.mY;
		setLocation( x, y);
	}

	private void updateType( Type type) {
		if (type != Type.NORM) {
			mType = type;
		}
		else if (mQuest.isFree()) {
			mType = Type.NORM;
		}
		else {
			mType = Type.DARK;
		}
	}

	public static enum Type {
		NORM( "hqm.quest.norm", "hqm.quest.big"),
		DARK( "hqm.dark.norm", "hqm.dark.big"),
		BASE( "hqm.base.norm", "hqm.base.big"),
		LINK( "hqm.link.norm", "hqm.link.big"),
		PREF( "hqm.pref.norm", "hqm.pref.big"),
		POST( "hqm.post.norm", "hqm.post.big");
		private String mBigKey;
		private String mNormKey;

		private Type( String normKey, String bigKey) {
			mNormKey = normKey;
			mBigKey = bigKey;
		}

		String getKey( boolean big) {
			return big ? mBigKey : mNormKey;
		}
	}
}
