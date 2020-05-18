package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DashBoard extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	private JTextArea statusArea;
	private JButton startServer;
	private JButton stopServer;
	private JButton clearStatus;
	private JPanel topPanel;
	private File logFile;
	private DashBoardListener listener;
	
	public DashBoard() {
		logFile = null;
		
		setLayout(new BorderLayout());
		statusArea = new JTextArea();
		statusArea.setFocusable(false);
		
		startServer = new JButton("Start");
		startServer.setActionCommand("Start");
		startServer.addActionListener((ae) -> {
			if(listener != null)
				listener.DashBoardEventOccured(new DashBoardEvent(DashBoard.this.startServer));
		});
		
		stopServer = new JButton("Stop");
		stopServer.setEnabled(false);
		stopServer.setActionCommand("Stop");
		stopServer.addActionListener((ae) -> {
			if(listener != null)
				listener.DashBoardEventOccured(new DashBoardEvent(DashBoard.this.stopServer));
		});
		
		clearStatus = new JButton("Clear");
		clearStatus.addActionListener((ae) -> {
			writeLog();
		});
		
		statusArea.setForeground(Color.RED);
		
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.add(startServer);
		topPanel.add(stopServer);
		topPanel.add(clearStatus);
		
		
		add(topPanel,BorderLayout.NORTH);
		add(new JScrollPane(statusArea));
	}
	
	public void addDashBoardListener(DashBoardListener listener) {
		this.listener = listener;
	}
	
	public void updateStatusArea(String event) {
		statusArea.append("\n"+event);
	}
	
	public void disableStart(boolean b) {
		startServer.setEnabled(!b);
	}
	
	public void disableStop(boolean b) {
		stopServer.setEnabled(!b);
	}
	
	public void initLogFile() {
		String folderName = "ChatApp";
		String fileName = sdf.format(new Date())+".log";
		String absolutePath = System.getProperty("user.home")+File.separator+folderName+File.separator+fileName;
		File logFile = new File(absolutePath);
		if(!logFile.exists()) {
			logFile.getParentFile().mkdirs();
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,"Unable to create the log file");
			}
		}
		this.logFile = logFile;
	}
	
	public void writeLog() {
		if(logFile == null || !logFile.exists())
			initLogFile();
		try {
			FileOutputStream fos = new FileOutputStream(logFile, true);
			fos.write(statusArea.getText().getBytes());
			fos.close();
			statusArea.setText("");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,"Unable to write to log file");
		}
	}

}