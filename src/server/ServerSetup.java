/**
 * 
 */
package server;
	
	import java.awt.EventQueue;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JLabel;
	import java.awt.Font;
	import javax.swing.JTextField;
	import javax.swing.JComboBox;
	import javax.swing.JButton;
	import javax.swing.JSeparator;
	import java.awt.SystemColor;
	import javax.swing.JCheckBox;
	import javax.swing.SwingConstants;
	import java.awt.Color;
	import javax.swing.DefaultComboBoxModel;
    import java.awt.event.ActionListener;
    import java.awt.event.ActionEvent;
    import java.io.*;
    import abstractions.User;
    import emu.EMU;
    import abstractions.EMUDoc;
    import server.FileManager;


/**
 * @author rdru
 * INV: 
 */
@SuppressWarnings("serial")
public class ServerSetup extends JFrame {
	private JPanel contentPane;
	private JTextField userFullName;
	private JTextField userEmail;
	private JTextField userLogname;
	private JTextField userPassword;
	private JTextField filePath;
	private JTextField docName;
	JCheckBox allowAddUser;
	JCheckBox allowRemoveUser;	
	JCheckBox allowRemoveDocument;	
	JCheckBox allowAddDocument;	
	JCheckBox allowServerLoad;	
	JCheckBox allowUpdateFrequency;
	public static String fileDir = "emu-docs";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerSetup frame = new ServerSetup();
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
	@SuppressWarnings("rawtypes")
	public ServerSetup() {
		setResizable(false);
		setTitle("EMU Server Setup");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 647, 784);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAddUser = new JLabel("Add User");
		lblAddUser.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblAddUser.setBounds(22, 19, 88, 16);
		contentPane.add(lblAddUser);
		
		JLabel lblUserFullName = new JLabel("User Full Name");
		lblUserFullName.setBounds(22, 47, 110, 16);
		contentPane.add(lblUserFullName);
		
		userFullName = new JTextField();
		userFullName.setBounds(22, 64, 239, 28);
		contentPane.add(userFullName);
		userFullName.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(22, 101, 61, 16);
		contentPane.add(lblEmail);
		
		userEmail = new JTextField();
		userEmail.setColumns(10);
		userEmail.setBounds(22, 118, 239, 28);
		contentPane.add(userEmail);
		
		JLabel lblLogname = new JLabel("Logname");
		lblLogname.setBounds(22, 155, 61, 16);
		contentPane.add(lblLogname);
		
		userLogname = new JTextField();
		userLogname.setColumns(10);
		userLogname.setBounds(22, 172, 239, 28);
		contentPane.add(userLogname);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(22, 209, 61, 16);
		contentPane.add(lblPassword);
		
		userPassword = new JTextField();
		userPassword.setColumns(10);
		userPassword.setBounds(22, 226, 239, 28);
		contentPane.add(userPassword);
		
		JLabel lblRemoveUser = new JLabel("Remove User");
		lblRemoveUser.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblRemoveUser.setBounds(22, 310, 110, 16);
		contentPane.add(lblRemoveUser);
		
		JComboBox userSelected = new JComboBox();
		userSelected.setBounds(22, 338, 136, 27);
		contentPane.add(userSelected);
		
		JButton removeUser = new JButton("Remove");
		removeUser.setBounds(160, 338, 101, 29);
		contentPane.add(removeUser);
		
		JSeparator horizontal1 = new JSeparator();
		horizontal1.setForeground(SystemColor.windowBorder);
		horizontal1.setBounds(6, 392, 632, 12);
		contentPane.add(horizontal1);
		
		JLabel lblServerOperation = new JLabel("Server Operation");
		lblServerOperation.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblServerOperation.setBounds(22, 416, 136, 16);
		contentPane.add(lblServerOperation);
		
		JLabel lblSetDefaultUpdate = new JLabel("Set Default Update Frequency");
		lblSetDefaultUpdate.setBounds(22, 448, 196, 16);
		contentPane.add(lblSetDefaultUpdate);
		
		JComboBox updateFrequency = new JComboBox();
		updateFrequency.setModel(new DefaultComboBoxModel(
			new String[] {"1", "2", "3", "4", "5", "8", "10", "15", "20"}));
		updateFrequency.setBounds(22, 465, 61, 27);
		contentPane.add(updateFrequency);
		
		JComboBox adaptativeLoad = new JComboBox();
		adaptativeLoad.setModel(new DefaultComboBoxModel(
			new String[] {"Light load", "Median load", "Heavy load"}));
		adaptativeLoad.setBounds(308, 465, 124, 27);
		contentPane.add(adaptativeLoad);
		
		JLabel lblSetAdaptiveServer = new JLabel("Set Adaptative Server Load");
		lblSetAdaptiveServer.setBounds(308, 448, 196, 16);
		contentPane.add(lblSetAdaptiveServer);
		
		JLabel lblDocumentManagement = new JLabel("Add Document");
		lblDocumentManagement.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblDocumentManagement.setBounds(308, 19, 184, 16);
		contentPane.add(lblDocumentManagement);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(308, 6, -12, 348);
		contentPane.add(separator_1);
		
		JLabel lblSelectAFile = new JLabel("Select a File");
		lblSelectAFile.setBounds(308, 47, 110, 16);
		contentPane.add(lblSelectAFile);
		
		filePath = new JTextField();
		filePath.setColumns(10);
		filePath.setBounds(308, 64, 239, 28);
		contentPane.add(filePath);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(548, 65, 77, 29);
		contentPane.add(btnBrowse);
		
		JLabel lblEmuDocumentName = new JLabel("EMU Document Name");
		lblEmuDocumentName.setBounds(308, 101, 147, 16);
		contentPane.add(lblEmuDocumentName);
		
		docName = new JTextField();
		docName.setColumns(10);
		docName.setBounds(308, 118, 160, 28);
		contentPane.add(docName);
		
		JButton addDoc = new JButton("Add");
		addDoc.setBounds(548, 118, 77, 29);
		contentPane.add(addDoc);
		addDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//EMUDoc doc = new EMUDoc(filePath.getText(), false, docName.getText());
				String home = System.getProperty("user.home");
				String completePath = new File(home, fileDir).toString();
				FileManager fileManager = new FileManager(completePath);
				fileManager.getList();
				fileManager.addFile("java_example.txt");
			}
		});
		
		JLabel lblRemoveDocument = new JLabel("Remove Document");
		lblRemoveDocument.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblRemoveDocument.setBounds(308, 155, 160, 16);
		contentPane.add(lblRemoveDocument);
		
		JComboBox selectedDoc = new JComboBox();
		selectedDoc.setBounds(308, 172, 136, 27);
		contentPane.add(selectedDoc);
		
		JButton removeDoc = new JButton("Remove");
		removeDoc.setBounds(548, 172, 77, 29);
		contentPane.add(removeDoc);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(284, -11, 0, 356);
		contentPane.add(separator_2);
		
		JSeparator vertical = new JSeparator();
		vertical.setOrientation(SwingConstants.VERTICAL);
		vertical.setForeground(new Color(154, 154, 154));
		vertical.setBounds(278, 6, 12, 387);
		contentPane.add(vertical);
		
		JSeparator horizontal2 = new JSeparator();
		horizontal2.setForeground(SystemColor.windowBorder);
		horizontal2.setBounds(6, 510, 632, 12);
		contentPane.add(horizontal2);
		
		JLabel lblSetupConfiguration = new JLabel("Setup Configuration");
		lblSetupConfiguration.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblSetupConfiguration.setBounds(22, 531, 167, 16);
		contentPane.add(lblSetupConfiguration);
		
		JLabel lblCheckOptionsThat = new JLabel("Check Options that do not require an Adm Autentication");
		lblCheckOptionsThat.setBounds(22, 559, 433, 16);
		contentPane.add(lblCheckOptionsThat);
		
		allowAddUser = new JCheckBox("Add User");
		allowAddUser.setBounds(22, 579, 128, 23);
		contentPane.add(allowAddUser);
		
		allowRemoveUser = new JCheckBox("Remove User");
		allowRemoveUser.setBounds(22, 602, 128, 23);
		contentPane.add(allowRemoveUser);
		
		allowRemoveDocument = new JCheckBox("Remove Document");
		allowRemoveDocument.setBounds(183, 602, 150, 23);
		contentPane.add(allowRemoveDocument);
		
		allowAddDocument = new JCheckBox("Add Document");
		allowAddDocument.setBounds(183, 579, 150, 23);
		contentPane.add(allowAddDocument);
		
		allowServerLoad = new JCheckBox("Set Server Load");
		allowServerLoad.setBounds(388, 602, 128, 23);
		contentPane.add(allowServerLoad);
		
		allowUpdateFrequency = new JCheckBox("Set Update Frequency");
		allowUpdateFrequency.setBounds(388, 579, 196, 23);
		contentPane.add(allowUpdateFrequency);
		
		JButton addUser = new JButton("Add");
		addUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if (allowAddUser.isSelected() || validateAdm())
				//	addUser();
				
				User newUser =  new User(0, userLogname.getText(), userPassword.getText(),
						userFullName.getText(), userEmail.getText());
				
				userLogname.setText(null);
				userPassword.setText(null);
				userFullName.setText(null);
				userEmail.setText(null);
				
				System.out.println(EMU.userList.getFirst().getName());
				System.out.println(EMU.userList.getLast().getName());
				System.out.println("test");
	
			}
		});
		addUser.setBounds(160, 269, 101, 29);
		contentPane.add(addUser);
		
		JButton setup = new JButton("Setup");
		setup.setBounds(524, 642, 101, 29);
		contentPane.add(setup);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.windowBorder);
		separator.setBounds(6, 683, 632, 12);
		contentPane.add(separator);
		
		JButton help = new JButton("Help");
		help.setBounds(524, 707, 101, 29);
		contentPane.add(help);
		
		JLabel lblSeconds = new JLabel("seconds");
		lblSeconds.setBounds(95, 469, 61, 16);
		contentPane.add(lblSeconds);
	}
}
