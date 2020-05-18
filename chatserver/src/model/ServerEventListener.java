package model;

import java.util.EventListener;

public interface ServerEventListener extends EventListener {
	
	public void serverEventOccured(ServerEvent se);

}
