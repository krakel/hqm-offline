package de.doerl.hqm.quest;

import java.awt.GraphicsEnvironment;

import javax.swing.Icon;
import javax.swing.UIManager;

import de.doerl.hqm.utils.ResourceManager;

public enum ParameterType {
	PARAMETER_SETS( "ParameterSets", "paramSets"),
	PARAMETER_BOOLEAN( "ParameterBoolean", "paramBool"),
	PARAMETER_INTEGER( "ParameterInteger", "paramInt"),
	PARAMETER_INT( "ParameterInt", "paramInt"),
	PARAMETER_INT_ARR( "ParameterIntArr", "paramIntArr"),
	PARAMETER_STACK( "ParameterStack", "paramStk"),
	PARAMETER_STRING( "ParameterString", "paramString");
	private String mToken;
	private String mIconName;

	private ParameterType( String token, String key) {
		mToken = token;
		mIconName = "hqm.icon." + key;
		ResourceManager.makeIcon( mIconName, key + ".gif");
	}

	public static ParameterType get( int idx) {
		ParameterType[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}

	public Icon getIcon() {
		if (GraphicsEnvironment.isHeadless()) {
			return null;
		}
		return UIManager.getIcon( mIconName);
	}

	public String getToken() {
		return mToken;
	}
}
