package model;

import java.util.EventListener;

public interface UserEventListener extends EventListener {
	
	public void userEventOccured(UserEvent ue);

}
