/*
 * 
 */
package server;
	
	import java.awt.EventQueue;
	
	import javax.swing.JCheckBox;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JLabel;
	import javax.swing.JTextField;
	import javax.swing.JComboBox;
	import javax.swing.ScrollPaneConstants;
	import javax.swing.JSeparator;
	import javax.swing.SwingConstants;
	import java.awt.SystemColor;
	import javax.swing.JTextArea;
	
	import abstractions.AtomId;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	
	import static server.Server.*;

/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("rawtypes")
public class ServerMonitor extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField sharedDocs;
	private JTextField openDocs;
	private JTextField registeredUsers;
	private JTextField activeUsers;
	private JComboBox selectDoc;
	private JTextArea users;
	private JComboBox selectUser;
	private JTextArea docs;
	private JTextArea serverLog;
	
	private JCheckBox sent;
	private JCheckBox received;
	private JCheckBox lockRequest;
	private JCheckBox lockRelaese;
	private JCheckBox lockGrant;
	private JCheckBox lockDeny;
	private JCheckBox userLogin;
	private JCheckBox userLogout;
	private JCheckBox userAdd;
	private JCheckBox userRemove;
	private JCheckBox docOpen;
	private JCheckBox docClose;
	private JCheckBox docAdd;
	private JCheckBox docRemove;
	
	StringBuilder fullLog = new StringBuilder();
	private JButton saveLog;
	
    public void serverLogAdd(boolean addToMonitor, String s) {
		fullLog.append(s + "\n");
		if (addToMonitor) {
			serverLog.append(s + "\n");
	        serverLog.setCaretPosition(serverLog.getDocument().getLength());
		}
	}
	public void logSent(int docId, int[] fusers, int[] tusers, int ncmds) {
		serverLogAdd(sent.isSelected(), "Update sent for " + docName(docId) + " from " 
			+ userNames(fusers) + " to " + userNames(tusers) + " with " + ncmds + " commands");
	}
	public void logRecieved(int userId, int docId, int ncmds) {
		serverLogAdd(received.isSelected(), "Update received for " + docName(docId) + " from " 
			+ userName(userId) + " with " + ncmds + " commands");
	}
	public void logLockRequest(int userId, int docId, AtomId aid) {
		serverLogAdd(lockRequest.isSelected(), "Lock request for " + docName(docId) + "/ " 
			+ aid + " from " + userName(userId));
	}
	public void logLockRelaese(int userId, int docId, AtomId aid) {
		serverLogAdd(lockRelaese.isSelected(), "Lock release for " + docName(docId) + "/ " 
			+ aid + " from " + userName(userId));
	}
	public void logLockGrant(int userId, int docId, AtomId aid) {
		serverLogAdd(lockGrant.isSelected(), "Lock granted for " + docName(docId) + "/ " 
			+ aid + " to " + userName(userId));
	}
	public void logLockDeny(int userId, int docId, AtomId aid) {
		serverLogAdd(lockDeny.isSelected(), "Lock deny for " + docName(docId) + "/ " + aid 
			+ " to " + userName(userId));
	}
	public void logUserLogin(int userId) {
		serverLogAdd(userLogin.isSelected(), "Login " + userName(userId));
	}
	public void logUserLogout(int userId) {
		serverLogAdd(userLogout.isSelected(), "Logout " + userName(userId));
	}
	public void logUserAdd(int userId, int docId) {
		serverLogAdd(userAdd.isSelected(), "User " + userName(userId) + " added");
	}
	public void logUserRemove(int userId, int docId) {
		serverLogAdd(userRemove.isSelected(), "User " + userName(userId) + " removed");
	}
	public void logDocOpen(int userId, int docId) {
		serverLogAdd(docOpen.isSelected(), "Document " + docName(docId) + " open by " 
			+ userName(userId));
	}
	public void logDocClose(int userId, int docId, int upr, int ups, int n) {
		serverLogAdd(docClose.isSelected(), "Document " + docName(docId) + " close by " 
			+ userName(userId) + ", received: " + upr + " sent: " + ups + " comands:" + n);
	}
	public void logDocAdd(int userId, int docId) {
		serverLogAdd(docAdd.isSelected(), "Document " + docName(docId) + " added by " 
			+ userName(userId));
	}
	public void logDocRemove(int userId, int docId) {
		serverLogAdd(docRemove.isSelected(), "Document " + docName(docId) + " removed by " 
			+ userName(userId));
	}

	public void updateDocs() {
		
	}
	/*
	 **Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerMonitor frame = new ServerMonitor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerMonitor() {
		setResizable(false);
		setEnabled(false);
		setTitle("EMU Server Monitor");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(5, 5, 653, 898);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel sharedDocuments = new JLabel("Shared Documents");
		sharedDocuments.setBounds(18, 12, 145, 16);
		contentPane.add(sharedDocuments);
		
		sharedDocs = new JTextField();
		sharedDocs.setBounds(141, 6, 59, 28);
		contentPane.add(sharedDocs);
		sharedDocs.setColumns(10);
		
		JLabel lblOpenDocuments = new JLabel("Open Documents");
		lblOpenDocuments.setBounds(18, 44, 115, 16);
		contentPane.add(lblOpenDocuments);
		
		openDocs = new JTextField();
		openDocs.setBounds(141, 38, 59, 28);
		contentPane.add(openDocs);
		openDocs.setColumns(10);
		
		JLabel lblRegisteredUsers = new JLabel("Registered users");
		lblRegisteredUsers.setBounds(353, 12, 122, 16);
		contentPane.add(lblRegisteredUsers);
		
		registeredUsers = new JTextField();
		registeredUsers.setBounds(470, 6, 59, 28);
		contentPane.add(registeredUsers);
		registeredUsers.setColumns(10);
		
		JLabel lblActiveUsers = new JLabel("Active Users");
		lblActiveUsers.setBounds(353, 44, 109, 16);
		contentPane.add(lblActiveUsers);
		
		activeUsers = new JTextField();
		activeUsers.setBounds(470, 38, 59, 28);
		contentPane.add(activeUsers);
		activeUsers.setColumns(10);
		
		JLabel lblSelectADocument = new JLabel("Select a Document");
		lblSelectADocument.setBounds(18, 105, 128, 16);
		contentPane.add(lblSelectADocument);
		
		selectDoc = new JComboBox();
		selectDoc.setBounds(18, 123, 182, 27);
		contentPane.add(selectDoc);
		
		JLabel lblUsersEditingThe = new JLabel("Users Editing the Document");
		lblUsersEditingThe.setBounds(18, 155, 182, 16);
		contentPane.add(lblUsersEditingThe);
		
		JScrollPane usersPane = new JScrollPane();
		usersPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		usersPane.setBounds(18, 174, 276, 133);
		contentPane.add(usersPane);
		
		users = new JTextArea();
		usersPane.setViewportView(users);
		
		JScrollPane docsPane = new JScrollPane();
		docsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		docsPane.setBounds(353, 174, 276, 133);
		contentPane.add(docsPane);
		
		docs = new JTextArea();
		docsPane.setViewportView(docs);
		
		JLabel lblSelectAnUser = new JLabel("Select an User");
		lblSelectAnUser.setBounds(353, 105, 128, 16);
		contentPane.add(lblSelectAnUser);
		
		selectUser = new JComboBox();
		selectUser.setBounds(353, 123, 182, 27);
		contentPane.add(selectUser);
		
		JLabel lblDocumentsEditedBy = new JLabel("Documents Edited by User");
		lblDocumentsEditedBy.setBounds(353, 155, 182, 16);
		contentPane.add(lblDocumentsEditedBy);
		
		JLabel lblServerStatusLog = new JLabel("Server Status Log");
		lblServerStatusLog.setBounds(19, 351, 128, 16);
		contentPane.add(lblServerStatusLog);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(19, 379, 610, 267);
		contentPane.add(scrollPane_2);
		
		serverLog = new JTextArea();
		scrollPane_2.setViewportView(serverLog);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.windowBorder);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(318, 92, 12, 238);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(SystemColor.windowBorder);
		separator_1.setBounds(6, 83, 642, 10);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(SystemColor.windowBorder);
		separator_2.setBounds(6, 326, 642, 10);
		contentPane.add(separator_2);
		
		JLabel lblSelectWhatTo = new JLabel("Select what to show on the log");
		lblSelectWhatTo.setBounds(18, 661, 209, 16);
		contentPane.add(lblSelectWhatTo);
		
		JLabel lblNewLabel = new JLabel("Update");
		lblNewLabel.setBounds(18, 689, 61, 16);
		contentPane.add(lblNewLabel);
		
		sent = new JCheckBox("Sent");
		sent.setBounds(18, 708, 128, 23);
		contentPane.add(sent);
		
		received = new JCheckBox("Received");
		received.setBounds(18, 733, 87, 23);
		contentPane.add(received);
		
		JLabel lblLock = new JLabel("Lock");
		lblLock.setBounds(172, 689, 61, 16);
		contentPane.add(lblLock);
		
		lockRequest = new JCheckBox("Request");
		lockRequest.setBounds(172, 708, 128, 23);
		contentPane.add(lockRequest);
		
		lockRelaese = new JCheckBox("Relaese");
		lockRelaese.setBounds(172, 733, 128, 23);
		contentPane.add(lockRelaese);
		
		lockGrant = new JCheckBox("Grant");
		lockGrant.setBounds(172, 758, 128, 23);
		contentPane.add(lockGrant);
		
		lockDeny = new JCheckBox("Deny");
		lockDeny.setBounds(172, 783, 128, 23);
		contentPane.add(lockDeny);
		
		JLabel lblUser = new JLabel("User");
		lblUser.setBounds(328, 689, 61, 16);
		contentPane.add(lblUser);
		
		userLogin = new JCheckBox("Login");
		userLogin.setBounds(328, 708, 128, 23);
		contentPane.add(userLogin);
		
		userLogout = new JCheckBox("Logout");
		userLogout.setBounds(328, 733, 128, 23);
		contentPane.add(userLogout);
		
		userAdd = new JCheckBox("Add");
		userAdd.setBounds(328, 758, 128, 23);
		contentPane.add(userAdd);
		
		userRemove = new JCheckBox("Remove");
		userRemove.setBounds(328, 783, 128, 23);
		contentPane.add(userRemove);
		
		JLabel lblDocument = new JLabel("Document");
		lblDocument.setBounds(487, 689, 100, 16);
		contentPane.add(lblDocument);
		
		docOpen = new JCheckBox("Open");
		docOpen.setBounds(487, 708, 128, 23);
		contentPane.add(docOpen);
		
		docClose = new JCheckBox("Close");
		docClose.setBounds(487, 733, 73, 23);
		contentPane.add(docClose);
		
		docAdd = new JCheckBox("Add");
		docAdd.setBounds(487, 758, 128, 23);
		contentPane.add(docAdd);
		
		docRemove = new JCheckBox("Remove");
		docRemove.setBounds(487, 783, 128, 23);
		contentPane.add(docRemove);
		
		saveLog = new JButton("Save Log");
		saveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// print to file the contents of fullLog
			}
		});
		saveLog.setBounds(497, 818, 117, 29);
		contentPane.add(saveLog);
	}
}
