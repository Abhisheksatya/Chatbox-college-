package gui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.AuthPacket;
import model.Client;
import model.ClientEvent;
import model.ClientListener;
import model.Message;
import model.User;

public class Login extends JFrame implements ClientListener, MainFrameListener{
	
	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private boolean validationStatus;
	private JPasswordField passwordField;
	private JTextField userNameField;
	private JMenuBar menuBar;
	private JMenu edit,help;
	private JMenuItem settings, helpContents;
	private SettingFrame settingFrame;
	private Client client;
	
	public Login() {
		setTitle("Login");
		
		validationStatus = false;
		
		menuBar = new JMenuBar();
		
		edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		
		settings = new JMenuItem("settings");
		settings.addActionListener((e) -> {
			if(settingFrame == null)
			settingFrame = new SettingFrame();
			settingFrame.setVisible(true);
		});
		
		edit.add(settings);
		
		help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		help.addActionListener((e) -> {
			//TODO create a help frame
		});
		
		helpContents = new JMenuItem("Help Contents");
		
		help.add(helpContents);
		
		menuBar.add(edit);
		menuBar.add(help);
		
		setJMenuBar(menuBar);
		
		
		setSize(300, 155);
		getContentPane().setLayout(null);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(138, 39, 124, 19);
		getContentPane().add(passwordField);
		
		userNameField = new JTextField();
		userNameField.setBounds(138, 8, 124, 19);
		getContentPane().add(userNameField);
		userNameField.setColumns(10);
		
		JLabel userLabel = new JLabel("User Name:");
		userLabel.setBounds(22, 10, 86, 15);
		getContentPane().add(userLabel);
		
		JLabel passLabel = new JLabel("Password:");
		passLabel.setBounds(22, 41, 76, 15);
		getContentPane().add(passLabel);
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener((e) -> {
			String userName = userNameField.getText();
			String password = new String(passwordField.getPassword());
			if(userName.equals("")) {
				JOptionPane.showMessageDialog(Login.this, 
						"The user name can not be empty!","Invalid User Name",JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				File file = new File(System.getProperty("user.home")+File.separator+"ChatApp"+File.separator+"clientSetting.cfg");
				String settings[] = new String[] {"localhost","1234"};
				try {
					FileInputStream fis = new FileInputStream(file);
					byte[] bytes = new byte[(int)file.length()];
					fis.read(bytes);
					settings = new String(bytes).split("\n");
					InetAddress.getByName(settings[0]);
					client = new Client(settings[0],Integer.parseInt(settings[1]));
				} catch (IOException|NumberFormatException e1) {
					JOptionPane.showMessageDialog(Login.this,"Settings file not found using default values");
					//e1.printStackTrace();
					client = new Client(settings[0],Integer.parseInt(settings[1]));
				}
				try {
					setTitle("Connecting...");
					client.connect(new AuthPacket(userName,password));
					client.setClientListener(Login.this);
					new Thread(new Runnable() {
						public void run() {
							try {
								client.readResponse();
							} catch (ClassNotFoundException | IOException e) {
								if(e instanceof IOException)
									JOptionPane.showMessageDialog(mainFrame, "Can not reach server");
								e.printStackTrace();
							}
						}
					}).start();
				} catch(UnknownHostException ex) {
					JOptionPane.showMessageDialog(Login.this,"The provided IP is not valid please change the IP\n"
							+ "by going into settings");					
				} catch (IOException e1) {
					//TODO display a proper message
					JOptionPane.showMessageDialog(Login.this,"Please check the server IP in Settings \n"
							+ "The Provided IP is not accepting connections on port " +settings[1]);
					setTitle("Login");
				}
			}
			

		});
		loginButton.setBounds(108, 68, 77, 25);
		getContentPane().add(loginButton);

		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	@Override
	public void clientEventOccured(ClientEvent ce) {
		Object obj = ce.getSource();
		if(obj instanceof AuthPacket) {
			setTitle("Login");
			JOptionPane.showMessageDialog(Login.this, "Invalid user name or password.");
		}
		else if(obj instanceof User) {
			try {
				this.setVisible(false);
				client.readResponse();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		else if(obj instanceof Set<?>) {
			
		}
		else if(obj instanceof User[]) {
			client.setAllUsers((User[])obj);
			if(mainFrame == null) {
				mainFrame = new MainFrame(client.getUser(),client.getDepartments(),(User[])obj);
				mainFrame.addMainFrameListener(this);
			}
			else {
				mainFrame.setAllUsers((User[])obj);
				mainFrame.setDepartments(client.getDepartments());
			}
			mainFrame.setVisible(true);
		}
		else if(obj instanceof Message) {
			Message message = (Message) obj;
			if(mainFrame.getReceiver() == null || !mainFrame.getReceiver().getUserId().equals(message.getSender())) {
				User temp = new User("","","","");
				for(User user : client.getAllUsers()) {
					if(user.getUserId().equals(message.getSender())) { 
						temp = user;
						break;
					}
				}
				JOptionPane.showMessageDialog(mainFrame,"You hava new Message From "+
				temp.toString(),"New Message",JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				mainFrame.addMessage(message);
			}
		}
		else if(obj instanceof Message[]) {
			for(Message message : (Message[])obj) {
				mainFrame.addMessage(message);
			}
		}
	}

	@Override
	public void mainFrameEventOccured(MainFrameEvent me) {
		try {
			client.sendObject(me.getSource());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
