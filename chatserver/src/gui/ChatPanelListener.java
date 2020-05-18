package gui;

import java.util.EventListener;

public interface ChatPanelListener extends EventListener {
	public void chatEventOccured(ChatEvent ce);
}
