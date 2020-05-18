package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private boolean validationStatus;
	private JPasswordField passwordField;
	private JTextField userNameField;
	public Login() {
		setTitle("Admin Login");
		
		validationStatus = false;
		
		setSize(300, 130);
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
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getValidationStatus()) {
					new MainFrame();
					Login.this.dispose();
				}
				else {
					JOptionPane.showMessageDialog(Login.this,
							"Invalid Credentials! Please enter a valid Credentials", "Invalid Creds",
							JOptionPane.INFORMATION_MESSAGE);
					passwordField.setText("");
				}
			}
		});
		loginButton.setBounds(108, 64, 77, 25);
		getContentPane().add(loginButton);

		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	boolean getValidationStatus() {
		String folderName = "ChatApp";
		String fileName = "settings.cfg";
		String absolutePath = System.getProperty("user.home")+File.separator+folderName+File.separator+fileName;
		File settingFile = new File(absolutePath);
		if(!settingFile.exists()) {
			if(userNameField.getText().equals("Admin") && new String(passwordField.getPassword()).equals("Admin")) {
				validationStatus = true;
			}
			return validationStatus;
		}
		try {
			FileInputStream fis = new FileInputStream(settingFile);
			byte[] bytes = new byte[(int)settingFile.length()];
			fis.read(bytes);
			String settings[] = new String(bytes).split("\n");
			fis.close();
			if(userNameField.getText().equals(settings[1]) && new String(passwordField.getPassword()).equals(settings[2])) {
				validationStatus = true;
			}
			return validationStatus;
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,"Something went Wrong");
			return true;
		}
	}
}
