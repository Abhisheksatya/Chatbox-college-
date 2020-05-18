package gui;

import java.util.EventListener;

public interface SettingListener extends EventListener {
	
	public void settingsChanged(SettingEvent se);
}
