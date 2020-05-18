package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.User;

public class UsersPanel extends JPanel {
	
	private JButton add;
	private JButton update;
	private JButton remove;
	private JPanel buttonPanel;
	private UserTable userTable;
	private UserForm userForm;
	private DefaultTableModel tableModel;
	private UsersPanelListener listener;
	private int rowToUpdate;
	private int[] rowsToRemove;
	
	public UsersPanel(Vector<User> allUsers) {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		rowToUpdate = -1;
		rowsToRemove = null;
		
		userForm = new UserForm();
		userForm.setUserFormListener((fe) -> {
			String actionCommand = ((JButton)fe.getSource()).getActionCommand();
			User user = fe.getUser();
			if(actionCommand.equals("Add")) {
				if(allUsers.contains(fe.getUser())) {
					JOptionPane.showMessageDialog(userForm, "The entered user ID is already in use.",
							"Duplicate UserId",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					userForm.setVisible(false);
					allUsers.add(user);
					Object row[] = new Object[] {user.getUserId(),user.getName(),user.getDepartment(),user.getPassword()};
					tableModel.addRow(row);
					userForm.clearFields();
					listener.usersPanelChanged(new UsersPanelEvent(user,actionCommand));
				}
			}
			else {
				userForm.setVisible(false);
				if(rowToUpdate != -1) {
					for(User usr : allUsers) {
						if(usr.equals(user)) {
							usr.setName(user.getName());
							usr.setDepartment(user.getDepartment());
							usr.setPassword(user.getPassword());
							break;
						}
					}
					tableModel.setValueAt(user.getName(), rowToUpdate, 1);
					tableModel.setValueAt(user.getDepartment(), rowToUpdate, 2);
					tableModel.setValueAt(user.getPassword(), rowToUpdate, 3);
					rowToUpdate = -1;
					listener.usersPanelChanged(new UsersPanelEvent(user,actionCommand));
				}

			}
		});
		
		add = new JButton("Add");
		add.addActionListener((ae) -> {
			userForm.setButtonAction("Add");
			userForm.setVisible(true);
		});
		
		update = new JButton("Update");
		update.addActionListener((e) -> {
			userForm.setButtonAction("Update");
			rowToUpdate = userTable.getSelectedRow();
			if(rowToUpdate != -1) {
				userForm.setValues((String)userTable.getValueAt(rowToUpdate,0),
				(String)userTable.getValueAt(rowToUpdate,1),(String)userTable.getValueAt(rowToUpdate,2),
				(String)userTable.getValueAt(rowToUpdate,3));
				userForm.setVisible(true);	
			}
			else {
				JOptionPane.showMessageDialog(UsersPanel.this,"Please Select a row to update"
						,"No Selection",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		remove = new JButton("Remove");
		remove.addActionListener((e) -> {
			rowsToRemove = userTable.getSelectedRows();
			
			if(rowsToRemove.length != 0) {
				for(int i = rowsToRemove.length-1; i >= 0; i--) {
					tableModel.removeRow(rowsToRemove[i]);
					User user = allUsers.remove(rowsToRemove[i]);
					listener.usersPanelChanged(new UsersPanelEvent(user,"Remove"));
				}
			}
			
		});
		
		Object[][] rows = new Object[allUsers.size()][4];
		
		buttonPanel.add(add);
		buttonPanel.add(remove);
		buttonPanel.add(update);
		
		
		int i = 0;
		for(User user : allUsers) {
			rows[i][0] = user.getUserId();
			rows[i][1] = user.getName();
			rows[i][2] = user.getDepartment();
			rows[i][3] = user.getPassword();
			i++;
		}
		
		tableModel = new DefaultTableModel(rows,new String[] {"User ID","User Name","Department","Password"});
		userTable = new UserTable(tableModel);
		setLayout(new BorderLayout(0, 0));
		
		add(buttonPanel,BorderLayout.NORTH);
		add(new JScrollPane(userTable),BorderLayout.CENTER);
		
	}
	
	public void setUsersPanelListener(UsersPanelListener listener) {
		this.listener = listener; 
	}

}
