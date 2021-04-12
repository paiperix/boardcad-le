package boardcad.settings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import boardcad.settings.Settings.Enumeration;
import boardcad.settings.Settings.FileName;
import boardcad.settings.Settings.Measurement;
import boardcad.settings.Settings.SettingChangedCallback;

public class Setting
{
	protected String mKey;
	protected Object mValue;
	protected String mDescription;
	protected boolean mDisabled = false;
	protected boolean mHidden = false;

	public boolean isDisabled() {
		return mDisabled;
	}

	public void setDisabled(boolean disabled) {
		this.mDisabled = disabled;
	}

	public boolean isHidden() {
		return mHidden;
	}

	public void setHidden(boolean hidden) {
		this.mHidden = hidden;
	}

	public List<SettingChangedCallback> mCallbacks = new ArrayList<SettingChangedCallback>();

	public Setting(final String key, final Object value, final String desc)
	{
		mKey = key;
		mValue = value;
		mDescription = desc;
	}

	public Setting(final String key, final Object value, final String desc, final SettingChangedCallback cb)
	{
		mKey = key;
		mValue = value;
		mDescription = desc;
		mCallbacks.add(cb);
	}

	public String key(){
		return mKey;
	}

	public String toString()
	{
		return mDescription + ":" + mValue.toString();
	}

	public Object value(){
		return mValue;
	}

	public int intValue(){
		return ((Integer)mValue).intValue();
	}

	public String stringValue(){
		return mValue.toString();
	}

	public Enumeration enumValue(){
		return (Enumeration)mValue;
	}

	public double doubleValue(){
		return ((Double)mValue).doubleValue();
	}

	public Color colorValue(){
		return (Color)mValue;
	}

	public String filenameValue(){
		return ((FileName)mValue).toString();
	}

	public double measurementValue(){
		return ((Measurement)mValue).getValue();
	}

	public void addCallback(final SettingChangedCallback cb)
	{
		mCallbacks.add(cb);
	}

	public void removeCallback(final SettingChangedCallback cb)
	{
		mCallbacks.remove(cb);
	}

	public void signal()
	{
		if(mCallbacks != null)
		{
			for (SettingChangedCallback cb : mCallbacks)
			{
				cb.onSettingChanged(this);
			}
		}
	}
};
