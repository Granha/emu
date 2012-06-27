/**
 * 
 */
package server;
	
	import java.awt.EventQueue;
	
	import javax.swing.JFrame;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	import server.ServerMonitor;
	import server.ServerSetup;

/**
 * @author rdru
 * INV: 
 */
public class ServerW extends JFrame {
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private JPanel contentPane;
	ServerMonitor monitor = new ServerMonitor();
	ServerSetup setup = new ServerSetup();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerW frame = new ServerW();
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
	public ServerW() {
		setResizable(false);
		frame = this;
		setTitle("EMU Server");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 494, 89);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnOpenEmuSetup = new JButton("Open EMU Setup");
		btnOpenEmuSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setup.setVisible(true);
			}
		});
		btnOpenEmuSetup.setBounds(22, 18, 136, 29);
		contentPane.add(btnOpenEmuSetup);
		
		JButton btnShowEmuMonitor = new JButton("Show EMU Monitor");
		btnShowEmuMonitor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setVisible(true);
			}
		});
		btnShowEmuMonitor.setBounds(170, 18, 148, 29);
		contentPane.add(btnShowEmuMonitor);
		
		JButton btnQuitEmuServer = new JButton("Quit EMU Server");
		btnQuitEmuServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String)JOptionPane.showInputDialog(frame,
					"Are you sure to Shutdown the EMU Server?\n" +
					"Provide the Administrator password to proceed:",
					"Customized Dialog", JOptionPane.WARNING_MESSAGE);
				// force server shutdown ...
				System.exit(0);
			}
		});
		btnQuitEmuServer.setBounds(330, 18, 136, 29);
		contentPane.add(btnQuitEmuServer);
	}
}
