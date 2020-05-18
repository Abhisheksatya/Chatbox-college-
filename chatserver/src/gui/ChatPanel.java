package gui;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import model.Message;
import model.User;

public class ChatPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JComboBox<User> userCombo;
	private JComboBox<Date> dateCombo;
	private JComboBox<User> conversed;
	private Vector<Message> chatHistory;
	private JButton view;
	private JButton export;
	private ChatPanelListener listener;
	static private JFileChooser jfc = new JFileChooser(System.getProperty("user.home")); 
	
	public ChatPanel(Vector<User> allUsers) {
		
		jfc.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return null;
			}
			
			@Override
			public boolean accept(File f) {
				String parts[] = f.getName().split("[.]");
				if(parts[parts.length-1].equals("html") || parts[parts.length-1].equals("htm"))
				return true;
				else return false;
			}
		});
		
		userCombo = new JComboBox<>(allUsers);
		userCombo.setEditable(false);
		userCombo.setBounds(100, 42, 254, 24);
		userCombo.addActionListener((e) -> {
			if(listener != null) {
				listener.chatEventOccured(new ChatEvent(userCombo.getSelectedItem(),"user"));
			}
		});
		
		dateCombo = new JComboBox<>();
		dateCombo.setEditable(false);
		dateCombo.setBounds(100, 110, 254, 24);
		dateCombo.addActionListener((e) -> {
			if(listener != null) {
				listener.chatEventOccured(new ChatEvent(new Object[]{userCombo.getSelectedItem(),
						dateCombo.getSelectedItem()},"date"));
			}
		});
		
		conversed = new JComboBox<>();
		conversed.setEditable(false);
		conversed.setBounds(100, 178, 254, 24);
		
		view = new JButton("View");
		view.setBounds(180, 245, 66, 24);
		view.addActionListener((e) -> {
			if(listener != null) {

				try {
					listener.chatEventOccured(new ChatEvent(new Object[] {userCombo.getSelectedItem(),
							conversed.getSelectedItem(),dateCombo.getSelectedItem(),},"view"));
				} catch(NullPointerException ex) {
					JOptionPane.showMessageDialog(ChatPanel.this, "No chats to show for"+((User)userCombo.getSelectedItem()).toString());
				}
				if(chatHistory != null) {
					new ChatViewFrame(chatHistory,(User)userCombo.getSelectedItem(),(User)conversed.getSelectedItem());
				}
			}
			
		});
		
		export = new JButton("Export");
		export.setBounds(275, 245,79, 24);
		export.addActionListener((e) -> {
			if(listener != null) {

				listener.chatEventOccured(new ChatEvent(new Object[] {userCombo.getSelectedItem(),
						conversed.getSelectedItem(),dateCombo.getSelectedItem(),},"view"));
				if(chatHistory != null) {
					int option = jfc.showSaveDialog(ChatPanel.this);
					if(option == JFileChooser.APPROVE_OPTION) {
						File file = jfc.getSelectedFile();
					}
				}
			}
		});
		
		chatHistory = null;
		
		setLayout(null);
		
		add(userCombo);
		add(dateCombo);
		add(conversed);
		add(view);
		add(export);
	}	
	
	public void setChatPanelListener(ChatPanelListener listener) {
		this.listener = listener;
	}
	
	public void setAllUsers(Vector<User> allUser) {
		userCombo.removeAllItems();
		userCombo.setModel(new DefaultComboBoxModel<User>(allUser));
	}
	
	public void setDates(Vector<Date> dates) {
		dateCombo.removeAllItems();
		dateCombo.setModel(new DefaultComboBoxModel<Date>(dates));
	}
	
	public void setConversed(Vector<User> con) {
		conversed.removeAll();
		conversed.setModel(new DefaultComboBoxModel<User>(con));
	}
	
	public void setChatHistory(Vector<Message> chatHistory) {
		this.chatHistory = chatHistory;
	}
}
