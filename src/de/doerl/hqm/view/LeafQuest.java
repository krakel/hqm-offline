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

	private static String fromObj( Object value) {
		return value != null ? String.valueOf( value) : "";
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
		String key = mType.getKey( mQuest.mBig, mQuest.containExt());
		Image back = ResourceManager.getImageUI( key);
		String rr = mQuest.mRepeatInfo.mType.getMarker();
		String tt = mQuest.mTriggerType.getMarker();
		Integer cc = mQuest.mCount;
		if (rr == null && tt == null && cc == null) {
			IconUpdate.create( this, back, mQuest.mIcon, 0.6, null);
		}
		else {
			IconUpdate.create( this, back, mQuest.mIcon, 0.6, fromObj( rr) + fromObj( tt) + fromObj( cc));
		}
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
		NORM( "hqm.quest.normA", "hqm.quest.bigA", "hqm.quest.normB", "hqm.quest.bigB"),
		DARK( "hqm.dark.normA", "hqm.dark.bigA", "hqm.dark.normB", "hqm.dark.bigB"),
		BASE( "hqm.base.normA", "hqm.base.bigA", "hqm.base.normB", "hqm.base.bigB"),
		LINK( "hqm.link.normA", "hqm.link.bigA", "hqm.link.normB", "hqm.link.bigB"),
		PREF( "hqm.pref.normA", "hqm.pref.bigA", "hqm.pref.normB", "hqm.pref.bigB"),
		POST( "hqm.post.normA", "hqm.post.bigA", "hqm.post.normB", "hqm.post.bigB");
		private String mNormKeyA;
		private String mBigKeyA;
		private String mNormKeyB;
		private String mBigKeyB;

		private Type( String normKeyA, String bigKeyA, String normKeyB, String bigKeyB) {
			mNormKeyA = normKeyA;
			mBigKeyA = bigKeyA;
			mNormKeyB = normKeyB;
			mBigKeyB = bigKeyB;
		}

		String getKey( boolean big, boolean alt) {
			if (alt) {
				return big ? mBigKeyB : mNormKeyB;
			}
			else {
				return big ? mBigKeyA : mNormKeyA;
			}
		}
	}
}
