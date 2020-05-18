package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SettingFrame extends JFrame {
	
	private JTextField portField;
	private JLabel portLabel;
	private JButton save, reset;
	private File settingFile;
	private JTextField ipField;
	private JLabel ipLabel;
	
	public SettingFrame() {
		super("Settings");
		
		initSettingsFile();
		
		String[] settings = readSettings();

		ipLabel = new JLabel("IP Address:");
		ipLabel.setBounds(111, 38, 83, 15);
		
		ipField = new JTextField();
		ipField.setBounds(226, 36, 128, 19);
		ipField.setText(settings[0]);
		
		portLabel = new JLabel("Port Number: ");
		portLabel.setBounds(97, 86, 97, 15);
		
		portField = new JTextField();
		portField.setBounds(226, 84, 128, 19);
		portField.setText(settings[1]);
		
		save = new JButton("Save");
		save.setBounds(162, 138, 75, 29);
		save.addActionListener((ae) -> {
			try {
				InetAddress.getByName(ipField.getText());
				int port = Integer.parseInt(portField.getText());
				if(port > 1024 && port < 65535) {
					writeSettings(new String[] {ipField.getText(),portField.getText()});
				}
				else throw new NumberFormatException();
			} catch(UnknownHostException e) {
				JOptionPane.showMessageDialog(SettingFrame.this,"The entered IP address is invalid!!",
						"Invalid IP",JOptionPane.INFORMATION_MESSAGE);
				
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(SettingFrame.this, "Please Enter a valid port number!!"
						, "Invalid port", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		reset = new JButton("Reset");
		reset.setBounds(249, 138, 75, 29);
		reset.addActionListener((ae) -> {
			ipField.setText("localhost");
			portField.setText("1234");
			writeSettings(new String[] {"localhost","1234"});
		});
		
		getContentPane().setLayout(null);
		
		getContentPane().add(ipLabel);
		getContentPane().add(ipField);
		getContentPane().add(portLabel);
		getContentPane().add(portField);
		getContentPane().add(save);
		getContentPane().add(reset);
		
		setResizable(false);
		setSize(450, 270);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}
	
	public void initSettingsFile() {
		String folderName = "ChatApp";
		String fileName = "clientSetting.cfg";
		String absolutePath = System.getProperty("user.home")+File.separator+folderName+File.separator+fileName;
		File settingFile = new File(absolutePath);
		if(!settingFile.exists()) {
			settingFile.getParentFile().mkdirs();
			try {
				settingFile.createNewFile();
				writeSettings(new String[] {"localhost","1234"});
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,"Unable to create the sttings file");
			}
		}
		this.settingFile = settingFile;
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
	
	public String[] readSettings() {
		try {
			FileInputStream fis = new FileInputStream(settingFile);
			byte[] bytes = new byte[(int)settingFile.length()];
			fis.read(bytes);
			String settings[] = new String(bytes).split("\n");
			Integer.parseInt(settings[1]);
			fis.close();
			return settings;
		}
		catch(IOException | NumberFormatException e) {
			String settings[] = new String[] {"localhost", "1234"};
			writeSettings(settings);
			return settings;
		}
	}
	
}
