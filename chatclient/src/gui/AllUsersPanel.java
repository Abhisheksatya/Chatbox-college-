package gui;

import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.User;

public class AllUsersPanel extends JPanel {
	private JTree tree;
	private JScrollPane scrollPane;
	private DefaultMutableTreeNode usersNode;
	private JButton chat;
	private UserPanelListener listener;
	private User[] allUsers;
	private Set<String> departments;
	private User user;
	
	public AllUsersPanel(User user, Set<String> departments, User[] allUsers) {
		
		this.user = user;
		this.departments = departments;
		this.allUsers = allUsers;
		
		usersNode = new DefaultMutableTreeNode("Users");
		createUsersTree();
		
		tree = new JTree(usersNode);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		
		chat = new JButton("Chat");
		chat.setBounds(362, 189, 66, 25);
		chat.addActionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if(node != null) {
				if(listener != null && node.getUserObject() instanceof User) {
					listener.userPanelEventOccured(new UserPanelEvent(node.getUserObject()));
				}
				else 
					JOptionPane.showMessageDialog(AllUsersPanel.this, "Please select a valid user"
							+ " and not a department!!");
			}
			else  {
				JOptionPane.showMessageDialog(AllUsersPanel.this, "Please Select a user to chat.",
						"No User Selected",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		scrollPane = new JScrollPane(tree);
		scrollPane.setBounds(12, 27, 426, 150);
		
		add(scrollPane);
		add(chat);
		
		setLayout(null);
	}
	
	public void createUsersTree() {
		usersNode.removeAllChildren();
		
		departments.forEach( department -> {
			DefaultMutableTreeNode temp = new DefaultMutableTreeNode(department); 
			for(User tempUser : allUsers) {
				if(tempUser.getDepartment().equalsIgnoreCase(department) && !tempUser.equals(user))
					temp.add(new DefaultMutableTreeNode(tempUser));
			}
			usersNode.add(temp);
		});
	}
	
	public void addUserPanelListener(UserPanelListener listener) {
		this.listener = listener;
	}

	public void setAllUsers(User[] allUsers) {
		this.allUsers = allUsers;
	}

	public void setDepartments(Set<String> departments) {
		this.departments = departments;
	}
}
