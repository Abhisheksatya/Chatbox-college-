package gui;

import java.awt.BorderLayout;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import model.Server;
import model.User;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private SettingPanel settingPanel;
	private DashBoard dashBoard;
	private ChatPanel chatPanel;
	private UsersPanel usersPanel;
	private boolean running;
	private Server server;
	
	public MainFrame() {
		super("Server");
		
		running = false;
		
		tabbedPane = new JTabbedPane();
		
		settingPanel = new SettingPanel();
		settingPanel.setSettingListener((SettingEvent se) -> {
			int port = se.getPort();
			server.setPort(port);
		});
		
		dashBoard = new DashBoard();
		dashBoard.addDashBoardListener((de) -> {
			String actionCommand = ((JButton)de.getSource()).getActionCommand();
			if(actionCommand == "Start") {
				new Thread(() -> {
					server.startServer();
				}).start();
				running = true;
				settingPanel.disableSettings(true);
				dashBoard.disableStart(true);
				dashBoard.disableStop(false);
			}
			else {
				server.stopServer();
				running = false;
				settingPanel.disableSettings(false);
				dashBoard.disableStart(false);
				dashBoard.disableStop(true);
			}
		});
		
		try {
			server = new Server(settingPanel.getPort());
			server.addServerEventListener((se) -> {
				String eventMessage = se.getEventMessage();
				dashBoard.updateStatusArea(eventMessage);
			});
			server.initAllUsers();
		} catch (IOException e) {
			running = false;
			settingPanel.disableSettings(true);
			JOptionPane.showMessageDialog(this,e.getMessage(),"Port already in use", JOptionPane.ERROR_MESSAGE);
		}
		//TODO replace the dummy chat panel
		chatPanel = new ChatPanel(server.getAllUsers());
		chatPanel.setChatPanelListener((e) -> {
			switch(e.getAction()) {
			case "user":
				chatPanel.setDates(server.getDates((User)e.getSource()));
				break;
			case "date":
				chatPanel.setConversed(server.getConversed((Object[])e.getSource()));
				break;
			case "view":
			case "export":
				chatPanel.setChatHistory(server.getChatHistory((Object[])e.getSource()));
				break;
				
			}
		});
		
		usersPanel = new UsersPanel(server.getAllUsers());
		usersPanel.setUsersPanelListener((e) -> {
			switch(e.getAction()) {
			case "Add":
				server.addUser((User)e.getSource());
				break;
			case "Remove":
				server.removeUser((User)e.getSource());
				break;
			case "Update":
				server.updateUser((User)e.getSource());
				break;
			}
		});
		
		tabbedPane.addTab("DashBoard", dashBoard);
		tabbedPane.addTab("Chats", chatPanel);
		tabbedPane.addTab("Settings", settingPanel);
		tabbedPane.addTab("Users", usersPanel);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		
		setSize(470,530);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	boolean isRunning() {
		return running;
	}

}
