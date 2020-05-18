package gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SettingPanel extends JPanel {
	
	private JTextField portField;
	private JLabel portLabel;
	private JButton save, reset;
	private SettingListener listener;
	private File settingFile;
	private JLabel adminUserNameLabel;
	private JTextField adminUserNameField;
	private JLabel adminPasswordLabel;
	private JPasswordField adminPasswordField;
	
	public SettingPanel() {
		initSettingsFile();
		
		String settings[] = readSettings();

		portLabel = new JLabel("Port Number: ");
		portLabel.setBounds(97, 38, 97, 15);
		
		portField = new JTextField();
		portField.setBounds(228, 36, 128, 19);
		portField.setText(settings[0]);
		
		adminUserNameLabel = new JLabel("Admin UserName:");
		adminUserNameLabel.setBounds(66, 65, 128, 19);
		
		adminUserNameField = new JTextField();
		adminUserNameField.setBounds(228, 67, 128, 19);
		adminUserNameField.setText(settings[1]);
		
		adminPasswordLabel = new JLabel("Admin Password:");
		adminPasswordLabel.setBounds(76, 98, 128, 19);
		
		adminPasswordField = new JPasswordField();
		adminPasswordField.setBounds(228, 98, 128, 19);
		adminPasswordField.setText(settings[2]);
		
		save = new JButton("Save");
		save.setBounds(160, 156, 67, 25);
		save.setActionCommand("SavePort");
		save.addActionListener((ActionEvent ae) -> {
			if(listener != null) {
				try {
					String []newSettings = new String[] {portField.getText(),adminUserNameField.getText(),new String(adminPasswordField.getPassword())};
					int newPort = Integer.parseInt(newSettings[0]);
					if(newPort > 1024 && newPort < 65535) {
						portField.setText(""+newPort);
						writeSettings(newSettings);
						listener.settingsChanged(new SettingEvent(ae.getSource(),newPort));
					}
					else throw new NumberFormatException();
				}
				catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(SettingPanel.this, "Please Enter a "
							+ "valid port number!", "Invalid Port Number",
							JOptionPane.WARNING_MESSAGE);
				}
			}
			
		});
		
		reset = new JButton("Reset");
		reset.setBounds(239, 156, 72, 25);
		reset.setActionCommand("reset");
		reset.addActionListener((ActionEvent ae) -> {
			if(listener != null) {
				portField.setText("1234");
				adminUserNameField.setText("Admin");
				adminPasswordField.setText("Admin");
				writeSettings(new String[] {"1234","Admin","Admin"});			
				listener.settingsChanged(new SettingEvent(ae.getSource(), 1234));
			}
		});
		
		setLayout(null);
		
		add(portLabel);
		add(portField);
		add(adminUserNameLabel);
		add(adminUserNameField);
		add(adminPasswordLabel);
		add(adminPasswordField);
		add(save);
		add(reset);
		
	}
	
	public void setSettingListener(SettingListener listener) {
		this.listener = listener;
	}
	
	public void disableSettings(boolean b) {
		if(b) {
			portField.setEditable(false);
			save.setEnabled(false);
			reset.setEnabled(false);
		}
		else {
			portField.setEditable(true);
			save.setEnabled(true);
			reset.setEnabled(true);
		}
	}
	
	public void initSettingsFile() {
		String folderName = "ChatApp";
		String fileName = "settings.cfg";
		String absolutePath = System.getProperty("user.home")+File.separator+folderName+File.separator+fileName;
		File settingFile = new File(absolutePath);
		if(!settingFile.exists()) {
			settingFile.getParentFile().mkdirs();
			try {
				settingFile.createNewFile();
				writeSettings(new String[] {"1234","Admin","Admin"});
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,"Unable to create the sttings file");
			}
		}
		this.settingFile = settingFile;
	}
	
	public String[] readSettings() {
		try {
			FileInputStream fis = new FileInputStream(settingFile);
			byte[] bytes = new byte[(int)settingFile.length()];
			fis.read(bytes);
			String settings[] = new String(bytes).split("\n");
			Integer.parseInt(settings[0]);
			fis.close();
			return settings;
		}
		catch(IOException | NumberFormatException e) {
			String settings[] = new String[] {"1234", "Admin", "Admin"};
			writeSettings(settings);
			return settings;
		}
	}
	
	public void writeSettings(String settings[]) {
		if(settingFile == null || !settingFile.exists())
			initSettingsFile();
		try {
			FileOutputStream fos = new FileOutputStream(settingFile, false);
			fos.write(String.join("\n", settings).getBytes());
			fos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,"Unable to write to the settings File");
		}
	}

	public int getPort() {
		try {
			return Integer.parseInt(portField.getText());
		}
		catch(NumberFormatException e) {
			return 1234;
		}
	}
}
