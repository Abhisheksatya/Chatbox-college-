package gui;

import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.User;

public class UserDetailsPanel extends JPanel implements Serializable{
	
	private JLabel userIdLabel;
	private JLabel userNameLabel;
	private JLabel departmentLabel;
	private JLabel userId;
	private JLabel userName;
	private JLabel department;
	
	public UserDetailsPanel(User user) {
		
		userIdLabel = new JLabel("User Id:");
		userIdLabel.setBounds(62, 12, 53, 15);
		userNameLabel = new JLabel("User Name:");
		userNameLabel.setBounds(37, 58, 79, 15);
		departmentLabel = new JLabel("Department:");
		departmentLabel.setBounds(27, 101, 88, 15);
		
		userId = new JLabel(user.getUserId());
		userId.setBounds(158, 12, 78, 15);
		userName = new JLabel(user.getName());
		userName.setBounds(158, 58, 78, 15);
		department = new JLabel(user.getDepartment());
		department.setBounds(158, 101, 78, 15);
		setLayout(null);
		
		add(userIdLabel);
		add(userId);
		add(userNameLabel);
		add(userName);
		add(departmentLabel);
		add(department);
	}

}
