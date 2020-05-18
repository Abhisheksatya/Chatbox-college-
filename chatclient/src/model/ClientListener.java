package model;

import java.util.EventListener;

public interface ClientListener extends EventListener {
	
	public void clientEventOccured(ClientEvent ce);

}
