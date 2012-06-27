/**
 * @author rdru
 * INV: 
 */
package interfaces;
	
	import java.awt.EventQueue;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.JLabel;
	import javax.swing.JComboBox;
	import javax.swing.JTextField;
	import javax.swing.JPasswordField;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	
	import abstractions.User;
	import static server.Server.*;

@SuppressWarnings("serial")
public class LoginW extends JFrame {
	public JFrame loginW = this;
	private JPanel contentPane;
	private JTextField lognameField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginW frame = new LoginW();
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
	public LoginW() {
		// semantic data
		
		setTitle("Login Window - Server Access");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 361, 224);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLoginName = new JLabel("Login Name");
		lblLoginName.setBounds(37, 75, 97, 16);
		contentPane.add(lblLoginName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(202, 75, 61, 16);
		contentPane.add(lblPassword);
		
		@SuppressWarnings("rawtypes")
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(154, 24, 131, 27);
		contentPane.add(comboBox);
		
		JLabel lblSelectTheServer = new JLabel("Select the Server:");
		lblSelectTheServer.setBounds(37, 28, 121, 16);
		contentPane.add(lblSelectTheServer);
		
		lognameField = new JTextField();
		lognameField.setBounds(37, 95, 120, 28);
		contentPane.add(lognameField);
		lognameField.setColumns(12);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(202, 95, 120, 28);
		contentPane.add(passwordField);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(-17, 182, 107, -1);
		contentPane.add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginW.setVisible(false);
			}
		});
		btnCancel.setBounds(37, 152, 117, 29);
		contentPane.add(btnCancel);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String logname = "to compile";
				String password = "to compile";
				String c = "to compile";
				User u;
				// request server login with logname and passwd
				u = login(c, logname, password);

				if (u == null) {
					// login failed ...
					// if fail show dialog and request login again
				}
				
				// if sucess, set the new user data with info from the server:

			}
		});
		btnLogin.setBounds(205, 152, 117, 29);
		contentPane.add(btnLogin);
	}
}
