package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.User;

public class UserForm extends JFrame {
	
	private JButton button;
	private JLabel userIdLabel;
	private JTextField userIdField;
	private JLabel userNameLabel;
	private JTextField userNameField;
	private JLabel departmentLabel;
	private JTextField departmentField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private UserFormListener listener;
	
	public UserForm() {
		super("User Details");
		//setType(Type.POPUP);
		setResizable(false);
		
		
		button = new JButton("Add");
		button.setBounds(101, 134, 95, 25);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(listener != null) {
					String userId, userName, department, password;
					userId = userIdField.getText();
					userName = userNameField.getText();
					department = departmentField.getText();
					password = new String(passwordField.getPassword());
					if(userId.equals("") || userName.equals("") || department.equals("") || password.equals("") ) {
						JOptionPane.showMessageDialog(UserForm.this, "One or more fields are "
								+ "empty please fill the fields","Empty Fields",JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						listener.UserFormFilled(new UserFormEvent(button, new User(userId,userName,department,password)));
					}
				}
			}
		});
		userIdLabel = new JLabel("User ID:");
		userIdLabel.setBounds(56, 7, 54, 15);
		userIdField = new JTextField();
		userIdField.setBounds(128, 5, 116, 19);
		userNameField = new JTextField();
		userNameField.setBounds(128, 36, 116, 19);
		userNameLabel = new JLabel("User Name:");
		userNameLabel.setBounds(31, 38, 79, 15);
		departmentField = new JTextField();
		departmentField.setBounds(128, 67, 116, 19);
		departmentLabel = new JLabel("Department:");
		departmentLabel.setBounds(22, 69, 88, 15);
		passwordField = new JPasswordField();
		passwordField.setBounds(128, 98, 116, 19);
		passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(40, 96, 70, 15);
		getContentPane().setLayout(null);
		
		getContentPane().add(userIdLabel);
		getContentPane().add(userIdField);
		getContentPane().add(userNameLabel);
		getContentPane().add(userNameField);
		getContentPane().add(departmentLabel);
		getContentPane().add(departmentField);
		getContentPane().add(passwordLabel);
		getContentPane().add(passwordField);
		getContentPane().add(button);
		setSize(295, 211);
	}
	
	public void setUserFormListener(UserFormListener listener) {
		this.listener = listener;
	}
	
	public void setValues(String userId,String userName,String department,String password) {
		userIdField.setText(userId);
		userNameField.setText(userName);
		departmentField.setText(department);
		passwordField.setText(password);
	}
	
	public void clearFields() {
		userIdField.setText("");
		userNameField.setText("");
		departmentField.setText("");
		passwordField.setText("");
	}
	
	public void setButtonAction(String action) {
		button.setText(action);
		button.setActionCommand(action);
		if(action.equals("Update"))
			userIdField.setEditable(false);
		else 
			userIdField.setEditable(true);
	}

}
